package d0024e.exupg_bibliotekssystem;

import controller.Controller;
import controller.LibrarianLoginViewController;
import controller.UserSearchViewController;
import controller.ViewChoiceController;
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
    public void openUserFirstView() {
        try{
            //Loads the FXML file onto "loader"
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-first-view.fxml"));
            //Loads the FXML file as a scene
            Scene scene = new Scene(loader.load());

            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate


            //Sets up the Stage with the scene and the title and give it to our primaryStage
            primaryStage.setTitle("User First View");
            primaryStage.setScene(scene);
            //Shows the stage
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openUserSearchView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-search-view.fxml"));
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate

            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("User Search View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openUserLoginView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-login-view.fxml"));
            Scene scene = new Scene(loader.load());

            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate

            primaryStage.setTitle("User Login View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openUserLoanViewRegisterView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-loan-register-view.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate

            primaryStage.setTitle("User Loan Register View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openUserLoanConfirmationView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-loan-confirmation-view.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            primaryStage.setTitle("User Loan Register View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openUserRecieptView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-reciept-view.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            primaryStage.setTitle("User Reciept View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openUserReturnView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-return-register-view.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            primaryStage.setTitle("User Return View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openUserReturnConfirmationView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-return-confirmation-view.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            primaryStage.setTitle("User Return Confirmation View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openUserReturnLastView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-return-last-view.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            primaryStage.setTitle("User Return Last View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openUserSearchResultView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-search-result-view.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            primaryStage.setTitle("User Search Result View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openUserSearchResultDetailsView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-search-result-details-view.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            primaryStage.setTitle("User Search Result Details View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
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
    public void openLibrarianFirstChoiceView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("librarian-first-choice-view.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            primaryStage.setTitle("Librarian First Choice View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openLibrarianHandleUserChoiceView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("librarian-handle-user-choice-view.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            primaryStage.setTitle("Librarian Handle User Choice View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openLibrarianChangeUserInformationView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("librarian-change-user-information.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            primaryStage.setTitle("Librarian Change User Information View");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openLibrarianDeleteUserView() {
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("librarian-delete-user-view.fxml"));
            Scene scene = new Scene(loader.load());
            //hämta referens till controller
            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            primaryStage.setTitle("Librarian Delete User View");
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