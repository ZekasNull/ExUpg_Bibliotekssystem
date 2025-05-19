package controller;

import d0024e.exupg_bibliotekssystem.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Användare;
import state.ApplicationState;

import java.util.Observable;

public class LoginViewController extends Controller{
    private final boolean DEBUGPRINTOUTS = MainApplication.DEBUGPRINTS;
    private ApplicationState state = ApplicationState.getInstance();
    @FXML
    public Button logInButton;
    public TextField idealBoxContents;
    public PasswordField pinBoxContents;

    public void onLogInButtonCLick(ActionEvent actionEvent) {
        if (idealBoxContents.getText().isEmpty() && pinBoxContents.getText().isEmpty()) {
            showErrorPopup("Du måste ange ditt ideal och ditt pinnummer");
            return;
        } else if (idealBoxContents.getText().isEmpty()) {
            showErrorPopup("Du måste ange ditt ideal");
            return;
        } else if (pinBoxContents.getText().isEmpty()) {
            showErrorPopup("Du måste ange ditt pinnummer");
            return;
        }
        //TODO kontrollern ska ansvara för att analysera informationen i användaren som hämtas (dvs rätt användartyp)
        if (DEBUGPRINTOUTS) System.out.println("LoginViewController: onloginbutton");
        Användare user = state.databaseService.logInUser(idealBoxContents.getText(), pinBoxContents.getText());
        showLoginConfirmationPopup(user != null);
        if (user != null) state.setCurrentUser(user);

    }

    @Override
    public void update(Observable o, Object arg) {
    }

    private void showLoginConfirmationPopup(boolean success) {
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Information");
        alert.setHeaderText("Login attempt");
        alert.setContentText("The login attempt was" + (success ? "" : "n't") + " successful.");
        alert.showAndWait();
    }
}

