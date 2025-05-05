CREATE TABLE IF NOT EXISTS bibliotekssystem."Film_Genre"
(
    film_jc_id  INTEGER NOT NULL
        CONSTRAINT "FK_Film_Genre.film_id"
            REFERENCES bibliotekssystem."Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    genre_jc_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Genre.genre_id"
            REFERENCES bibliotekssystem."Genre"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (film_jc_id, genre_jc_id)
);

