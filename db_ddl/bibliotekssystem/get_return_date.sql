CREATE OR REPLACE FUNCTION bibliotekssystem.get_return_date(loan_id integer) RETURNS date
    LANGUAGE plpgsql
AS
$$
DECLARE
    return_date DATE;
BEGIN
    SELECT
        l.lånedatum + lp.lånperiod
    INTO return_date
    FROM
        "Lån" l
            JOIN "Exemplar" e ON l.streckkod = e.streckkod
            JOIN "Låneperiod" lp ON e.låntyp = lp.låntyp
    WHERE
        l.streckkod = loan_id;

    RETURN return_date;
END;
$$;

ALTER FUNCTION bibliotekssystem.get_return_date(INTEGER) OWNER TO postgres;

