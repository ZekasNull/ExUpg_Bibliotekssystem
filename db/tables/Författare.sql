CREATE TABLE IF NOT EXISTS "Författare"
(
    författare_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    förnamn       VARCHAR(25) NOT NULL,
    efternamn     VARCHAR(25) NOT NULL
);

