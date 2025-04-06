CREATE TABLE IF NOT EXISTS "Film_Regissör"
(
    film_id     INTEGER NOT NULL
        CONSTRAINT "FK_Film_Regissör.film_id"
            REFERENCES "Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    regissör_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Regissör.regissör_id"
            REFERENCES "Regissör"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (film_id, regissör_id)
);

