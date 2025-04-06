CREATE TABLE IF NOT EXISTS "Bok"
(
    bok_id  INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    isbn_13 VARCHAR(13) NOT NULL
        CONSTRAINT bok_isbn_ak
            UNIQUE,
    titel   VARCHAR(50) NOT NULL
);

COMMENT ON CONSTRAINT bok_isbn_ak ON "Bok" IS 'ISBN är alltid unikt';

