package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class LibrarianChangeUserInformationController extends Controller {
    public Button ReturnToLibrarianHandleUserChoiceViewButton;

    public void onLibrarianReturnToLibrarianHandleUserChoiceViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ReturnToLibrarianHandleUserChoiceViewButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openLibrarianHandleUserChoiceView();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
