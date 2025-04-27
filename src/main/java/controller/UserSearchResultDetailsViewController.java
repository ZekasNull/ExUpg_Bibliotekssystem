package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class UserSearchResultDetailsViewController extends Controller {
    public Button ReturnToUserSearchResultViewButton;

    public void onUserReturnToUserSearchResultViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ReturnToUserSearchResultViewButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserSearchResultView();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
