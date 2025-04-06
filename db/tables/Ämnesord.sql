CREATE TABLE IF NOT EXISTS "Ämnesord"
(
    ord_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    ord    VARCHAR(25) NOT NULL
        CONSTRAINT "Ämnesord_pk"
            UNIQUE
);

COMMENT ON CONSTRAINT "Ämnesord_pk" ON "Ämnesord" IS 'Ämnesord måste vara unika';

