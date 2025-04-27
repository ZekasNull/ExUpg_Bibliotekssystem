package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import state.ApplicationState;
import java.util.Observable;

public class LibrarianLoginViewController extends Controller {
    private ApplicationState state;

    @FXML
    private TextField emailBoxContents; //FIXME ska vara ltu-ideal

    @FXML
    private PasswordField passwordBoxContents;

    public Button LogInLibrarianButton;

    public void onLibrarianLogInLibrarianButtonClick(ActionEvent event) {
        state.databaseService.logInLibrarian(emailBoxContents.getText(), passwordBoxContents.getText());


        //FIXME deaktiverad i testsyfte
        /*Stage currentStage = (Stage) LogInLibrarianButton.getScene().getWindow();
        currentStage.close();
        MainApplication.openLibrarianFirstChoiceView();*/
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean success = state.getCurrentUser() != null;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Information");
        alert.setHeaderText("Login attempt");
        alert.setContentText("The login attempt was" + (success ? "n" : "n't") + " successful.");

        alert.showAndWait();
    }

    public ApplicationState getState() {
        return state;
    }

    public void setState(ApplicationState state) {
        this.state = state;
    }
}
