package controller;

import service.ViewLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.util.Observable;

public class LibrarianFirstChoiceViewController extends Controller{

    public Button GoBackToMainMenuButton;

    public void HandleInventoryClicked(MouseEvent mouseEvent) {
        viewLoader.setView(ViewLoader.Views.HANDLE_INVENTORY);
    }

    public void HandleNotReturnedClick(MouseEvent mouseEvent) {
        viewLoader.setView(ViewLoader.Views.NOT_RETURNED);
    }

    public void onGoBackToMainMenuButtonClick(ActionEvent actionEvent) {
        viewLoader.setView(ViewLoader.Views.MAIN_MENU);
    }

    public void initialize() {
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
