package controller;

import com.sun.prism.shader.FillEllipse_Color_AlphaTest_Loader;
import d0024e.exupg_bibliotekssystem.MainApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javassist.Loader;
import model.*;
import org.jboss.jandex.Main;
import state.ApplicationState;

import javax.persistence.SecondaryTable;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.stage.Popup;

public class FirstLoggedOutViewController extends Controller {
    private ApplicationState state = ApplicationState.getInstance(); //... JUSTE det här behövdes för state lämnades null och jag kom inte på ett bättre sätt
    private ApplicationState APPSTATE;

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

    //Tableview för film och dess kolumner
    public TableView<Film> notLoggedInFilmSearchTable;
    public TableColumn<Film, String> filmTitleColumn;
    public TableColumn<Film, String> productionCountryColumn;
    public TableColumn<Film, Integer> ageLimitColumn;
    public TableColumn<Film, String> directorsColumn;
    public TableColumn<Film, String> actorsColumn;
    public TableColumn<Film, String> genreColumn;

    private String selectedObject;

    public void onUserGoToLoginViewButtonClick(ActionEvent actionEvent) {
        super.getState().app.openLoginView();
    }

    public void handleBokOption(ActionEvent actionEvent) {
        notLoggedInBookSearchTable.setVisible(true);
        notLoggedInFilmSearchTable.setVisible(false);
        selectedObject = "Bok";
        objektTypFlerVal.setText(selectedObject); //Sätter vilket typ av objekt som ska sökas efter
    }

    public void handleFilmOption(ActionEvent actionEvent) {
        notLoggedInBookSearchTable.setVisible(false);
        notLoggedInFilmSearchTable.setVisible(true);
        selectedObject = "Film";
        objektTypFlerVal.setText(selectedObject);
    }

    public void handleTidskriftOption(ActionEvent actionEvent) {
        selectedObject = "Tidskrift";
        objektTypFlerVal.setText(selectedObject);
    }
//TODO: Se till att det inte blir randomized ämnesord
    public void onNotLoggedInSearchButtonClick(ActionEvent actionEvent) {
/*Faktiska som körs och inte bara hämtar information
* Den går igenom vilket objekt som för nuvarande finns i splitmenubutton (drop down menyn) och går till den det gäller
* */
        if (selectedObject == null){
            System.out.println("Gnäll");
        }
        if (searchtermBoxContents.getText().trim().isEmpty()){
            return; //TODO pop up om att du får inte söka på inget
        }
        try{
            if(selectedObject.equals("Bok")){
                List<Bok> searchTerm = state.databaseService.searchAndGetBooks(searchtermBoxContents.getText().trim());

                ObservableList<Bok> data = FXCollections.observableArrayList(searchTerm);
                notLoggedInBookSearchTable.setItems(data);
                System.out.println("Rows added to table: " + data.size() + " Bok");

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
                List<Film> searchTerm = state.databaseService.searchAndGetFilms(searchtermBoxContents.getText().trim());

                ObservableList<Film> data = FXCollections.observableArrayList(searchTerm);
                notLoggedInFilmSearchTable.setItems(data);
                System.out.println("Rows added to table: " + data.size() + " Film");

                filmTitleColumn.setCellValueFactory(new PropertyValueFactory<>("titel"));
                productionCountryColumn.setCellValueFactory(new PropertyValueFactory<>("produktionsland"));
                ageLimitColumn.setCellValueFactory(new PropertyValueFactory<>("åldersgräns"));
                directorsColumn.setCellValueFactory(cellData -> {
                    Film film = cellData.getValue();
                    String directorName = film.getRegissörs().stream()
                            .map(regissör -> regissör.getFörnamn() + " " + regissör.getEfternamn())
                            .collect(Collectors.joining(", "));
                    return new SimpleStringProperty(directorName);
                });
                actorsColumn.setCellValueFactory(cellData -> {
                    Set<Skådespelare> actors = cellData.getValue().getSkådespelares();
                    String fullNamn = actors.stream()
                            .map(actor -> actor.getFörnamn() + " " + actor.getEfternamn())
                            .collect(Collectors.joining(","));
                    return new SimpleStringProperty(fullNamn);
                });
                genreColumn.setCellValueFactory(cellData -> {
                    Set<Genre> genres = cellData.getValue().getGenres();
                    String allaGenre = genres.stream()
                            .map(Genre::getGenreNamn)
                            .collect(Collectors.joining(", "));
                    return new SimpleStringProperty(allaGenre);
                });
            }
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
        System.out.println("System");
        if (state.getCurrentUser() == null) {return;}
        if(state.getCurrentUser().getAnvändartyp().getAnvändartyp().equalsIgnoreCase("allmänhet")) {
            //state av en scene har en currentUser, vi hämtar dens användartyp(klass) som vi sen behöver hämta exakt användartyp(type) ifrån, sen .equals
            System.out.println("I work :D");
        }
    }
}
