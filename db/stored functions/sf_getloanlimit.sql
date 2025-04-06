CREATE OR REPLACE FUNCTION sf_getloanlimit("användarid" INTEGER) RETURNS INTEGER
    LANGUAGE plpgsql
AS
$$
DECLARE
    loanlimit INT;
BEGIN
    SELECT ut.max_lån
    INTO loanlimit
    FROM "Användartyp" ut
             JOIN "Användare" u ON ut.användartyp = u.användartyp
    WHERE u.användare_id = användarid;

    RETURN loanlimit;
END;
$$;

