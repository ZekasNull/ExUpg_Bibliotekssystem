package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class LibrarianDeleteUserViewController extends Controller {
    public Button ReturnToLibrarianHandleUserViewFromDeleteUserViewButton;

    public void onLibrarianReturnToLibrarianHandleUserViewFromDeleteUserViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ReturnToLibrarianHandleUserViewFromDeleteUserViewButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openLibrarianHandleUserChoiceView();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
