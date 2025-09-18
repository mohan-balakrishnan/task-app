# 🐳 Task Management Application - Production Docker Setup

This is a production-ready containerized setup for the Task Management Application using Docker Compose.

## 🚀 Quick Start

### Prerequisites
- Docker 20.10+
- Docker Compose 2.0+
- 4GB+ RAM available

### 1. Setup Environment
```bash
cp .env.example .env
# Edit .env with your production values
```

### 2. Deploy
```bash
make deploy
```

### 3. Verify
```bash
make health
```

## 📋 Production Features

✅ **Security Hardened**
- Non-root container execution
- Minimal attack surface
- Environment-based secrets
- Network isolation

✅ **High Availability**
- Health checks with automatic restart
- Resource limits and reservations
- Graceful shutdown handling
- Connection pooling

✅ **Monitoring & Observability**
- Actuator endpoints
- Structured logging
- Performance metrics
- Database monitoring

✅ **Scalability**
- Horizontal scaling ready
- Load balancer support
- Stateless application design
- Database connection optimization

## 🔧 Configuration

### Environment Variables
See `.env.example` for all available configuration options.

### Resource Limits
Default limits (adjustable in docker-compose.yml):
- API: 1GB RAM, 2 CPU cores
- Database: 512MB RAM, 1 CPU core
- Nginx: 256MB RAM, 0.5 CPU core

## 🛠️ Operations

### Deployment
```bash
make deploy          # Full deployment
make up              # Start services
make down            # Stop services
make restart         # Restart all
```

### Monitoring
```bash
make health          # Check service health
make logs            # View all logs
make monitor         # Resource monitoring
```

### Maintenance
```bash
make backup-db       # Database backup
make clean           # Clean environment
make update          # Update images
```

### Development
```bash
make dev             # Development mode
make shell-api       # API container shell
make shell-db        # Database shell
```

## 🔒 Security

### Production Checklist
- [ ] Change default passwords in `.env`
- [ ] Use strong JWT secret (32+ characters)
- [ ] Enable HTTPS with SSL certificates
- [ ] Configure firewall rules
- [ ] Set up log monitoring
- [ ] Enable automated backups
- [ ] Configure resource limits
- [ ] Set up monitoring alerts

### SSL/HTTPS Setup
1. Place SSL certificates in `./ssl/` directory
2. Update nginx configuration
3. Set `FORCE_HTTPS=true` in `.env`

## 📊 Monitoring

### Health Endpoints
- Application: `http://localhost:8080/api/actuator/health`
- Database: Automatic health checks
- Nginx: `http://localhost/health`

### Logs
```bash
# Real-time logs
make logs

# Specific service logs
make logs-api
make logs-db
make logs-nginx
```

## 🗄️ Database Management

### Backup
```bash
# Manual backup
make backup-db

# Restore from backup
make restore-db BACKUP_FILE=backup_20250918_160000.sql
```

### Access
```bash
# MySQL shell
make shell-db

# Connection details
Host: localhost:3307
User: taskuser
Database: task_management
```

## 🚀 Scaling

### Horizontal Scaling
```bash
# Scale API instances
docker-compose up -d --scale task-management-api=3
```

### Load Balancing
Update nginx configuration to distribute load across multiple API instances.

## 🛡️ Troubleshooting

### Common Issues

**Port conflicts:**
```bash
# Check port usage
netstat -tulpn | grep -E ':(80|8080|3307)'

# Change ports in .env file
```

**Memory issues:**
```bash
# Check resource usage
make monitor

# Increase limits in docker-compose.yml
```

**Database connection:**
```bash
# Check database logs
make logs-db

# Test connection
make shell-db
```

### Emergency Procedures

**Complete system reset:**
```bash
make clean
make deploy
```

**Database recovery:**
```bash
make restore-db BACKUP_FILE=latest_backup.sql
```

## 📈 Performance Optimization

### JVM Tuning
The application uses optimized JVM settings:
- Container-aware memory management
- G1 garbage collector
- String deduplication

### Database Optimization
- Connection pooling enabled
- Query optimization
- Proper indexing
- Automated maintenance

## 📞 Support

### Logs Location
- Application: `docker-compose logs task-management-api`
- Database: `docker-compose logs mysql-db`
- Nginx: `docker-compose logs nginx`

### Configuration Files
- Main: `docker-compose.yml`
- Development: `docker-compose.dev.yml`
- Nginx: `nginx/nginx.conf`
- Application: `src/main/resources/application.yml`

---

## 🎯 Production Deployment Checklist

- [ ] Environment variables configured
- [ ] SSL certificates installed
- [ ] Firewall rules configured
- [ ] Monitoring set up
- [ ] Backup strategy implemented
- [ ] Resource limits set
- [ ] Health checks verified
- [ ] Security scan passed
- [ ] Load testing completed
- [ ] Documentation updated

**Ready for production!** 🚀
