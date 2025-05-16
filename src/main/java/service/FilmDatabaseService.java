package service;

import db.DBConnector;
import model.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 */
public class FilmDatabaseService {

    private final DBConnector DBC;
    //debug
    private final boolean debugPrints = true;

    public FilmDatabaseService() {
        this.DBC = DBConnector.getInstance();
    }

    public void addNewFilm(Film film) {
        if (debugPrints) System.out.println("filmdbservice: incoming film to add: " + film.getTitel());
        EntityManager em = DBC.getEntityManager();

        try {
            em.getTransaction().begin();
            //ensure any new directors, genres and actors are persisted
            for (Skådespelare s : film.getSkådespelares()) {
                if (s.getId() == null) {
                    em.persist(s);
                }
            }
            for(Genre g : film.getGenres()){
                if(g.getId() == null){
                    em.persist(g);
                }
            }
            for (Regissör r : film.getRegissörs()) {
                if (r.getId() == null) {
                    em.persist(r);
                }
            }

            em.persist(film); //nya objekt spåras med persist
            em.getTransaction().commit(); //utför
        }finally {
            em.close();
        }
    }

    public void updateFilm(Film film) {
        EntityManager em = DBC.getEntityManager();
        if (debugPrints) System.out.println("filmdbservice: incoming film to update: " + film.getTitel() +
                "\n State in database: " + em.find(Film.class, film));


        try {
            em.getTransaction().begin();
            //hantera om det finns nya regissörer/skådespelare/genres
            //nytt id ges av db, så saknas det är det ny entitet
            for (Regissör r : film.getRegissörs()) {
                if (r.getId() == null) {
                    em.persist(r);
                }
            }
            for(Genre g : film.getGenres()){
                if(g.getId() == null){
                    em.persist(g);
                }
            }
            for (Skådespelare s : film.getSkådespelares()) {
                if (s.getId() == null) {
                    em.persist(s);
                }
            }

            em.merge(film); //detached objekt uppdateras
            em.getTransaction().commit(); //utför
        }finally {
            em.close();
        }
    }

    public void deleteFilm(Film film) {
        if (debugPrints) System.out.println("filmdbservice: incoming film to delete: " + film.toString());
        EntityManager em = DBC.getEntityManager();

        try {
            em.getTransaction().begin();
            Object temp = em.merge(film); //em spårar objekt igen
            em.remove(temp); //ta bort från db
            em.getTransaction().commit(); //utför ändringar
        }finally {
            em.close();
        }
    }
    
    public void deleteFilmCopy(Exemplar ex) {
        if (debugPrints) System.out.println("filmdbservice: incoming film copy to delete: " + ex.toString());
        EntityManager em = DBC.getEntityManager();

        try {
            em.getTransaction().begin();
            Exemplar temp = em.find(Exemplar.class, ex.getStreckkod()); //hämta ex från db
            temp.getFilm_id().getExemplars().remove(temp); //pga fetching måste den raderas från films lista också
            em.remove(temp);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Söker i databasen efter en regissör eller skapar en ny om den sökte inte fanns.
     * @param names där [0] är förnamn och [1] efternamn.
     * @return existerande regissör om denne hittades, annars en helt ny
     */
    public Regissör findOrCreateDirector(String[] names) {
        EntityManager em = DBC.getEntityManager();
        Regissör result = null;

        TypedQuery<Regissör> query = em.createQuery("SELECT r FROM Regissör r WHERE r.förnamn = :förnamn AND r.efternamn = :efternamn", Regissör.class);
        query.setParameter("förnamn", names[0]);
        query.setParameter("efternamn", names[1]);
        try {
            em.getTransaction().begin();
            result = query.getSingleResult();
            if (debugPrints) System.out.println("dbservice: director found!! in db, returning existing");
        }catch (NoResultException e) {
            if (debugPrints) System.out.println("dbservice: director not found in db, creating new");
            result = new Regissör();
            result.setFörnamn(names[0]);
            result.setEfternamn(names[1]);
        }finally {
            em.close();
        }
        return result;
    }

    public Skådespelare findOrCreateActor(String[] names) {
        EntityManager em = DBC.getEntityManager();
        Skådespelare result = null;

        TypedQuery<Skådespelare> query = em.createQuery("SELECT s FROM Skådespelare s WHERE s.förnamn = :förnamn AND s.efternamn = :efternamn", Skådespelare.class);
        query.setParameter("förnamn", names[0]);
        query.setParameter("efternamn", names[1]);
        try {
            em.getTransaction().begin();
            result = query.getSingleResult();
            if (debugPrints) System.out.println("dbservice: actor found!! in db, returning existing");
        }catch (NoResultException e) {
            if (debugPrints) System.out.println("dbservice: actor not found in db, creating new");
            result = new Skådespelare();
            result.setFörnamn(names[0]);
            result.setEfternamn(names[1]);
        }finally {
            em.close();
        }
        return result;
    }

    public Genre findOrCreateGenre(String genreName) {
        EntityManager em = DBC.getEntityManager();
        Genre result;

        TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g WHERE g.genreNamn = :genre", Genre.class);
        query.setParameter("genre", genreName);
        try {
            em.getTransaction().begin();
            result = query.getSingleResult();
            if (debugPrints) System.out.println("dbservice: genre found!! in db, returning existing");
        }catch (NoResultException e) {
            if (debugPrints) System.out.println("dbservice: genre not found in db, creating new");
            result = new Genre();
            result.setGenreNamn(genreName);
        }finally {
            em.close();
        }
        return result;
    }
}
