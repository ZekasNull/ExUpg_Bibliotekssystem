package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class UserSearchViewController extends Controller {

    @FXML
    private Button searchForObjectButton;
    @FXML
    private Button goBackToUserViewFromSearchButton;

    @FXML
    public void onUserGoBackToUserViewFromSearchClick(ActionEvent event) throws Exception {
        Stage currentStage = (Stage) goBackToUserViewFromSearchButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserFirstView();
    }

    @FXML
    public void onUserSearchForObjectButtonClick(ActionEvent event) throws Exception {
        Stage currentStage = (Stage) searchForObjectButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserSearchResultView();
        // TODO: Connect the search bar as a "requirement" for the button to base it's search on

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
