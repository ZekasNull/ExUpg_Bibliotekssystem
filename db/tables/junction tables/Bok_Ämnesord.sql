CREATE TABLE IF NOT EXISTS "Bok_Ämnesord"
(
    ord_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Ämnesord.ord_id"
            REFERENCES "Ämnesord"
            ON UPDATE CASCADE ON DELETE CASCADE,
    bok_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Ämnesord.bok_id"
            REFERENCES "Bok"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (ord_id, bok_id)
);

