package db;

import model.*;
import org.postgresql.util.PSQLException;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

public class DatabaseService {
    private final boolean debugPrints = true;
    private final DBConnector dbc;

    public DatabaseService() {
        this.dbc = DBConnector.getInstance();
    }

    /**
     * Anropar en stored procedure för att hitta lån som inte är återlämnade.
     * (anledningen till stored procedure är att java inte verkade hantera timestamps smidigt nog)
     * @return En lista av lån.
     */
    @SuppressWarnings("unchecked") //shush java
    public List<Lån> visaEjÅterlämnadeBöcker () {
        List<Lån> försenade;
        EntityManager em = dbc.getEntityManager();

        try {
            em.getTransaction().begin();
            försenade = em.createNativeQuery("SELECT * FROM bibliotekssystem.sf_find_overdue_loans()", Lån.class)
                    .getResultList();
            em.getTransaction().commit();
            return försenade;

        } finally {
            em.close();
        }


    }


    /**
     * Lägger till allt från böcker till exemplar till lån i db.
     * Förutsätter att objektet inte har ett id, annars finns risk att existerande objekt uppdateras.
     * @param objekt En lista med objekt som itereras över för att läggas till
     * @throws PSQLException
     */
    public void läggTillNyaObjekt (List<?> objekt) throws Exception{
        EntityManager em = dbc.getEntityManager();

        try {
            em.getTransaction().begin();

            for (Object o : objekt) {
                em.persist(o); //nya objekt spåras med persist

            }
            em.getTransaction().commit(); //utför
        } catch (Exception e) {
            rollbackAndFindDatabaseError(e, em);
        }finally {
            em.close();
        }
    }

    /**
     * Tar en lista objekt av obestämd typ och raderar dem från databasen.
     * @param deletionList Lista över objekt som ska raderas (varje objekt måste ha ett giltigt ID)
     * @throws PSQLException när fel uppstår i databasen (sannolikt att det finns andra objekt som beror på objektet - då får det inte raderas då on cascade/delete inte tillåter det)
     */
    public void raderaObjekt(List<?> deletionList) throws Exception {
        //local
        EntityManager em = dbc.getEntityManager();

        try {
            em.getTransaction().begin();

            for (Object o : deletionList) {
                Object temp = em.merge(o); //em spårar objekt igen
                em.remove(temp); //ta bort från db
            }
            em.getTransaction().commit(); //utför ändringar
        } catch (Exception e) {
            System.out.println( "dbservice: raderaObjekt: " + e.getMessage());
            rollbackAndFindDatabaseError(e, em);
        } finally {
            em.close();
        }
    }

    /**
     * Tar en lista av ändrade objekt och uppdaterar dem i databasen. Obs, kräver existerande ID, annars finns risk att nya objekt skapas.
     * @param changeList Listan av objekt som ska ändras
     */
    public void ändraObjekt(List<?> changeList) throws Exception {
        EntityManager em = dbc.getEntityManager();

        try {
            em.getTransaction().begin();
            for (Object toUpdate : changeList) {
                em.merge(toUpdate); //attach detached entity
            }
            em.getTransaction().commit(); //skickar upp ändringar i databasen
        } catch (Exception e) {
            rollbackAndFindDatabaseError(e, em);
        } finally {
            em.close();
        }
    }

    public Ämnesord searchAmnesord(String searchterm) throws Exception {
        EntityManager em = dbc.getEntityManager();
        Ämnesord result = null;


        try {
            em.getTransaction().begin();
            TypedQuery<Ämnesord> query = em.createQuery("SELECT am FROM Ämnesord am WHERE am.ord = :sökterm", Ämnesord.class);
            query.setParameter("sökterm", searchterm);
            result = query.getSingleResult();
            em.getTransaction().commit(); //skickar ändringar
        } catch (Exception e) {
            rollbackAndFindDatabaseError(e, em);
        } finally {
            em.close();
        }
        if (debugPrints) System.out.println("dbservice: searchAmnesord ran and is returning " + result);
        return result;
    }

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
        finally{
            em.close();
        }
        return user;
    }

    /**
     * Söker efter böcker med hjälp av sökterm, matchande något av titel, isbn-13, ämnesord eller författare (för/efternamn)
     * @param searchterm en enkel string
     * @return List<Bok> av alla resultat som kan vara tom om inget hittades
     */
    public List<Bok> searchAndGetBooks(String searchterm) {
        //metodvariabler
        List<Bok> resultlist;
        EntityManager em = dbc.getEntityManager();

        //query
        try {
            em.getTransaction().begin();

            TypedQuery<Bok> query = em.createQuery(
                    "SELECT DISTINCT b FROM Bok b " +
                            "LEFT JOIN FETCH b.Författare a " +
                            "LEFT JOIN b.Ämnesord k " +
                            "WHERE LOWER(b.titel) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
                            "OR LOWER(b.isbn13) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
                            "OR LOWER(a.förnamn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
                            "OR LOWER(a.efternamn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
                            "OR LOWER(k.ord) LIKE LOWER(CONCAT('%', :searchTerm, '%'))", Bok.class);

            query.setParameter("searchTerm", searchterm);

            resultlist = query.getResultList();

            for (Bok bok : resultlist) {
                bok.getÄmnesord().size();
            } //tvinga em att ladda ämnesord lazily för att undvika en fetch som inte beter sig i query

            em.getTransaction().commit(); //run query

            return resultlist;

        } finally {
            //runs after return in try block is encountered, but before it is executed
            em.close();
        }
    }

    /**
     * Söker efter filmer med hjälp av sökterm, matchande något av titel, produktionsland, åldersgräns,
     * genre, regissör (för/efternamn) eller skådespelare (för/efternamn)
     * @param searchTerm en enkel string
     * @return en List<Film> av resultat som kan vara tom om inget hittades
     */
    public List<Film> searchAndGetFilms(String searchTerm) {
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

    /**
     * Hjälpmetod för att hitta databasfel
     * @param e Felet som fångades
     * @param em EntityManager inblandad
     * @throws PSQLException om databasen hade ett fel, kastar annars om det ursprungliga felet
     */
    private static void rollbackAndFindDatabaseError(Exception e, EntityManager em) throws Exception {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        Throwable error = e.getCause();
        while (error != null) {
            if(error instanceof PSQLException){
                throw (PSQLException) error;
            }
            error = error.getCause();
        }
        throw e;
    }
}
