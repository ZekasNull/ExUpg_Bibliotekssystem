CREATE TABLE IF NOT EXISTS bibliotekssystem."Bok_Författare"
(
    bok_id        INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Författare.bok_id"
            REFERENCES bibliotekssystem."Bok"
            ON UPDATE CASCADE ON DELETE CASCADE,
    författare_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Författare.författare_id"
            REFERENCES bibliotekssystem."Författare"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (bok_id, författare_id)
);

ALTER TABLE bibliotekssystem."Bok_Författare"
    OWNER TO postgres;

