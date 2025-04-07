CREATE TABLE IF NOT EXISTS bibliotekssystem."Film_Regissör"
(
    film_id     INTEGER NOT NULL
        CONSTRAINT "FK_Film_Regissör.film_id"
            REFERENCES bibliotekssystem."Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    regissör_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Regissör.regissör_id"
            REFERENCES bibliotekssystem."Regissör"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (film_id, regissör_id)
);

ALTER TABLE bibliotekssystem."Film_Regissör"
    OWNER TO postgres;

