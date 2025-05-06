CREATE OR REPLACE PROCEDURE bibliotekssystem.sp_lägg_till_film(IN p_titel character varying, IN p_produktionsland character varying, IN "p_åldersgräns" integer, IN "p_regissör_förnamn_arr" character varying[], IN "p_regissör_efternamn_arr" character varying[], IN "p_skådespelare_förnamn_arr" character varying[], IN "p_skådespelare_efternamn_arr" character varying[], IN p_genrenamn_arr character varying[], OUT debug_status_code integer, OUT debug_status_message text)
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

