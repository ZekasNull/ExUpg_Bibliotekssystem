CREATE TABLE IF NOT EXISTS "Lån"
(
    lån_id       INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    streckkod    INTEGER                          NOT NULL
        CONSTRAINT "FK_Lån.streckkod"
            REFERENCES "Exemplar"
            ON UPDATE RESTRICT ON DELETE CASCADE,
    användare_id INTEGER                          NOT NULL
        CONSTRAINT "FK_Lån.användare_id"
            REFERENCES "Användare"
            ON UPDATE CASCADE ON DELETE CASCADE,
    lånedatum    TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL
);

