package db;

import model.Användare;
import model.Bok;
import model.Film;
import state.ApplicationState;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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
     * @param searchterm som matchar något av författare,
     * @return
     */
    public Bok searchAndGetBooks(String searchterm) {
        String search = searchterm.toLowerCase();
        EntityManager em = dbc.getEntityManager();

        //em.createStoredProcedureQuery()

        return null; //TODO remove this line
    }



    public Film searchFilm() {

        return null; //TODO remove
    }

    public Bok searchTidskrift() {

        return null; //TODO remove
    }


}
