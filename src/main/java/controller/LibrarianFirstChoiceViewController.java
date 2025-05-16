package controller;

import javafx.scene.input.MouseEvent;
import java.util.Observable;

public class LibrarianFirstChoiceViewController extends Controller{

    public void HandleInventoryClicked(MouseEvent mouseEvent) {
        super.getState().vy.loadScene("handle-inventory-view.fxml", "Hantera inventarie");
    }

    public void HandleNotReturnedClick(MouseEvent mouseEvent) {
        //TODO: Lägg till att gå till Visa ej återlämnade objekt
    }
    @Override
    public void update(Observable o, Object arg) {
    }
}
