CREATE TABLE IF NOT EXISTS bibliotekssystem."Upplaga"
(
    upplaga_id   INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    tidskrift_id INTEGER NOT NULL
        CONSTRAINT "FK_Upplaga.tidsskrift_id"
            REFERENCES bibliotekssystem."Tidskrift"
            ON UPDATE CASCADE ON DELETE RESTRICT,
    upplaga_nr   INTEGER NOT NULL,
    år           INTEGER NOT NULL
);

