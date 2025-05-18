package controller;

import db.DatabaseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.io.IOException;

import net.bytebuddy.implementation.bind.annotation.Super;
import org.hibernate.dialect.Database;
import service.BookDatabaseService;
import service.FilmDatabaseService;
import state.BorrowItemInterface;

import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MainMenuController extends Controller {
    public Button LogOutButton;
    public Button ShowProfileButton;
    public Button EmployeeViewButton;

    @FXML
    public Button LogInViewButton; //Knapp för att logga in
    public TextField searchtermBoxContents; //Vad som står i själva sökboxen
    public SplitMenuButton objektTypFlerVal; //Vilket typ av objekt som ska filtreras
    public Button BorrowAllButton;
    public TableColumn<BorrowItemInterface, String> BorrowListColumn;
    public TableView<BorrowItemInterface> BorrowTable;
    private String selectedObject;
    public Button BorrowObjectButton;

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

    public void onUserLogInViewButtonClick(ActionEvent actionEvent) throws IOException {
        super.getState().vy.loadPopup("login-view.fxml", "Log in view");
        //ger pop-up istället för utbyte av scenen som viewloader skulle
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
        if (searchtermBoxContents.getText().trim().isEmpty()&& selectedObject == null){
            showErrorPopup("Du måste välja en objekttyp och minst ett sökord");
            return;
        } else if (searchtermBoxContents.getText().trim().isEmpty()){
            showErrorPopup("Du måste ange sökord");
            return;
        }else if (selectedObject == null){
            showErrorPopup("Du måste välja en objekttyp");
            return;
        }
        try{
            if(selectedObject.equals("Bok")){
                List<Bok> searchTerm = super.getState().databaseService.searchAndGetBooks(searchtermBoxContents.getText().trim());

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
                List<Film> searchTerm = super.getState().databaseService.searchAndGetFilms(searchtermBoxContents.getText().trim());

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
        //FIXME: Logga ut ordentligt :|
    }

    public void onShowProfileButtonClick(ActionEvent actionEvent) {
        super.getState().vy.loadScene("show-profile-view.fxml", "Profil");
    }

    public void onEmployeeViewButton(ActionEvent actionEvent) {
        super.getState().vy.loadScene("librarian-first-choice-view.fxml", "Bibliotekarnas första val");
    }

    public void onBorrowObjectButtonClick(ActionEvent actionEvent) {
        //Kolla så användaren är inloggad
        if (super.getState().getCurrentUser() == null) {
            showErrorPopup("Du måste logga in för att låna objekt");
            return;
        }
        //Sätter selectedItem

        BorrowItemInterface selectedItem = null;
        if (selectedObject.equals("Bok")) {
            selectedItem = notLoggedInBookSearchTable.getSelectionModel().getSelectedItem();
        } else if (selectedObject.equals("Film")) {
            selectedItem = notLoggedInFilmSearchTable.getSelectionModel().getSelectedItem();
        }

        if(selectedItem == null) {
            showErrorPopup("Du måste välja ett objekt från en sökning först");
            return;
        }

        //Ser ifall det finns ett exemplar ledigt
        Optional<Exemplar> exemplarAttLåna = selectedItem
                .getExemplars()
                .stream()
                .filter(Exemplar::getTillgänglig)
                .findFirst();

        if (exemplarAttLåna.isEmpty()) {
            showErrorPopup("Inga exemplar är tillgängliga för utlåning");
            return;
        }

        //Sätter exemplaret som är i listan som icket tillgänglig
        super.getState().databaseService.markExemplarAsBorrowed(exemplarAttLåna.get());

        if (exemplarAttLåna.isEmpty()) {
            showErrorPopup("Inga exemplar är tillgängliga för utlåning");
            return;
        }

        super.getState().databaseService.isItemAvailable(selectedItem);
        if (exemplarAttLåna.isEmpty()) {
            showErrorPopup("Inga exemplar är tillgängliga för utlåning");
            return;
        }

        super.getState().getBorrowList().add(selectedItem);
        BorrowTable.setItems(super.getState().getBorrowList());
        BorrowListColumn.setCellValueFactory(new PropertyValueFactory<>("titel"));

        System.out.println(super.getState().getBorrowList().size() + " rows added to Låna");
    }

    public void onBorrowAllButtonClick(ActionEvent actionEvent) {
        //Hämtar och kollar att lånelistan inte är tom
        List<BorrowItemInterface> borrowList = super.getState().getBorrowList();
        if (borrowList.isEmpty()) {
            showErrorPopup("Lånelistan är tom");
            return;
        }
        //Itererar genom listan och kollar varje item
        for (BorrowItemInterface item : borrowList) {
            Optional<Exemplar> optionalExemplar = super.getState().databaseService.isItemAvailable(item);

            if (optionalExemplar.isPresent()) {
                Exemplar exemplar = optionalExemplar.get();

                super.getState().databaseService.markExemplarAsBorrowed(exemplar);

                //Skapa nytt lån
                Lån lån = new Lån();
                lån.setAnvändare(super.getState().getCurrentUser());
                lån.setStreckkod(exemplar);

                //Se till att lånet blir registrerat i datbasen
                super.getState().databaseService.registerLoan (lån);

            } else {
                System.out.println("Inget tillgängligt exemplar för: " + item.getTitel());
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        String currentUserType = super.getState().getCurrentUser().getAnvändartyp().getAnvändartyp();
        if (currentUserType == null) {
            System.out.println("Ingen e inloggad");
        } else if(currentUserType.equals("bibliotekarie")) {
            System.out.println("Bibliotekarie inloggad");
            LogOutButton.setVisible(true);
            System.out.println("Bibliotekarie inloggad");
            ShowProfileButton.setVisible(true);
            EmployeeViewButton.setVisible(true);
            LogInViewButton.setVisible(false);
            BorrowObjectButton.setOpacity(2); //FIXME: ta reda på varför den blir genomskinlig efter flera objekt läggs på borrowlist
            BorrowAllButton.setVisible(true);
        }else{
            LogOutButton.setVisible(true);
            ShowProfileButton.setVisible(true);
            LogInViewButton.setVisible(false);
            BorrowObjectButton.setOpacity(2);
            BorrowAllButton.setVisible(true);
        }
    }

    public void tableViewMouseClick(javafx.scene.input.MouseEvent mouseEvent) {
        //Skriver ut i konsol för testsyfte
        System.out.println(notLoggedInBookSearchTable.getSelectionModel().getSelectedItem().toString());
    }
}
