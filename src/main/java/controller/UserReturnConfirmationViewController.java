package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class UserReturnConfirmationViewController extends Controller  {
    public Button GoToReturnLastView;

    public void onUserGoToReturnLastViewClick(ActionEvent event) {
        Stage currentStage = (Stage) GoToReturnLastView.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserReturnLastView();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
