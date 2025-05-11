package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import state.ApplicationState;

import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.stream.Collectors;

public class MainMenuController extends Controller {
    public Button LogOutButton;
    public Button ShowProfileButton;
    public Button EmployeeViewButton;
    private ApplicationState state = ApplicationState.getInstance(); //... JUSTE det här behövdes för state lämnades null och jag kom inte på ett bättre sätt

    @FXML
    public Button LogInViewButton; //Knapp för att logga in
    public TextField searchtermBoxContents; //Vad som står i själva sökboxen
    public SplitMenuButton objektTypFlerVal; //Vilket typ av objekt som ska filtreras
    private String selectedObject;
    public Button BorrowObjectButton; //TODO: se till att det kommer upp ett meddelande om att logga in on hover

    public TableColumn<Bok, String> titleColumn;
    public TableColumn<Bok, String> isbnColumn;
    public TableColumn<Bok, String> subjectColumn;
    public TableColumn<Bok, String> authorNameColumn;
    public TableView<Bok> notLoggedInBookSearchTable; // tableView för bok, ovan är dess kolumner

    public Button SearchButton;

    //Tableview för film och dess kolumner
    public TableView<Film> notLoggedInFilmSearchTable;
    public TableColumn<Film, String> filmTitleColumn;
    public TableColumn<Film, String> productionCountryColumn;
    public TableColumn<Film, Integer> ageLimitColumn;
    public TableColumn<Film, String> directorsColumn;
    public TableColumn<Film, String> actorsColumn;
    public TableColumn<Film, String> genreColumn;


    public void onUserLogInViewButtonClick(ActionEvent actionEvent) {
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
    public void onSearchButtonClick(ActionEvent actionEvent) {
/*Faktiska som körs och inte bara hämtar information
* Den går igenom vilket objekt som för nuvarande finns i splitmenubutton (drop down menyn) och går till den det gäller
* */
        if (selectedObject == null){
            System.out.println("Gnäll");
            //TODO: Se ifall selecteobject är null och då ge en varning
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void onLogOutButtonClick(ActionEvent actionEvent) {
    }

    public void onShowProfileButtonClick(ActionEvent actionEvent) {
    }

    public void onEmployeeViewButton(ActionEvent actionEvent) {
    }

    @Override
    public void update(Observable o, Object arg) {
        String currentUserType = state.getCurrentUser().getAnvändartyp().getAnvändartyp();
        if (currentUserType == null) {
            System.out.println("Ingen e inloggad");
        } else if(currentUserType.equals("bibliotekarie")) {
            System.out.println("Bibliotekarie inloggad");
            LogOutButton.setVisible(true);
            System.out.println("Bibliotekarie inloggad");
            ShowProfileButton.setVisible(true);
            EmployeeViewButton.setVisible(true);
            LogInViewButton.setVisible(false);
        }else{
            LogOutButton.setVisible(true);
            ShowProfileButton.setVisible(true);
            LogInViewButton.setVisible(false);
        }
    }
}
