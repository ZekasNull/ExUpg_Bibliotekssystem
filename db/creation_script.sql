-- Kan innehålla fel jämfört med individuella .sql filer men ska återskapa hela databasen på en gång
create sequence "Användare_användar_id_seq";

create table "Bok"
(
    bok_id  integer generated always as identity
        primary key,
    isbn_13 varchar(13) not null
        constraint bok_isbn_ak
            unique,
    titel   varchar(50) not null
);

comment on constraint bok_isbn_ak on "Bok" is 'ISBN är alltid unikt';

create table "Ämnesord"
(
    ord_id integer generated always as identity
        primary key,
    ord    varchar(25) not null
        constraint "Ämnesord_pk"
            unique
);

comment on constraint "Ämnesord_pk" on "Ämnesord" is 'Ämnesord måste vara unika';

create table "Bok_Ämnesord"
(
    ord_id integer not null
        constraint "FK_Bok_Ämnesord.ord_id"
            references "Ämnesord"
            on update cascade on delete cascade,
    bok_id integer not null
        constraint "FK_Bok_Ämnesord.bok_id"
            references "Bok"
            on update cascade on delete cascade,
    primary key (ord_id, bok_id)
);

create table "Författare"
(
    författare_id integer generated always as identity
        primary key,
    förnamn       varchar(25) not null,
    efternamn     varchar(25) not null
);

create table "Bok_Författare"
(
    bok_id        integer not null
        constraint "FK_Bok_Författare.bok_id"
            references "Bok"
            on update cascade on delete cascade,
    författare_id integer not null
        constraint "FK_Bok_Författare.författare_id"
            references "Författare"
            on update cascade on delete cascade,
    primary key (bok_id, författare_id)
);

create table "Skådespelare"
(
    skådespelare_id integer generated always as identity
        primary key,
    förnamn         varchar(25) not null,
    efternamn       varchar(25) not null
);

create table "Film"
(
    film_id         integer generated always as identity
        primary key,
    titel           varchar(50)       not null,
    produktionsland varchar(30)       not null,
    åldersgräns     integer default 0 not null
);

create table "Film_Skådespelare"
(
    skådespelare_id integer not null
        constraint "FK_Film_Skådespelare.skådespelare_id"
            references "Skådespelare"
            on update cascade on delete cascade,
    film_id         integer not null
        constraint "FK_Film_Skådespelare.film_id"
            references "Film"
            on update cascade on delete cascade,
    primary key (skådespelare_id, film_id)
);

create table "Regissör"
(
    regissör_id integer generated always as identity
        primary key,
    förnamn     varchar(25) not null,
    efternamn   varchar(25) not null
);

create table "Film_Regissör"
(
    film_id     integer not null
        constraint "FK_Film_Regissör.film_id"
            references "Film"
            on update cascade on delete cascade,
    regissör_id integer not null
        constraint "FK_Film_Regissör.regissör_id"
            references "Regissör"
            on update cascade on delete cascade,
    primary key (film_id, regissör_id)
);

create table "Genre"
(
    genre_id   integer generated always as identity
        primary key,
    genre_namn varchar(25) not null
        constraint genre_pk
            unique
);

comment on constraint genre_pk on "Genre" is 'Genre måste vara unik';

create table "Film_Genre"
(
    film_id  integer not null
        constraint "FK_Film_Genre.film_id"
            references "Film"
            on update cascade on delete cascade,
    genre_id integer not null
        constraint "FK_Film_Genre.genre_id"
            references "Genre"
            on update cascade on delete cascade,
    primary key (film_id, genre_id)
);

create table "Användartyp"
(
    användartyp varchar(10) not null
        primary key,
    max_lån     smallint    not null
);

create table "Användare"
(
    användare_id integer generated always as identity
        primary key,
    användartyp  varchar(10) not null
        constraint "FK_Användare.användartyp"
            references "Användartyp"
            on update cascade on delete restrict,
    användarnamn varchar(10) not null
        constraint användare_pk
            unique,
    pin          varchar(4)  not null,
    fullt_namn   varchar(50) not null
);

comment on constraint användare_pk on "Användare" is 'Användarnamn måste vara unikt';

alter sequence "Användare_användar_id_seq" owned by "Användare".användare_id;

create table "Låneperiod"
(
    låntyp    varchar(10) not null
        primary key,
    lånperiod interval    not null
);

create table "Exemplar"
(
    streckkod integer generated always as identity
        primary key,
    film_id   integer
        constraint "FK_Exemplar.film_id"
            references "Film"
            on update cascade on delete restrict,
    bok_id    integer
        constraint "FK_Exemplar.bok_id"
            references "Bok"
            on update cascade on delete restrict,
    låntyp    varchar(10) not null
        constraint "FK_Exemplar.låntyp"
            references "Låneperiod"
            on update cascade on delete restrict,
    constraint must_have_single_id
        check (((film_id IS NULL) AND (bok_id IS NOT NULL)) OR ((bok_id IS NULL) AND (film_id IS NOT NULL)))
);

create table "Lån"
(
    lån_id       integer generated always as identity
        primary key,
    streckkod    integer                          not null
        constraint "FK_Lån.streckkod"
            references "Exemplar"
            on update restrict on delete cascade,
    användare_id integer                          not null
        constraint "FK_Lån.användare_id"
            references "Användare"
            on update cascade on delete cascade,
    lånedatum    timestamp default LOCALTIMESTAMP not null
);

create function sf_getloanlimit("användarid" integer) returns integer
    language plpgsql
as
$$
DECLARE
    loanLimit INT;
BEGIN
    SELECT ut.max_lån
    INTO loanlimit
    FROM "Användartyp" ut
             JOIN "Användare" u ON ut.användartyp = u.användartyp
    WHERE u.användare_id = användarid;

    RETURN loanlimit;
END;
$$;

create function sf_getcurrentloanscount("användarid" integer) returns integer
    stable
    language plpgsql
as
$$
DECLARE
    currentLoans int;
BEGIN
    SELECT COUNT(lån_id)
    INTO currentLoans
    FROM "Lån"
    WHERE användarid = "Lån".användare_id;

    RETURN currentLoans;
END;
$$;

create function check_loan_limit() returns trigger
    language plpgsql
as
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

create trigger check_loan_limit_trigger
    before insert
    on "Lån"
    for each row
execute procedure check_loan_limit();

