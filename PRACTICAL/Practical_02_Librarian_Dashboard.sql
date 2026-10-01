-- PRACTICAL 02: THE LIBRARIAN'S DASHBOARD

CREATE DATABASE IF NOT EXISTS bookflow_db;
USE bookflow_db;

CREATE TABLE IF NOT EXISTS Loans (
    loan_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    book_id INT NOT NULL,
    borrowed_on DATE NOT NULL,
    returned_on DATE NULL,
    FOREIGN KEY (member_id) REFERENCES Members(member_id),
    FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

INSERT INTO Loans (member_id, book_id, borrowed_on)
SELECT 1, 1, CURDATE()
WHERE NOT EXISTS (
    SELECT 1 FROM Loans
    WHERE member_id = 1 AND book_id = 1 AND returned_on IS NULL
);

SELECT m.full_name AS member_name, b.title AS book_title
FROM Loans l
JOIN Members m ON l.member_id = m.member_id
JOIN Books b ON l.book_id = b.book_id
WHERE l.returned_on IS NULL;

SELECT published_year, COUNT(book_id) AS total_books
FROM Books
GROUP BY published_year
ORDER BY published_year;

CREATE TABLE IF NOT EXISTS Donation_History (
    donation_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    donated_by VARCHAR(150) NOT NULL,
    donated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

START TRANSACTION;

INSERT INTO Books (title, isbn, published_year)
SELECT 'Atomic Habits', '9780735211292', 2018
WHERE NOT EXISTS (
    SELECT 1 FROM Books WHERE isbn = '9780735211292'
);

INSERT INTO Donation_History (book_id, donated_by)
SELECT b.book_id, 'Library Donation'
FROM Books b
WHERE b.isbn = '9780735211292'
AND NOT EXISTS (
    SELECT 1 FROM Donation_History d
    WHERE d.book_id = b.book_id
    AND d.donated_by = 'Library Donation'
);

COMMIT;

CREATE INDEX idx_books_isbn ON Books(isbn);

SELECT * FROM Donation_History;
SHOW INDEX FROM Books;
