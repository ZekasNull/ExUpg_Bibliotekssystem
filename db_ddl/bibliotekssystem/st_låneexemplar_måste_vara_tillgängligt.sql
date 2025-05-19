CREATE OR REPLACE FUNCTION bibliotekssystem.st_låneexemplar_måste_vara_tillgängligt() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    --
    IF NOT EXISTS (SELECT 1
                   FROM bibliotekssystem."Exemplar"
                   WHERE streckkod = new.streckkod
                     AND tillgänglig = TRUE) THEN
        RAISE EXCEPTION 'Exemplar med id % är redan utlånat.', new.streckkod;
    END IF;

    RETURN new;
END;
$$;

