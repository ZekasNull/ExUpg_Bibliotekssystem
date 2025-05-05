package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;

public class FirstLoggedOutViewController {
    public Button GoToLoginViewButton; //Knapp för att logga in
    public TextField searchtermBoxContents; //Vad som står i själva sökboxen
    public SplitMenuButton objektTypFlerVal; //Vilket typ av objekt som ska filtreras
    public Button BorrowObjectButton; //TODO: se till att det kommer upp ett meddelande om att logga in on hover

    public void onUserGoToLoginViewButtonClick(ActionEvent actionEvent) {
        //TODO: lägg till en väg att gå till inlogget
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
}
