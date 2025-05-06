CREATE OR REPLACE FUNCTION bibliotekssystem.st_check_loan_limit() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
DECLARE
    current_loans  INT := sf_getcurrentloanscount(new.användare_id);
    loan_limit     INT := sf_getloanlimit(new.användare_id);
    incoming_loans INT;
BEGIN
    SELECT COUNT(new.lån_id) INTO incoming_loans;
    IF current_loans + incoming_loans > loan_limit THEN
        RAISE EXCEPTION 'User has reached the maximum allowed simultaneous loans.';
    END IF;
    RETURN new;
END;
$$;

