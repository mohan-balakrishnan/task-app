-- Sample data for Task Management System
-- This file is executed after schema creation

-- Insert default users (password is 'password' encoded with BCrypt)
INSERT IGNORE INTO users (id, username, email, password, first_name, last_name, role, enabled, created_at, updated_at) VALUES
(1, 'admin', 'admin@taskmanager.com', '$2a$10$2eQ8/1Z1fOz1zRzYjzXg9OWJGhph8CeOPYc7XcR5ZGZIrF9YO2L4W', 'Admin', 'User', 'ADMIN', 1, NOW(), NOW()),
(2, 'johndoe', 'john.doe@example.com', '$2a$10$2eQ8/1Z1fOz1zRzYjzXg9OWJGhph8CeOPYc7XcR5ZGZIrF9YO2L4W', 'John', 'Doe', 'USER', 1, NOW(), NOW()),
(3, 'janesmith', 'jane.smith@example.com', '$2a$10$2eQ8/1Z1fOz1zRzYjzXg9OWJGhph8CeOPYc7XcR5ZGZIrF9YO2L4W', 'Jane', 'Smith', 'USER', 1, NOW(), NOW()),
(4, 'bobwilson', 'bob.wilson@example.com', '$2a$10$2eQ8/1Z1fOz1zRzYjzXg9OWJGhph8CeOPYc7XcR5ZGZIrF9YO2L4W', 'Bob', 'Wilson', 'USER', 1, NOW(), NOW());

-- Insert sample categories
INSERT IGNORE INTO categories (id, name, description, color, user_id, created_at, updated_at) VALUES
(1, 'Work', 'Work related tasks and projects', '#FF5722', 2, NOW(), NOW()),
(2, 'Personal', 'Personal tasks and activities', '#2196F3', 2, NOW(), NOW()),
(3, 'Urgent', 'High priority urgent tasks', '#F44336', 2, NOW(), NOW()),
(4, 'Home', 'Home and family related tasks', '#4CAF50', 2, NOW(), NOW()),
(5, 'Health', 'Health and fitness related tasks', '#9C27B0', 2, NOW(), NOW()),
(6, 'Learning', 'Education and skill development', '#FF9800', 3, NOW(), NOW()),
(7, 'Finance', 'Financial and budget related tasks', '#607D8B', 3, NOW(), NOW()),
(8, 'Travel', 'Travel and vacation planning', '#795548', 4, NOW(), NOW());

-- Insert sample tasks
INSERT IGNORE INTO tasks (id, title, description, status, priority, due_date, user_id, category_id, created_at, updated_at, completed_at) VALUES
(1, 'Complete Project Documentation', 'Write comprehensive documentation for the new project including API docs and user guides', 'IN_PROGRESS', 'HIGH', '2025-09-25', 2, 1, '2025-09-15 10:00:00', NOW(), NULL),
(2, 'Team Meeting Preparation', 'Prepare agenda and materials for weekly team meeting. Include project updates and sprint planning', 'PENDING', 'MEDIUM', '2025-09-20', 2, 1, '2025-09-16 09:00:00', NOW(), NULL),
(3, 'Grocery Shopping', 'Buy groceries for the week including fresh vegetables, fruits, and household items', 'PENDING', 'LOW', '2025-09-19', 2, 2, '2025-09-17 14:00:00', NOW(), NULL),
(4, 'Review Code Changes', 'Review and approve pending code changes from team members', 'COMPLETED', 'HIGH', '2025-09-18', 2, 1, '2025-09-14 11:00:00', NOW(), '2025-09-18 16:30:00'),
(5, 'Update Resume', 'Update resume with recent projects and skills', 'PENDING', 'MEDIUM', '2025-09-30', 2, 2, '2025-09-16 15:00:00', NOW(), NULL),
(6, 'Doctor Appointment', 'Schedule and attend routine health checkup', 'PENDING', 'HIGH', '2025-09-22', 2, 5, '2025-09-17 08:00:00', NOW(), NULL),
(7, 'Learn Spring Security', 'Complete online course on Spring Security fundamentals', 'IN_PROGRESS', 'MEDIUM', '2025-10-01', 3, 6, '2025-09-10 12:00:00', NOW(), NULL),
(8, 'Budget Planning', 'Plan monthly budget and review expenses', 'PENDING', 'HIGH', '2025-09-25', 3, 7, '2025-09-15 20:00:00', NOW(), NULL),
(9, 'Vacation Planning', 'Research and book vacation for December holidays', 'PENDING', 'LOW', '2025-10-15', 4, 8, '2025-09-12 16:00:00', NOW(), NULL),
(10, 'Home Repairs', 'Fix leaking faucet and paint bedroom walls', 'IN_PROGRESS', 'MEDIUM', '2025-09-28', 2, 4, '2025-09-13 13:00:00', NOW(), NULL);

-- Insert sample task labels
INSERT IGNORE INTO task_labels (task_id, label, created_at) VALUES
(1, 'documentation', NOW()),
(1, 'important', NOW()),
(1, 'deadline', NOW()),
(2, 'meeting', NOW()),
(2, 'team', NOW()),
(3, 'shopping', NOW()),
(3, 'weekly', NOW()),
(4, 'code-review', NOW()),
(4, 'completed', NOW()),
(5, 'career', NOW()),
(5, 'personal-development', NOW()),
(6, 'health', NOW()),
(6, 'appointment', NOW()),
(7, 'learning', NOW()),
(7, 'programming', NOW()),
(8, 'finance', NOW()),
(8, 'monthly', NOW()),
(9, 'vacation', NOW()),
(9, 'travel', NOW()),
(10, 'home-improvement', NOW()),
(10, 'diy', NOW());

-- Set AUTO_INCREMENT values to continue from inserted data
ALTER TABLE users AUTO_INCREMENT = 5;
ALTER TABLE categories AUTO_INCREMENT = 9;
ALTER TABLE tasks AUTO_INCREMENT = 11;
ALTER TABLE task_labels AUTO_INCREMENT = 23;
