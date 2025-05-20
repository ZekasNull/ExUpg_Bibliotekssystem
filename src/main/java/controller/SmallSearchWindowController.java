package controller;

import d0024e.exupg_bibliotekssystem.MainApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import service.BookDatabaseService;
import service.FilmDatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Skapar ett litet sökfönster. Bör ha referens till kontrollern som skapade den så att den kan stänga sig själv när
 * sökningen är klar.
 */
public class SmallSearchWindowController extends Controller {
    //tjänster
    BookDatabaseService bookDatabaseService;
    FilmDatabaseService filmDatabaseService;
    //förälderns stage
    private Stage stage;
    //debug
    private final boolean DEBUGPRINTOUTS = MainApplication.DEBUGPRINTING;

    //inställning
    public enum Mode {BOK, FILM};
    private Mode mode = Mode.BOK;



    //boktable
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

    //filmtable
    @FXML
    private TableView<Film> FilmSearchTable;
    @FXML
    private TableColumn<?, ?> filmTitleColumn;
    @FXML
    private TableColumn<?, ?> productionCountryColumn;
    @FXML
    private TableColumn<?, ?> ageLimitColumn;
    @FXML
    private TableColumn<Film, String> actorsColumn;
    @FXML
    private TableColumn<Film, String> directorsColumn;
    @FXML
    private TableColumn<Film, String> genreColumn;


    public void setMode(Mode mode) {
        if(DEBUGPRINTOUTS) System.out.println("smallSearchWindowController: Setting mode to " + mode);
        this.mode = mode;
        if(mode == Mode.FILM) {
            notLoggedInBookSearchTable.setVisible(false);
            FilmSearchTable.setVisible(true);
        }else if (mode == Mode.BOK){
            notLoggedInBookSearchTable.setVisible(true);
            FilmSearchTable.setVisible(false);
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadServicesFromState() {
        super.loadServicesFromState();
        //tjänster
        bookDatabaseService = getState().getBookDatabaseService();
        filmDatabaseService = getState().getFilmDatabaseService();
    }

    public void initialize() {
        //böcker
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
                //cellData = en instans av "CellDataFetures<Objekt, String> som ger access till objektet som kommer skrivas ut i vilken rad det gäller i Tableview
        );
        //Här är det fråga om sammansätta förnamn och efternamn
        authorNameColumn.setCellValueFactory(cellData -> {
            Bok bok = cellData.getValue();
            String authorName = bok.getFörfattare().stream()
                    .map(författare -> författare.getFörnamn() + " " + författare.getEfternamn())
                    .collect(Collectors.joining(","));
            return new SimpleStringProperty(authorName);
        });

        //filmer
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

    @FXML
    void okButtonPressed(ActionEvent event) {
        switch (mode){
            case BOK:
                if(notLoggedInBookSearchTable.getSelectionModel().getSelectedItem() == null) return; //om inget valts
                Bok selectedBook = notLoggedInBookSearchTable.getSelectionModel().getSelectedItem();
                if(DEBUGPRINTOUTS) System.out.println("smallSearchWindowController: Adding book to state");
               getState().setBookSearchResults(new ArrayList<>(List.of(selectedBook)));
                break;
            case FILM:
                if(FilmSearchTable.getSelectionModel().getSelectedItem() == null) return;
                Film selectedFilm = FilmSearchTable.getSelectionModel().getSelectedItem();
                if(DEBUGPRINTOUTS) System.out.println("smallSearchWindowController: Adding film to state");
                getState().setFilmSearchResults(new ArrayList<>(List.of(selectedFilm)));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }
        stage.close();
    }

    @FXML
    void onSearchButtonClick(ActionEvent event) {
        List<Bok> bokSearchResults;
        List<Film> filmSearchResults;
        String searchterm = searchtermBoxContents.getText().trim();

        switch (mode){
            case BOK:
                bokSearchResults = bookDatabaseService.searchAndGetBooks(searchterm);
                ObservableList<Bok> bookData = FXCollections.observableArrayList(bokSearchResults);
                notLoggedInBookSearchTable.setItems(bookData);
                if(DEBUGPRINTOUTS) System.out.println("smallSearchWindowController: Rows added to table: " + bookData.size());
                break;
            case FILM:
                filmSearchResults = filmDatabaseService.searchAndGetFilms(searchterm);
                ObservableList<Film> filmData = FXCollections.observableArrayList(filmSearchResults);
                FilmSearchTable.setItems(filmData);
                if(DEBUGPRINTOUTS) System.out.println("smallSearchWindowController: Rows added to table: " + filmData.size());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
