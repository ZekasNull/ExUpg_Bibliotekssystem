package controller;

import d0024e.exupg_bibliotekssystem.MainApplication;
import service.ViewLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import state.ApplicationState;

import java.io.IOException;
import java.util.Observer;
import java.util.Optional;

/**
 * Abstrakt klass för en kontroller.
 * Den implementerar Observer för att uppdatera sin vy om förändringar i state.
 * Den har en referens till ett ApplicationState.
 */
public abstract class Controller implements Observer {


    private ApplicationState state;
    protected ViewLoader viewLoader;

    public ApplicationState getState() {
        return state;
    }
    public void setState(ApplicationState state) {
        this.state = state;
    }

    /**
     * Fungerar ungefär som en tredje konstruktor som körs efter vanliga konstruktorn och initialise.
     * Ser till att kontroller etablerar referenser till state och viewLoader. Bör överskuggas om fler tjänster behöver laddas in från state.
     * Laddar alltid viewLoader då den är central för navigation i programmet. Andra gemensamma tjänster bör läggas här. Börjar alltid observera state.
     */
    public void loadServicesFromState() {
        if (state == null) throw new IllegalStateException("Controller(super): State must be set before controller services can be initialised");
        this.viewLoader = state.getViewLoaderService();
        state.addObserver(this);

    }

    protected void showInformationPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(message);

        alert.showAndWait();
    }

    protected void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fel");
        alert.setHeaderText(message);

        alert.showAndWait();
    }

    protected boolean onDeleteUserConfirmation(boolean hasChildren) {
        String additionalWarning = hasChildren ? " Alla exemplar kommer också att raderas" : "";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bekräfta");
        alert.setHeaderText("Är du säker?");
        alert.setContentText("Vill du verkligen radera detta objekt?" + additionalWarning);

        // Set button types explicitly
        ButtonType yesButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Nej", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show and wait for user input
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == yesButton;
    }

    protected String[] openNameInputDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("nameInputDialog.fxml"));
        Scene inputDialog = new Scene(loader.load());
        Stage popupDialog = new Stage();

        //controller references
        NameInputDialogController controller = loader.getController();
        controller.setStage(popupDialog);

        //setup window
        popupDialog.setTitle("Namn");
        popupDialog.setScene(inputDialog);
        popupDialog.setResizable(false);
        popupDialog.showAndWait();

        //when closed
        return controller.getNames();
    }

}