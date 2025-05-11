CREATE OR REPLACE FUNCTION bibliotekssystem.sf_find_overdue_loans()
    RETURNS TABLE("lån_id" integer, streckkod integer, "lånedatum" timestamp without time zone, "användare_id" integer)
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

