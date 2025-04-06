CREATE TABLE IF NOT EXISTS "Genre"
(
    genre_id   INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    genre_namn VARCHAR(25) NOT NULL
        CONSTRAINT genre_pk
            UNIQUE
);

COMMENT ON CONSTRAINT genre_pk ON "Genre" IS 'Genre måste vara unik';

