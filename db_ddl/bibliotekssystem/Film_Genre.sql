CREATE TABLE IF NOT EXISTS bibliotekssystem."Film_Genre"
(
    film_id  INTEGER NOT NULL
        CONSTRAINT "FK_Film_Genre.film_id"
            REFERENCES bibliotekssystem."Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    genre_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Genre.genre_id"
            REFERENCES bibliotekssystem."Genre"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

