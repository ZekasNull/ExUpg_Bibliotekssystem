CREATE OR REPLACE FUNCTION bibliotekssystem.sf_getcurrentloanscount("användarid" integer) RETURNS integer
    STABLE
    LANGUAGE plpgsql
AS
$$
DECLARE
    currentLoans int;
BEGIN
    SELECT COUNT(lån_id)
    INTO currentLoans
    FROM "Lån"
    WHERE användarid = "Lån".användare_id;

    RETURN currentLoans;
END;
$$;

ALTER FUNCTION bibliotekssystem.sf_getcurrentloanscount(INTEGER) OWNER TO postgres;

