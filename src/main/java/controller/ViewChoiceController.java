package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import state.ApplicationState;

import java.util.Observable;

public class ViewChoiceController extends Controller{
    public Button GoToLibrarianLoginViewButton;
    @FXML
    private Button goToUserFirstViewButton;

    @FXML
    protected void onUserButtonClick(ActionEvent event) throws Exception {
        Stage currentStage = (Stage) goToUserFirstViewButton.getScene().getWindow();
        currentStage.close();

        //FIXME anrop saknas/ska ändras
        //super.getState().app.openUserFirstView();
    }

    public void onLibraryGoToLibrarianLoginViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) goToUserFirstViewButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openLibrarianLoginView();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}