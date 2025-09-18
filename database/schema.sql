-- Task Management System Database Schema
-- MySQL 8.0 Compatible

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS task_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE task_management;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_user_username (username),
    INDEX idx_user_email (email),
    INDEX idx_user_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    color VARCHAR(7),
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_category_user_id (user_id),
    INDEX idx_category_name (name),
    UNIQUE KEY uk_category_user_name (user_id, name),

    CONSTRAINT fk_category_user 
        FOREIGN KEY (user_id) REFERENCES users(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tasks table
CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'PENDING',
    priority ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL DEFAULT 'MEDIUM',
    due_date DATE,
    reminder_date TIMESTAMP NULL,
    user_id BIGINT NOT NULL,
    category_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,

    INDEX idx_task_user_id (user_id),
    INDEX idx_task_status (status),
    INDEX idx_task_priority (priority),
    INDEX idx_task_due_date (due_date),
    INDEX idx_task_category_id (category_id),
    INDEX idx_task_created_at (created_at),

    CONSTRAINT fk_task_user 
        FOREIGN KEY (user_id) REFERENCES users(id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_task_category 
        FOREIGN KEY (category_id) REFERENCES categories(id) 
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Task labels table
CREATE TABLE IF NOT EXISTS task_labels (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT NOT NULL,
    label VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_task_label_task_id (task_id),
    INDEX idx_task_label_label (label),
    UNIQUE KEY uk_task_label (task_id, label),

    CONSTRAINT fk_task_label_task 
        FOREIGN KEY (task_id) REFERENCES tasks(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create indexes for better performance
CREATE INDEX idx_tasks_user_status ON tasks(user_id, status);
CREATE INDEX idx_tasks_user_priority ON tasks(user_id, priority);
CREATE INDEX idx_tasks_user_category ON tasks(user_id, category_id);
CREATE INDEX idx_tasks_user_due_date ON tasks(user_id, due_date);

-- Create view for task statistics
CREATE OR REPLACE VIEW task_statistics AS
SELECT 
    u.id as user_id,
    u.username,
    COUNT(t.id) as total_tasks,
    COUNT(CASE WHEN t.status = 'PENDING' THEN 1 END) as pending_tasks,
    COUNT(CASE WHEN t.status = 'IN_PROGRESS' THEN 1 END) as in_progress_tasks,
    COUNT(CASE WHEN t.status = 'COMPLETED' THEN 1 END) as completed_tasks,
    COUNT(CASE WHEN t.due_date < CURDATE() AND t.status != 'COMPLETED' THEN 1 END) as overdue_tasks
FROM users u
LEFT JOIN tasks t ON u.id = t.user_id
GROUP BY u.id, u.username;
