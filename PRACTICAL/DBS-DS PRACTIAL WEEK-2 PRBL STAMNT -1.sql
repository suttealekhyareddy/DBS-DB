CREATE DATABASE IF NOT EXISTS skytrack_db;
USE skytrack_db;
SELECT DATABASE();
CREATE TABLE Flights (
    flight_id INT PRIMARY KEY,
    flight_number VARCHAR(20) NOT NULL UNIQUE,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    departure_date DATE NOT NULL,
    ticket_price DECIMAL(10,2) CHECK (ticket_price > 0)
);
DESCRIBE Flights;
CREATE TABLE Passengers (
    passenger_id INT PRIMARY KEY,
    passenger_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);
DESCRIBE Passengers;
CREATE TABLE Bookings (
    booking_id INT PRIMARY KEY,
    passenger_id INT NOT NULL,
    flight_id INT NOT NULL,
    booking_date DATE NOT NULL,
    
    FOREIGN KEY (passenger_id)
        REFERENCES Passengers(passenger_id),
        
    FOREIGN KEY (flight_id)
        REFERENCES Flights(flight_id)
);
DESCRIBE Bookings;
INSERT INTO Flights
(flight_id, flight_number, source, destination, departure_date, ticket_price)
VALUES
(1, 'ST101', 'Hyderabad', 'Delhi', '2026-09-01', 5500.00),
(2, 'ST102', 'Hyderabad', 'Mumbai', '2026-09-02', 4800.00),
(3, 'ST103', 'Chennai', 'Delhi', '2026-09-03', 6200.00),
(4, 'ST104', 'Bangalore', 'Hyderabad', '2026-09-04', 3500.00),
(5, 'ST105', 'Mumbai', 'Kolkata', '2026-09-05', 7000.00),
(6, 'ST106', 'Delhi', 'Chennai', '2026-09-06', 6500.00),
(7, 'ST107', 'Hyderabad', 'Bangalore', '2026-09-07', 4000.00),
(8, 'ST108', 'Kolkata', 'Mumbai', '2026-09-08', 5800.00),
(9, 'ST109', 'Chennai', 'Hyderabad', '2026-09-09', 4200.00),
(10, 'ST110', 'Delhi', 'Bangalore', '2026-09-10', 6100.00);
SELECT * FROM Flights;
INSERT INTO Passengers
(passenger_id, passenger_name, email)
VALUES
(101, 'Rahul Sharma', 'rahul@gmail.com'),
(102, 'Priya Reddy', 'priya@gmail.com'),
(103, 'Amit Kumar', 'amit@gmail.com'),
(104, 'Sneha Rao', 'sneha@gmail.com'),
(105, 'Kiran Patel', 'kiran@gmail.com'),
(106, 'Anjali Singh', 'anjali@gmail.com'),
(107, 'Vijay Kumar', 'vijay@gmail.com'),
(108, 'Meena Das', 'meena@gmail.com'),
(109, 'Arjun Reddy', 'arjun@gmail.com'),
(110, 'Kavya Sharma', 'kavya@gmail.com');
SELECT * FROM Passengers;
INSERT INTO Bookings
(booking_id, passenger_id, flight_id, booking_date)
VALUES
(1, 101, 1, '2026-08-01'),
(2, 102, 2, '2026-08-02'),
(3, 103, 3, '2026-08-03'),
(4, 104, 4, '2026-08-04'),
(5, 105, 5, '2026-08-05'),
(6, 106, 6, '2026-08-06'),
(7, 107, 7, '2026-08-07'),
(8, 108, 8, '2026-08-08'),
(9, 109, 9, '2026-08-09'),
(10, 110, 10, '2026-08-10');
SELECT * FROM Bookings;
SELECT * FROM Flights;
SELECT * FROM Passengers;
SELECT * FROM Bookings;
SELECT
    p.passenger_name AS Passenger_Name,
    f.flight_number AS Flight_Number,
    f.source AS Source,
    f.destination AS Destination
FROM Bookings b
INNER JOIN Passengers p
    ON b.passenger_id = p.passenger_id
INNER JOIN Flights f
    ON b.flight_id = f.flight_id;
    SELECT
    destination,
    COUNT(*) AS Total_Flights
FROM Flights
GROUP BY destination;
SELECT
    destination,
    COUNT(*) AS Total_Flights
FROM Flights
GROUP BY destination
ORDER BY Total_Flights DESC;
CREATE TABLE Flight_History (
    history_id INT PRIMARY KEY,
    flight_id INT NOT NULL,
    action VARCHAR(50) NOT NULL,
    action_date DATE NOT NULL,
    
    FOREIGN KEY (flight_id)
        REFERENCES Flights(flight_id)
);
DESCRIBE Flight_History;
START TRANSACTION;
INSERT INTO Flights
(flight_id, flight_number, source, destination, departure_date, ticket_price)
VALUES
(11, 'ST111', 'Hyderabad', 'Pune', '2026-09-11', 4500.00);
INSERT INTO Flight_History
(history_id, flight_id, action, action_date)
VALUES
(1, 11, 'NEW FLIGHT ADDED', CURDATE());
COMMIT;
SELECT * FROM Flights
WHERE flight_id = 11;
SELECT * FROM Flight_History;
START TRANSACTION;
INSERT INTO Flights
(flight_id, flight_number, source, destination, departure_date, ticket_price)
VALUES
(12, 'ST112', 'Delhi', 'Pune', '2026-09-12', 5200.00);
SELECT * FROM Flights
WHERE flight_id = 12;
ROLLBACK;
SELECT * FROM Flights
WHERE flight_id = 12;
CREATE INDEX idx_flights_flight_number
ON Flights(flight_number);
SHOW INDEX FROM Flights;
SELECT *
FROM Flights
WHERE flight_number = 'ST105';
SELECT *
FROM Flights
WHERE flight_number = 'ST105';
SELECT *
FROM Flights
WHERE destination = 'Delhi';
SHOW TABLES;
SELECT 'Flights' AS Table_Name, COUNT(*) AS Total_Rows
FROM Flights
UNION ALL
SELECT 'Passengers', COUNT(*)
FROM Passengers
UNION ALL
SELECT 'Bookings', COUNT(*)
FROM Bookings
UNION ALL
SELECT 'Flight_History', COUNT(*)
FROM Flight_History;
SHOW TABLES;
SELECT * FROM Flights;
SELECT * FROM Passengers;
SELECT * FROM Bookings;
SELECT * FROM Flight_History;
SELECT
    p.passenger_name AS Passenger_Name,
    f.flight_number AS Flight_Number,
    f.source AS Source,
    f.destination AS Destination
FROM Bookings b
INNER JOIN Passengers p
    ON b.passenger_id = p.passenger_id
INNER JOIN Flights f
    ON b.flight_id = f.flight_id;
    SELECT
    destination,
    COUNT(*) AS Total_Flights
FROM Flights
GROUP BY destination
ORDER BY Total_Flights DESC;
