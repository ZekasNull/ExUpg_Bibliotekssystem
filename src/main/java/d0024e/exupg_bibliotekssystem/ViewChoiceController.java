package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import state.ApplicationState;

public class ViewChoiceController {
    public ApplicationState state;
    public Button GoToLibrarianLoginViewButton;
    @FXML
    private Button goToUserFirstViewButton;

    @FXML
    protected void onUserButtonClick(ActionEvent event) throws Exception {
        Stage currentStage = (Stage) goToUserFirstViewButton.getScene().getWindow();
        currentStage.close();

        state.app.openUserFirstView();
    }

    public void onLibraryGoToLibrarianLoginViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) goToUserFirstViewButton.getScene().getWindow();
        currentStage.close();

        state.app.openLibrarianLoginView();
    }
}