package controller;

import service.ViewLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.Observable;

public class HandleInventoryViewController extends Controller{
    public Label HandleBooksButton;
    public Label HandleMoviesButton;
    public Button GoBackToFirstChoiceButton;

    public void initialize() {
    }

    public void onHandleBooksButtonClick(MouseEvent mouseEvent) {
        viewLoader.setView(ViewLoader.Views.HANDLE_BOOKS);
    }

    public void onHandleMoviesButtonClick(MouseEvent mouseEvent) {
        viewLoader.setView(ViewLoader.Views.HANDLE_MOVIES);
    }

    public void onGoBackToFirstChoiceButtonClick(ActionEvent actionEvent) {
        viewLoader.setView(ViewLoader.Views.LIBRARIAN_FIRST_CHOICE);
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
