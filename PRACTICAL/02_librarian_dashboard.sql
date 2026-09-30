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

CREATE TABLE Loans (
    loan_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    book_id INT NOT NULL,
    borrowed_on DATE DEFAULT (CURRENT_DATE),
    returned_on DATE,
    FOREIGN KEY (member_id) REFERENCES Members(member_id),
    FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

CREATE TABLE Donation_History (
    donation_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT,
    donated_by VARCHAR(150) NOT NULL,
    donated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

INSERT INTO Loans (member_id, book_id)
SELECT m.member_id, b.book_id
FROM Members m
CROSS JOIN Books b
WHERE m.email = 'jhanasri@example.com'
AND b.isbn = '9780553418026'
LIMIT 1;

SELECT m.full_name, b.title
FROM Loans l
JOIN Members m ON l.member_id = m.member_id
JOIN Books b ON l.book_id = b.book_id
WHERE l.returned_on IS NULL;

SELECT published_year, COUNT(book_id) AS total_books
FROM Books
GROUP BY published_year
ORDER BY published_year;

START TRANSACTION;

INSERT INTO Books (title, isbn, published_year)
VALUES ('Atomic Habits', '9780735211292', 2018);

INSERT INTO Donation_History (book_id, donated_by)
SELECT book_id, 'Library Donation'
FROM Books
WHERE isbn = '9780735211292';

COMMIT;

CREATE INDEX idx_books_isbn ON Books(isbn);

SHOW INDEX FROM Books;