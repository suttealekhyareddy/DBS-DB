CREATE DATABASE bookflow_db;
USE bookflow_db;

CREATE TABLE Books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    published_year INT CHECK (published_year < 2027)
);

CREATE TABLE Members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

INSERT INTO Books (title, isbn, published_year) VALUES
('The Martian', '9780553418026', 2014),
('Dune', '9780441172719', 1965),
('Clean Code', '9780132350884', 2008);

INSERT INTO Members (full_name, email) VALUES
('Jhanasri Thota', 'jhanasri@example.com'),
('Amrutha J', 'amrutha@example.com'),
('Keerthana', 'keerthana@example.com');

SELECT * FROM Books;
SELECT * FROM Members;