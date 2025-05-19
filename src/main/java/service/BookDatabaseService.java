package service;

import db.DBConnector;
import model.*;
import model.Författare;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class BookDatabaseService {
    private final DBConnector DBC;
    //debug
    private final boolean DEBUGPRINTS = true;

    public BookDatabaseService() {
        this.DBC = DBConnector.getInstance();
    }

    /**
     * Söker efter böcker med hjälp av sökterm, matchande något av titel, isbn-13, ämnesord eller författare (för/efternamn)
     *
     * @param searchterm en enkel string
     * @return List<Bok> av alla resultat som kan vara tom om inget hittades
     */
    public List<Bok> searchAndGetBooks(String searchterm) {
        //metodvariabler
        List<Bok> resultlist;
        EntityManager em = DBC.getEntityManager();

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

    public void addNewBook(Bok book) {
        if (DEBUGPRINTS) System.out.println("bookdbservice: incoming book to add: " + book.getTitel());
        EntityManager em = DBC.getEntityManager();

        try {
            em.getTransaction().begin();
            //ensure any new authors and keywords are persisted
            for (Författare f : book.getFörfattare()) {
                if (f.getId() == null) {
                    em.persist(f);
                }else{
                    f = em.merge(f);
                }
            }
            for(Ämnesord a : book.getÄmnesord()){
                if(a.getId() == null){
                    em.persist(a);
                }else{
                    a = em.merge(a);
                }
            }

            em.persist(book); //nya objekt spåras med persist
            em.getTransaction().commit(); //utför
        }finally {
            em.close();
        }
    }

    public void updateBook(Bok book) {
        EntityManager em = DBC.getEntityManager();

        try {
            em.getTransaction().begin();
            //hantera om det finns nya regissörer/skådespelare/genres
            //nytt id ges av db, så saknas det är det ny entitet
            for (Författare f : book.getFörfattare()) {
                if (f.getId() == null) {
                    em.persist(f);
                }
            }
            for(Ämnesord a : book.getÄmnesord()){
                if(a.getId() == null){
                    em.persist(a);
                }
            }

            em.merge(book); //detached objekt uppdateras
            em.getTransaction().commit(); //utför
        }finally {
            em.close();
        }
    }

    public void deleteBook(Bok book) {
        if (DEBUGPRINTS) System.out.println("bookdbservice: incoming book to delete: " + book.toString());
        EntityManager em = DBC.getEntityManager();

        try {
            em.getTransaction().begin();
            Object temp = em.merge(book); //em spårar objekt igen
            em.remove(temp); //ta bort från db
            em.getTransaction().commit(); //utför ändringar
        }finally {
            em.close();
        }

    }

    public void deleteBookCopy(Exemplar ex) {
        if (DEBUGPRINTS) System.out.println("bookdbservice: incoming book copy to delete: " + ex.toString());
        EntityManager em = DBC.getEntityManager();
        Exemplar managed;

        try {
            em.getTransaction().begin();
            managed = em.merge(ex);
            managed.getBok().getExemplars().remove(managed); //pga fetching måste den raderas från bokens lista också
            em.remove(managed);
            em.getTransaction().commit();
        } catch (EntityNotFoundException e) {
            if(e.getMessage().contains( "Unable to find model.Bok")){
                //kan kasta en exception om ett exemplar skulle raderas men boken blev raderad innan.
                if (DEBUGPRINTS) System.out.println("BookDatabaseService: Tried to delete a copy whose book does not exist or was already deleted.");
            }else{
                throw e;
            }

        } finally {
            em.close();
        }

    }

    public Ämnesord findOrCreateÄmnesord(String ord) {
        EntityManager em = DBC.getEntityManager();
        Ämnesord result;

        TypedQuery<Ämnesord> query = em.createQuery("SELECT a FROM Ämnesord a WHERE a.ord = :ord", Ämnesord.class);
        query.setParameter("ord", ord);
        try {
            em.getTransaction().begin();
            result = query.getSingleResult();
            if (DEBUGPRINTS) System.out.println("dbservice: Ämnesord found!! in db, returning existing");
        }catch (NoResultException e) {
            if (DEBUGPRINTS) System.out.println("dbservice: Ämnesord not found in db, creating new");
            result = new Ämnesord();
            result.setOrd(ord);
        }finally {
            em.close();
        }
        return result;
    }

    public Författare findOrCreateAuthor(String[] names) {
        EntityManager em = DBC.getEntityManager();
        Författare result;

        TypedQuery<Författare> query = em.createQuery("SELECT f FROM Författare f WHERE f.förnamn = :förnamn AND f.efternamn = :efternamn", Författare.class);
        query.setParameter("förnamn", names[0]);
        query.setParameter("efternamn", names[1]);
        try {
            em.getTransaction().begin();
            result = query.getSingleResult();
            if (DEBUGPRINTS) System.out.println("dbservice: författare found!! in db, returning existing");
        }catch (NoResultException e) {
            if (DEBUGPRINTS) System.out.println("dbservice: författare not found in db, creating new");
            result = new Författare();
            result.setFörnamn(names[0]);
            result.setEfternamn(names[1]);
        }finally {
            em.close();
        }
        return result;
    }
}
