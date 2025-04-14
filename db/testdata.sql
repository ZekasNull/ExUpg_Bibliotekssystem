
--Format för böcker: Titel, isbn_13, ämnesord som array, förnamn, efternamn.
    --svarar med två ut-parametrar statuskod och statusmeddelande

-- Bok 1: unik författare och ämnesord
CALL sp_lägg_till_bok(
        'Grunderna i databaser',
        '9780000000001',
        ARRAY['databaser', 'sql', 'relational'],
        'Anna',
        'Karlsson',
        NULL,
        NULL
     );

-- Bok 2: delar författare med bok 1, delar ämnesord med bok 1
CALL sp_lägg_till_bok(
        'Avancerade databaser',
        '9780000000002',
        ARRAY['databaser', 'optimering', 'sql'],
        'Anna',
        'Karlsson',
        NULL,
        NULL
     );

-- Bok 3: ny författare, delar ämnesord med bok 2
CALL sp_lägg_till_bok(
        'Prestandaoptimering i SQL',
        '9780000000003',
        ARRAY['sql', 'optimering', 'indexering'],
        'Erik',
        'Svensson',
        NULL,
        NULL
     );

-- Bok 4: delar författare med bok 3, nya ämnesord
CALL sp_lägg_till_bok(
        'Systemutveckling i praktiken',
        '9780000000004',
        ARRAY['systemutveckling', 'agilt', 'kravhantering'],
        'Erik',
        'Svensson',
        NULL,
        NULL
     );

-- Bok 5: ny författare, delar ämnesord med bok 4 och bok 1
CALL sp_lägg_till_bok(
        'Projektledning för systemvetare',
        '9780000000005',
        ARRAY['agilt', 'projektledning', 'databaser'],
        'Maria',
        'Lindberg',
        NULL,
        NULL
     );