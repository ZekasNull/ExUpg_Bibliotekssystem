package service;

import d0024e.exupg_bibliotekssystem.MainApplication;
import db.DBConnector;
import model.Användare;
import model.Lån;
import org.postgresql.util.PSQLException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Hanterar databasfrågor gällande användare och deras lån.
 */
public class UserDatabaseService {
    private static final boolean DEBUGPRINTS = MainApplication.DEBUGPRINTING;
    private final DBConnector DBC;

    public UserDatabaseService() {
        this.DBC = DBConnector.getInstance();
    }

    /**
     * "Loggar in" genom att kolla om en användare med den pin-koden existerar.
     *
     * @param användarnamn (unikt i databasen)
     * @param pin
     * @return Användare om den hittas, annars null
     */
    public Användare logInUser(String användarnamn, String pin) {
        //local var
        EntityManager em = DBC.getEntityManager();
        Användare user = null;

        //query
        TypedQuery<Användare> query = em.createQuery(
                "SELECT u FROM Användare u WHERE u.användarnamn = :username AND u.pin = :pin", Användare.class);
        query.setParameter("username", användarnamn);
        query.setParameter("pin", pin);

        //kör query
        try {
            user = query.getSingleResult(); //användarnamn är unikt, måste vara 1 eller inget resultat

        } catch (NoResultException e) {
            System.out.println("dbservice: Ingen användare med den kombinationen hittades"); //debug
        } finally {
            em.close();
        }
        return user;
    }

    /**
     * Hämtar en uppdaterad användare med ett användarobjekt som är detached.
     * @param a Användaren som söks
     * @return Användareobjekt från databasen
     */
    public Användare getUser(Användare a) {
        EntityManager em = DBC.getEntityManager();
        Användare foundUser;
        try {
            em.getTransaction().begin();
            foundUser = em.find(Användare.class, a.getId());
            em.getTransaction().commit();
            return foundUser;
        }finally {
            em.close();
        }
    }

    /**
     * Lägger till en lista med lån i databasen.
     * Förutsätter att objektet inte har ett id, annars finns risk att existerande objekt uppdateras.
     *
     * @param låns En lista med lån som itereras över för att läggas till
     * @throws PSQLException om lån inte kan läggas till. Kan bero på lånegräns eller att ex redan är lånat.
     */
    public void läggTillNyaLån(List<Lån> låns) throws PSQLException {
        EntityManager em = DBC.getEntityManager();

        try {
            em.getTransaction().begin();
            for (Lån l : låns) {
                em.persist(l); //nya objekt spåras med persist
            }
            em.getTransaction().commit(); //utför
        } catch (Exception e) {
            em.getTransaction().rollback();
            Throwable error = e.getCause();
            while (error != null) {
                if (error instanceof PSQLException) {
                    throw (PSQLException) error;
                }
                error = error.getCause();
            }
            throw e;
        }finally {
            em.close();
        }
    }

    /**
     * Återlämnar ett lån genom att radera det från databasen.
     * @param lån Det lån som ska raderas.
     */
    public void returnLoan(Lån lån) {
        //metodvariabler
        EntityManager em = DBC.getEntityManager();
        Lån managedLån;


        em.getTransaction().begin();

        //query
        try {
            managedLån = em.merge(lån);
            em.remove(managedLån);
            em.getTransaction().commit(); //utför
        }finally {
            em.close();
        }
    }

    /**
     * Lånets returdatum måste beräknas från lånedatumet + låntypen.
     * @param lån Lånet vars returdatum söks
     * @return returdatum för lånet som Instant
     */
    public Instant getReturnDateForLoan(Lån lån) {
        if(lån.getId() == null) throw new IllegalArgumentException("Lånet måste ha ett ID");
        EntityManager em = DBC.getEntityManager();
        Query returnDateQuery = em.createNamedQuery("getReturnDate");
        returnDateQuery.setParameter(1, lån.getId());

        try {
            em.getTransaction().begin();
            Timestamp returdatum = (Timestamp) returnDateQuery.getSingleResult();
            em.getTransaction().commit();
            return returdatum.toInstant();
        } finally {
            em.close();
        }
    }

    /**
     * Anropar en stored procedure för att hitta lån som inte är återlämnade.
     * (anledningen till stored procedure är att java inte verkade hantera timestamps smidigt nog)
     *
     * @return En lista av lån.
     */
    @SuppressWarnings("unchecked") //shush java
    public List<Lån> visaEjÅterlämnadeBöcker() {
        List<Lån> försenade;

        EntityManager em = DBC.getEntityManager();


        Query loanQuery = em.createNativeQuery("SELECT * FROM bibliotekssystem.sf_find_overdue_loans()", Lån.class);


        try {
            em.getTransaction().begin();
            //"Jag ska inte ha några deriverade data i databasen, det blir enklare så" - citat av idioten Linnea
            försenade = loanQuery.getResultList();

            for (Lån l : försenade) {
                l.setReturDatum(getReturnDateForLoan(l));
            }
            em.getTransaction().commit();
            return försenade;

        } finally {
            em.close();
        }


    }
}
