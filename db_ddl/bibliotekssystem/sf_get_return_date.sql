CREATE OR REPLACE FUNCTION bibliotekssystem.sf_get_return_date(loan_id integer) RETURNS date
    LANGUAGE plpgsql
AS
$$
DECLARE
    return_date DATE;
BEGIN
    SELECT l.lånedatum + lp.lånperiod
    INTO return_date
    FROM bibliotekssystem."Lån" l
             JOIN bibliotekssystem."Exemplar" e ON l.streckkod = e.streckkod
             JOIN bibliotekssystem."Låneperiod" lp ON e.låntyp = lp.låntyp
    WHERE l.streckkod = loan_id;

    RETURN return_date;
END;
$$;

