package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class UserLoginViewController  extends Controller {
    public Button logInButton;

    @FXML
    public void onUserLogInButtonClick(ActionEvent event) {
        //TODO: Validera det användaren skriver in i båda fälten mot det som finns sparat i databasen. alt skriv om hela metoden

        Stage currentStage = (Stage) logInButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserLoanViewRegisterView();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
