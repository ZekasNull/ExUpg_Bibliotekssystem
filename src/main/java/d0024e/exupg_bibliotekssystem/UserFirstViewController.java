package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserFirstViewController {

    @FXML
    private Button goBackButton;

    @FXML
    protected void goBackToViewChoice(ActionEvent event) throws Exception {
        Stage currentStage = (Stage) goBackButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openViewChoice();
    }
}