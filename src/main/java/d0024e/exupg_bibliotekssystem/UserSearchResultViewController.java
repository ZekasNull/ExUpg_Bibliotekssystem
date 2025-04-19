package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserSearchResultViewController {
    public Button ReturnToUserSearchViewButton;
    public Button SearchResult1Button;

    public void onUserReturnToUserSearchViewButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ReturnToUserSearchViewButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openUserSearchView();
    }

    public void onUserSearchResult1ButtonClick(ActionEvent event) {
        //TODO: Viktigt att sätta ihop sökresultat med knapp och action
        Stage currentStage = (Stage) SearchResult1Button.getScene().getWindow();
        currentStage.close();

        MainApplication.openUserSearchResultDetailsView();
    }
}
