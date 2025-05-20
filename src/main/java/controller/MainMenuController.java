package controller;

import d0024e.exupg_bibliotekssystem.MainApplication;
import service.ViewLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.*;

import java.io.IOException;

import org.postgresql.util.PSQLException;
import service.BookDatabaseService;
import service.FilmDatabaseService;
import service.UserDatabaseService;
import state.ApplicationState;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class MainMenuController extends Controller {
    //debug
    private final boolean DEBUGPRINTOUTS = MainApplication.DEBUGPRINTING;
    //programreferenser
    private ApplicationState state;
    private BookDatabaseService bookDatabaseService;
    private UserDatabaseService userDatabaseService;
    private FilmDatabaseService filmDatabaseService;

    //intern data
    private ObservableList<Exemplar> exemplarTableList = FXCollections.observableArrayList();
    private ObservableList<Exemplar> exemplarToBorrowList = FXCollections.observableArrayList();

    //fxml
    @FXML
    private Button LogOutButton;
    public Button ShowProfileButton;
    public Button EmployeeViewButton;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userRoleLabel;

    @FXML
    public Button LogInViewButton; //Knapp för att logga in
    public TextField searchtermBoxContents; //Vad som står i själva sökboxen
    public SplitMenuButton objektTypFlerVal; //Vilket typ av objekt som ska filtreras
    public Button BorrowAllButton;

    private String selectedObjectType;
    public Button borrowObjectButton;

    //borrowtable
    public TableView<Exemplar> BorrowTable;
    public TableColumn<Exemplar, String> BorrowListColumn;

    //booktable
    public TableColumn<Bok, String> titleColumn;
    public TableColumn<Bok, String> isbnColumn;
    public TableColumn<Bok, String> subjectColumn;
    public TableColumn<Bok, String> authorNameColumn;
    public TableView<Bok> notLoggedInBookSearchTable; // tableView för bok, ovan är dess kolumner

    public Button SearchButton;

    @FXML
    private Button removeLoanButton;

    //Tableview för film och dess kolumner
    public TableView<Film> notLoggedInFilmSearchTable;
    public TableColumn<Film, String> filmTitleColumn;
    public TableColumn<Film, String> productionCountryColumn;
    public TableColumn<Film, Integer> ageLimitColumn;
    public TableColumn<Film, String> directorsColumn;
    public TableColumn<Film, String> actorsColumn;
    public TableColumn<Film, String> genreColumn;

    //tableview tidskrift
    @FXML
    private TableView<Upplaga> tidskriftTable;

    @FXML
    private TableColumn<Upplaga, String> tidskriftNameColumn;

    @FXML
    private TableColumn<Upplaga, Integer> tidskriftYearColumn;

    @FXML
    private TableColumn<Upplaga, Integer> tidskriftIssueColumn;

    //exemplar
    @FXML
    private TableView<Exemplar> exemplarViewTable;

    @FXML
    private TableColumn<Exemplar, ?> BarcodeColumn;

    @FXML
    private TableColumn<Exemplar, String> LåntypColumn;

    @FXML
    private TableColumn<Exemplar, String> TillgängligColumn;

    public void initialize() {
        //initialise tables and columns
        userRoleLabel.setText("");
        //booksearchtable
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

        //filmsearchtable
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

        //exemplar
        exemplarViewTable.setItems(exemplarTableList);
        BarcodeColumn.setCellValueFactory(new PropertyValueFactory<>("streckkod"));
        LåntypColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLåntyp().getLåntyp()));
        TillgängligColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTillgänglig() ? "Ja" : "Nej"));

        //borrowTable
        BorrowTable.setItems(exemplarToBorrowList);
        BorrowListColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getBok() == null) {
                return new SimpleStringProperty(cellData.getValue().getFilm_id().getTitel());
            }else{
                return new SimpleStringProperty(cellData.getValue().getBok().getTitel());
            }
        });

        //tidskrifttable
        tidskriftNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTidskrift().getNamn()));
        tidskriftYearColumn.setCellValueFactory(new PropertyValueFactory<>("år"));
        tidskriftIssueColumn.setCellValueFactory(new PropertyValueFactory<>("upplaga_nr"));

    }

    @Override
    public void loadServicesFromState() {
        if (DEBUGPRINTOUTS) System.out.println("Loading MainMenuController services, state MUST EXIST WTF" + getState());
        super.loadServicesFromState();
        this.state = getState();
        this.bookDatabaseService = state.getBookDatabaseService();
        this.userDatabaseService = state.getUserDatabaseService();
        this.filmDatabaseService = state.getFilmDatabaseService();
    }

    public void onUserLogInViewButtonClick(ActionEvent actionEvent) throws IOException {
        viewLoader.loadPopup(ViewLoader.Views.LOGIN_WINDOW);
        //ger pop-up istället för utbyte av hela Stage
    }

    public void handleBokOption(ActionEvent actionEvent) {
        //set tables
        notLoggedInBookSearchTable.setVisible(true);
        notLoggedInFilmSearchTable.setVisible(false);
        tidskriftTable.setVisible(false);

        exemplarViewTable.setDisable(false);
        //aktivera exemplartable

        //class var
        selectedObjectType = "Bok";
        objektTypFlerVal.setText(selectedObjectType); //Sätter vilket typ av objekt som ska sökas efter
    }

    public void handleFilmOption(ActionEvent actionEvent) {
        //set tables
        notLoggedInBookSearchTable.setVisible(false);
        notLoggedInFilmSearchTable.setVisible(true);

        exemplarViewTable.setDisable(false);
        //aktivera exemplartable

        //class var
        tidskriftTable.setVisible(false);
        selectedObjectType = "Film";
        objektTypFlerVal.setText(selectedObjectType);
    }

    public void handleTidskriftOption(ActionEvent actionEvent) {
        //set tables
        notLoggedInBookSearchTable.setVisible(false);
        notLoggedInFilmSearchTable.setVisible(false);

        exemplarViewTable.setDisable(true);
        //aktivera exemplartable

        //class var
        tidskriftTable.setVisible(true);
        selectedObjectType = "Tidskrift";
        objektTypFlerVal.setText(selectedObjectType);
    }

//TODO: Se till att det inte blir randomized ämnesord

    public void onSearchButtonClick(ActionEvent actionEvent) {
        /*Den går igenom vilket objekt som för nuvarande finns i splitmenubutton (drop down menyn)
         och går till den det gäller
        */
        String trimmedSearchTerm = searchtermBoxContents.getText().trim();

        //check that user selected options and entered text
        if (trimmedSearchTerm.isEmpty()&& selectedObjectType == null){
            showErrorPopup("Du måste välja en objekttyp och minst ett sökord");
            return;
        } else if (trimmedSearchTerm.isEmpty()){
            showErrorPopup("Du måste ange sökord");
            return;
        }else if (selectedObjectType == null){
            showErrorPopup("Du måste välja en objekttyp");
            return;
        }


        switch(selectedObjectType) {
            case "Bok":
                List<Bok> bokResults = bookDatabaseService.searchAndGetBooks(trimmedSearchTerm);
                ObservableList<Bok> bookData = FXCollections.observableArrayList(bokResults);
                notLoggedInBookSearchTable.setItems(bookData);
                if (DEBUGPRINTOUTS)
                    System.out.println("MainMenuController: Rows added to table: " + bookData.size() + " Bok");

                break;
            case "Film":
                List<Film> filmResults = filmDatabaseService.searchAndGetFilms(trimmedSearchTerm);
                ObservableList<Film> filmData = FXCollections.observableArrayList(filmResults);
                notLoggedInFilmSearchTable.setItems(filmData);
                if (DEBUGPRINTOUTS)
                    System.out.println("MainMenuController: Rows added to table: " + filmData.size() + " Film");
                break;
            case "Tidskrift":
                List<Tidskrift> tidskriftsResults = bookDatabaseService.searchAndGetTidskrifter(trimmedSearchTerm);
                ObservableList<Upplaga> tidskriftData = FXCollections.observableArrayList();
                for(Tidskrift t : tidskriftsResults) {
                    tidskriftData.addAll(t.getUpplagas());
                }

                tidskriftTable.setItems(tidskriftData);
                break;
            default:
                if (DEBUGPRINTOUTS)
                    System.out.println("MainMenuController: Invalid search state");
                break;
        }
    }

    public void onLogOutButtonClick(ActionEvent actionEvent) {
        getState().setCurrentUser(null);
    }

    public void onShowProfileButtonClick(ActionEvent actionEvent) {
        viewLoader.setView(ViewLoader.Views.PROFILE);
    }

    public void onEmployeeViewButton(ActionEvent actionEvent) {
        viewLoader.setView(ViewLoader.Views.LIBRARIAN_FIRST_CHOICE);
    }

    public void onBorrowObjectButtonClick(ActionEvent actionEvent) {
        //Kolla så användaren är inloggad
        if (state.getCurrentUser() == null) {
            showErrorPopup("Du måste logga in för att låna objekt");
            return;
        }

        //Sätter selectedItem och kollar vilken objekttyp det gäller
        Exemplar selectedItem;

        selectedItem = exemplarViewTable.getSelectionModel().getSelectedItem();

        //Kollar om användaren valt en rad
        if(selectedItem == null) {
            showErrorPopup("Du måste välja ett exemplar först");
            return;
        }

        exemplarToBorrowList.add(selectedItem);
    }

    public void onBorrowAllButtonClick(ActionEvent actionEvent) throws Exception{
        Instant timeOfLoan = Instant.now();
        if (exemplarToBorrowList.isEmpty()) {
            showErrorPopup("Lånelistan är tom");
            return;
        }

        List<Lån> nyaLån = new ArrayList<>();
        Lån nextLoan;
        for (Exemplar ex : exemplarToBorrowList) {
            if(ex.getLåntyp().getLåntyp().equals("referenslitteratur")){
                showErrorPopup("Du kan inte låna referenslitteratur");
                return;
            }
            nextLoan = new Lån();
            nextLoan.setAnvändare(state.getCurrentUser());
            nextLoan.setStreckkod(ex);
            nyaLån.add(nextLoan);
        }

        try {
            userDatabaseService.läggTillNyaLån(nyaLån); //skicka lista med inkommande lån
        } catch (PSQLException e) {
            if (DEBUGPRINTOUTS) System.out.println("MainMenuController: Caught error trying to register loans");
            String msg = e.getMessage();
            if (msg != null && msg.contains("redan utlånat")) {
                showErrorPopup("Du kan ej låna fler objekt");
                return;
            } else if (msg != null && msg.contains("reached the maximum allowed")) {
                int currentLoans, incomingLoans, maxLoans;
                currentLoans = state.getCurrentUser().getLåns().size();
                incomingLoans = exemplarToBorrowList.size();
                maxLoans = state.getCurrentUser().getAnvändartyp().getMaxLån();
                showErrorPopup("Du har "+currentLoans+" av "+maxLoans+" maximala lån och försökte låna "+incomingLoans+" till.");
                return;
            }
        }

        state.updateUserInformation(); //ser till att användarens nya lån laddas
        printReceipt(timeOfLoan);

        exemplarToBorrowList.clear();

    }

    private void printReceipt(Instant timeOfLoan) {
        //metodvariabler
        Set<Lån> låns = state.getCurrentUser().getLåns();
        
        låns = låns.stream().filter(lån -> lån.getReturDatum().isAfter(timeOfLoan) ).collect(Collectors.toSet());

        System.out.println("=== LÅNEKVITTO ===");
        System.out.println("Låntagare: " + state.getCurrentUser().getFulltNamn());
        System.out.println("Lånedatum: " + timeOfLoan);
        System.out.println("Lånade objekt:");
        for (Lån lån : låns) {
            if (lån.getStreckkod().getBok() != null) {
                System.out.println(" - " + lån.getStreckkod().getBok().getTitel());
            } else {
                System.out.println(" - " + lån.getStreckkod().getFilm_id().getTitel());
            }
            System.out.println("   Returneras senast: " + lån.getReturDatum());
        }
        System.out.println("================");


    }

    @Override
    public void update(Observable o, Object arg) {
        //FIXME borde inte vara update som ställer om i fönstret

        if (DEBUGPRINTOUTS) System.out.println("MainMenuController: Update called");
        if(arg != ApplicationState.UpdateType.USER) return; //ignore non-user updates

        Användare currentUser = state.getCurrentUser();

        //om det inte längre finns en currentuser
        if(getState().getCurrentUser() == null) {
            userNameLabel.setText("Ej inloggad");
            userRoleLabel.setText("");
            setWindowStateToNotLoggedIn();
            return;
        }
        userNameLabel.setText(currentUser.getFulltNamn());
        userRoleLabel.setText(currentUser.getAnvändartyp().getAnvändartyp());
        if(currentUser.getAnvändartyp().getAnvändartyp().equals("bibliotekarie")) {
            setWindowStateToAdmin();
        } else {
            setWindowStateToUser();
        }
    }

    private void setWindowStateToAdmin() {
        if (DEBUGPRINTOUTS) System.out.println("MainMenuController: Bibliotekarie inloggad");
        setWindowStateToUser();
        EmployeeViewButton.setVisible(true);
    }

    private void setWindowStateToUser() {
        LogOutButton.setVisible(true);
        ShowProfileButton.setVisible(true);
        LogInViewButton.setVisible(false);
        borrowObjectButton.setDisable(false);
        BorrowAllButton.setDisable(false);
    }

    private void setWindowStateToNotLoggedIn() {
        if (DEBUGPRINTOUTS) System.out.println("MainMenuController: Ingen e inloggad");
        LogOutButton.setVisible(false);
        ShowProfileButton.setVisible(false);
        EmployeeViewButton.setVisible(false);
        LogInViewButton.setVisible(true);
        borrowObjectButton.setDisable(true);
        BorrowAllButton.setDisable(true);
    }

    @FXML
    void removeLoanButtonClicked(ActionEvent event) {

        Exemplar selectedEx = BorrowTable.getSelectionModel().getSelectedItem();
        exemplarToBorrowList.remove(selectedEx);

        removeLoanButton.setDisable(exemplarToBorrowList.isEmpty());
    }

    public void filmTableClicked(MouseEvent mouseEvent) {
        if(notLoggedInFilmSearchTable.getSelectionModel().getSelectedItem() == null) return;
        exemplarTableList.clear();
        exemplarTableList.addAll(notLoggedInFilmSearchTable.getSelectionModel().getSelectedItem().getExemplars());
        if (DEBUGPRINTOUTS) System.out.println("MainMenuController: Film table clicked, loaded " + exemplarTableList.size() + " exemplars");
    }

    public void bookTableClicked(MouseEvent mouseEvent) {
        if(notLoggedInBookSearchTable.getSelectionModel().getSelectedItem() == null) return;
        exemplarTableList.clear();
        exemplarTableList.addAll(notLoggedInBookSearchTable.getSelectionModel().getSelectedItem().getExemplars());
        if (DEBUGPRINTOUTS) System.out.println("MainMenuController: Book table clicked, loaded " + exemplarTableList.size() + " exemplars");
    }

    public void borrowTableClicked(MouseEvent mouseEvent) {
        removeLoanButton.setDisable(exemplarToBorrowList.isEmpty());
    }

    public void exemplarViewTableClicked(MouseEvent mouseEvent) {
        if (exemplarViewTable.getSelectionModel().getSelectedItem() == null) {
            borrowObjectButton.setDisable(true);
            return;
        }
        Exemplar ex = exemplarViewTable.getSelectionModel().getSelectedItem();
        if (DEBUGPRINTOUTS) System.out.println("MainMenuController: Exemplar view table clicked, selected exemplar has loantype: " + ex.getLåntyp().getLåntyp() +" and the availability is: "+ex.getTillgänglig() );

        if (ex.getLåntyp().getLåntyp().equals("referenslitteratur")) { //får inte lånas
            borrowObjectButton.setDisable(true);
            return;
        }
        if (!ex.getTillgänglig()) { //redan utlånad
            borrowObjectButton.setDisable(true);
            return;
        }
        if(exemplarToBorrowList.contains(ex)){ //får inte låna samma ex flera gånger
            borrowObjectButton.setDisable(true);
        }else{
            borrowObjectButton.setDisable(false);
        }


    }

    private void borrowingAllowed (Boolean state) {
        borrowObjectButton.setDisable(!state);
    }
}
