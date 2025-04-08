CREATE TABLE IF NOT EXISTS bibliotekssystem."Film_Skådespelare"
(
    skådespelare_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Skådespelare.skådespelare_id"
            REFERENCES bibliotekssystem."Skådespelare"
            ON UPDATE CASCADE ON DELETE CASCADE,
    film_id         INTEGER NOT NULL
        CONSTRAINT "FK_Film_Skådespelare.film_id"
            REFERENCES bibliotekssystem."Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (skådespelare_id, film_id)
);

