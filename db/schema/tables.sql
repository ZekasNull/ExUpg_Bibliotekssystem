CREATE TABLE "Bok"
(
    bok_id  INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    isbn_13 VARCHAR(13) NOT NULL
        CONSTRAINT bok_isbn_ak
            UNIQUE,
    titel   VARCHAR(50) NOT NULL
);

COMMENT ON CONSTRAINT bok_isbn_ak ON "Bok" IS 'ISBN är alltid unikt';

CREATE TABLE "Ämnesord"
(
    ord_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    ord    VARCHAR(25) NOT NULL
        CONSTRAINT "Ämnesord_pk"
            UNIQUE
);

COMMENT ON CONSTRAINT "Ämnesord_pk" ON "Ämnesord" IS 'Ämnesord måste vara unika';

CREATE TABLE "Bok_Ämnesord"
(
    ord_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Ämnesord.ord_id"
            REFERENCES "Ämnesord"
            ON UPDATE CASCADE ON DELETE CASCADE,
    bok_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Ämnesord.bok_id"
            REFERENCES "Bok"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (ord_id, bok_id)
);

CREATE TABLE "Författare"
(
    författare_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    förnamn       VARCHAR(25) NOT NULL,
    efternamn     VARCHAR(25) NOT NULL
);

CREATE TABLE "Bok_Författare"
(
    bok_id        INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Författare.bok_id"
            REFERENCES "Bok"
            ON UPDATE CASCADE ON DELETE CASCADE,
    författare_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Författare.författare_id"
            REFERENCES "Författare"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (bok_id, författare_id)
);

CREATE TABLE "Skådespelare"
(
    skådespelare_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    förnamn         VARCHAR(25) NOT NULL,
    efternamn       VARCHAR(25) NOT NULL
);

CREATE TABLE "Film"
(
    film_id         INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    titel           VARCHAR(50)       NOT NULL,
    produktionsland VARCHAR(30)       NOT NULL,
    åldersgräns     INTEGER DEFAULT 0 NOT NULL
);

CREATE TABLE "Film_Skådespelare"
(
    skådespelare_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Skådespelare.skådespelare_id"
            REFERENCES "Skådespelare"
            ON UPDATE CASCADE ON DELETE CASCADE,
    film_id         INTEGER NOT NULL
        CONSTRAINT "FK_Film_Skådespelare.film_id"
            REFERENCES "Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (skådespelare_id, film_id)
);

CREATE TABLE "Regissör"
(
    regissör_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    förnamn     VARCHAR(25) NOT NULL,
    efternamn   VARCHAR(25) NOT NULL
);

CREATE TABLE "Film_Regissör"
(
    film_id     INTEGER NOT NULL
        CONSTRAINT "FK_Film_Regissör.film_id"
            REFERENCES "Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    regissör_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Regissör.regissör_id"
            REFERENCES "Regissör"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (film_id, regissör_id)
);

CREATE TABLE "Genre"
(
    genre_id   INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    genre_namn VARCHAR(25) NOT NULL
        CONSTRAINT genre_pk
            UNIQUE
);

COMMENT ON CONSTRAINT genre_pk ON "Genre" IS 'Genre måste vara unik';

CREATE TABLE "Film_Genre"
(
    film_id  INTEGER NOT NULL
        CONSTRAINT "FK_Film_Genre.film_id"
            REFERENCES "Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    genre_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Genre.genre_id"
            REFERENCES "Genre"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE "Användartyp"
(
    användartyp VARCHAR(10) NOT NULL
        PRIMARY KEY,
    max_lån     SMALLINT    NOT NULL
);

CREATE TABLE "Användare"
(
    användare_id  INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    användartyp  VARCHAR(10) NOT NULL
        CONSTRAINT "FK_Användare.användartyp"
            REFERENCES "Användartyp"
            ON UPDATE CASCADE ON DELETE RESTRICT,
    användarnamn VARCHAR(10) NOT NULL
        CONSTRAINT användare_pk
            UNIQUE,
    pin          VARCHAR(4)  NOT NULL,
    fullt_namn   VARCHAR(50) NOT NULL
);

COMMENT ON CONSTRAINT användare_pk ON "Användare" IS 'Användarnamn måste vara unikt';

CREATE TABLE "Låneperiod"
(
    låntyp    VARCHAR(10) NOT NULL
        PRIMARY KEY,
    lånperiod INTERVAL    NOT NULL
);

CREATE TABLE "Exemplar"
(
    streckkod INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    film_id   INTEGER
        CONSTRAINT "FK_Exemplar.film_id"
            REFERENCES "Film"
            ON UPDATE CASCADE ON DELETE RESTRICT,
    bok_id    INTEGER
        CONSTRAINT "FK_Exemplar.bok_id"
            REFERENCES "Bok"
            ON UPDATE CASCADE ON DELETE RESTRICT,
    låntyp    VARCHAR(10) NOT NULL
        CONSTRAINT "FK_Exemplar.låntyp"
            REFERENCES "Låneperiod"
            ON UPDATE CASCADE ON DELETE RESTRICT
        CONSTRAINT must_have_single_id
            CHECK (((film_id IS NULL) AND (bok_id IS NOT NULL)) OR ((bok_id IS NULL) AND (film_id IS NOT NULL)))
);

CREATE TABLE "Lån"
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

