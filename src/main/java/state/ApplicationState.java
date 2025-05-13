package state;

import d0024e.exupg_bibliotekssystem.MainApplication;
import db.DatabaseService;
import model.Användare;
import model.Bok;

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
    private final boolean debugPrints = false;

    //servicereferenser
    public MainApplication app;
    public final DatabaseService databaseService;
    private static ApplicationState instance;

    public List<Bok> getBookSearchResults() {
        return bookSearchResults;
    }

    public void setBookSearchResults(List<Bok> bookSearchResults) {
        if (debugPrints) System.out.println("ApplicationState: Setting bookSearchResults and notifying observers.");
        this.bookSearchResults = bookSearchResults;
        setChanged();
        notifyObservers();
    }

    //
    private List<Bok> bookSearchResults;
    private Användare currentUser; //FIXME testvariabel


    private ApplicationState() {
        this.databaseService = new DatabaseService();
    }

    public static ApplicationState getInstance() {
        if (instance == null) {
            instance = new ApplicationState();
        }
        return instance;
    }


    public Användare getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(Användare currentUser) {
        this.currentUser = currentUser;
        setChanged();
        notifyObservers();
    }
}
