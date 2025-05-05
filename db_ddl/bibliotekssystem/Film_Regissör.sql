CREATE TABLE IF NOT EXISTS bibliotekssystem."Film_Regissör"
(
    film_jc_id     INTEGER NOT NULL
        CONSTRAINT "FK_Film_Regissör.film_id"
            REFERENCES bibliotekssystem."Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    regissör_jc_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Regissör.regissör_id"
            REFERENCES bibliotekssystem."Regissör"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (film_jc_id, regissör_jc_id)
);

