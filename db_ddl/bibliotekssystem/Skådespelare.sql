CREATE TABLE IF NOT EXISTS bibliotekssystem."Skådespelare"
(
    skådespelare_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    förnamn         VARCHAR(25) NOT NULL,
    efternamn       VARCHAR(25) NOT NULL
);

