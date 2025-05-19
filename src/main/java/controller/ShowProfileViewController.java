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
//Ger upp, chatgpt i loadData
    public void loadData() {
        if (getState().getCurrentUser() == null) return;

        Set<Lån> loansSet = getState().getCurrentUser().getLåns();

        // ❶ fetch return‑date once per loan and store in the transient field
        for (Lån l : loansSet) {
            Instant rd = getState().getUserDatabaseService().getReturnDateForLoan(l);
            l.setReturDatum(rd);          // fills the transient field
        }

        // ❷ convert to observable list & bind table
        loans = FXCollections.observableArrayList(loansSet);


        /*if (super.getState() .getCurrentUser() == null) return; //Just-in-case
        System.out.println("loadData was called");

        System.out.println(super.getState().getCurrentUser().getLåns());

        Set<Lån> loansSet = super.getState().getCurrentUser().getLåns();

        ObservableList<Lån> loans = FXCollections.observableArrayList(loansSet);

        UserShowProfileViewLoanTable.setItems(loans);
        TitleColumn.setCellValueFactory(cellData -> {
            var streckkod = cellData.getValue().getStreckkod();
            if(streckkod.getBok() == null) {
                return new SimpleStringProperty(streckkod.getFilm_id().getTitel());
            }
            else{
                return new SimpleStringProperty(streckkod.getBok().getTitel());
            }
        });
        BarcodeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStreckkod().getStreckkod().toString()));
        BorrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("lånedatum"));
        ReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returDatum"));*/

    }

    public void onReturnToMainMenuButtonClick(ActionEvent actionEvent) {
        viewLoader.setView(ViewLoader.Views.MAIN_MENU);
    }

    public void onReturnLoanButtonClick(ActionEvent actionEvent) {

        Lån lån = UserShowProfileViewLoanTable.getSelectionModel().getSelectedItem();

        userDatabaseService.returnLoan(lån);
        getState().updateUserInformation();

//        try {
//            super.getState().getDatabaseService().raderaObjekt(selectedLoans);
//            loadData();
//        } catch (PSQLException e) {
//            String msg = e.getMessage();
//            if (msg != null && msg.contains("some specific database error text")) {
//                showErrorPopup("Kan inte returnera lån: " + msg);
//            } else {
//                throw e;
//            }
//        } catch (Exception e) {
//            showErrorPopup("Kunde inte returnera lån: " + e.getMessage());
//        }
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
