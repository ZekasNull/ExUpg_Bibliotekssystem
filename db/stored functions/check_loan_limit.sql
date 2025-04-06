CREATE OR REPLACE FUNCTION check_loan_limit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
DECLARE
    current_loans INT := sf_getcurrentloanscount(new.användare_id);
    loan_limit    INT := sf_getloanlimit(new.användare_id);
BEGIN

    IF current_loans > loan_limit THEN
        RAISE EXCEPTION 'User has reached the maximum allowed simultaneous loans.';
    END IF;
    RETURN new;
END;
$$;