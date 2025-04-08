CREATE TABLE IF NOT EXISTS bibliotekssystem."Låneperiod"
(
    låntyp    VARCHAR(20) NOT NULL
        PRIMARY KEY,
    lånperiod INTERVAL    NOT NULL
);

