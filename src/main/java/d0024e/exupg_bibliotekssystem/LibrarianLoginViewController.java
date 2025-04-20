package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LibrarianLoginViewController {
    public Button LogInLibrarianButton;

    public void onLibrarianLogInLibrarianButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) LogInLibrarianButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openLibrarianFirstChoiceView();
    }
}
