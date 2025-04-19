package d0024e.exupg_bibliotekssystem;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserSearchViewController {

    @FXML
    private Button searchForObjectButton;
    @FXML
    private Button goBackToUserViewFromSearchButton;

    @FXML
    public void onUserGoBackToUserViewFromSearchClick(ActionEvent event) throws Exception {
        Stage currentStage = (Stage) goBackToUserViewFromSearchButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openUserFirstView();
    }

    @FXML
    public void onUserSearchForObjectButtonClick(ActionEvent event) throws Exception {
        Stage currentStage = (Stage) searchForObjectButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openUserSearchResultView();
        // TODO: Connect the search bar as a "requirement" for the button to base it's search on

    }
}
