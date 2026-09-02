CREATE DATABASE IF NOT EXISTS medicare_db;
USE medicare_db;
SELECT DATABASE();
CREATE TABLE Doctors (
    doctor_id INT PRIMARY KEY,
    doctor_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    consultation_fee DECIMAL(10,2) CHECK (consultation_fee > 0)
);
DESCRIBE Doctors;
CREATE TABLE Patients (
    patient_id INT PRIMARY KEY,
    patient_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);
DESCRIBE Patients;
CREATE TABLE Appointments (
    appointment_id INT PRIMARY KEY,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    FOREIGN KEY (doctor_id)
        REFERENCES Doctors(doctor_id),
    FOREIGN KEY (patient_id)
        REFERENCES Patients(patient_id)
);
DESCRIBE Appointments;
INSERT INTO Doctors
(doctor_id, doctor_name, specialization, consultation_fee)
VALUES
(1, 'Dr. Ravi Kumar', 'Cardiology', 800.00),
(2, 'Dr. Priya Sharma', 'Neurology', 1000.00),
(3, 'Dr. Anil Reddy', 'Orthopedics', 700.00),
(4, 'Dr. Sneha Rao', 'Dermatology', 600.00),
(5, 'Dr. Kiran Patel', 'Cardiology', 900.00),
(6, 'Dr. Meena Singh', 'Pediatrics', 500.00),
(7, 'Dr. Arjun Das', 'Neurology', 1100.00),
(8, 'Dr. Kavya Reddy', 'Gynecology', 750.00),
(9, 'Dr. Vijay Kumar', 'Orthopedics', 850.00),
(10, 'Dr. Anjali Rao', 'Dermatology', 650.00);
SELECT * FROM Doctors;
INSERT INTO Patients
(patient_id, patient_name, email)
VALUES
(101, 'Rahul Sharma', 'rahul.patient@gmail.com'),
(102, 'Sita Reddy', 'sita.patient@gmail.com'),
(103, 'Amit Kumar', 'amit.patient@gmail.com'),
(104, 'Sneha Patel', 'sneha.patient@gmail.com'),
(105, 'Kiran Rao', 'kiran.patient@gmail.com'),
(106, 'Anjali Singh', 'anjali.patient@gmail.com'),
(107, 'Vijay Das', 'vijay.patient@gmail.com'),
(108, 'Meena Reddy', 'meena.patient@gmail.com'),
(109, 'Arjun Sharma', 'arjun.patient@gmail.com'),
(110, 'Kavya Kumar', 'kavya.patient@gmail.com');
SELECT * FROM Patients;
INSERT INTO Appointments
(appointment_id, doctor_id, patient_id, appointment_date)
VALUES
(1, 1, 101, '2026-08-11'),
(2, 2, 102, '2026-08-12'),
(3, 3, 103, '2026-08-13'),
(4, 4, 104, '2026-08-14'),
(5, 5, 105, '2026-08-15'),
(6, 6, 106, '2026-08-16'),
(7, 7, 107, '2026-08-17'),
(8, 8, 108, '2026-08-18'),
(9, 9, 109, '2026-08-19'),
(10, 10, 110, '2026-08-20');
SELECT * FROM Appointments;
SELECT
    p.patient_name AS Patient_Name,
    d.doctor_name AS Doctor_Name,
    d.specialization AS Specialization,
    a.appointment_date AS Appointment_Date
FROM Appointments a
INNER JOIN Patients p
    ON a.patient_id = p.patient_id
INNER JOIN Doctors d
    ON a.doctor_id = d.doctor_id;
    SELECT
    specialization,
    COUNT(*) AS Total_Doctors
FROM Doctors
GROUP BY specialization;
CREATE TABLE Doctor_History (
    history_id INT PRIMARY KEY,
    doctor_id INT NOT NULL,
    action VARCHAR(50) NOT NULL,
    action_date DATE NOT NULL,

    FOREIGN KEY (doctor_id)
        REFERENCES Doctors(doctor_id)
);
DESCRIBE Doctor_History;
START TRANSACTION;
INSERT INTO Doctors
(doctor_id, doctor_name, specialization, consultation_fee)
VALUES
(11, 'Dr. Neha Verma', 'Cardiology', 950.00);
INSERT INTO Doctor_History
(history_id, doctor_id, action, action_date)
VALUES
(1, 11, 'NEW DOCTOR REGISTERED', CURDATE());
COMMIT;
SELECT *
FROM Doctors
WHERE doctor_id = 11;
SELECT * FROM Doctor_History;
START TRANSACTION;
INSERT INTO Doctors
(doctor_id, doctor_name, specialization, consultation_fee)
VALUES
(12, 'Dr. Raj Malhotra', 'Neurology', 1200.00);
SELECT *
FROM Doctors
WHERE doctor_id = 12;
ROLLBACK;
SELECT *
FROM Doctors
WHERE doctor_id = 12;
CREATE INDEX idx_doctors_specialization
ON Doctors(specialization);
SHOW INDEX FROM Doctors;
SELECT *
FROM Doctors
WHERE specialization = 'Cardiology';
EXPLAIN
SELECT *
FROM Doctors
WHERE specialization = 'Cardiology';
SHOW TABLES;
SELECT COUNT(*) AS Total_Tables
FROM information_schema.tables
WHERE table_schema = 'medicare_db';
SELECT 'Doctors' AS Table_Name, COUNT(*) AS Total_Rows
FROM Doctors
UNION ALL
SELECT 'Patients', COUNT(*)
FROM Patients
UNION ALL
SELECT 'Appointments', COUNT(*)
FROM Appointments
UNION ALL
SELECT 'Doctor_History', COUNT(*)
FROM Doctor_History;
SELECT
    p.patient_name AS Patient_Name,
    d.doctor_name AS Doctor_Name,
    d.specialization AS Specialization,
    a.appointment_date AS Appointment_Date
FROM Appointments a
INNER JOIN Patients p
    ON a.patient_id = p.patient_id
INNER JOIN Doctors d
    ON a.doctor_id = d.doctor_id;
    SELECT
    specialization,
    COUNT(*) AS Total_Doctors
FROM Doctors
GROUP BY specialization
ORDER BY Total_Doctors DESC;
