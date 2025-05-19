package state;

import d0024e.exupg_bibliotekssystem.MainApplication;
import d0024e.exupg_bibliotekssystem.ViewLoader;
import db.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Användare;
import model.Bok;
import model.Film;
import service.BookDatabaseService;

import java.util.ArrayList;
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
    public final boolean DEBUGPRINTS = MainApplication.DEBUGPRINTS;

    //servicereferenser
    public MainApplication app;
    public final DatabaseService databaseService;
    private static ApplicationState instance;
    public ViewLoader vy;

    //data
    private List<Bok> bookSearchResults;
    private List<Film> filmSearchResults;
    private Användare currentUser;
    private ObservableList<BorrowItemInterface> borrowList;
    public ObservableList<BorrowItemInterface> getBorrowList() {
        if (borrowList == null) {
            ArrayList<BorrowItemInterface> locallist = new ArrayList<BorrowItemInterface>();
            borrowList = FXCollections.observableArrayList(locallist);
        }
        return borrowList;
    }
    //updates
    public enum UpdateType {
        BOOK, FILM, USER
    }



    //singleton-konstruktor
    public static ApplicationState getInstance() {
        if (instance == null) {
            instance = new ApplicationState();
        }
        return instance;
    }
    private ApplicationState() {
        this.databaseService = new DatabaseService();
        this.vy = new ViewLoader();
    }

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
}
