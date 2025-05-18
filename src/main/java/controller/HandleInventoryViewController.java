package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.util.Observable;

public class HandleInventoryViewController extends Controller{
    public Label HandleBooksButton;
    public Label HandleMoviesButton;
    public Button GoBackToFirstChoiceButton;

    public void onHandleBooksButtonClick(MouseEvent mouseEvent) {
        super.getState().vy.loadScene("add-book-view.fxml", "Hantera böcker");
    }

    public void onHandleMoviesButtonClick(MouseEvent mouseEvent) {
        super.getState().vy.loadScene("add-movie-view.fxml", "Hantera film");
    }

    public void onGoBackToFirstChoiceButtonClick(ActionEvent actionEvent) {
        super.getState().vy.loadScene("librarian-first-choice.fxml", "Bibliotekarnas första val");
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
