-- Smart Banking Management System - Database Schema
-- Author: Avi Jaiswal
-- Database: MySQL

-- Create database
CREATE DATABASE IF NOT EXISTS smart_banking;
USE smart_banking;

-- Drop existing tables (for fresh setup)
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS users;

-- Users table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    password VARCHAR(64) NOT NULL,  -- SHA-256 hashed
    balance DECIMAL(15, 2) DEFAULT 0.00,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    INDEX idx_account (account_number),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Transactions table
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    transaction_type ENUM(
        'DEPOSIT', 
        'WITHDRAWAL', 
        'TRANSFER_SENT', 
        'TRANSFER_RECEIVED',
        'ACCOUNT_CREATED',
        'LOGIN',
        'LOGOUT',
        'PASSWORD_CHANGED'
    ) NOT NULL,
    amount DECIMAL(15, 2) DEFAULT 0.00,
    description VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_date (transaction_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create an admin account (password: admin123)
-- SHA-256 hash of 'admin123'
INSERT INTO users (account_number, full_name, email, phone, password, balance, role) 
VALUES (
    'SB000001', 
    'System Administrator', 
    'admin@smartbank.com', 
    '9999999999',
    '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',  -- admin123
    1000000.00,
    'ADMIN'
);

-- Create sample user accounts for testing
-- Password for all test users: test123
-- SHA-256 hash of 'test123'
INSERT INTO users (account_number, full_name, email, phone, password, balance, role) 
VALUES 
(
    'SB000002',
    'John Doe',
    'john@example.com',
    '9876543210',
    'ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae',  -- test123
    50000.00,
    'USER'
),
(
    'SB000003',
    'Jane Smith',
    'jane@example.com',
    '9876543211',
    'ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae',  -- test123
    75000.00,
    'USER'
),
(
    'SB000004',
    'Robert Johnson',
    'robert@example.com',
    '9876543212',
    'ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae',  -- test123
    100000.00,
    'USER'
);

-- Insert initial transaction logs
INSERT INTO transactions (user_id, transaction_type, amount, description)
VALUES
(1, 'ACCOUNT_CREATED', 1000000.00, 'Admin account created'),
(2, 'ACCOUNT_CREATED', 50000.00, 'Account created with initial deposit'),
(3, 'ACCOUNT_CREATED', 75000.00, 'Account created with initial deposit'),
(4, 'ACCOUNT_CREATED', 100000.00, 'Account created with initial deposit');

-- Create view for account summary
CREATE OR REPLACE VIEW account_summary AS
SELECT 
    u.account_number,
    u.full_name,
    u.email,
    u.balance,
    u.role,
    COUNT(t.transaction_id) as total_transactions,
    u.created_at
FROM users u
LEFT JOIN transactions t ON u.user_id = t.user_id
GROUP BY u.user_id;

-- Create stored procedure for fund transfer (ensures ACID properties)
DELIMITER //

CREATE PROCEDURE transfer_funds(
    IN sender_id INT,
    IN recipient_account VARCHAR(20),
    IN transfer_amount DECIMAL(15, 2),
    OUT status VARCHAR(50)
)
BEGIN
    DECLARE recipient_id INT;
    DECLARE sender_balance DECIMAL(15, 2);
    
    -- Start transaction
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET status = 'FAILED';
    END;
    
    START TRANSACTION;
    
    -- Get sender balance
    SELECT balance INTO sender_balance FROM users WHERE user_id = sender_id FOR UPDATE;
    
    -- Check if sender has sufficient balance
    IF sender_balance < transfer_amount OR (sender_balance - transfer_amount) < 1000 THEN
        SET status = 'INSUFFICIENT_BALANCE';
        ROLLBACK;
    ELSE
        -- Get recipient ID
        SELECT user_id INTO recipient_id FROM users WHERE account_number = recipient_account FOR UPDATE;
        
        IF recipient_id IS NULL THEN
            SET status = 'RECIPIENT_NOT_FOUND';
            ROLLBACK;
        ELSE
            -- Deduct from sender
            UPDATE users SET balance = balance - transfer_amount WHERE user_id = sender_id;
            
            -- Add to recipient
            UPDATE users SET balance = balance + transfer_amount WHERE user_id = recipient_id;
            
            -- Log transactions
            INSERT INTO transactions (user_id, transaction_type, amount, description)
            VALUES (sender_id, 'TRANSFER_SENT', transfer_amount, CONCAT('Transfer to ', recipient_account));
            
            INSERT INTO transactions (user_id, transaction_type, amount, description)
            VALUES (recipient_id, 'TRANSFER_RECEIVED', transfer_amount, CONCAT('Transfer from sender'));
            
            COMMIT;
            SET status = 'SUCCESS';
        END IF;
    END IF;
END //

DELIMITER ;

-- Create trigger to update last_login on successful login
DELIMITER //

CREATE TRIGGER update_last_login
AFTER INSERT ON transactions
FOR EACH ROW
BEGIN
    IF NEW.transaction_type = 'LOGIN' THEN
        UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = NEW.user_id;
    END IF;
END //

DELIMITER ;

-- Display setup completion message
SELECT 'Database setup completed successfully!' AS Status;
SELECT 'Test Accounts Created:' AS Info;
SELECT account_number, full_name, email, 'Password: admin123 or test123' AS credentials, role 
FROM users;

-- Display system statistics
SELECT 'System Statistics:' AS Info;
SELECT 
    COUNT(*) as total_users,
    SUM(balance) as total_balance,
    (SELECT COUNT(*) FROM transactions) as total_transactions
FROM users;