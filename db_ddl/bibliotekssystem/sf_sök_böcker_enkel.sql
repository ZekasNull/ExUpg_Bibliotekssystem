CREATE OR REPLACE FUNCTION bibliotekssystem.sf_sök_böcker_enkel("i_sökterm" text)
    RETURNS TABLE(bok_id integer, isbn_13 character varying, titel character varying, "författare" text, "ämnesord" text)
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

