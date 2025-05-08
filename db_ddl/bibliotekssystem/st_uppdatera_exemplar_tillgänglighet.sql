CREATE OR REPLACE FUNCTION bibliotekssystem.st_uppdatera_exemplar_tillgänglighet() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    --TG_OP (text) is the operation that fired the trigger
    IF TG_OP = 'INSERT' THEN
        UPDATE bibliotekssystem."Exemplar"
        SET tillgänglig = FALSE
        WHERE streckkod = NEW.streckkod;

    ELSIF TG_OP = 'DELETE' THEN
        UPDATE bibliotekssystem."Exemplar"
        SET tillgänglig = TRUE
        WHERE streckkod = OLD.streckkod;
    END IF;

    RETURN NULL; -- return null because something has to return
END;
$$;

