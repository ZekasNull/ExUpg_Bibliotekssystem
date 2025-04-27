package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class UserRecieptViewController  extends Controller {
    public Button ReturnToUserMainMenuButton;

    public void onUserReturnToUserMainMenuButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ReturnToUserMainMenuButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserFirstView();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
