package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserLoanConfirmationController {
    public Button ContinueToRecieptButton;

    public void onUserContinueToRecieptButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ContinueToRecieptButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openUserRecieptView();
    }
}
