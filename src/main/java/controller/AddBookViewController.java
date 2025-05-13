package controller;

import d0024e.exupg_bibliotekssystem.MainApplication;
import db.DatabaseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AddBookViewController extends Controller {
    //debug
    private final boolean debugPrintouts = false;

    //internt fönster för sökning
    private Stage searchWindow; //håller det lilla sökfönstret

    //lista över exemplar och böcker som visas i tables
    private ObservableList<Exemplar> exemplarList = FXCollections.observableArrayList();
    private ObservableList<Bok> bookList = FXCollections.observableArrayList();

    //när exemplar eller böcker ska tas bort visas de inte längre och hamnar därför i en annan lista
    private ArrayList<Exemplar> exemplarDeletionList = new ArrayList<>();
    private ArrayList<Bok> bookDeletionList = new ArrayList<>();

    //TODO kontrollsträngar bör inte vara string, konvertera till enums senare
    private String exemplarLoanType = ""; //"bok" eller "kurslitteratur" eller "referenslitteratur"
    private String formMode = ""; //"Adding", "Editing", ""

    //private enum FormMode { ADDING, EDITING, NONE }
    //private enum LoanType { BOK, KURSLITTERATUR, REFERENSLITTERATUR }

    //kontrollerar vad som sker när bekräfta ändringar trycks
    private boolean
        hasNewBooks = false,
        hasNewExemplars = false,
        hasChangedBooks = false,
        hasDeletedExemplars = false,
        hasDeletedBooks = false;

    //form text fields
    @FXML
    private TextField idBoxContents;

    @FXML
    private TextField authorFirstNameBoxContents1;

    @FXML
    private TextField authorLastNameBoxContents1;

    @FXML
    private TextField authorFirstNameBoxContents2;

    @FXML
    private TextField authorLastNameBoxContents2;

    @FXML
    private TextField authorFirstNameBoxContents3;

    @FXML
    private TextField authorLastNameBoxContents3;

    @FXML
    private TextField isbnBoxContents;

    @FXML
    private TextField titleBoxContents;

    @FXML
    private TextField amnesordBoxContents1;

    @FXML
    private TextField amnesordBoxContents2;

    @FXML
    private TextField amnesordBoxContents3;

    //bookviewtable
    @FXML
    private TableView<Bok> bookViewTable;
    @FXML
    private TableColumn<Bok, String> AuthorColumn;
    @FXML
    private TableColumn<?, ?> titleColumn;

    //ExemplarViewTable
    @FXML
    private TableView<Exemplar> ExemplarViewTable;
    @FXML
    private TableColumn<?, ?> BarcodeColumn;
    @FXML
    private TableColumn<Exemplar, String> LåntypColumn;
    @FXML
    private TableColumn<Exemplar, String> TillgängligColumn;

    //buttons
    @FXML
    private Button addNewBookButton;

    @FXML
    private Button searchBookButton;

    @FXML
    private Button addCopyButton;

    /**
     * När nya exemplar läggs till ska de ha en låntyp. Den väljs här
     */
    @FXML
    private SplitMenuButton chooseLoanTypeButton;
    @FXML
    void handleBokOption(ActionEvent event) {
        exemplarLoanType = "bok";
        chooseLoanTypeButton.setText("Bok");
    }

    @FXML
    void handleKurslitteraturOption(ActionEvent event) {
        exemplarLoanType = "kurslitteratur";
        chooseLoanTypeButton.setText("Kurslitteratur");
    }

    @FXML
    void handleReferenslitteraturOption(ActionEvent event) {
        exemplarLoanType = "referenslitteratur";
        chooseLoanTypeButton.setText("Referenslitteratur");
    }

    @FXML
    private Button removeBookButton;

    @FXML
    private Button removeCopyButton;

    @FXML
    private Button formOkButton;

    /**
     * När denna knapp trycks läggs antingen en ny bok till eller så ändras en existerande bok.
     * @param event
     */
    @FXML
    void formOkButtonPressed(ActionEvent event) throws Exception {
        //se till att kritiska fält innehåller något exklusive whitespace
        List<TextField> requiredFields = Arrays.asList(
                isbnBoxContents,
                titleBoxContents,
                authorFirstNameBoxContents1,
                authorLastNameBoxContents1
        );
        if (requiredFields.stream().anyMatch(f -> f.getText().isBlank())) return; //ev. flytta in i adding? felmeddelande?

        switch (formMode) {
            case "Adding":
                okAddNewBook();
                break;
            case "Editing":
                okEditExistingBook();
                break;
            default:
                System.out.println("AddBookViewController: formMode is neither 'Adding' nor 'Editing'");
                break;
        }

    }

    private void okEditExistingBook() throws Exception {
        //TODO antar just nu att alla författare och ämnesord är nya


        Bok editedBook = bookViewTable.getSelectionModel().getSelectedItem();

        //ämnesordändringar
        String[] amnesord = {amnesordBoxContents1.getText(), amnesordBoxContents2.getText(), amnesordBoxContents3.getText()};
        HashSet<Ämnesord> newÄmnesordSet = new HashSet<>();
        for(String ord : amnesord){
            if (ord.isEmpty()) continue; //skippa om ord inte finns
            Ämnesord newÄmnesOrd = getState().databaseService.searchAmnesord(ord); //hämta ord om det redan finns

            if (newÄmnesOrd == null) {
                if (debugPrintouts) System.out.println("AddBookViewController: Ord " + ord + " not found in database, creating new one");
                newÄmnesOrd = new Ämnesord();
                newÄmnesOrd.setOrd(ord);
            }
            newÄmnesordSet.add(newÄmnesOrd);
        }
        //författarändringar
        String[] firstName = {authorFirstNameBoxContents1.getText(), authorFirstNameBoxContents2.getText(), authorFirstNameBoxContents3.getText()};
        String[] lastName = {authorLastNameBoxContents1.getText(), authorLastNameBoxContents2.getText(), authorLastNameBoxContents3.getText()};
        HashSet<Författare> newFörfattare = new HashSet<>();
        for (int i = 0; i < firstName.length; i++) {
            if(firstName[i].isEmpty() || lastName[i].isEmpty()) continue;

            Författare f = new Författare();
            f.setFörnamn(firstName[i]);
            f.setEfternamn(lastName[i]);
            newFörfattare.add(f);
        }

        //bokändringar
        editedBook.setTitel(titleBoxContents.getText());
        editedBook.setIsbn13(isbnBoxContents.getText());
        editedBook.setFörfattare(newFörfattare);
        editedBook.setÄmnesord(newÄmnesordSet);
        //meta
        hasChangedBooks = true;
        bookList.remove(editedBook);
        bookList.add(editedBook); //tvinga uppdatering för observablelist genom att ta bort och lägga till igen
        clearForm();
        disableForm(true);
    }

    private void okAddNewBook() throws Exception {
        //TODO antar just nu att alla författare och ämnesord är nya

        //skapa ämnesord
        String[] amnesord = {amnesordBoxContents1.getText(), amnesordBoxContents2.getText(), amnesordBoxContents3.getText()};
        HashSet<Ämnesord> newÄmnesordSet = new HashSet<>();
        for(String ord : amnesord){
            if (ord.isEmpty()) continue; //skippa om ord inte finns
            Ämnesord newÄmnesOrd = getState().databaseService.searchAmnesord(ord); //hämta ord om det redan finns

            if (newÄmnesOrd == null) {
                if (debugPrintouts) System.out.println("AddBookViewController: Ord " + ord + " not found in database, creating new one");
                newÄmnesOrd = new Ämnesord();
                newÄmnesOrd.setOrd(ord);
            }
            newÄmnesordSet.add(newÄmnesOrd);
        }

        //skapa författare
        String[] firstName = {authorFirstNameBoxContents1.getText(), authorFirstNameBoxContents2.getText(), authorFirstNameBoxContents3.getText()};
        String[] lastName = {authorLastNameBoxContents1.getText(), authorLastNameBoxContents2.getText(), authorLastNameBoxContents3.getText()};
        HashSet<Författare> newFörfattare = new HashSet<>();
        for (int i = 0; i < firstName.length; i++) {
            if(firstName[i].isEmpty() || lastName[i].isEmpty()) continue;

            Författare f = new Författare();
            f.setFörnamn(firstName[i]);
            f.setEfternamn(lastName[i]);
            newFörfattare.add(f);
        }

        //skapa bok
        Bok newBook = new Bok();
        newBook.setTitel(titleBoxContents.getText());
        newBook.setIsbn13(isbnBoxContents.getText());
        newBook.setFörfattare(newFörfattare);
        newBook.setÄmnesord(newÄmnesordSet);


        //lägg till ny bok i listan
        bookList.add(newBook);
        //uppdatera tableview
        clearForm();
        disableForm(true);
        hasNewBooks = true;
    }

    @FXML
    private Button clearFormButton;

    /**
     * Om en bok är vald i bookviewtable, sätt till edit book state.
     */
    @FXML
    private Button editBookButton;
    @FXML
    void editBookButtonPressed(ActionEvent event) {
        setWindowToEditingExistingBooksState();
    }

    /**
     * Förbereder klassens tables genom att ställa in deras cellvaluefactories.
     */
    public void initialize() {
        prepareTables();
    }

    @FXML
    void bookTableClicked(MouseEvent event) {
        editBookButton.setDisable(bookList.isEmpty()); //stäng av eller sätt på knapp om boklistan innehåller något
        if (bookList.isEmpty()) return; //förhindra exekvering när listan är tom

        populateTextFields();

        //rensa existerande exemplar i andra listan
        exemplarList.clear();
        exemplarList.addAll(bookViewTable.getSelectionModel().getSelectedItem().getExemplars()); //hämta ex från bok


    }

    private void populateTextFields() {
        //ta vald bok
        Bok bok = bookViewTable.getSelectionModel().getSelectedItem();
        Ämnesord[] ordlista = bok.getÄmnesord().toArray(new Ämnesord[0]); //konvertera till array för att tillåta indexering
        Författare[] författarlista = bok.getFörfattare().toArray(new Författare[0]); //storlek 0 på skickad array för att tvinga fram ny
        //populate fields
        if(bok.getId() != null) idBoxContents.setText(bok.getId().toString()); //id kan vara null för nya böcker
        isbnBoxContents.setText(bok.getIsbn13());
        titleBoxContents.setText(bok.getTitel());
        //ämnesord
        amnesordBoxContents1.setText(ordlista.length > 0 ?  ordlista[0].getOrd() : "");
        amnesordBoxContents2.setText(ordlista.length > 1 ?  ordlista[1].getOrd() : "");
        amnesordBoxContents3.setText(ordlista.length > 2 ?  ordlista[2].getOrd() : "");
        //författare1
        authorFirstNameBoxContents1.setText(författarlista.length > 0 ? författarlista[0].getFörnamn() : "");
        authorLastNameBoxContents1.setText(författarlista.length > 0 ? författarlista[0].getEfternamn() : "");
        //författare2
        authorFirstNameBoxContents2.setText(författarlista.length > 1 ? författarlista[1].getFörnamn() : "");
        authorLastNameBoxContents2.setText(författarlista.length > 1 ? författarlista[1].getEfternamn() : "");
        //författare3
        authorFirstNameBoxContents3.setText(författarlista.length > 2 ? författarlista[2].getFörnamn() : "");
        authorLastNameBoxContents3.setText(författarlista.length > 2 ? författarlista[2].getEfternamn() : "");
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        //TODO Ska stänga fönstret och återgå till föregående vy
    }

    @FXML
    void addExemplarTableButtonPressed(ActionEvent event) {
        //se till att låntyp är vald
        if(exemplarLoanType.isEmpty()) {
            chooseLoanTypeButton.requestFocus();
            return;
        };
        //Skapar ett exemplar för den valda boken med den valda låntypen. Läggs in i databasen när bekräfta ändringar trycks.

        //se till att bara en bok finns (och därmed bara dess exemplar)
        Bok bok;
        if (bookList.size() > 1) {
            bok = bookViewTable.getSelectionModel().getSelectedItem();
            bookList.clear();
            bookList.add(bok);
        } else {
            bok = bookList.get(0);
        }


        //skapa nytt ex
        Exemplar nyttex = new Exemplar();
        nyttex.setBok(bok);
        nyttex.setNewLåntyp(exemplarLoanType);
        exemplarList.add(nyttex);
        hasNewExemplars = true;
    }

    @FXML
    void clearFormButtonPressed(ActionEvent event) {
        clearForm();
    }

    @FXML
    void removeBokTableButtonPressed(ActionEvent event) {
        Bok bok = bookViewTable.getSelectionModel().getSelectedItem();
        if (bok.getId() != null) { //böcker utan id kan tas bort direkt då de inte finns i db
            if (onDeleteUserConfirmation()) {
                hasDeletedBooks = true;
                bookDeletionList.add(bok);
            }
        }
        bookList.remove(bok);
        exemplarList.clear();
        if(bookList.isEmpty()) {
            hasNewBooks = false;
        }
    }

    @FXML
    void removeExemplarButtonPressed(ActionEvent event) {
        Bok bok = bookViewTable.getSelectionModel().getSelectedItem();
        Exemplar ex = ExemplarViewTable.getSelectionModel().getSelectedItem();
        if (ex.getStreckkod() != null) {
            if (onDeleteUserConfirmation()) {
                bok.getExemplars().remove(ex); //ta bort exemplaret från bokens lista
                hasDeletedExemplars = true;
                exemplarDeletionList.add(ex);
                exemplarList.remove(ex);
            }
        }else{
            exemplarList.remove(ex);
        }
    }

    /**
     * Lägger till nya böcker eller exemplar i databasen baserat på det som finns i bookList och ExemplarList
     * @param event
     */
    @FXML
    void confirmButtonPressed(ActionEvent event) throws Exception {
        //
        if (hasDeletedExemplars) { //radera exemplar först
            getState().databaseService.raderaObjekt(exemplarDeletionList);
        }
        if(hasDeletedBooks) {
            getState().databaseService.raderaObjekt(bookDeletionList);
        }

        if (hasNewBooks) {
            getState().databaseService.läggTillNyaObjekt(bookList);
        }

        if (hasNewExemplars) {
            getState().databaseService.läggTillNyaObjekt(getOnlyNewExemplars());
        }

        if (hasChangedBooks) {
            getState().databaseService.ändraObjekt(bookList);
        }

        showConfirmation();
        setWindowToDefaultState();
        //TODO anrop för att stänga fönstret
    }



    /**
     * Lägger till en helt ny bok.
     * @param event
     */
    @FXML
    void addNewBookToListButtonPressed(ActionEvent event) {
        setWindowToAddingNewBooksState();
    }

    @FXML
    void searchBookButtonPressed(ActionEvent event) throws IOException {
        openSearchWindow();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(debugPrintouts) System.out.println("AddBookViewController: Update");
        bookList.clear();
        bookList.addAll(FXCollections.observableArrayList(getState().getBookSearchResults())); //interna boklistan tilldelas från appstate's sökresultat
    }

    //hjälpmetoder
    /**
     * Stänger det lilla sökfönstret när dess ok-knapp trycks.
     */
    public void closeSmallSearchWindow(){
        if (debugPrintouts) System.out.println("AddBookViewController: Closing small search window");
        searchWindow.close();
    }

    private void prepareTables() {
        ExemplarViewTable.setItems(exemplarList);
        bookViewTable.setItems(bookList);

        //exemplar table
        BarcodeColumn.setCellValueFactory(new PropertyValueFactory<>("streckkod"));
        LåntypColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLåntyp().getLåntyp()));
        TillgängligColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTillgänglig() ? "Ja" : "Nej"));

        //book table
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titel"));
        //För ämnesord behövde vi ta och skriva ut varje element i listan med ett ',' mellan
        //Här är det fråga om sammansätta förnamn och efternamn
        AuthorColumn.setCellValueFactory(cellData -> {
            Bok bok = cellData.getValue();
            String authorName = bok.getFörfattare().stream()
                    .map(författare -> författare.getFörnamn() + " " + författare.getEfternamn())
                    .collect(Collectors.joining(","));
            return new SimpleStringProperty(authorName);
        });
    }

    private List<Exemplar> getOnlyNewExemplars() {
        //tar exemplarlistan och ger tillbaka en lista med bara de exemplar som saknar streckkod (genereras vid insert i databasen)
        return exemplarList.stream()
                .filter(e -> e.getStreckkod() == null)
                .collect(Collectors.toList());
    }

    private boolean onDeleteUserConfirmation() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bekräfta");
        alert.setHeaderText("Är du säker?");
        alert.setContentText("Vill du verkligen radera detta objekt? Om det är en bok kommer alla exemplar också raderas.");

        // Set button types explicitly
        ButtonType yesButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Nej", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show and wait for user input
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == yesButton;
    }

    private void showConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Ändringarna skickades!");

        alert.showAndWait();

    }

    private void openSearchWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("smallSearchWindow.fxml"));
        Scene searchwindow = new Scene(loader.load());

        //hämta referens till controller
        smallSearchWindowController controller = loader.getController();
        controller.setState(getState()); //ge referens till appstate
        controller.setParentController(this);
        getState().addObserver(controller);

        searchWindow = new Stage();
        searchWindow.setTitle("Search window");
        searchWindow.setScene(searchwindow);
        searchWindow.initModality(Modality.APPLICATION_MODAL);

        //ovanstående som metodanrop i konstruktor?
        searchWindow.show();
    }

    private void setWindowToDefaultState() {
        //button states
        searchBookButton.setDisable(false);
        addNewBookButton.setDisable(false);
        editBookButton.setDisable(true);


        //clean form and disable
        clearForm();
        disableForm(true);

        //clean up lists
        bookList.clear();
        exemplarList.clear();
        exemplarDeletionList.clear();
        bookDeletionList.clear();

        //make sure tables and buttons work
        bookViewTable.setDisable(false);
        ExemplarViewTable.setDisable(false);
        removeBookButton.setDisable(false);
        chooseLoanTypeButton.setDisable(false);
        addCopyButton.setDisable(false);
        removeCopyButton.setDisable(false);
    }

    private void setWindowToAddingNewBooksState() {
        formMode = "Adding";
        searchBookButton.setDisable(true);

        //form ska rensas och vara tillgänglig
        clearForm();
        disableForm(false);

        //rensa boklistan om den innehöll något
        if(!bookList.isEmpty()) bookList.clear();

        //exemplar kan bara läggas till när boken finns i databasen
        exemplarList.clear();
        ExemplarViewTable.setDisable(true); //rensa, visa tom lista och disable
        chooseLoanTypeButton.setDisable(true);
        addCopyButton.setDisable(true);
        removeCopyButton.setDisable(true);
    }

    private void setWindowToEditingExistingBooksState() {
        formMode = "Editing";

        //form blir enabled
        disableForm(false);

        //exemplar ska inte röras innan boken ändrats
        removeBookButton.setDisable(true);
        chooseLoanTypeButton.setDisable(true);
        addCopyButton.setDisable(true);
        removeCopyButton.setDisable(true);

    }

    /**
     * Rensar alla textrutor från innehåll
     */
    private void clearForm() {
        idBoxContents.clear();
        isbnBoxContents.clear();
        titleBoxContents.clear();
        amnesordBoxContents1.clear();
        amnesordBoxContents2.clear();
        amnesordBoxContents3.clear();
        authorFirstNameBoxContents1.clear();
        authorLastNameBoxContents1.clear();
        authorFirstNameBoxContents2.clear();
        authorLastNameBoxContents2.clear();
        authorFirstNameBoxContents3.clear();
        authorLastNameBoxContents3.clear();
    }

    /**
     * Stänger av eller sätter på alla textrutor.
     * @param state det tillstånd som önskas
     */
    private void disableForm(boolean state) {
        titleBoxContents.setDisable(state);
        isbnBoxContents.setDisable(state);
        amnesordBoxContents1.setDisable(state);
        amnesordBoxContents2.setDisable(state);
        amnesordBoxContents3.setDisable(state);
        authorFirstNameBoxContents1.setDisable(state);
        authorLastNameBoxContents1.setDisable(state);
        authorFirstNameBoxContents2.setDisable(state);
        authorLastNameBoxContents2.setDisable(state);
        authorFirstNameBoxContents3.setDisable(state);
        authorLastNameBoxContents3.setDisable(state);
        clearFormButton.setDisable(state);
        formOkButton.setDisable(state);
    }
}
