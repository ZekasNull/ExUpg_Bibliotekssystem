package d0024e.exupg_bibliotekssystem;

import javafx.application.Application;
import javafx.stage.Stage;
import state.ApplicationState;

public class MainApplication extends Application {
    public static final boolean DEBUGPRINTS = true;
    private ApplicationState APPSTATE;

    public MainApplication() {
    }

    @Override
    public void init() throws Exception {
        this.APPSTATE = ApplicationState.getInstance();
        APPSTATE.app = this; //referens till MainApplication, kommer förmodligen inte behövas för evigt
    }

    @Override //Starts the program and sets up the primaryStage
    public void start(Stage var1) throws Exception {
        APPSTATE.vy.initialize(var1);
        APPSTATE.vy.setView("Huvudmeny");
    }
    public static void main(String[] args) {
        launch(args);
    }
}