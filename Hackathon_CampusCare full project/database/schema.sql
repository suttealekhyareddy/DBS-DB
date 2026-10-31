
CREATE DATABASE IF NOT EXISTS campuscare;
USE campuscare;

-- Spring Data JPA creates these tables on startup. This SQL is a reference schema.
CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  full_name VARCHAR(120) NOT NULL,
  email VARCHAR(180) NOT NULL UNIQUE,
  university_id VARCHAR(60) UNIQUE,
  phone VARCHAR(25),
  department VARCHAR(100),
  year_of_study VARCHAR(20),
  section_name VARCHAR(20),
  designation VARCHAR(100),
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL,
  approved BOOLEAN NOT NULL DEFAULT FALSE,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS complaints (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  complaint_code VARCHAR(30) NOT NULL UNIQUE,
  title VARCHAR(180) NOT NULL,
  description TEXT NOT NULL,
  category VARCHAR(60) NOT NULL,
  priority VARCHAR(20) NOT NULL,
  status VARCHAR(30) NOT NULL,
  building VARCHAR(100),
  floor_name VARCHAR(30),
  room_number VARCHAR(30),
  location_code VARCHAR(100),
  reporter_id BIGINT NOT NULL,
  assigned_staff_id BIGINT,
  department VARCHAR(100),
  resolution_remarks TEXT,
  sla_due_at DATETIME,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (reporter_id) REFERENCES users(id),
  FOREIGN KEY (assigned_staff_id) REFERENCES users(id),
  INDEX idx_complaint_status(status),
  INDEX idx_complaint_category(category),
  INDEX idx_complaint_department(department)
);

CREATE TABLE IF NOT EXISTS complaint_history (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  complaint_id BIGINT NOT NULL,
  changed_by BIGINT NOT NULL,
  old_status VARCHAR(30),
  new_status VARCHAR(30) NOT NULL,
  remarks TEXT,
  changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (complaint_id) REFERENCES complaints(id),
  FOREIGN KEY (changed_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS feedback (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  complaint_id BIGINT NOT NULL UNIQUE,
  user_id BIGINT NOT NULL,
  rating INT NOT NULL,
  comments TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (complaint_id) REFERENCES complaints(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
