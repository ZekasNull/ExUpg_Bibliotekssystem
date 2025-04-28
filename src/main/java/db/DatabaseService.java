package db;

import model.Användare;
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

    public boolean logInLibrarian(String användarnamn, String pin) {
        EntityManager em = dbc.getEntityManager();
        TypedQuery<Användare> query = em.createQuery(
                "SELECT u FROM Användare u WHERE u.användarnamn = :username AND u.pin = :pin", Användare.class);
        query.setParameter("username", användarnamn);
        query.setParameter("pin", pin);
        try
        {
            Användare user = query.getSingleResult(); //användarnamn är unikt, måste vara 1 eller inget resultat
            //FIXME Inte bra att användartyp har samma namn på table och kolumn
            if (!user.getAnvändartyp().getAnvändartyp().equalsIgnoreCase("bibliotekarie")) {
                System.out.println("User is not a librarian");
                state.setCurrentUser(null); //endast för att testa notifyobservers vid användartypfail
            }else{
                state.setCurrentUser(user);
            }
        }
        catch (NoResultException e)
        {
            System.out.println("dbservice: I scream because error was handled"); //debug
            return false; //användare hittades inte, inloggning misslyckas (nödvändigt för att visa alert vid misslyckande)
        }
        em.close();
        return true; //användare hittades, inloggning lyckas (oviktigt, sköts via notifyObservers)
    }
}
