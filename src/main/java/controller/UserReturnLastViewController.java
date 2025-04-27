package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class UserReturnLastViewController extends Controller  {
    public Button returnToHomepageReturnView;

    public void onUserReturnToHomepageReturnViewClick(ActionEvent event) {
        Stage currentStage = (Stage) returnToHomepageReturnView.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserFirstView();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
