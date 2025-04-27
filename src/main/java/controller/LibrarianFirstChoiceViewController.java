package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class LibrarianFirstChoiceViewController extends Controller {
    public Button GoToHandleUserViewButton;
    public Button GoToHandleInventoryViewButton;

    public void onLibrarianGoToHandleUserViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) GoToHandleUserViewButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openLibrarianHandleUserChoiceView();
    }

    public void onUserGoToHandleInventoryViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) GoToHandleUserViewButton.getScene().getWindow();
        currentStage.close();

    }
    @Override
    public void update(Observable o, Object arg) {

    }
}
