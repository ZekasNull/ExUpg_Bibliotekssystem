package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserSearchResultDetailsViewController {
    public Button ReturnToUserSearchResultViewButton;

    public void onUserReturnToUserSearchResultViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ReturnToUserSearchResultViewButton.getScene().getWindow();
        currentStage.close();

        //MainApplication.openUserSearchResultView();
    }
}
