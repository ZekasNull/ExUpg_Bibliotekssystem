package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ViewChoiceController {

    public Button GoToLibrarianLoginViewButton;
    @FXML
    private Button goToUserFirstViewButton;

    @FXML
    protected void onUserButtonClick(ActionEvent event) throws Exception {
        Stage currentStage = (Stage) goToUserFirstViewButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openUserFirstView();
    }

    public void onLibraryGoToLibrarianLoginViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) goToUserFirstViewButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openLibrarianLoginView();
    }
}