CREATE TABLE IF NOT EXISTS bibliotekssystem."Genre"
(
    genre_id   INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    genre_namn VARCHAR(25) NOT NULL
        CONSTRAINT genre_pk
            UNIQUE
);

COMMENT ON CONSTRAINT genre_pk ON bibliotekssystem."Genre" IS 'Genre måste vara unik';

ALTER TABLE bibliotekssystem."Genre"
    OWNER TO postgres;

