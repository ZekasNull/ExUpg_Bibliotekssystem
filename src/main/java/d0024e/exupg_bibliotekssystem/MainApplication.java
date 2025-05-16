package d0024e.exupg_bibliotekssystem;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import state.ApplicationState;
import java.io.IOException;

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
        primaryStage = var1;
        this.APPSTATE.vy = new ViewLoader(primaryStage);
        //openViewChoice();
        APPSTATE.vy.loadScene("main-menu.fxml", "Main Menu");
        //openAddBookView(); //FIXME test
        //openAddFilmView(); //FIXME test
    }

    public void openLoginView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(loader.load());

            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            APPSTATE.addObserver(controller);

            Stage loginStage = new Stage();
            loginStage.setTitle("Login View");
            loginStage.setScene(scene);
            loginStage.initModality(Modality.APPLICATION_MODAL); //Så inget kan göras runtom pop-up fönstret
            loginStage.show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
//NOTE: flytta upp vilken som om vi vill testa specifik sida, det ovan är det som är tänkt ska stanna
/*

    public void openAddBookView() throws IOException {
        //Load the fxml file onto "loader"
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("add-book-view.fxml"));
        //Loads the FXML file as a scene
        Scene scene = new Scene(loader.load());

        Controller controller = loader.getController();
        controller.setState(APPSTATE); //ge referens till appstate
        APPSTATE.addObserver(controller);

        primaryStage.setTitle("Hantera böcker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void openAddFilmView() throws IOException {
        //Load the fxml file onto "loader"
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("add-movie-view.fxml"));
        //Loads the FXML file as a scene
        Scene scene = new Scene(loader.load());

        Controller controller = loader.getController();
        controller.setState(APPSTATE); //ge referens till appstate
        APPSTATE.addObserver(controller);

        primaryStage.setTitle("Hantera filmer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void openViewChoice() {
        try{
            //Load the fxml file onto "loader"
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("view-choice.fxml"));
            //Loads the FXML file as a scene
            Scene scene = new Scene(loader.load());

            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate

            //Sets up the Stage with the scene and the title and give it to our primaryStage
            primaryStage.setTitle("View Choice");
            primaryStage.setScene(scene);
            //Shows the stage
            primaryStage.show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void openLibrarianLoginView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("librarian-login-view.fxml"));
            Scene scene = new Scene(loader.load());

            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            APPSTATE.addObserver(controller);

            primaryStage.setTitle("Librarian Login View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}*/