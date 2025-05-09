package state;

import d0024e.exupg_bibliotekssystem.MainApplication;
import db.DatabaseService;
import model.Användare;

import java.awt.print.Book;
import java.util.Observable;

/**
 * Represents the state of the application and serves as a central point for
 * managing the current state and services accessible across the application.
 * This class extends Observable, allowing components to observe and react to changes.
 * (tack för beskrivningen vilken AI det nu är)
 */
public class ApplicationState extends Observable {
    //sub-states
    //private final UserState userState;
    //private final SearchState searchState;
    //private final DatabaseService databaseService;

    //servicereferenser
    public final DatabaseService databaseService;
    private static ApplicationState instance;

    //testvariabler
    public MainApplication app;


    private Användare currentUser; //FIXME testvariabel


    public ApplicationState() {
        this.databaseService = new DatabaseService(this);
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
