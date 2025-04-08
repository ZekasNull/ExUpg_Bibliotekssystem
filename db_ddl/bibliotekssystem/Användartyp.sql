CREATE TABLE IF NOT EXISTS bibliotekssystem."Användartyp"
(
    användartyp VARCHAR(20) NOT NULL
        PRIMARY KEY,
    max_lån     SMALLINT    NOT NULL
);

