package controller;

import d0024e.exupg_bibliotekssystem.ViewLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import javax.swing.text.View;
import java.util.Observable;

public class HandleInventoryViewController extends Controller{
    public Label HandleBooksButton;
    public Label HandleMoviesButton;
    public Button GoBackToFirstChoiceButton;

    public void onHandleBooksButtonClick(MouseEvent mouseEvent) {
        ViewLoader.setView("Hantera böcker");
    }

    public void onHandleMoviesButtonClick(MouseEvent mouseEvent) {
        ViewLoader.setView("Hantera filmer");
    }

    public void onGoBackToFirstChoiceButtonClick(ActionEvent actionEvent) {
        ViewLoader.setView("Bibliotikaries första val");
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
