package test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class db_test {
    private static EntityManagerFactory emf;


    //attempts to find a user with provided vars
    public static boolean userExists(EntityManager em, String användarnamn, String pin) {
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM Användare u WHERE u.användarnamn = :användarnamn AND u.pin = :pin", Long.class)
                .setParameter("användarnamn", användarnamn)
                .setParameter("pin", pin)
                .getSingleResult();

        return count > 0;
    }

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("Bibliotek_PU");
        EntityManager test = emf.createEntityManager();

        System.out.println(userExists(test, "asdf-1", "0000"));

        // Quick test
        /*
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
        */



        // Finally, close the factory upon application exit
        emf.close();
    }
}
