CREATE TABLE IF NOT EXISTS "Film_Genre"
(
    film_id  INTEGER NOT NULL
        CONSTRAINT "FK_Film_Genre.film_id"
            REFERENCES "Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    genre_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Genre.genre_id"
            REFERENCES "Genre"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

