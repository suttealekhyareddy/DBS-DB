CREATE DATABASE bookflow_db;

-- Run the following commands after connecting to bookflow_db.

CREATE TABLE IF NOT EXISTS Books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    published_year INT CHECK (published_year < 2027)
);

CREATE TABLE IF NOT EXISTS Members (
    member_id SERIAL PRIMARY KEY,
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
('Hasith Varma', 'hasith@example.com');

SELECT * FROM Books;
SELECT * FROM Members;
