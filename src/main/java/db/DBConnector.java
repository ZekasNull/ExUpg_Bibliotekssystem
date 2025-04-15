package db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Representerar databasanslutningen. Bör skapas när programmet startar.
 */
public class DBConnector {

    private static final String PERSISTENCE_UNIT_NAME = "Bibliotek_PU";
    private static DBConnector instance;
    private EntityManagerFactory entityManagerFactory;


    private DBConnector() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    /**
     * För singletons får instanser inte skapas. Den skapar en instans om ingen finns, annars ges referens.
     */
    public static DBConnector getInstance() {
        if (instance == null) {
            instance = new DBConnector();
        }
        return instance;
    }

    /**
     * Skapar ny entitymanager som cirka typ representerar en transaktion.
     * @return referens till en ny EntityManager
     */
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Fabriken bör stängas när programmet stängs
     */
    public void shutdown() {
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
