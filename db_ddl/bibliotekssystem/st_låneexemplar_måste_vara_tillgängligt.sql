CREATE OR REPLACE FUNCTION bibliotekssystem.st_låneexemplar_måste_vara_tillgängligt() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    --
    IF NOT EXISTS (
        SELECT 1 FROM bibliotekssystem."Exemplar"
        WHERE streckkod = NEW.streckkod AND tillgänglig = TRUE
    ) THEN
        RAISE EXCEPTION 'Exemplar med id % är redan utlånat.', NEW.streckkod;
    END IF;

    RETURN NEW;
END;
$$;

