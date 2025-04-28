CREATE TABLE IF NOT EXISTS bibliotekssystem."Användartyp"
(
    användartyp_id VARCHAR(20) NOT NULL
        PRIMARY KEY,
    max_lån        SMALLINT    NOT NULL
);

