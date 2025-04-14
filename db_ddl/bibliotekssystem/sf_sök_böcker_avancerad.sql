CREATE OR REPLACE FUNCTION bibliotekssystem.sf_sök_böcker_avancerad(i_titel text DEFAULT NULL::text, i_isbn text DEFAULT NULL::text, "i_förnamn" text DEFAULT NULL::text, i_efternamn text DEFAULT NULL::text, "i_ämnesord" text[] DEFAULT NULL::text[])
    RETURNS TABLE(bok_id integer, isbn_13 character varying, titel character varying, "författare" text, "ämnesord" text)
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY
        SELECT
            b.bok_id,
            b.isbn_13,
            b.titel,
            STRING_AGG(DISTINCT f.förnamn || ' ' || f.efternamn, ', ') AS författare, -- kombinera författarens för och efternamn
            STRING_AGG(DISTINCT a.ord, ', ') AS ämnesord --aggregera ord till kommaseparerad lista
        FROM bibliotekssystem."Bok" b
                 LEFT JOIN bibliotekssystem."Bok_Författare" bf ON b.bok_id = bf.bok_id
                 LEFT JOIN bibliotekssystem."Författare" f ON bf.författare_id = f.författare_id
                 LEFT JOIN bibliotekssystem."Bok_Ämnesord" ba ON b.bok_id = ba.bok_id
                 LEFT JOIN bibliotekssystem."Ämnesord" a ON ba.ord_id = a.ord_id
        WHERE
            (i_titel IS NULL OR b.titel ILIKE '%' || i_titel || '%') AND
            (i_isbn IS NULL OR b.isbn_13 = i_isbn) AND
            (i_förnamn IS NULL OR f.förnamn ILIKE '%' || i_förnamn || '%') AND
            (i_efternamn IS NULL OR f.efternamn ILIKE '%' || i_efternamn || '%') AND

            /*
            Detta filter fungerar genom att:
            Först utvärdera om parameter skickades. Om inte, ignorera.
            Sedan kontrollera om det finns någon rad (en räcker) i ett uttryck där 1 väljs för varje rad i det resultatset där:
            Ämnesord joinas med bok_ämnesord när bok_ämnesord.bok_id har ett som matchar ett i bok.bok_id och ämnesord.ord har ett som matchar något ord i parameterns inputarray.
            */
            (i_ämnesord IS NULL OR EXISTS (
                SELECT 1
                FROM bibliotekssystem."Bok_Ämnesord" ba2
                         JOIN bibliotekssystem."Ämnesord" a2 ON ba2.ord_id = a2.ord_id
                WHERE ba2.bok_id = b.bok_id
                  AND a2.ord = ANY(i_ämnesord)
            ))
        GROUP BY b.bok_id, b.isbn_13, b.titel;
END;
$$;

