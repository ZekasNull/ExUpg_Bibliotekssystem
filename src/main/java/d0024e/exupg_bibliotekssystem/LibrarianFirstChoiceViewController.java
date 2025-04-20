package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LibrarianFirstChoiceViewController {
    public Button GoToHandleUserViewButton;
    public Button GoToHandleInventoryViewButton;

    public void onLibrarianGoToHandleUserViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) GoToHandleUserViewButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openLibrarianHandleUserChoiceView();
    }

    public void onUserGoToHandleInventoryViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) GoToHandleUserViewButton.getScene().getWindow();
        currentStage.close();

    }
}
