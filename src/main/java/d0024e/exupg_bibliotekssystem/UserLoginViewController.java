package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserLoginViewController {
    public Button logInButton;

    @FXML
    public void onUserLogInButtonClick(ActionEvent event) {
        //TODO: Validera det användaren skriver in i båda fälten mot det som finns sparat i databasen. alt skriv om hela metoden

        Stage currentStage = (Stage) logInButton.getScene().getWindow();
        currentStage.close();

         //MainApplication.openUserLoanViewRegisterView();
    }
}
