package d0024e.exupg_bibliotekssystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    private static Stage primaryStage;

    @Override //Starts the program and sets up the primaryStage
    public void start(Stage var1) throws Exception {
        MainApplication.primaryStage = var1;
        openViewChoice();
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
    TODO: Add in prompt to check if user is logged in? - Should be done in controllers
    TODO: Se till så allt matchar med hur jag namngivit det (man kan tro att det är olika personer ibland)
    * */
    public static void openViewChoice() {
        try{
            //Load the fxml file onto "loader"
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("view-choice.fxml"));
            //Loads the FXML file as a scene
            Scene scene = new Scene(loader.load());
            //Sets up the Stage with the scene and the title and give it to our primaryStage
            primaryStage.setTitle("View Choice");
            primaryStage.setScene(scene);
            //Shows the stage
            primaryStage.show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void openUserFirstView() {
        try{
            //Loads the FXML file onto "loader"
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-first-view.fxml"));
            //Loads the FXML file as a scene
            Scene scene = new Scene(loader.load());
            //Sets up the Stage with the scene and the title and give it to our primaryStage
            primaryStage.setTitle("User First View");
            primaryStage.setScene(scene);
            //Shows the stage
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void openUserSearchView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-search-view.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("User Search View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void openUserLoginView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-login-view.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("User Login View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void openUserLoanViewRegisterView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-loan-register-view.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("User Loan Register View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void openUserLoanConfirmationView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-loan-confirmation-view.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("User Loan Register View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void openUserRecieptView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-reciept-view.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("User Reciept View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void openUserReturnView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-return-register-view.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("User Return View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void openUserReturnConfirmationView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-return-confirmation-view.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("User Return Confirmation View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void openUserReturnLastView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-return-last-view.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("User Return Last View");
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