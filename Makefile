# Task Management Application - Production Commands

.PHONY: help build up down logs clean rebuild health prod dev

# Colors for output
RED := \033[0;31m
GREEN := \033[0;32m
YELLOW := \033[0;33m
BLUE := \033[0;34m
NC := \033[0m # No Color

help: ## Show this help message
	@echo "$(BLUE)Task Management Application - Production Commands$(NC)"
	@echo ""
	@echo "$(GREEN)Available commands:$(NC)"
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  $(YELLOW)%-15s$(NC) %s\n", $$1, $$2}' $(MAKEFILE_LIST)

build: ## Build all Docker images
	@echo "$(BLUE)Building Docker images...$(NC)"
	docker-compose build --no-cache
	@echo "$(GREEN)Build completed!$(NC)"

up: ## Start all services in production mode
	@echo "$(BLUE)Starting services in production mode...$(NC)"
	docker-compose up -d
	@echo "$(GREEN)Services started!$(NC)"
	@echo "API: http://localhost:8080"
	@echo "Frontend: http://localhost"

down: ## Stop all services
	@echo "$(YELLOW)Stopping services...$(NC)"
	docker-compose down
	@echo "$(GREEN)Services stopped!$(NC)"

logs: ## View logs from all services
	docker-compose logs -f

logs-api: ## View logs from API service only
	docker-compose logs -f task-management-api

logs-db: ## View logs from database service only
	docker-compose logs -f mysql-db

logs-nginx: ## View logs from nginx service only
	docker-compose logs -f nginx

health: ## Check health of all services
	@echo "$(BLUE)Checking service health...$(NC)"
	@docker-compose ps
	@echo ""
	@echo "$(BLUE)API Health:$(NC)"
	@curl -s http://localhost:8080/api/actuator/health | jq . || echo "$(RED)API not responding$(NC)"

clean: ## Stop services and remove volumes (WARNING: deletes data)
	@echo "$(RED)Warning: This will delete all data!$(NC)"
	@read -p "Are you sure? [y/N] " -n 1 -r; \
	if [[ $$REPLY =~ ^[Yy]$$ ]]; then \
		echo ""; \
		echo "$(YELLOW)Cleaning up...$(NC)"; \
		docker-compose down -v; \
		docker system prune -f; \
		echo "$(GREEN)Cleanup completed!$(NC)"; \
	else \
		echo ""; \
		echo "$(GREEN)Cancelled$(NC)"; \
	fi

rebuild: ## Clean, rebuild, and start services
	@echo "$(BLUE)Rebuilding everything...$(NC)"
	$(MAKE) down
	$(MAKE) build
	$(MAKE) up

restart: ## Restart all services
	@echo "$(BLUE)Restarting services...$(NC)"
	docker-compose restart
	@echo "$(GREEN)Services restarted!$(NC)"

dev: ## Start in development mode
	@echo "$(BLUE)Starting in development mode...$(NC)"
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
	@echo "$(GREEN)Development environment started!$(NC)"
	@echo "API: http://localhost:8080 (with debug on port 5005)"

prod: up ## Start in production mode (alias for up)

shell-api: ## Open shell in API container
	docker-compose exec task-management-api /bin/bash

shell-db: ## Open MySQL shell
	docker-compose exec mysql-db mysql -u taskuser -ptaskpassword task_management

shell-nginx: ## Open shell in nginx container
	docker-compose exec nginx /bin/sh

backup-db: ## Backup database
	@echo "$(BLUE)Creating database backup...$(NC)"
	@mkdir -p backups
	docker-compose exec mysql-db mysqldump -u taskuser -ptaskpassword --single-transaction --routines --triggers task_management > backups/backup_$$(date +%Y%m%d_%H%M%S).sql
	@echo "$(GREEN)Backup created in backups/ directory$(NC)"

restore-db: ## Restore database (set BACKUP_FILE=filename.sql)
	@if [ -z "$(BACKUP_FILE)" ]; then \
		echo "$(RED)Usage: make restore-db BACKUP_FILE=backup.sql$(NC)"; \
		exit 1; \
	fi
	@echo "$(BLUE)Restoring database from $(BACKUP_FILE)...$(NC)"
	docker-compose exec -T mysql-db mysql -u taskuser -ptaskpassword task_management < $(BACKUP_FILE)
	@echo "$(GREEN)Database restored!$(NC)"

monitor: ## Monitor system resources
	@echo "$(BLUE)Monitoring system resources...$(NC)"
	docker stats

update: ## Update Docker images
	@echo "$(BLUE)Updating Docker images...$(NC)"
	docker-compose pull
	@echo "$(GREEN)Images updated!$(NC)"

security-scan: ## Run security scan on images
	@echo "$(BLUE)Running security scan...$(NC)"
	docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
		aquasec/trivy image task-management_task-management-api:latest

deploy: ## Deploy to production (build, up, health check)
	@echo "$(BLUE)Deploying to production...$(NC)"
	$(MAKE) build
	$(MAKE) up
	@sleep 30
	$(MAKE) health
	@echo "$(GREEN)Deployment completed!$(NC)"
