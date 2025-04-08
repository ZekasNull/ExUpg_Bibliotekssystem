CREATE OR REPLACE FUNCTION bibliotekssystem.sf_getloanlimit("användarid" integer) RETURNS integer
    LANGUAGE plpgsql
AS
$$
DECLARE
    loanLimit INT;
BEGIN
    SELECT ut.max_lån
    INTO loanlimit
    FROM "Användartyp" ut
             JOIN "Användare" u ON ut.användartyp = u.användartyp
    WHERE u.användare_id = användarid;

    RETURN loanlimit;
END;
$$;

