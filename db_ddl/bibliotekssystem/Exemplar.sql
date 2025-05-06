CREATE TABLE IF NOT EXISTS bibliotekssystem."Exemplar"
(
    streckkod INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    film_id   INTEGER
        CONSTRAINT "FK_Exemplar.film_id"
            REFERENCES bibliotekssystem."Film"
            ON UPDATE CASCADE ON DELETE RESTRICT,
    bok_id    INTEGER
        CONSTRAINT "FK_Exemplar.bok_id"
            REFERENCES bibliotekssystem."Bok"
            ON UPDATE CASCADE ON DELETE RESTRICT,
    låntyp    VARCHAR(20) NOT NULL
        CONSTRAINT "FK_Exemplar.låntyp"
            REFERENCES bibliotekssystem."Låneperiod"
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT must_have_single_id
        CHECK (((film_id IS NULL) AND (bok_id IS NOT NULL)) OR ((bok_id IS NULL) AND (film_id IS NOT NULL)))
);

