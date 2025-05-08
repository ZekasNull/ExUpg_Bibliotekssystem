CREATE OR REPLACE FUNCTION bibliotekssystem.sf_getloanlimit("användarid" integer) RETURNS integer
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

