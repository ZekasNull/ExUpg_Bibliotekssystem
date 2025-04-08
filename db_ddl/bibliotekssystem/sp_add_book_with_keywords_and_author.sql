CREATE OR REPLACE PROCEDURE bibliotekssystem.sp_add_book_with_keywords_and_author(IN p_bok_titel text, IN p_isbn_13 character varying, IN "p_ämnesord_arr" text[], IN "p_förf_förnamn" character varying, IN "p_förf_efternamn" character varying)
    LANGUAGE plpgsql
AS
$$
DECLARE
    -- Instansvariabler prefix med f för funktion
    f_bok_id INT;
    f_ämnesord TEXT;
    f_ämnesord_id INT;
    f_författar_id INT;
BEGIN
    -- Lägg till bok, spara bokid
    INSERT INTO "Bok" (titel, isbn_13)
    VALUES (p_bok_titel,p_isbn_13)
    RETURNING bok_id INTO f_bok_id;


    -- Lägg till författaren, spara id
    INSERT INTO "Författare" (förnamn, efternamn)
    VALUES (p_förf_förnamn, p_förf_efternamn)
    RETURNING författare_id INTO f_författar_id;

    -- Skapa association
    INSERT INTO "Bok_Författare" (bok_id, författare_id)
    VALUES (f_bok_id, f_författar_id)
    ON CONFLICT DO NOTHING; -- tydligen best practice, om än osannolikt att det händer

    -- För varje ämnesord
    FOREACH f_ämnesord IN ARRAY p_ämnesord_arr
        LOOP
            -- Kontrollera om det redan finns
            SELECT ord_id INTO f_ämnesord_id
            FROM "Ämnesord"
            WHERE ord = f_ämnesord;

            -- Om ej hittad, lägg till och spara ordid
            IF NOT FOUND THEN
                INSERT INTO "Ämnesord" (ord)
                VALUES (f_ämnesord)
                RETURNING ord_id INTO f_ämnesord_id;
            END IF;

            -- Skapa association
            INSERT INTO "Bok_Ämnesord" (ord_id, bok_id)
            VALUES (f_ämnesord_id,f_bok_id)
            ON CONFLICT DO NOTHING;
        END LOOP;
END;
$$;

