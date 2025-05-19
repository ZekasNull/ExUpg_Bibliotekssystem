package controller;

import service.ViewLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Lån;

import java.util.Observable;

public class ShowNotReturnedViewController extends Controller {
    private ObservableList<Lån> lånList = FXCollections.observableArrayList();


    @FXML
    private Button GoBackToHandleInventoryButton;

    @FXML
    private TableColumn<Lån, String> barcodeColumn;

    @FXML
    private TableColumn<?, ?> loanDateColumn;

    @FXML
    private TableColumn<?, ?> loanIdColumn;

    @FXML
    private TableColumn<Lån, String> loantypeColumn;

    @FXML
    private TableView<Lån> overdueLoansTable;

    @FXML
    private TableColumn<?, ?> returnDateColumn;

    @FXML
    private TableColumn<Lån, String> titleColumn;

    @FXML
    private TableColumn<Lån, String> usernameColumn;

    @FXML
    void punishButtonPressed(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("En attackdrönare har skickats.");

        alert.showAndWait();
    }

    @FXML
    void refreshButtonPressed(ActionEvent event) {
        lånList.clear();
        lånList.addAll(getState().getUserDatabaseService().visaEjÅterlämnadeBöcker());
    }

    public void initialize() {
        //table
        overdueLoansTable.setItems(lånList);
        loanIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAnvändare().getAnvändarnamn()));
        barcodeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStreckkod().getStreckkod().toString()));
        loantypeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStreckkod().getLåntyp().getLåntyp()));
        titleColumn.setCellValueFactory(cellData -> {
            var streckkod = cellData.getValue().getStreckkod();
            if(streckkod.getBok() == null) {
                return new SimpleStringProperty(streckkod.getFilm_id().getTitel());
            }
            else{
                return new SimpleStringProperty(streckkod.getBok().getTitel());
            }
        });
        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("lånedatum"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returDatum"));
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @FXML
    public void onGoBackToHandleInventoryClick(ActionEvent actionEvent) {
        viewLoader.setView(ViewLoader.Views.HANDLE_INVENTORY);
    }
}
