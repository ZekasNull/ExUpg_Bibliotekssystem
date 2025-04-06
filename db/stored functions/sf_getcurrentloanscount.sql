CREATE OR REPLACE FUNCTION sf_getcurrentloanscount("användarid" INTEGER) RETURNS INTEGER
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