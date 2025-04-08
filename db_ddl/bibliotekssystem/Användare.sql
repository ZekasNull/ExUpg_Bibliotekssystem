CREATE TABLE IF NOT EXISTS bibliotekssystem."Användare"
(
    användare_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    användartyp  VARCHAR(10) NOT NULL
        CONSTRAINT "FK_Användare.användartyp"
            REFERENCES bibliotekssystem."Användartyp"
            ON UPDATE CASCADE ON DELETE RESTRICT,
    användarnamn VARCHAR(10) NOT NULL
        CONSTRAINT användare_pk
            UNIQUE,
    pin          VARCHAR(4)  NOT NULL,
    fullt_namn   VARCHAR(50) NOT NULL
);

COMMENT ON CONSTRAINT användare_pk ON bibliotekssystem."Användare" IS 'Användarnamn måste vara unikt';

