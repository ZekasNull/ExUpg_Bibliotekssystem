package controller;

import service.ViewLoader;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Användare;
import model.Lån;
import service.UserDatabaseService;
import state.ApplicationState;

import java.time.Instant;
import java.util.Observable;
import java.util.Set;

public class ShowProfileViewController extends Controller{
    //tjänst
    UserDatabaseService userDatabaseService;
    //data
    ObservableList<Lån> loans = FXCollections.observableArrayList();

    @FXML
    public Button ReturnToMainMenuButton;
    public TableView<Lån> UserShowProfileViewLoanTable;
    public TableColumn<Lån, String> TitleColumn;
    public TableColumn<Lån, String> BarcodeColumn;
    public TableColumn<Lån, Instant> BorrowDateColumn;
    public TableColumn<Lån, Instant> ReturnDateColumn;
    public Button ReturnLoanButton;


    public void initialize() {

        UserShowProfileViewLoanTable.setItems(loans);

        // --- column factories ---
        TitleColumn.setCellValueFactory(cd -> {
            var ex = cd.getValue().getStreckkod();
            String title = ex.getBok() != null
                    ? ex.getBok().getTitel()
                    : ex.getFilm_id().getTitel();
            return new SimpleStringProperty(title);
        });

        BarcodeColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue()
                        .getStreckkod()
                        .getStreckkod()
                        .toString()));

        BorrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("lånedatum"));

        ReturnDateColumn.setCellValueFactory(cd ->
                new SimpleObjectProperty<>(getState().getUserDatabaseService().getReturnDateForLoan(cd.getValue())));
    }

    @Override
    public void loadServicesFromState() {
        super.loadServicesFromState();
        userDatabaseService = getState().getUserDatabaseService();
    }

    public void onReturnToMainMenuButtonClick(ActionEvent actionEvent) {
        viewLoader.setView(ViewLoader.Views.MAIN_MENU);
    }

    public void onReturnLoanButtonClick(ActionEvent actionEvent) {

        Lån lån = UserShowProfileViewLoanTable.getSelectionModel().getSelectedItem();

        userDatabaseService.returnLoan(lån);
        getState().updateUserInformation();

    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg != ApplicationState.UpdateType.USER) return;
        Användare user = super.getState().getCurrentUser();

        if(user == null){ //hantera om användare är null (loggade ut)
            loans.clear(); //rensa lista
            return;
        }else {
            loans.clear(); //rensa lista
            //ladda eller uppdatera användarens lån -
            Set<Lån> loansSet = user.getLåns();

            // returdatum måste beräknas för varje
            for (Lån l : loansSet) {
                l.setReturDatum(userDatabaseService.getReturnDateForLoan(l));
            }

            // ❷ convert to observable list & bind table
            loans.addAll(loansSet);
        }

    }
}
