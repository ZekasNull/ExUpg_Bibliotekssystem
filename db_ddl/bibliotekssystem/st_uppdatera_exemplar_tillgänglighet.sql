CREATE OR REPLACE FUNCTION bibliotekssystem.st_uppdatera_exemplar_tillgänglighet() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    --TG_OP (text) is the operation that fired the trigger
    IF tg_op = 'INSERT' THEN
        UPDATE bibliotekssystem."Exemplar"
        SET tillgänglig = FALSE
        WHERE streckkod = new.streckkod;

    ELSIF tg_op = 'DELETE' THEN
        UPDATE bibliotekssystem."Exemplar"
        SET tillgänglig = TRUE
        WHERE streckkod = old.streckkod;
    END IF;

    RETURN NULL; -- return null because something has to return
END;
$$;

