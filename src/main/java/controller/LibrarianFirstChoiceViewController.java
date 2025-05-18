package controller;

import d0024e.exupg_bibliotekssystem.ViewLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.util.Observable;

public class LibrarianFirstChoiceViewController extends Controller{

    public Button GoBackToMainMenuButton;

    public void HandleInventoryClicked(MouseEvent mouseEvent) {
        ViewLoader.setView("Hantera inventarie");
    }

    public void HandleNotReturnedClick(MouseEvent mouseEvent) {
        ViewLoader.setView("Ej återlämnade");
    }

    public void onGoBackToMainMenuButtonClick(ActionEvent actionEvent) {
        ViewLoader.setView("Huvudmeny");
    }
    @Override
    public void update(Observable o, Object arg) {
    }
}
