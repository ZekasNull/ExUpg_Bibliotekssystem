package d0024e.exupg_bibliotekssystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserLoanRegisterViewController {
    public Button AddItemButton;
    public Button ContinueToLoanConfirmationButton;

    public void onUserAddItemClick(ActionEvent event) {
        /*TODO: mycket
        * 1. Se till att lånet kan läggas till, bara nummer
        * 2. Se till att programmet kan kolla igenom vilka böcker som har det id och komma tillbaka med informationen
        * 3. Se till att den informationen blir korrekt och att det ser bra ut visuellt
        * 4. Se till så att lånen för ett lånekonto inte överskrids, ex student lånar 6 böcker
        * 5. Se till så att användaren inte över lag har lånat mer än tillåtet antal böcker
        * 6. Se till så att de angivna böckerna sparas för konfirmation samt kvitto
        * */
    }

    public void onUserContinueToLoanConfirmationButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) ContinueToLoanConfirmationButton.getScene().getWindow();
        currentStage.close();

        MainApplication.openUserLoanConfirmationView();
    }
}
