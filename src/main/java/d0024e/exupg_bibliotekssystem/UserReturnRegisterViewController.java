package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserReturnRegisterViewController {
    public Button GoToReturnConfirmation;

    public void onUserGoToReturnConfirmationClick(ActionEvent event) {
        Stage currentStage = (Stage) GoToReturnConfirmation.getScene().getWindow();
        currentStage.close();

        //MainApplication.openUserReturnConfirmationView();
    }
}
