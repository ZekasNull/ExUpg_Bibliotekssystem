Databas: PostgreSQL 17

Setup:

Se till att en postgres användare med användarnamn "postgres" och lösenord "root" existerar, eller justera persistence.xml.

1. Se till att ett schema "bibliotekssystem" finns under default-databasen som bör vara "postgres".
2. Kör hela creation_script.sql mot detta schema.
3. Kör hela lookup_table_constants.sql
4. Kör hela testdata.sql

Nu bör databasen vara redo. Starta programmet med javafx:run, eller använd den run-config som finns i .idea/runConfigurations (inte test).