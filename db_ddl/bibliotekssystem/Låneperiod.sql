CREATE TABLE IF NOT EXISTS bibliotekssystem."Låneperiod"
(
    låntyp    VARCHAR(10) NOT NULL
        PRIMARY KEY,
    lånperiod INTERVAL    NOT NULL
);

