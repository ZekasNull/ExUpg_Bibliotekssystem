package test;

import model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class db_test {
    private static EntityManagerFactory emf;




    public static void main(String[] args) {

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
