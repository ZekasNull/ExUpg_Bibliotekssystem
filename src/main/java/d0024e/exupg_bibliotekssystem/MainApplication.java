package d0024e.exupg_bibliotekssystem;

import javafx.application.Application;
import javafx.stage.Stage;
import state.ApplicationState;

import javax.swing.text.View;

public class MainApplication extends Application {
    public static final boolean DEBUGPRINTS = true;
    private ApplicationState APPSTATE;
    private Stage primaryStage;

    public MainApplication() {
    }

    @Override
    public void init() throws Exception {
        this.APPSTATE = ApplicationState.getInstance();
        APPSTATE.app = this; //referens till MainApplication, kommer förmodligen inte behövas för evigt
    }

    @Override //Starts the program and sets up the primaryStage
    public void start(Stage var1) throws Exception {
        ViewLoader.initialize(var1, APPSTATE);
        ViewLoader.setView("Huvudmeny");
    }
    public static void main(String[] args) {
        launch(args);
    }
}