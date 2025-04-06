CREATE TRIGGER check_loan_limit_trigger
    BEFORE INSERT
    ON "Lån"
    FOR EACH ROW
EXECUTE FUNCTION check_loan_limit();