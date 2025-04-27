package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class UserLoanConfirmationController  extends Controller {
    public Button ContinueToRecieptButton;

    public void onUserContinueToRecieptButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ContinueToRecieptButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserRecieptView();
    }
    @Override
    public void update(Observable o, Object arg) {

    }
}
