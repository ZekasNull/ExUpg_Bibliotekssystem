package controller;

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
import state.BorrowItemInterface;

import java.time.Instant;
import java.util.Observable;

public class ShowProfileViewController extends Controller{
    @FXML
    public Button ReturnToMainMenuButton;
    public TableView<BorrowItemInterface> UserShowProfileViewLoanTable;
    public TableColumn<BorrowItemInterface, String> TitleColumn;
    public TableColumn<BorrowItemInterface, String> BarcodeColumn;
    public TableColumn<BorrowItemInterface, Instant> BorrowDateColumn;
    public TableColumn<BorrowItemInterface, Instant > ReturnDateColumn;
    public Button ReturnLoanButton;

    public void showLoanTable() {
        System.out.println(super.getState().databaseService.getLoanForUser(super.getState().getCurrentUser()));

        ObservableList<BorrowItemInterface> loans = FXCollections.observableArrayList(super.getState().getBorrowList());
        UserShowProfileViewLoanTable.setItems(loans);
        TitleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitel()));
        BarcodeColumn.setCellValueFactory(new PropertyValueFactory<>("streckkod"));
        BorrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("lånedatum"));
        ReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returDatum"));
    }

    public void onReturnToMainMenuButtonClick(ActionEvent actionEvent) {
        super.getState().vy.loadScene("Main-Menu.fxml", "Main Menu");
    }

    public void onReturnLoanButtonClick(ActionEvent actionEvent) {


    }

    @Override
    public void update(Observable o, Object arg) {}
}
