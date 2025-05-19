CREATE TABLE IF NOT EXISTS bibliotekssystem."Bok"
(
    bok_id  INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    isbn_13 VARCHAR(13) NOT NULL
        CONSTRAINT bok_isbn_ak
            UNIQUE,
    titel   VARCHAR(50) NOT NULL
);

COMMENT ON CONSTRAINT bok_isbn_ak ON bibliotekssystem."Bok" IS 'ISBN är alltid unikt';

CREATE TABLE IF NOT EXISTS bibliotekssystem."Ämnesord"
(
    ord_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    ord    VARCHAR(25) NOT NULL
        CONSTRAINT "Ämnesord_pk"
            UNIQUE
);

COMMENT ON CONSTRAINT "Ämnesord_pk" ON bibliotekssystem."Ämnesord" IS 'Ämnesord måste vara unika';

CREATE TABLE IF NOT EXISTS bibliotekssystem."Bok_Ämnesord"
(
    ord_jc_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Ämnesord.ord_id"
            REFERENCES bibliotekssystem."Ämnesord"
            ON UPDATE CASCADE ON DELETE CASCADE,
    bok_jc_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Ämnesord.bok_id"
            REFERENCES bibliotekssystem."Bok"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (ord_jc_id, bok_jc_id)
);

CREATE TABLE IF NOT EXISTS bibliotekssystem."Författare"
(
    författare_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    förnamn       VARCHAR(25) NOT NULL,
    efternamn     VARCHAR(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS bibliotekssystem."Bok_Författare"
(
    bok_jc_id        INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Författare.bok_id"
            REFERENCES bibliotekssystem."Bok"
            ON UPDATE CASCADE ON DELETE CASCADE,
    författare_jc_id INTEGER NOT NULL
        CONSTRAINT "FK_Bok_Författare.författare_id"
            REFERENCES bibliotekssystem."Författare"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (bok_jc_id, författare_jc_id)
);

CREATE TABLE IF NOT EXISTS bibliotekssystem."Skådespelare"
(
    skådespelare_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    förnamn         VARCHAR(25) NOT NULL,
    efternamn       VARCHAR(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS bibliotekssystem."Film"
(
    film_id         INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    titel           VARCHAR(50)       NOT NULL,
    produktionsland VARCHAR(30)       NOT NULL,
    åldersgräns     INTEGER DEFAULT 0 NOT NULL
);

CREATE TABLE IF NOT EXISTS bibliotekssystem."Film_Skådespelare"
(
    skådespelare_jc_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Skådespelare.skådespelare_id"
            REFERENCES bibliotekssystem."Skådespelare"
            ON UPDATE CASCADE ON DELETE CASCADE,
    film_jc_id         INTEGER NOT NULL
        CONSTRAINT "FK_Film_Skådespelare.film_id"
            REFERENCES bibliotekssystem."Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (skådespelare_jc_id, film_jc_id)
);

CREATE TABLE IF NOT EXISTS bibliotekssystem."Regissör"
(
    regissör_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    förnamn     VARCHAR(25) NOT NULL,
    efternamn   VARCHAR(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS bibliotekssystem."Film_Regissör"
(
    film_jc_id     INTEGER NOT NULL
        CONSTRAINT "FK_Film_Regissör.film_id"
            REFERENCES bibliotekssystem."Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    regissör_jc_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Regissör.regissör_id"
            REFERENCES bibliotekssystem."Regissör"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (film_jc_id, regissör_jc_id)
);

CREATE TABLE IF NOT EXISTS bibliotekssystem."Genre"
(
    genre_id   INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    genre_namn VARCHAR(25) NOT NULL
        CONSTRAINT genre_pk
            UNIQUE
);

COMMENT ON CONSTRAINT genre_pk ON bibliotekssystem."Genre" IS 'Genre måste vara unik';

CREATE TABLE IF NOT EXISTS bibliotekssystem."Film_Genre"
(
    film_jc_id  INTEGER NOT NULL
        CONSTRAINT "FK_Film_Genre.film_id"
            REFERENCES bibliotekssystem."Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    genre_jc_id INTEGER NOT NULL
        CONSTRAINT "FK_Film_Genre.genre_id"
            REFERENCES bibliotekssystem."Genre"
            ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (film_jc_id, genre_jc_id)
);

CREATE TABLE IF NOT EXISTS bibliotekssystem."Användartyp"
(
    användartyp_id VARCHAR(20) NOT NULL
        PRIMARY KEY,
    max_lån        SMALLINT    NOT NULL
);

CREATE TABLE IF NOT EXISTS bibliotekssystem."Användare"
(
    användare_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    användartyp  VARCHAR(20) NOT NULL
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

CREATE TABLE IF NOT EXISTS bibliotekssystem."Låneperiod"
(
    låntyp    VARCHAR(20) NOT NULL
        PRIMARY KEY,
    lånperiod INTERVAL    NOT NULL
);

CREATE TABLE IF NOT EXISTS bibliotekssystem."Exemplar"
(
    streckkod   INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    film_id     INTEGER
        CONSTRAINT "FK_Exemplar.film_id"
            REFERENCES bibliotekssystem."Film"
            ON UPDATE CASCADE ON DELETE CASCADE,
    bok_id      INTEGER
        CONSTRAINT "FK_Exemplar.bok_id"
            REFERENCES bibliotekssystem."Bok"
            ON UPDATE CASCADE ON DELETE CASCADE,
    låntyp      VARCHAR(20)          NOT NULL
        CONSTRAINT "FK_Exemplar.låntyp"
            REFERENCES bibliotekssystem."Låneperiod"
            ON UPDATE CASCADE ON DELETE RESTRICT,
    tillgänglig BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT must_have_single_id
        CHECK (((film_id IS NULL) AND (bok_id IS NOT NULL)) OR ((bok_id IS NULL) AND (film_id IS NOT NULL)))
);

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

CREATE TABLE IF NOT EXISTS bibliotekssystem."Tidskrift"
(
    tidskrift_id INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    namn         VARCHAR(25) NOT NULL
        CONSTRAINT tidskrift_pk
            UNIQUE
);

COMMENT ON CONSTRAINT tidskrift_pk ON bibliotekssystem."Tidskrift" IS 'Tidskriftens namn måste vara unikt';

CREATE TABLE IF NOT EXISTS bibliotekssystem."Upplaga"
(
    upplaga_id   INTEGER GENERATED ALWAYS AS IDENTITY
        PRIMARY KEY,
    tidskrift_id INTEGER NOT NULL
        CONSTRAINT "FK_Upplaga.tidsskrift_id"
            REFERENCES bibliotekssystem."Tidskrift"
            ON UPDATE CASCADE ON DELETE RESTRICT,
    upplaga_nr   INTEGER NOT NULL,
    år           INTEGER NOT NULL
);

CREATE OR REPLACE VIEW bibliotekssystem.vy_bok_info(bok_id, titel, isbn_13, författare, ämnesord) AS
SELECT b.bok_id,
       b.titel,
       b.isbn_13,
       STRING_AGG(DISTINCT (f."förnamn"::TEXT || ' '::TEXT) || f.efternamn::TEXT, ', '::TEXT) AS "författare",
       STRING_AGG(DISTINCT a.ord::TEXT, ', '::TEXT)                                           AS "ämnesord"
FROM bibliotekssystem."Bok" b
         LEFT JOIN bibliotekssystem."Bok_Författare" bf ON b.bok_id = bf.bok_jc_id
         LEFT JOIN bibliotekssystem."Författare" f ON bf."författare_jc_id" = f."författare_id"
         LEFT JOIN bibliotekssystem."Bok_Ämnesord" ba ON b.bok_id = ba.bok_jc_id
         LEFT JOIN bibliotekssystem."Ämnesord" a ON ba.ord_jc_id = a.ord_id
GROUP BY b.bok_id, b.titel, b.isbn_13;

CREATE OR REPLACE PROCEDURE bibliotekssystem.sp_lägg_till_bok(IN p_bok_titel TEXT, IN p_isbn_13 CHARACTER VARYING,
                                                              IN "p_ämnesord_arr" TEXT[],
                                                              IN "p_förf_förnamn" CHARACTER VARYING,
                                                              IN "p_förf_efternamn" CHARACTER VARYING,
                                                              OUT status_kod INTEGER, OUT status_meddelande TEXT)
    LANGUAGE plpgsql
AS
$$
DECLARE
    -- Instansvariabler prefix med f för funktion
    -- FIXME klarar för närvarande inte om boken har flera författare - bör uppdateras till att hantera det.
    f_bok_id         INT;
    f_ämnesord       TEXT;
    f_ämnesord_id    INT;
    f_författar_id   INT;
    debug_constraint TEXT;
BEGIN
    -- Lägg till bok, spara bokid
    INSERT
    INTO "Bok" (titel, isbn_13)
    VALUES (p_bok_titel, p_isbn_13)
    RETURNING bok_id INTO f_bok_id;


    -- Kontrollera om författaren redan finns
    SELECT författare_id
    INTO f_författar_id
    FROM "Författare"
    WHERE p_förf_förnamn = "Författare".förnamn
      AND p_förf_efternamn = "Författare".efternamn;

    -- Om inte hittad, Lägg till författaren, spara id
    IF NOT found THEN
        INSERT
        INTO "Författare" (förnamn, efternamn)
        VALUES (p_förf_förnamn, p_förf_efternamn)
        RETURNING författare_id INTO f_författar_id;
    END IF;

    -- Skapa association
    INSERT
    INTO "Bok_Författare" (bok_jc_id, författare_jc_id)
    VALUES (f_bok_id, f_författar_id)
    ON CONFLICT DO NOTHING;
    -- tydligen best practice, om än osannolikt att det händer

    -- För varje ämnesord
    FOREACH f_ämnesord IN ARRAY p_ämnesord_arr
        LOOP
            -- Kontrollera om det redan finns
            SELECT ord_id
            INTO f_ämnesord_id
            FROM "Ämnesord"
            WHERE ord = f_ämnesord;

            -- Om ej hittad, lägg till och spara ordid
            IF NOT found THEN
                INSERT
                INTO "Ämnesord" (ord)
                VALUES (f_ämnesord)
                RETURNING ord_id INTO f_ämnesord_id;
            END IF;

            -- Skapa association
            INSERT
            INTO "Bok_Ämnesord" (ord_jc_id, bok_jc_id)
            VALUES (f_ämnesord_id, f_bok_id)
            ON CONFLICT DO NOTHING;
        END LOOP;
    -- debug/status
    status_kod := 0;
    status_meddelande := 'Success';
    RAISE NOTICE 'Proceduren lyckades'; -- endast db-konsol

EXCEPTION
    -- TODO bör kanske skriva i sina outputs om andra fel än bok_isbn_ak inträffar.
    WHEN UNIQUE_VIOLATION THEN
        GET STACKED DIAGNOSTICS
            debug_constraint = CONSTRAINT_NAME;

        -- hantera om isbn inte är unikt
        IF debug_constraint = 'bok_isbn_ak' THEN
            status_kod := 1;
            status_meddelande := 'Detta ISBN-13 (' || p_isbn_13 || ') finns redan i databasen.';
        END IF;
        ROLLBACK;
END;
$$;

CREATE OR REPLACE PROCEDURE bibliotekssystem.sp_lägg_till_film(IN p_titel CHARACTER VARYING,
                                                               IN p_produktionsland CHARACTER VARYING,
                                                               IN "p_åldersgräns" INTEGER,
                                                               IN "p_regissör_förnamn_arr" CHARACTER VARYING[],
                                                               IN "p_regissör_efternamn_arr" CHARACTER VARYING[],
                                                               IN "p_skådespelare_förnamn_arr" CHARACTER VARYING[],
                                                               IN "p_skådespelare_efternamn_arr" CHARACTER VARYING[],
                                                               IN p_genrenamn_arr CHARACTER VARYING[],
                                                               OUT debug_status_code INTEGER,
                                                               OUT debug_status_message TEXT)
    LANGUAGE plpgsql
AS
$$
DECLARE
    f_filmid        INT;
    f_regissörid    INT;
    f_skådespelarid INT;
    f_genreid       INT;
    i               INT;
BEGIN
    IF ARRAY_LENGTH(p_regissör_förnamn_arr, 1) IS DISTINCT FROM ARRAY_LENGTH(p_regissör_efternamn_arr, 1) OR
       ARRAY_LENGTH(p_skådespelare_förnamn_arr, 1) IS DISTINCT FROM ARRAY_LENGTH(p_skådespelare_efternamn_arr, 1) THEN
        debug_status_code := 1;
        debug_status_message := FORMAT(
                'Fel: Regissörer har [%I] förnamn och [%I] efternamn, Skådespelare har [%I] förnamn och [%I] efternamn.',
                ARRAY_LENGTH(p_regissör_förnamn_arr, 1), ARRAY_LENGTH(p_regissör_efternamn_arr, 1),
                ARRAY_LENGTH(p_skådespelare_förnamn_arr, 1), ARRAY_LENGTH(p_skådespelare_efternamn_arr, 1));
        RETURN;
    END IF;

    -- Lägg till film
    INSERT INTO "Film" (titel, produktionsland, åldersgräns)
    VALUES (p_titel, p_produktionsland, p_åldersgräns)
    RETURNING film_id INTO f_filmid;

    -- Lägg till regissörer genom att iterera över förnamn och efternamnsarrayer
    FOR i IN 1..ARRAY_LENGTH(p_regissör_förnamn_arr, 1)
        LOOP
            -- Hitta direktören om hen redan finns
            SELECT regissör_id
            INTO f_regissörid
            FROM "Regissör"
            WHERE "Regissör".förnamn = p_regissör_förnamn_arr[i]
              AND "Regissör".efternamn = p_regissör_förnamn_arr[i];

            IF NOT found THEN
                INSERT INTO "Regissör" (förnamn, efternamn)
                VALUES (p_regissör_förnamn_arr[i], p_regissör_efternamn_arr[i])
                RETURNING regissör_id INTO f_regissörid;
            END IF;

            INSERT INTO "Film_Regissör" (film_jc_id, regissör_jc_id)
            VALUES (f_filmid, f_regissörid)
            ON CONFLICT DO NOTHING;
        END LOOP;

    -- Lägg till skådespelare
    FOR i IN 1..ARRAY_LENGTH(p_skådespelare_förnamn_arr, 1)
        LOOP
            SELECT skådespelare_id
            INTO f_skådespelarid
            FROM "Skådespelare"
            WHERE förnamn = p_skådespelare_förnamn_arr[i]
              AND efternamn = p_skådespelare_efternamn_arr[i];

            IF NOT found THEN
                INSERT INTO "Skådespelare" (förnamn, efternamn)
                VALUES (p_skådespelare_förnamn_arr[i], p_skådespelare_efternamn_arr[i])
                RETURNING skådespelare_id INTO f_skådespelarid;
            END IF;

            INSERT INTO "Film_Skådespelare" (skådespelare_jc_id, film_jc_id)
            VALUES (f_skådespelarid, f_filmid)
            ON CONFLICT DO NOTHING;
        END LOOP;

    -- Lägg till genre
    FOR i IN 1..ARRAY_LENGTH(p_genrenamn_arr, 1)
        LOOP
            SELECT genre_id
            INTO f_genreid
            FROM "Genre"
            WHERE genre_namn = p_genrenamn_arr[i];

            IF NOT found THEN
                INSERT INTO "Genre" (genre_namn)
                VALUES (p_genrenamn_arr[i])
                RETURNING genre_id INTO f_genreid;
            END IF;

            INSERT INTO "Film_Genre" (film_jc_id, genre_jc_id)
            VALUES (f_filmid, f_genreid)
            ON CONFLICT DO NOTHING;
        END LOOP;

    debug_status_code := 0;
    debug_status_message := 'Success'; --till anropare
    RAISE NOTICE 'Proceduren lyckades'; -- endast db-konsol

EXCEPTION
    WHEN OTHERS THEN
        debug_status_code := 1;
        debug_status_message := sqlerrm;
        ROLLBACK;
END;
$$;

CREATE OR REPLACE FUNCTION bibliotekssystem.st_check_loan_limit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
DECLARE
    current_loans  INT := bibliotekssystem.sf_getcurrentloanscount(new.användare_id);
    loan_limit     INT := bibliotekssystem.sf_getloanlimit(new.användare_id);
    incoming_loans INT;
BEGIN
    SELECT COUNT(new.lån_id) INTO incoming_loans;
    IF current_loans + incoming_loans > loan_limit THEN
        RAISE EXCEPTION 'User has reached the maximum allowed simultaneous loans.';
    END IF;
    RETURN new;
END;
$$;

CREATE TRIGGER check_loan_limit_trigger
    BEFORE INSERT
    ON bibliotekssystem."Lån"
    FOR EACH ROW
EXECUTE PROCEDURE bibliotekssystem.st_check_loan_limit();

CREATE OR REPLACE FUNCTION bibliotekssystem.sf_sök_böcker_avancerad(i_titel TEXT DEFAULT NULL::TEXT,
                                                                    i_isbn TEXT DEFAULT NULL::TEXT,
                                                                    "i_förnamn" TEXT DEFAULT NULL::TEXT,
                                                                    i_efternamn TEXT DEFAULT NULL::TEXT,
                                                                    "i_ämnesord" TEXT[] DEFAULT NULL::TEXT[])
    RETURNS TABLE
            (
                bok_id       INTEGER,
                isbn_13      CHARACTER VARYING,
                titel        CHARACTER VARYING,
                "författare" TEXT,
                "ämnesord"   TEXT
            )
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY
        SELECT b.bok_id,
               b.isbn_13,
               b.titel,
               STRING_AGG(DISTINCT f.förnamn || ' ' || f.efternamn, ', ') AS författare, -- kombinera författarens för och efternamn
               STRING_AGG(DISTINCT a.ord, ', ')                           AS ämnesord    --aggregera ord till kommaseparerad lista
        FROM bibliotekssystem."Bok" b
                 LEFT JOIN bibliotekssystem."Bok_Författare" bf ON b.bok_id = bf.bok_jc_id
                 LEFT JOIN bibliotekssystem."Författare" f ON bf.författare_jc_id = f.författare_id
                 LEFT JOIN bibliotekssystem."Bok_Ämnesord" ba ON b.bok_id = ba.bok_jc_id
                 LEFT JOIN bibliotekssystem."Ämnesord" a ON ba.ord_jc_id = a.ord_id
        WHERE (i_titel IS NULL OR b.titel ILIKE '%' || i_titel || '%')
          AND (i_isbn IS NULL OR b.isbn_13 = i_isbn)
          AND (i_förnamn IS NULL OR f.förnamn ILIKE '%' || i_förnamn || '%')
          AND (i_efternamn IS NULL OR f.efternamn ILIKE '%' || i_efternamn || '%')
          AND

            /*
            Detta filter fungerar genom att:
            Först utvärdera om parameter skickades. Om inte, ignorera.
            Sedan kontrollera om det finns någon rad (en räcker) i ett uttryck där 1 väljs för varje rad i det resultatset där:
            Ämnesord joinas med bok_ämnesord när bok_ämnesord.bok_id har ett som matchar ett i bok.bok_id och ämnesord.ord har ett som matchar något ord i parameterns inputarray.
            */
            (i_ämnesord IS NULL OR EXISTS (SELECT 1
                                           FROM bibliotekssystem."Bok_Ämnesord" ba2
                                                    JOIN bibliotekssystem."Ämnesord" a2 ON ba2.ord_jc_id = a2.ord_id
                                           WHERE ba2.bok_jc_id = b.bok_id
                                             AND a2.ord = ANY (i_ämnesord)))
        GROUP BY b.bok_id, b.isbn_13, b.titel;
END;
$$;

CREATE OR REPLACE FUNCTION bibliotekssystem.sf_get_return_date(loan_id INTEGER) RETURNS DATE
    LANGUAGE plpgsql
AS
$$
DECLARE
    return_date DATE;
BEGIN
    SELECT l.lånedatum + lp.lånperiod
    INTO return_date
    FROM bibliotekssystem."Lån" l
             JOIN bibliotekssystem."Exemplar" e ON l.streckkod = e.streckkod
             JOIN bibliotekssystem."Låneperiod" lp ON e.låntyp = lp.låntyp
    WHERE l.streckkod = loan_id;

    RETURN return_date;
END;
$$;

CREATE OR REPLACE FUNCTION bibliotekssystem.sf_sök_böcker_enkel("i_sökterm" TEXT)
    RETURNS TABLE
            (
                bok_id       INTEGER,
                isbn_13      CHARACTER VARYING,
                titel        CHARACTER VARYING,
                "författare" TEXT,
                "ämnesord"   TEXT
            )
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY
        SELECT b.bok_id,
               b.isbn_13,
               b.titel,
               STRING_AGG(DISTINCT f.förnamn || ' ' || f.efternamn, ', ') AS författare,
               STRING_AGG(DISTINCT a.ord, ', ')                           AS ämnesord
        FROM bibliotekssystem."Bok" b
                 LEFT JOIN bibliotekssystem."Bok_Författare" bf ON b.bok_id = bf.bok_jc_id
                 LEFT JOIN bibliotekssystem."Författare" f ON bf.författare_jc_id = f.författare_id
                 LEFT JOIN bibliotekssystem."Bok_Ämnesord" ba ON b.bok_id = ba.bok_jc_id
                 LEFT JOIN bibliotekssystem."Ämnesord" a ON ba.ord_jc_id = a.ord_id
        GROUP BY b.bok_id, b.isbn_13, b.titel
        HAVING i_sökterm IS NULL
            OR (
            b.titel ILIKE '%' || i_sökterm || '%' OR
            b.isbn_13 ILIKE '%' || i_sökterm || '%' OR
            STRING_AGG(DISTINCT f.förnamn || ' ' || f.efternamn, ', ') ILIKE '%' || i_sökterm || '%' OR
            STRING_AGG(DISTINCT a.ord, ', ') ILIKE '%' || i_sökterm || '%'
            );
END;
$$;

CREATE OR REPLACE FUNCTION bibliotekssystem.st_uppdatera_exemplar_tillgänglighet() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    --TG_OP (text) is the operation that fired the trigger
    IF tg_op = 'INSERT' THEN
        UPDATE bibliotekssystem."Exemplar"
        SET tillgänglig = FALSE
        WHERE streckkod = new.streckkod;

    ELSIF tg_op = 'DELETE' THEN
        UPDATE bibliotekssystem."Exemplar"
        SET tillgänglig = TRUE
        WHERE streckkod = old.streckkod;
    END IF;

    RETURN NULL; -- return null because something has to return
END;
$$;

CREATE TRIGGER trg_uppdatera_exemplar_tillgänglighet
    AFTER INSERT OR DELETE
    ON bibliotekssystem."Lån"
    FOR EACH ROW
EXECUTE PROCEDURE bibliotekssystem.st_uppdatera_exemplar_tillgänglighet();

CREATE OR REPLACE FUNCTION bibliotekssystem.sf_getcurrentloanscount("användarid" INTEGER) RETURNS INTEGER
    STABLE
    LANGUAGE plpgsql
AS
$$
DECLARE
    currentloans INT;
BEGIN
    SELECT COUNT(lån_id)
    INTO currentloans
    FROM bibliotekssystem."Lån"
    WHERE användarid = "Lån".användare_id;

    RETURN currentloans;
END;
$$;

CREATE OR REPLACE FUNCTION bibliotekssystem.sf_getloanlimit("användarid" INTEGER) RETURNS INTEGER
    LANGUAGE plpgsql
AS
$$
DECLARE
    loanlimit INT;
BEGIN
    SELECT ut.max_lån
    INTO loanlimit
    FROM bibliotekssystem."Användartyp" ut
             JOIN bibliotekssystem."Användare" u ON ut.användartyp_id = u.användartyp
    WHERE u.användare_id = användarid;

    RETURN loanlimit;
END;
$$;

CREATE OR REPLACE FUNCTION bibliotekssystem.sf_find_overdue_loans()
    RETURNS TABLE
            (
                "lån_id"       INTEGER,
                streckkod      INTEGER,
                "lånedatum"    TIMESTAMP WITHOUT TIME ZONE,
                "användare_id" INTEGER
            )
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY
        SELECT l.lån_id, l.streckkod, l.lånedatum, l.användare_id
        FROM bibliotekssystem."Lån" l
                 JOIN bibliotekssystem."Exemplar" e ON l.streckkod = e.streckkod
                 JOIN bibliotekssystem."Låneperiod" lp ON e.låntyp = lp.låntyp
        WHERE l.lånedatum + lp.lånperiod < NOW();
END;
$$;

CREATE OR REPLACE FUNCTION bibliotekssystem.st_låneexemplar_måste_vara_tillgängligt() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    --
    IF NOT EXISTS (SELECT 1
                   FROM bibliotekssystem."Exemplar"
                   WHERE streckkod = new.streckkod
                     AND tillgänglig = TRUE) THEN
        RAISE EXCEPTION 'Exemplar med id % är redan utlånat.', new.streckkod;
    END IF;

    RETURN new;
END;
$$;

CREATE TRIGGER trigger_check_copy_availability
    BEFORE INSERT
    ON bibliotekssystem."Lån"
    FOR EACH ROW
EXECUTE PROCEDURE bibliotekssystem.st_låneexemplar_måste_vara_tillgängligt();

