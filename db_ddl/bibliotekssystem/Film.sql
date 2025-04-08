CREATE TABLE IF NOT EXISTS bibliotekssystem."Film"
(
    film_id         INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    titel           VARCHAR(50)       NOT NULL,
    produktionsland VARCHAR(30)       NOT NULL,
    åldersgräns     INTEGER DEFAULT 0 NOT NULL
);

