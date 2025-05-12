package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.Observable;

public class AddBookViewController extends Controller {

    //exemplar

    @FXML
    private TableView<?> copyTable;

    @FXML
    private TableColumn<?, ?> Available;

    @FXML
    private TableColumn<?, ?> Barcode;

    @FXML
    private TableColumn<?, ?> LoanType;

    @FXML
    private TableColumn<?, ?> AuthorColumn;

    @FXML
    private TextField amnesordBoxContents1;

    @FXML
    private TextField amnesordBoxContents2;

    @FXML
    private TextField amnesordBoxContents3;

    @FXML
    private TextField authorLastNameBoxContents1;

    @FXML
    private TextField authorLastNameBoxContents2;

    @FXML
    private TextField authorLastNameBoxContents3;

    @FXML
    private TextField authorNameBoxContents1;

    @FXML
    private TextField authorNameBoxContents2;

    @FXML
    private TextField authorNameBoxContents3;

    @FXML
    private TableView<?> bokDisplayTable;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;



    @FXML
    private TextField isbnBoxContents;

    @FXML
    private Button searchButton;

    @FXML
    private TextField titleBoxContents;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    void addCopyButtonPressed(ActionEvent event) {

    }

    @FXML
    void bookTableClicked(MouseEvent event) {

    }

    @FXML
    void cancelButtonClicked(ActionEvent event) {

    }

    @FXML
    void confirmButtonClicked(ActionEvent event) {

    }

    @FXML
    void copyTableClicked(MouseEvent event) {

    }

    @FXML
    void handleBokOption(ActionEvent event) {

    }

    @FXML
    void handleReferenslitteraturOption(ActionEvent event) {

    }

    @FXML
    void loantypeChosen(ActionEvent event) {

    }

    @FXML
    void removeCopyButtonPressed(ActionEvent event) {

    }

    @FXML
    void searchBooksButtonPressed(ActionEvent event) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
