package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;


public class LibrarianHandleUserChoiceViewController extends Controller {
    public Button ChangeUserInformationButton;
    public Button RemoveUserButton;
    public Button ReturnToFirstChoiceViewButton;

    public void onUserChangeUserInformationButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ChangeUserInformationButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openLibrarianChangeUserInformationView();
    }

    public void onUserRemoveUserButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ChangeUserInformationButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openLibrarianDeleteUserView();
    }

    public void onLibrarianReturnToFirstChoiceViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ChangeUserInformationButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openLibrarianFirstChoiceView();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
