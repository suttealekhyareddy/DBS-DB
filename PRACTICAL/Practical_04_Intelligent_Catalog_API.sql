-- PRACTICAL 04: INTELLIGENT CATALOG - PostgreSQL + pgvector
-- Requires PostgreSQL with pgvector installed.

CREATE EXTENSION IF NOT EXISTS vector;

ALTER TABLE Books
ADD COLUMN IF NOT EXISTS embedding vector(3);

UPDATE Books
SET embedding = CASE isbn
    WHEN '9780553418026' THEN '[0.10,0.80,0.20]'::vector
    WHEN '9780441172719' THEN '[0.15,0.75,0.25]'::vector
    WHEN '9780132350884' THEN '[0.80,0.20,0.10]'::vector
    WHEN '9780735211292' THEN '[0.40,0.40,0.20]'::vector
END;

SELECT
    book_id,
    title,
    embedding <-> '[0.12,0.78,0.22]'::vector AS l2_distance
FROM Books
WHERE embedding IS NOT NULL
ORDER BY embedding <-> '[0.12,0.78,0.22]'::vector
LIMIT 3;

SELECT
    book_id,
    title,
    embedding <=> '[0.12,0.78,0.22]'::vector AS cosine_distance
FROM Books
WHERE embedding IS NOT NULL
ORDER BY embedding <=> '[0.12,0.78,0.22]'::vector
LIMIT 3;
