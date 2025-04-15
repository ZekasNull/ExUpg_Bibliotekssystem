package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserReturnConfirmationViewController {
    public Button GoToReturnLastView;

    public void onUserGoToReturnLastViewClick(ActionEvent event) {
        Stage currentStage = (Stage) GoToReturnLastView.getScene().getWindow();
        currentStage.close();

        MainApplication.openUserReturnLastView();
    }
}
