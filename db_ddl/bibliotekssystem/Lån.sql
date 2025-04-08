CREATE TABLE IF NOT EXISTS bibliotekssystem."Lån"
(
    lån_id       INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    streckkod    INTEGER                          NOT NULL
        CONSTRAINT "FK_Lån.streckkod"
            REFERENCES bibliotekssystem."Exemplar"
            ON UPDATE RESTRICT ON DELETE CASCADE,
    användare_id INTEGER                          NOT NULL
        CONSTRAINT "FK_Lån.användare_id"
            REFERENCES bibliotekssystem."Användare"
            ON UPDATE CASCADE ON DELETE CASCADE,
    lånedatum    TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL
);

CREATE TRIGGER check_loan_limit_trigger
    BEFORE INSERT
    ON bibliotekssystem."Lån"
    FOR EACH ROW
EXECUTE PROCEDURE bibliotekssystem.check_loan_limit();

