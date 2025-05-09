package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Användare;
import model.Användartyp;
import state.ApplicationState;

import java.util.Observable;

public class LoginViewController extends Controller{
    private ApplicationState state = ApplicationState.getInstance();
    @FXML
    public Button logInButton;
    public TextField idealBoxContents;
    public PasswordField pinBoxContents;
    public Scene scene;

    public FirstLoggedOutViewController mainMenuController;

    public void setMainMenuController(FirstLoggedOutViewController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }
    public void onLogInButtonCLick(ActionEvent actionEvent) {
        //TODO kontrollern ska ansvara för att analysera informationen i användaren som hämtas (dvs rätt användartyp)
        System.out.println("onloginbutton");
        state.setCurrentUser(state.databaseService.logInUser(idealBoxContents.getText(), pinBoxContents.getText()));
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("State changed, updating loginview");
        boolean success = state.getCurrentUser() != null;
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Information");
        alert.setHeaderText("Login attempt");
        alert.setContentText("The login attempt was" + (success ? "" : "n't") + " successful.");

        alert.showAndWait();
    }

    public ApplicationState getState() {
        return state;
    }

    public void setState(ApplicationState state) {
        this.state = state;
    }
}

