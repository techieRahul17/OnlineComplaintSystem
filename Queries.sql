CREATE DATABASE complaint_db;
USE complaint_db;

-- 1. Table for Admin Credentials
CREATE TABLE admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);

-- Insert default admin (User: admin, Pass: admin123)
INSERT INTO admins (username, password) VALUES ('admin', 'admin123');

-- 2. Table for Complaints (Combined User info here for simpler assignment logic)
CREATE TABLE complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    complaint_id VARCHAR(20) UNIQUE NOT NULL, -- The tracking ID (e.g., CMP-5829)
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'Pending',     -- Pending, In Progress, Resolved
    remarks TEXT,                             -- Admin comments
    submission_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);