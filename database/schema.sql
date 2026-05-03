-- EcoFeast Database Schema
-- Sustainable Food Redistribution System

-- Create Database
CREATE DATABASE IF NOT EXISTS ecofeast;
USE ecofeast;

-- Users Table (Admin/Regular User roles)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') DEFAULT 'USER',
    account_status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Food Items Table
CREATE TABLE food_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    quantity INT NOT NULL,
    expiry_date DATE NOT NULL,
    location VARCHAR(255),
    image_url VARCHAR(255),
    status ENUM('AVAILABLE', 'RESERVED', 'DISTRIBUTED') DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_admin_id (admin_id),
    INDEX idx_status (status),
    INDEX idx_expiry_date (expiry_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Requests Table
CREATE TABLE requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    item_id INT NOT NULL,
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity_requested INT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'COLLECTED') DEFAULT 'PENDING',
    rejection_reason VARCHAR(255),
    issue_date DATE,
    return_date DATE,
    collection_date DATE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES food_items(item_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_item_id (item_id),
    INDEX idx_status (status),
    UNIQUE KEY unique_user_item_request (user_id, item_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample Admin User (password: admin123)
INSERT INTO users (first_name, last_name, email, phone_number, password, role, account_status, is_active)
VALUES ('Admin', 'User', 'admin@ecofeast.com', '9824046355',
        '0192023a7bbd73250516f069df18b500', 'ADMIN', 'APPROVED', TRUE);

-- Sample Regular User (password: user123)
INSERT INTO users (first_name, last_name, email, phone_number, password, role, account_status, is_active)
VALUES ('Abhi', 'Basnet', 'abhibasnet@gmail.com', '9824046354',
        '6ad14ba9986e3615423dfca256d04e3f', 'USER', 'APPROVED', TRUE);
