package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class UserSearchResultViewController extends Controller  {
    public Button ReturnToUserSearchViewButton;
    public Button SearchResult1Button;

    public void onUserReturnToUserSearchViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ReturnToUserSearchViewButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserSearchView();
    }

    public void onUserSearchResult1ButtonClick(ActionEvent event) {
        //TODO: Viktigt att sätta ihop sökresultat med knapp och action
        Stage currentStage = (Stage) SearchResult1Button.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserSearchResultDetailsView();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
