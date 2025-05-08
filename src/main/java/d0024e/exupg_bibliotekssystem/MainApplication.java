package d0024e.exupg_bibliotekssystem;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import state.ApplicationState;

public class MainApplication extends Application {
    private ApplicationState APPSTATE;
    private Stage primaryStage;

    public MainApplication() {

    }

    @Override
    public void init() throws Exception {
        this.APPSTATE = new ApplicationState();
        APPSTATE.app = this; //referens till MainApplication
        //yep detta går att loopa oändligt app.state.app.state etc. Kanske dåligt?
    }

    @Override //Starts the program and sets up the primaryStage
    public void start(Stage var1) throws Exception {
        primaryStage = var1;
        //openViewChoice();
        openLoggedOutView();
        //NOTE: översta för det gamla UIt(inlogg), understa för det nya(sökning)
    }
    /* For me and my lousey memory :P - essentially the outline for it to work
    public static void openWindowName() {
    try{
        FXMLLoader lname = new FXMLLoader(MainApplication.class.getResource("fxml-file-name.fxml));
        Scene sname = new Scene(lname.load());
        primaryStage.setTitle("Window name");
        primaryStage.setScene(sname);
        primaryStage.show();
    }catch()Exception e){
        e.printStackTrace();
        }
    }
    Kolla Eventhandler TipCalculator, anonymous inner class Tiplabel.setTitle, eller med Lambda, lambda är att föredra
    * */
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
    public void openLoggedOutView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("first-logged-out-view.fxml"));
            Scene scene = new Scene(loader.load());

            Controller controller = loader.getController();
            controller.setState(APPSTATE);
            APPSTATE.addObserver(controller);

            primaryStage.setTitle("Logged Out View");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}