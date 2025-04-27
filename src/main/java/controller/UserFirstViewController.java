package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Observable;

public class UserFirstViewController extends Controller  {
    //TODO: Check if the user is logged in (after loan) and add a log out button, alt always log the user out after a loan

    @FXML
    public Button searchButton;
    public Button loginButton;
    public Button returnButton;
    @FXML
    private Button goBackButton;

    @FXML
    protected void onUserGoBackToViewChoiceClick(ActionEvent event) throws Exception {
        Stage currentStage = (Stage) goBackButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openViewChoice();
    }
    @FXML
    protected void onUserGoToSearchViewChoiceClick(ActionEvent event) throws Exception{
        Stage currentStage = (Stage) searchButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserSearchView();
    }

    public void onUserGoToLoanViewChoiceClick(ActionEvent event) {
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserLoginView();
    }

    public void onUserGoToReturnViewChoiceClick(ActionEvent event) {
        Stage currentStage = (Stage) returnButton.getScene().getWindow();
        currentStage.close();

        super.getState().app.openUserReturnView();
    }
    @Override
    public void update(Observable o, Object arg) {

    }
}