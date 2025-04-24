package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LibrarianDeleteUserViewController {
    public Button ReturnToLibrarianHandleUserViewFromDeleteUserViewButton;

    public void onLibrarianReturnToLibrarianHandleUserViewFromDeleteUserViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ReturnToLibrarianHandleUserViewFromDeleteUserViewButton.getScene().getWindow();
        currentStage.close();

        //MainApplication.openLibrarianHandleUserChoiceView();
    }
}
