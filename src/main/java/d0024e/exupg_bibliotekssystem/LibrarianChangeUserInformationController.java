package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LibrarianChangeUserInformationController {
    public Button ReturnToLibrarianHandleUserChoiceViewButton;

    public void onLibrarianReturnToLibrarianHandleUserChoiceViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ReturnToLibrarianHandleUserChoiceViewButton.getScene().getWindow();
        currentStage.close();

        //MainApplication.openLibrarianHandleUserChoiceView();
    }
}
