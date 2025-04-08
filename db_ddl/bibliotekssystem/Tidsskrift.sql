CREATE TABLE IF NOT EXISTS bibliotekssystem."Tidsskrift"
(
    tidsskrift_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    namn          VARCHAR(25) NOT NULL
        CONSTRAINT tidsskrift_pk
            UNIQUE
);

COMMENT ON CONSTRAINT tidsskrift_pk ON bibliotekssystem."Tidsskrift" IS 'Tidsskriftens namn måste vara unikt';

