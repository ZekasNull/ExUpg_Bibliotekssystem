CREATE TABLE IF NOT EXISTS bibliotekssystem."Tidskrift"
(
    tidskrift_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    namn         VARCHAR(25) NOT NULL
        CONSTRAINT tidskrift_pk
            UNIQUE
);

COMMENT ON CONSTRAINT tidskrift_pk ON bibliotekssystem."Tidskrift" IS 'Tidskriftens namn måste vara unikt';

