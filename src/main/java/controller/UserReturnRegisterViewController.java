package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class UserReturnRegisterViewController extends Controller  {
    public Button GoToReturnConfirmation;

    public void onUserGoToReturnConfirmationClick(ActionEvent event) {
        Stage currentStage = (Stage) GoToReturnConfirmation.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserReturnConfirmationView();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
