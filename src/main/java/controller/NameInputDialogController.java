package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Observable;

public class NameInputDialogController extends Controller {
    Stage stage;
    private String[] names;

    public String[] getNames() {
        return names;
    }

    @FXML
    private TextField firstnameInputField;

    @FXML
    private TextField lastnameInputField;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        stage.close();
    }

    @FXML
    void okButtonPressed(ActionEvent event) {
        names = new String[]{firstnameInputField.getText().trim(), lastnameInputField.getText().trim()};
        stage.close();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
