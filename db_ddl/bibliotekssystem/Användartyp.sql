CREATE TABLE IF NOT EXISTS bibliotekssystem."Användartyp"
(
    användartyp VARCHAR(10) NOT NULL
        PRIMARY KEY,
    max_lån     SMALLINT    NOT NULL
);

ALTER TABLE bibliotekssystem."Användartyp"
    OWNER TO postgres;

