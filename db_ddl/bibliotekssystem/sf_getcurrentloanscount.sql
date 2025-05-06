CREATE OR REPLACE FUNCTION bibliotekssystem.sf_getcurrentloanscount("användarid" integer) RETURNS integer
    STABLE
    LANGUAGE plpgsql
AS
$$
DECLARE
    currentloans INT;
BEGIN
    SELECT COUNT(lån_id)
    INTO currentloans
    FROM "Lån"
    WHERE användarid = "Lån".användare_id;

    RETURN currentloans;
END;
$$;

