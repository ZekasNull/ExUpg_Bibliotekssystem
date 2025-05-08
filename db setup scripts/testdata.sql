--Format för böcker: Titel, isbn_13, ämnesord som array, förnamn, efternamn.
--svarar med två ut-parametrar statuskod och statusmeddelande (kastas bort här, därav null)

-- Bok 1: unik författare och ämnesord
CALL sp_lägg_till_bok(
        'Grunderna i databaser',
        '9780000000001',
        ARRAY ['databaser', 'sql', 'relational'],
        'Anna',
        'Karlsson',
        NULL,
        NULL
     );

INSERT INTO "Exemplar" (bok_id, låntyp)
VALUES ((SELECT bok_id FROM bibliotekssystem."Bok" WHERE isbn_13 = '9780000000001'), 'kurslitteratur'),
       ((SELECT bok_id FROM bibliotekssystem."Bok" WHERE isbn_13 = '9780000000001'), 'kurslitteratur'),
       ((SELECT bok_id FROM bibliotekssystem."Bok" WHERE isbn_13 = '9780000000001'), 'kurslitteratur'),
       ((SELECT bok_id FROM bibliotekssystem."Bok" WHERE isbn_13 = '9780000000001'), 'kurslitteratur');


-- Bok 2: delar författare med bok 1, delar ämnesord med bok 1
CALL sp_lägg_till_bok(
        'Avancerade databaser',
        '9780000000002',
        ARRAY ['databaser', 'optimering', 'sql'],
        'Anna',
        'Karlsson',
        NULL,
        NULL
     );
INSERT INTO "Exemplar" (bok_id, låntyp)
VALUES ((SELECT bok_id FROM bibliotekssystem."Bok" WHERE isbn_13 = '9780000000002'), 'referenslitteratur'),
       ((SELECT bok_id FROM bibliotekssystem."Bok" WHERE isbn_13 = '9780000000002'), 'referenslitteratur');


-- Bok 3: ny författare, delar ämnesord med bok 2
CALL sp_lägg_till_bok(
        'Prestandaoptimering i SQL',
        '9780000000003',
        ARRAY ['sql', 'optimering', 'indexering'],
        'Erik',
        'Svensson',
        NULL,
        NULL
     );
INSERT INTO "Exemplar" (bok_id, låntyp)
VALUES ((SELECT bok_id FROM bibliotekssystem."Bok" WHERE isbn_13 = '9780000000003'), 'bok'),
       ((SELECT bok_id FROM bibliotekssystem."Bok" WHERE isbn_13 = '9780000000003'), 'bok');


-- Bok 4: delar författare med bok 3, nya ämnesord, endast 1 exemplar
CALL sp_lägg_till_bok(
        'Systemutveckling i praktiken',
        '9780000000004',
        ARRAY ['systemutveckling', 'agilt', 'kravhantering'],
        'Erik',
        'Svensson',
        NULL,
        NULL
     );
INSERT INTO "Exemplar" (bok_id, låntyp)
VALUES ((SELECT bok_id FROM bibliotekssystem."Bok" WHERE isbn_13 = '9780000000004'), 'bok');

-- Bok 5: ny författare, delar ämnesord med bok 4 och bok 1, inga exemplar
CALL sp_lägg_till_bok(
        'Projektledning för systemvetare',
        '9780000000005',
        ARRAY ['agilt', 'projektledning', 'databaser'],
        'Maria',
        'Lindberg',
        NULL,
        NULL
     );

-- Bok 6: ny författare, inga ämnesord, ett exemplar
CALL sp_lägg_till_bok(
        'Wuthering Heights',
        '9780000000006',
        ARRAY []::TEXT[],
        'Emily',
        'Brontë',
        NULL,
        NULL
     );
INSERT INTO "Exemplar" (bok_id, låntyp)
VALUES ((SELECT bok_id FROM bibliotekssystem."Bok" WHERE isbn_13 = '9780000000006'), 'bok');

-- Testfilmer (godtyckliga)
CALL sp_lägg_till_film(
        'The Matrix',
        'USA',
        15,
        ARRAY ['Lana', 'Lilly'], -- Director first names
        ARRAY ['Wachowski', 'Wachowski'], -- Director last names
        ARRAY ['Keanu', 'Laurence', 'Carrie-Anne', 'Hugo', 'Joe'],
        ARRAY ['Reeves', 'Fishburne', 'Moss', 'Weaving', 'Pantoliano'],
        ARRAY ['Science Fiction', 'Action'],
        NULL, NULL
     );
INSERT INTO "Exemplar" (film_id, låntyp)
VALUES ((SELECT film_id FROM bibliotekssystem."Film" WHERE titel = 'The Matrix'), 'film');

-- Gemensam skådespelare med The Matrix (Bruce) och genre (action)
CALL sp_lägg_till_film(
        'Die Hard',
        'USA',
        16,
        ARRAY ['John'],
        ARRAY ['McTiernan'],
        ARRAY ['Bruce', 'Alan', 'Bonnie', 'Reginald', 'Alexander'],
        ARRAY ['Willis', 'Rickman', 'Bedelia', 'VelJohnson', 'Goddunov'],
        ARRAY ['Action', 'Thriller'],
        NULL, NULL
     );
INSERT INTO "Exemplar" (film_id, låntyp)
VALUES ((SELECT film_id FROM bibliotekssystem."Film" WHERE titel = 'Die Hard'), 'film');

CALL sp_lägg_till_film(
        'Pulp Fiction',
        'USA',
        18,
        ARRAY ['Quentin'],
        ARRAY ['Tarantino'],
        ARRAY ['John', 'Samuel', 'Uma', 'Bruce', 'Ving'],
        ARRAY ['Travolta', 'Jackson', 'Thurman', 'Willis', 'Rhames'],
        ARRAY ['Crime', 'Drama'],
        NULL, NULL
     );
INSERT INTO "Exemplar" (film_id, låntyp)
VALUES ((SELECT film_id FROM bibliotekssystem."Film" WHERE titel = 'Pulp Fiction'), 'film');

CALL sp_lägg_till_film(
        'Peruna',
        'Finland',
        12,
        ARRAY ['Janne'],
        ARRAY ['Koukkari'],
        ARRAY ['Joonas', 'Mikko', 'Hannu-Pekka', 'Kari', 'Jaana'],
        ARRAY ['Saartamo', 'Kouki', 'Björkman', 'Hietalahti', 'Sundqvist'],
        ARRAY ['Comedy', 'Adventure'],
        NULL, NULL
     );
INSERT INTO "Exemplar" (film_id, låntyp)
VALUES ((SELECT film_id FROM bibliotekssystem."Film" WHERE titel = 'Peruna'), 'film');

CALL sp_lägg_till_film(
        'Ghost in the Shell',
        'Japan',
        15,
        ARRAY ['Mamoru'],
        ARRAY ['Oshii'],
        ARRAY ['Atsuko', 'Akio', 'Iemasa', 'Tamio', 'Kōichi'],
        ARRAY ['Tanaka', 'Ōtsuka', 'Kayumi', 'Ōki', 'Yamadera'],
        ARRAY ['Animation', 'Science Fiction'],
        NULL, NULL
     );
INSERT INTO "Exemplar" (film_id, låntyp)
VALUES ((SELECT film_id FROM bibliotekssystem."Film" WHERE titel = 'Ghost in the Shell'), 'film');


--Användare - En av varje typ räcker för test. Mest intressant är egentligen bibliotekarie
INSERT INTO "Användare" (användartyp, användarnamn, pin, fullt_namn)
VALUES ('allmänhet', 'svesve-1', '0000', 'Sven Svensson'),
       ('student', 'maxove-1', '0000', 'Maximal Överlånare'),
       ('forskare', 'marcur-1', '1867', 'Marie Curie'),
       ('unianställda', 'jorgnil-1', '1337', 'Jörgen Nilsson'),
       ('bibliotekarie', 'joshal-1', '0000', 'Josef Hallberg');

-- Maximal överlånare får maxlån för testsyften
INSERT INTO "Lån" (streckkod, användare_id, lånedatum)
VALUES (1, (SELECT användare_id FROM "Användare" WHERE användarnamn = 'maxove-1'), CURRENT_TIMESTAMP),
       (2, (SELECT användare_id FROM "Användare" WHERE användarnamn = 'maxove-1'), CURRENT_TIMESTAMP);

-- Marie Curie får ett oåterlämnat lån för testsyften
INSERT INTO "Lån" (streckkod, användare_id, lånedatum)
VALUES (1, (SELECT användare_id FROM "Användare" WHERE användarnamn = 'marcur-1'), '1890-12-23');