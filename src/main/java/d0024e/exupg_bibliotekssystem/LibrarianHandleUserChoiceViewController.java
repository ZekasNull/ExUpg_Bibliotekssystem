package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class LibrarianHandleUserChoiceViewController {
    public Button ChangeUserInformationButton;
    public Button RemoveUserButton;
    public Button ReturnToFirstChoiceViewButton;

    public void onUserChangeUserInformationButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ChangeUserInformationButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openLibrarianChangeUserInformationView();
    }

    public void onUserRemoveUserButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ChangeUserInformationButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openLibrarianDeleteUserView();
    }

    public void onLibrarianReturnToFirstChoiceViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ChangeUserInformationButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openLibrarianFirstChoiceView();
    }
}
