package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Bok;
import model.Ämnesord;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

/**
 * Skapar ett litet sökfönster. Bör ha referens till kontrollern som skapade den så att den kan stänga sig själv när
 * sökningen är klar.
 */
public class smallSearchWindowController extends Controller {
    private AddBookViewController parentController;

    private final boolean debugPrintouts = true;

    public void setParentController(AddBookViewController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private TableColumn<Bok, String> authorNameColumn;

    @FXML
    private TableColumn<?, ?> isbnColumn;

    @FXML
    private TableView<Bok> notLoggedInBookSearchTable;

    @FXML
    private TextField searchtermBoxContents;

    @FXML
    private TableColumn<Bok, String> subjectColumn;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    void okButtonPressed(ActionEvent event) {
        if(debugPrintouts) System.out.println("smallSearchWindowController: Adding book to state");

        if(notLoggedInBookSearchTable.getSelectionModel().getSelectedItem() == null) return; //om inget valts
        Bok selectedBook = notLoggedInBookSearchTable.getSelectionModel().getSelectedItem();
        super.getState().setBookSearchResults(new ArrayList<>(List.of(selectedBook)));
        parentController.closeSmallSearchWindow();
    }

    @FXML
    void onSearchButtonClick(ActionEvent event) {

        List<Bok> searchResults = super.getState().databaseService.searchAndGetBooks(searchtermBoxContents.getText().trim());


        ObservableList<Bok> data = FXCollections.observableArrayList(searchResults);
        notLoggedInBookSearchTable.setItems(data);
        if(debugPrintouts) System.out.println("smallSearchWindowController: Rows added to table: " + data.size());

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titel"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn13"));
        //För ämnesord behövde vi ta och skriva ut varje element i listan med ett ',' mellan
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

    @Override
    public void update(Observable o, Object arg) {

    }
}
