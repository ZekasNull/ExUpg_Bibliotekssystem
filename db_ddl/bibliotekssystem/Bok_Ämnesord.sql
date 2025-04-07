CREATE TABLE IF NOT EXISTS bibliotekssystem."Bok_Ämnesord"
(
    ord_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Ämnesord.ord_id"
            REFERENCES bibliotekssystem."Ämnesord"
            ON UPDATE CASCADE ON DELETE CASCADE,
    bok_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Ämnesord.bok_id"
            REFERENCES bibliotekssystem."Bok"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (ord_id, bok_id)
);

ALTER TABLE bibliotekssystem."Bok_Ämnesord"
    OWNER TO postgres;

