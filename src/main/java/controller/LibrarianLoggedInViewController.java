package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;

public class LibrarianLoggedInViewController {
    public Button GoToLogoutViewButton; //Knapp för att logga in
    public TextField searchtermBoxContents; //Vad som står i själva sökboxen
    public SplitMenuButton objektTypFlerVal; //Vilket typ av objekt som ska filtreras
    public Button ShowProfileButton;
    public Button EmployeeViewButton;

    public void onUserGoToLogoutViewButtonClick(ActionEvent actionEvent) {
        //TODO: lägg till att gå tillbaka till utloggad vy
    }

    public void handleBokOption(ActionEvent actionEvent) {
        //TODO: Spara "bok" som valet i SplitMenuButton
    }

    public void handleFilmOption(ActionEvent actionEvent) {
        //TODO: Spara "film" som valet i SplitMenuButton
    }

    public void handleTidskriftOption(ActionEvent actionEvent) {
        //TODO: Spara "tidskrift" som valet i SplitMenuButton
    }

    public void onShowProfileButtonClick(ActionEvent actionEvent) {
        //TODO: lägg till vägar till varje
    }

    public void onEmployeeViewButtonClick(ActionEvent actionEvent) {
        //TODO: lägg till att gå till bibliotekspersonalvy
    }
}
