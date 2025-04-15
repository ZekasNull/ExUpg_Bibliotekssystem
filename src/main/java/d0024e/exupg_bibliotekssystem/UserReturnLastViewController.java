package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserReturnLastViewController {
    public Button returnToHomepageReturnView;

    public void onUserReturnToHomepageReturnViewClick(ActionEvent event) {
        Stage currentStage = (Stage) returnToHomepageReturnView.getScene().getWindow();
        currentStage.close();

        MainApplication.openUserFirstView();
    }
}
