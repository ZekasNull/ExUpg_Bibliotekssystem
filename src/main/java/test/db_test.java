package test;

import db.DatabaseService;
import model.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class db_test {
    private static EntityManagerFactory emf;

    public static void main(String[] args) throws Exception {
        testBokSearchByTerm("sql");


    }

    private static void testSkapaOchRaderaBokOchExemplar() throws Exception {
        DatabaseService dbservice = new DatabaseService();
        ArrayList<Object> testnylista = new ArrayList<>();

        //testa skapa ny bok ändra den boken, lägg till exemplar för boken, ta bort boken och exemplaret

        //författare (HELT NY - för att använda existerande måste den författaren hämtas från db
        //nya författare får id när de förs in i databasen, sätt därför inte det
        Författare nybokförf = new Författare();
        nybokförf.setFörnamn("förnamn");
        nybokförf.setEfternamn("efternamn");

        //ämnesord
        Ämnesord testord = new Ämnesord();
        testord.setOrd("testord");

        // skapa - ska ha titel, isbn-13, författarobjekt, ämnesord
        Bok testnybok = new Bok();
        testnybok.setTitel("testnybok");
        testnybok.setIsbn13("0000000000013");
        testnybok.getFörfattare().add(nybokförf);
        testnybok.getÄmnesord().add(testord);

        //lägger till en bok, författare och ämnesord
        testnylista.add(testnybok); //gör till insert-lista för dbservice
        dbservice.läggTillNyaObjekt(testnylista);


        //skapa exemplar
        Exemplar testex = new Exemplar();
        testex.setBok(testnybok);
        testex.setNewLåntyp("bok");

        testnylista.add(testex);
        dbservice.läggTillNyaObjekt(testnylista);

        System.out.println("Nu radering - bör ta bort bok och exemplar");
        Bok insertedBok = dbservice.searchAndGetBooks("0000000000013").get(0); //hämta id från db
        ArrayList<Object> deletelist = new ArrayList<>(insertedBok.getExemplars());
        deletelist.add(insertedBok);

        dbservice.raderaObjekt(deletelist);
        //obs! efteråt kommer testord och testförfattare fortfarande att finnas i db. cascade på sådant skulle riskera att en författare med andra böcker också tas bort, vilket inte tillåts av db's on delete.
    }

    private static void låntest() throws Exception {
        DatabaseService test = new DatabaseService();
        Exemplar ex;

        //hämta användaren
        Användare user = test.logInUser("maxove-1", "0000");

        //hämta testexemplar
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Bibliotek_PU");
        EntityManager emtest = emf.createEntityManager();
        emtest.getTransaction().begin();
        TypedQuery<Exemplar> query = emtest.createQuery("SELECT e FROM Exemplar e WHERE e.streckkod = 2", Exemplar.class); //create query
        ex = query.getSingleResult(); //run query and store result

        //skapa lån
        Lån nyttLån = new Lån();
        nyttLån.setAnvändare(user);
        nyttLån.setStreckkod(ex);

        ArrayList<Object> addList = new ArrayList<>();
        addList.add(nyttLån);


        //för in i db
        test.läggTillNyaObjekt(addList);
    }

    private static void testFilmSearchByTerm(String searchTerm) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Bibliotek_PU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            TypedQuery<Film> query = em.createQuery(
            "SELECT DISTINCT f FROM Film f " +
            "LEFT JOIN f.skådespelares a " +
            "LEFT JOIN f.regissörs d " +
            "LEFT JOIN f.genres g " +
            "WHERE LOWER(f.titel) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
               "OR LOWER(f.produktionsland) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
               "OR CAST(f.åldersgräns AS string) LIKE CONCAT('%', :searchTerm, '%')" +
               "OR LOWER(a.förnamn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
               "OR LOWER(a.efternamn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
               "OR LOWER(d.förnamn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
               "OR LOWER(d.efternamn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
               "OR LOWER(g.genreNamn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))"
        , Film.class);

            query.setParameter("searchTerm", searchTerm);
            List<Film> results = query.getResultList();

            for (Film film : results) {
                System.out.println(film);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
            emf.close();
        }
    }

    private static void testBokSearchByTerm(String searchTerm) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Bibliotek_PU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // JPQL query for searching across title, isbn13, author name, and keyword word
            TypedQuery<Bok> query = em.createQuery(
            "SELECT DISTINCT b FROM Bok b " +
            "LEFT JOIN FETCH b.Författare a " +
            "LEFT JOIN FETCH b.Ämnesord k " +
            "WHERE LOWER(b.titel) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
               "OR LOWER(b.isbn13) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
               "OR LOWER(a.förnamn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
                "OR LOWER(a.efternamn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
               "OR LOWER(k.ord) LIKE LOWER(CONCAT('%', :searchTerm, '%'))", Bok.class);

            query.setParameter("searchTerm", searchTerm);

            List<Bok> results = query.getResultList();
            System.out.println(results.size());

            for (Bok bok : results) {
                System.out.println(bok.getFörfattare().getClass());
                System.out.println(bok);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
            emf.close();
        }
    }

    //Ett antal test som kollar att entiteterna är korrekta i utformning genom att kolla deras toString
    //kraschar sådär mysigt ofta när entiteterna är felkonfigurerade
    private static void testAnvändareEntity() {
        emf = Persistence.createEntityManagerFactory("Bibliotek_PU");
        EntityManager emTest = emf.createEntityManager();
        Användare testAnv;

        emTest.getTransaction().begin();
        testAnv = emTest.createQuery("SELECT u from Användare u", Användare.class).getResultList().get(0);

        System.out.println(testAnv.toString());
    }

    private static void testLåntypEntity() {
        emf = Persistence.createEntityManagerFactory("Bibliotek_PU");
        EntityManager emTest = emf.createEntityManager();
        Låneperiod testP;

        emTest.getTransaction().begin();
        testP = emTest.createQuery("SELECT lp FROM Låneperiod lp WHERE låntyp = 'kurslitteratur'", Låneperiod.class).getSingleResult();

        System.out.println(testP.toString());
    }

    private static void testBokEntity() {
        emf = Persistence.createEntityManagerFactory("Bibliotek_PU");
        EntityManager emTest = emf.createEntityManager();
        Bok testbok;

        emTest.getTransaction().begin();

        testbok = emTest.createQuery("SELECT b from Bok b", Bok.class).getResultList().get(0);

        System.out.println(testbok.toString());

        // Finally, close the factory upon application exit
        emTest.close();
        emf.close();
    }

    private static void testFilmEntity() {
        emf = Persistence.createEntityManagerFactory("Bibliotek_PU");
        EntityManager test = emf.createEntityManager();
        Film testfilm;

        test.getTransaction().begin();

        testfilm = (Film) test.createQuery("SELECT f FROM Film f").getSingleResult();

        System.out.println(testfilm.toString());

        test.close();
    }

    //attempts to find a user with provided vars
    public static boolean userExists(EntityManager em, String användarnamn, String pin) {
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM Användare u WHERE u.användarnamn = :användarnamn AND u.pin = :pin", Long.class)
                .setParameter("användarnamn", användarnamn)
                .setParameter("pin", pin)
                .getSingleResult();

        return count > 0;
    }

    public static void nyregissörtest() {
        emf = Persistence.createEntityManagerFactory("Bibliotek_PU");
        EntityManager test = emf.createEntityManager();
        Film testfilm, testfilm2;
        //variables

        //start
        test.getTransaction().begin();

        testfilm = (Film) test.createQuery("SELECT f FROM Film f").getSingleResult();

        for (Regissör r : testfilm.getRegissörs()) {
            System.out.println(r.getFörnamn() + " " + r.getEfternamn());
        }//show current directors


        //create new director
        Regissör paul = new Regissör();
        paul.setFörnamn("paul");
        paul.setEfternamn("verhoeven");
        test.persist(paul); //new entities must be persisted

        testfilm.getRegissörs().add(paul); //link director to movie

        test.getTransaction().commit(); //write to db

        test.clear(); //reset entitymanager
        test.getTransaction().begin();

        testfilm2 = (Film) test.createQuery("SELECT f FROM Film f").getSingleResult();

        for (Regissör r : testfilm2.getRegissörs()) {
            System.out.println(r.getFörnamn() + " " + r.getEfternamn());
        }
        test.getTransaction().commit();
        test.close();

    }

    public static void användartyptest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Användartyp typtest = new Användartyp();
        typtest.setAnvändartyp("Student");
        typtest.setMaxLån((short) 1);
        em.persist(typtest);

        Användare test = new Användare();
        test.setAnvändarnamn("asdf-1");
        test.setPin("0000");
        test.setFulltNamn("Asdf Asdfsson");
        test.setAnvändartyp(typtest);

        em.persist(test); //effektivt som att säga "detta objekt ska persista=finnas kvar"
        em.getTransaction().commit(); //skickar faktiska ändringar till databasen
    }
}
