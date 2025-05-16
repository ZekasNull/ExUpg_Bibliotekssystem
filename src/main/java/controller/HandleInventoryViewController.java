package controller;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.util.Observable;

public class HandleInventoryViewController extends Controller{
    public Label HandleBooksButton;
    public Label HandleMoviesButton;

    public void onHandleBooksButtonClick(MouseEvent mouseEvent) {
        super.getState().vy.loadScene("add-book-view.fxml", "Hantera böcker");
    }

    public void onHandleMoviesButtonClick(MouseEvent mouseEvent) {
        super.getState().vy.loadScene("add-movie-view.fxml", "Hantera film");
    }
    @Override
    public void update(Observable o, Object arg) {
    }
}
