CREATE DATABASE bookflow_db;
USE bookflow_db;
SELECT DATABASE();
CREATE TABLE Books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    published_year INT,
    CHECK (published_year < 2027)
);

CREATE TABLE Members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL
);

INSERT INTO Books (title, isbn, published_year)
VALUES
('The Alchemist', '9780061122415', 1988),
('Harry Potter', '9780747532743', 1997),
('The Martian', '9780804139021', 2014);

INSERT INTO Members (full_name, email)
VALUES
('Alekhya Reddy', 'alekhya@gmail.com'),
('Keerthana G', 'keerthana@gmail.com'),
('Rahul Kumar', 'rahul@gmail.com');

SELECT * FROM Books;
SELECT * FROM Members;