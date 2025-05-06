package db;

import model.Användare;
import model.Bok;
import model.Film;
import state.ApplicationState;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class DatabaseService {
    private final DBConnector dbc = DBConnector.getInstance();
    private final ApplicationState state;

    /**
     * Tjänst för handlingar mot databasen.
     *
     * @param state Referens till applikationens state där resultat ska sparas.
     */
    public DatabaseService(ApplicationState state) {
        this.state = state;
    }

    /* public static void main(String[] args) {
        DatabaseService test = new DatabaseService(new ApplicationState());
        test.logInLibrarian("aaaasdf", "0000");
    } */

    /**
     * "Loggar in" genom att kolla om en användare med den pin-koden existerar.
     * @param användarnamn (unikt i databasen)
     * @param pin
     * @return Användare om den hittas, annars null
     */
    public Användare logInUser(String användarnamn, String pin) {
        //local var
        EntityManager em = dbc.getEntityManager();
        Användare user = null;

        //query
        TypedQuery<Användare> query = em.createQuery(
                "SELECT u FROM Användare u WHERE u.användarnamn = :username AND u.pin = :pin", Användare.class);
        query.setParameter("username", användarnamn);
        query.setParameter("pin", pin);

        //kör query
        try
        {
            user = query.getSingleResult(); //användarnamn är unikt, måste vara 1 eller inget resultat
        }
        catch (NoResultException e)
        {
            System.out.println("dbservice: Ingen användare med den kombinationen hittades"); //debug
        }
        em.close();
        return user;
    }

    /**
     * Söker efter böcker med hjälp av sökterm, matchande något av titel, isbn-13, ämnesord eller författare (för/efternamn)
     * @param searchterm en enkel string
     * @return List<Bok> av alla resultat, eller null om det inte fanns något resultat.
     */
    public List<Bok> searchAndGetBooks(String searchterm) {
        //metodvariabler
        List<Bok> resultlist;
        EntityManager em = dbc.getEntityManager();

        //query
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

            query.setParameter("searchTerm", searchterm);

            resultlist = query.getResultList();

            em.getTransaction().commit(); //run query

            return resultlist;

        } finally {
            //runs after return in try block is encountered, but before it is executed
            em.close();
        }
    }



    public List<Film> searchFilm(String searchTerm) {
        EntityManager em = dbc.getEntityManager();

        try {
            em.getTransaction().begin();

            TypedQuery<Film> query = em.createQuery(
                    "SELECT DISTINCT f FROM Film f " +
                            "LEFT JOIN FETCH f.skådespelares a " +
                            "LEFT JOIN FETCH f.regissörs d " +
                            "LEFT JOIN FETCH f.genres g " +
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

            em.getTransaction().commit();

            return results;
        } finally {
            em.close();
        }
    }

    //TODO Implementera men inte prioritet
    public Bok searchTidskrift() {

        return null; //TODO remove
    }


}
