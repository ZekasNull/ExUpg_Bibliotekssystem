CREATE OR REPLACE VIEW bibliotekssystem.vy_bok_info(bok_id, titel, isbn_13, författare, ämnesord) AS
SELECT b.bok_id,
       b.titel,
       b.isbn_13,
       STRING_AGG(DISTINCT (f."förnamn"::TEXT || ' '::TEXT) || f.efternamn::TEXT, ', '::TEXT) AS "författare",
       STRING_AGG(DISTINCT a.ord::TEXT, ', '::TEXT)                                           AS "ämnesord"
FROM bibliotekssystem."Bok" b
         LEFT JOIN bibliotekssystem."Bok_Författare" bf ON b.bok_id = bf.bok_id
         LEFT JOIN bibliotekssystem."Författare" f ON bf."författare_id" = f."författare_id"
         LEFT JOIN bibliotekssystem."Bok_Ämnesord" ba ON b.bok_id = ba.bok_id
         LEFT JOIN bibliotekssystem."Ämnesord" a ON ba.ord_id = a.ord_id
GROUP BY b.bok_id, b.titel, b.isbn_13;

