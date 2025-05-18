package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.util.Observable;

public class LibrarianFirstChoiceViewController extends Controller{

    public Button GoBackToMainMenuButton;

    public void HandleInventoryClicked(MouseEvent mouseEvent) {
        super.getState().vy.loadScene("handle-inventory-view.fxml", "Hantera inventarie");
    }

    public void HandleNotReturnedClick(MouseEvent mouseEvent) {
        super.getState().vy.loadScene("show-not-returned-view.fxml", "Ej återlämnade");
    }

    public void onGoBackToMainMenuButtonClick(ActionEvent actionEvent) {
        super.getState().vy.loadScene("Main-Menu.fxml", "Main Menu");
    }
    @Override
    public void update(Observable o, Object arg) {
    }
}
