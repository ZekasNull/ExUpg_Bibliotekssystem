CREATE TABLE IF NOT EXISTS "Bok_Författare"
(
    bok_id        INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Författare.bok_id"
            REFERENCES "Bok"
            ON UPDATE CASCADE ON DELETE CASCADE,
    författare_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Författare.författare_id"
            REFERENCES "Författare"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (bok_id, författare_id)
);

