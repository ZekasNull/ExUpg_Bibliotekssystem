package controller;

import d0024e.exupg_bibliotekssystem.ViewLoader;
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
import model.Lån;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.postgresql.util.PSQLException;
import state.ApplicationState;
import state.BorrowItemInterface;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Set;

public class ShowProfileViewController extends Controller{
    @FXML
    public Button ReturnToMainMenuButton;
    public TableView<Lån> UserShowProfileViewLoanTable;
    public TableColumn<Lån, String> TitleColumn;
    public TableColumn<Lån, String> BarcodeColumn;
    public TableColumn<Lån, Instant> BorrowDateColumn;
    public TableColumn<Lån, Instant> ReturnDateColumn;
    public Button ReturnLoanButton;


    public void initialize() {
    }
//Ger upp, chatgpt i loadData
    public void loadData() {
        if (super.getState().getCurrentUser() == null) return;

        Set<Lån> loansSet = super.getState().getCurrentUser().getLåns();

        // ❶ fetch return‑date once per loan and store in the transient field
        for (Lån l : loansSet) {
            Instant rd = super.getState().databaseService.getReturnDateForLoan(l);
            l.setReturDatum(rd);          // fills the transient field
        }

        // ❷ convert to observable list & bind table
        ObservableList<Lån> loans = FXCollections.observableArrayList(loansSet);
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
                new SimpleObjectProperty<>(super.getState().databaseService.getReturnDateForLoan(cd.getValue())));

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
        super.getState().vy.setView("Huvudmeny");
    }

    public void onReturnLoanButtonClick(ActionEvent actionEvent) throws PSQLException {
        List<Lån> selectedLoans = new ArrayList<>(UserShowProfileViewLoanTable.getSelectionModel().getSelectedItems());

        if (selectedLoans.isEmpty()) {
            showErrorPopup("Vänligen välj minst ett lån att returnera.");
            return;
        }

        try {
            super.getState().databaseService.raderaObjekt(selectedLoans);
            loadData();
        } catch (PSQLException e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("some specific database error text")) {
                showErrorPopup("Kan inte returnera lån: " + msg);
            } else {
                throw e;
            }
        } catch (Exception e) {
            showErrorPopup("Kunde inte returnera lån: " + e.getMessage());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        loadData();
    }
}
