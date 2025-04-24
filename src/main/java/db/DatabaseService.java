package db;

import model.Användare;
import state.ApplicationState;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class DatabaseService {
    private final DBConnector dbc = DBConnector.getInstance();
    private final ApplicationState state;

    public DatabaseService(ApplicationState state) {
        this.state = state;
    }

    //TODO kastar fel om ingen användare hittas, bör hanteras
    public void logInLibrarian(String användarnamn, String pin) {
        EntityManager em = dbc.getEntityManager();
        TypedQuery<Användare> query = em.createQuery(
                "SELECT u FROM Användare u WHERE u.användarnamn = :username AND u.pin = :pin", Användare.class);
        query.setParameter("username", användarnamn);
        query.setParameter("pin", pin);

        Användare user = query.getSingleResult(); //användarnamn är unikt, måste vara 1 eller inget resultat
        em.close();

        //FIXME Inte bra att användartyp har samma namn på table och kolumn
        if (!user.getAnvändartyp().getAnvändartyp().equalsIgnoreCase("bibliotekarie")) {
            System.out.println("User is not a librarian");
            state.setCurrentUser(null); //endast för att testa notifyobservers vid användartypfail
        }else{
            state.setCurrentUser(user);
        }

    }
}
