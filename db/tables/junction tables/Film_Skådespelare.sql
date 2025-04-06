CREATE TABLE IF NOT EXISTS "Film_Skådespelare"
(
    skådespelare_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Skådespelare.skådespelare_id"
            REFERENCES "Skådespelare"
            ON UPDATE CASCADE ON DELETE CASCADE,
    film_id         INTEGER NOT NULL
        CONSTRAINT "FK_Film_Skådespelare.film_id"
            REFERENCES "Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (skådespelare_id, film_id)
);

