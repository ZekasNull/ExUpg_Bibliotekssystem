CREATE OR REPLACE PROCEDURE bibliotekssystem.sp_add_book_with_keywords_and_author(IN p_bok_titel TEXT,
                                                                                  IN p_isbn_13 CHARACTER VARYING,
                                                                                  IN "p_ämnesord_arr" TEXT[],
                                                                                  IN "p_förf_förnamn" CHARACTER VARYING,
                                                                                  IN "p_förf_efternamn" CHARACTER VARYING,
                                                                                  OUT "status_kod" INT,
                                                                                  OUT "status_meddelande" TEXT)
    LANGUAGE plpgsql
AS
$$
DECLARE
    -- Instansvariabler prefix med f för funktion
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
        INTO "Bok_Författare" (bok_id, författare_id)
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
                INTO "Bok_Ämnesord" (ord_id, bok_id)
            VALUES (f_ämnesord_id, f_bok_id)
            ON CONFLICT DO NOTHING;
        END LOOP;
    -- debug/status
    status_kod := 0;
    status_meddelande := 'Success';
    RAISE NOTICE 'Proceduren lyckades'; -- endast db-konsol

EXCEPTION
    WHEN UNIQUE_VIOLATION THEN
        GET STACKED DIAGNOSTICS
            debug_constraint = CONSTRAINT_NAME;

        -- hantera om isbn inte är unikt
        IF debug_constraint = 'bok_isbn_ak' THEN
            status_kod := 1;
            status_meddelande := 'Detta ISBN-13 (' || p_isbn_13 || ') finns redan i databasen';
        END IF;
END;
$$;

