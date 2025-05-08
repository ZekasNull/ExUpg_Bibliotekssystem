package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Bok;
import model.Film;
import model.Tidskrift;
import model.Ämnesord;
import state.ApplicationState;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

public class FirstLoggedOutViewController extends Controller {
    private final ApplicationState state = ApplicationState.getInstance(); //... JUSTE det här behövdes för state lämnades null och jag kom inte på ett bättre sätt
    @FXML
    public Button GoToLoginViewButton; //Knapp för att logga in
    public TextField searchtermBoxContents; //Vad som står i själva sökboxen
    public SplitMenuButton objektTypFlerVal; //Vilket typ av objekt som ska filtreras
    public Button BorrowObjectButton; //TODO: se till att det kommer upp ett meddelande om att logga in on hover

    public TableColumn<Bok, String> titleColumn;
    public TableColumn<Bok, String> isbnColumn;
    public TableColumn<Bok, String> subjectColumn;
    public TableColumn<Bok, String> authorNameColumn;
    public TableView<Bok> notLoggedInBookSearchTable; // tableView för bok, ovan är dess kolumner

    public Button NotLoggedInSearchButton;

    private String selectedObject;

    public void onUserGoToLoginViewButtonClick(ActionEvent actionEvent) {
        //TODO: lägg till en väg att gå till inlogget
    }

    public void handleBokOption(ActionEvent actionEvent) {
        selectedObject = "Bok";
        objektTypFlerVal.setText(selectedObject); //Sätter vilket typ av objekt som ska sökas efter
    }

    public void handleFilmOption(ActionEvent actionEvent) {
        selectedObject = "Film";
        objektTypFlerVal.setText(selectedObject);
    }

    public void handleTidskriftOption(ActionEvent actionEvent) {
        selectedObject = "Tidskrift";
        objektTypFlerVal.setText(selectedObject);
    }

    public void onNotLoggedInSearchButtonClick(ActionEvent actionEvent) {
/*Faktiska som körs och inte bara hämtar information
* Den går igenom vilket objekt som för nuvarande finns i splitmenubutton (drop down menyn) och går till den det gäller
* */
        try{
            if(selectedObject.equals("Bok")){
                List<Bok> searchTerm = state.databaseService.searchAndGetBooks(searchtermBoxContents.getText());

                ObservableList<Bok> data = FXCollections.observableArrayList(searchTerm);
                notLoggedInBookSearchTable.setItems(data);
                System.out.println("Rows added to table: " + data.size() + " (Bok");

                //Sätter in det returnerade värdet från sökningens titel i titelkolumnen osv (Ligger här för att dela upp det mellan bok,film, och tidsskrift)
                titleColumn.setCellValueFactory(new PropertyValueFactory<>("titel"));
                isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn13"));
                //För ämnesord behövde vi ta och skriva ut varje element i listan med ett , mellan
                subjectColumn.setCellValueFactory(cellData ->
                                new SimpleStringProperty(
                                        cellData.getValue()
                                                .getÄmnesord()
                                                .stream()
                                                .map(Ämnesord::getOrd)
                                                .collect(Collectors.joining(", "))
                                )
                        //cellData = en instans av "CellDataFetures<Bok, String> som ger access till objektet Bok som kommer skrivas ut i vilken rad det gäller i Tableview
                );
                //Här är det fråga om sammansätta förnamn och efternamn
                authorNameColumn.setCellValueFactory(cellData -> {
                    Bok bok = cellData.getValue();
                    String authorName = bok.getFörfattare().stream()
                            .map(författare -> författare.getFörnamn() + " " + författare.getEfternamn())
                            .collect(Collectors.joining(","));
                    return new SimpleStringProperty(authorName);
                });
            }
            else if (selectedObject.equals("Film")){
                List<Film> searchTerm = state.databaseService.searchAndGetFilms(searchtermBoxContents.getText());

                ObservableList<Film> data = FXCollections.observableArrayList(searchTerm);
                //notLoggedInFilmSearchTable.setItems(data); - Kräver ett annat table?
            }
            //TODO: Fungerar inte för film ännu, tänker den får ett eget tableview och behöver då sina egna lambda I think
            /*else if (selectedObject.equals("Tidskrift")){
                List<Tidskrift> searchTerm = state.databaseService.searchTidskrift(searchtermBoxContents.getText());

                ObservableList<Tidskrift> data = FXCollections.observableArrayList(searchTerm);
                notLoggedInSearchTable.setItems(data);
            }
            Utkommenterad tills vidare*/
            //TODO: else sats som säger att användaren måste välja en objekttyp
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//Nu har jag helt ärligt glömt varför jag behövde den men jag provade utan och det fick jag inte
    @Override
    public void update(Observable o, Object arg) {

    }
}
