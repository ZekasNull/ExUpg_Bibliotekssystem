package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserRecieptViewController {
    public Button ReturnToUserMainMenuButton;

    public void onUserReturnToUserMainMenuButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ReturnToUserMainMenuButton.getScene().getWindow();
        currentStage.close();

        //MainApplication.openUserFirstView();
    }
}
