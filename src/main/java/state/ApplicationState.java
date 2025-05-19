package state;

import d0024e.exupg_bibliotekssystem.MainApplication;
import service.ViewLoader;
import model.Användare;
import model.Bok;
import model.Film;
import service.BookDatabaseService;
import service.FilmDatabaseService;
import service.UserDatabaseService;

import java.util.List;
import java.util.Observable;

/**
 * Represents the state of the application and serves as a central point for
 * managing the current state and services accessible across the application.
 * This class extends Observable, allowing components to observe and react to changes.
 * (tack för beskrivningen vilken AI det nu är)
 */
public class ApplicationState extends Observable {
    //debug
    private final boolean DEBUGPRINTS = MainApplication.DEBUGPRINTING;

    //databastjänster
    private BookDatabaseService bookDatabaseService;
    private FilmDatabaseService filmDatabaseService;
    private UserDatabaseService userDatabaseService;

    //programtjänster
    private ViewLoader viewLoaderService;


    //data
    private List<Bok> bookSearchResults;
    private List<Film> filmSearchResults;
    private Användare currentUser;



    //enum för uppdateringstyp
    public enum UpdateType {
        BOOK, FILM, USER
    }

    //singleton-konstruktor
//    public static ApplicationState getInstance() {
//        if (instance == null) {
//            instance = new ApplicationState();
//        }
//        return instance;
//    }
    public ApplicationState() {
        //skapa databastjänstreferenser
        bookDatabaseService = new BookDatabaseService();
        filmDatabaseService = new FilmDatabaseService();
        userDatabaseService = new UserDatabaseService();

        //skapa programtjänstreferenser
        viewLoaderService = new ViewLoader(this);
    }

    //getters och setters för tjänster

    public ViewLoader getViewLoaderService() {
        return viewLoaderService;
    }

    public UserDatabaseService getUserDatabaseService() {
        return userDatabaseService;
    }
    public void setUserDatabaseService(UserDatabaseService userDatabaseService) {
        this.userDatabaseService = userDatabaseService;
    }

    public FilmDatabaseService getFilmDatabaseService() {
        return filmDatabaseService;
    }
    public void setFilmDatabaseService(FilmDatabaseService filmDatabaseService) {
        this.filmDatabaseService = filmDatabaseService;
    }

    public BookDatabaseService getBookDatabaseService() {
        return bookDatabaseService;
    }
    public void setBookDatabaseService(BookDatabaseService bookDatabaseService) {
        this.bookDatabaseService = bookDatabaseService;
    }

    //getters och setters för data

    public List<Bok> getBookSearchResults() {
        return bookSearchResults;
    }
    public void setBookSearchResults(List<Bok> bookSearchResults) {
        if (DEBUGPRINTS) System.out.println("ApplicationState: Setting bookSearchResults and notifying observers.");
        this.bookSearchResults = bookSearchResults;
        setChanged();
        notifyObservers(UpdateType.BOOK);
    }

    public Användare getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(Användare currentUser) {
        if (DEBUGPRINTS) System.out.println("ApplicationState: Setting currentUser and notifying observers.");
        this.currentUser = currentUser;
        setChanged();
        notifyObservers(UpdateType.USER);
    }

    public List<Film> getFilmSearchResults() {
        return filmSearchResults;
    }
    public void setFilmSearchResults(List<Film> filmSearchResults) {
        this.filmSearchResults = filmSearchResults;
        setChanged();
        notifyObservers(UpdateType.FILM);
    }

    //metoder

    /**
     * Om användarens information behöver uppdateras. Försöker hitta nuvarande användare i databasen och uppdatera.
     */
    public void updateUserInformation() {
        if (DEBUGPRINTS) System.out.println("ApplicationState: Updating user information.");
        currentUser = userDatabaseService.getUser(currentUser);
        setChanged();
        notifyObservers(UpdateType.USER);
    }


}
