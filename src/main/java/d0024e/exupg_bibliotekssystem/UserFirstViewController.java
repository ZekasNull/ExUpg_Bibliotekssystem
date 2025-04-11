package d0024e.exupg_bibliotekssystem;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserFirstViewController {

    @FXML
    public Button searchButton;
    public Button loginButton;
    @FXML
    private Button goBackButton;

    @FXML
    protected void onUserGoBackToViewChoiceClick(ActionEvent event) throws Exception {
        Stage currentStage = (Stage) goBackButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openViewChoice();
    }
    @FXML
    protected void onUserGoToSearchViewChoiceClick(ActionEvent event) throws Exception{
        Stage currentStage = (Stage) searchButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openUserSearchView();
    }

    public void onUserGoToLoanViewChoiceClick(ActionEvent event) {
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openUserLoginView();
    }
}