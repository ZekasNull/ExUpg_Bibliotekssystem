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
import javafx.util.Pair;
import model.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import service.BookDatabaseService;
import service.InputValidatorService;
import state.ApplicationState;


public class AddBookViewController extends Controller {
    //debug
    private final boolean DEBUGPRINTOUTS = MainApplication.DEBUGPRINTING;

    //tjänstreferenser
    private BookDatabaseService bookDatabaseService;

    //lista över exemplar och böcker som visas i tables
    private ObservableList<Exemplar> exemplarList = FXCollections.observableArrayList();
    private ObservableList<Bok> bookList = FXCollections.observableArrayList();
    private ObservableList<Författare> formAuthorList = FXCollections.observableArrayList();
    private ObservableList<Ämnesord> formKeywordList = FXCollections.observableArrayList();


    //när exemplar eller böcker ska tas bort visas de inte längre och hamnar därför i en annan lista
    private ArrayList<Exemplar> exemplarDeletionList = new ArrayList<>();
    private ArrayList<Bok> bookDeletionList = new ArrayList<>();

    //enums för FormMode och böckers exemplartyp
    private enum FormMode { ADDING, EDITING, NONE }
    private FormMode formMode = FormMode.NONE;
    private enum LoanType {
        BOK("bok"),
        KURSLITTERATUR("kurslitteratur"),
        REFERENSLITTERATUR("referenslitteratur"),
        NOT_SELECTED("ingetvalt");

        private final String låntyp;

        LoanType(String låntyp){
            this.låntyp = låntyp;
        }
        public String getLåntyp() {
            return låntyp;
        }
    }
    private LoanType selectedLoanType = LoanType.NOT_SELECTED;

    //booleans för kontroll av vad som ska göras när bekräfta-knappen trycks
    private boolean hasNewBooks, hasChangedBooks = false;

    //form text fields
    @FXML
    private TextField idBoxContents;

    @FXML
    private TextField isbnBoxContents;

    @FXML
    private TextField titleBoxContents;

    //form author table
    @FXML
    private TableView<Författare> authorTable;

    @FXML
    private TableColumn<Författare, ?> authorFirstNameColumn;

    @FXML
    private TableColumn<Författare, ?> authorLastNameColumn;

    //form keyword table
    @FXML
    private TableView<Ämnesord> keywordTable;
    @FXML
    private TableColumn<?, ?> keywordColumn;

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
    private Button confirmButton;

    @FXML
    private Button addNewBookButton;

    @FXML
    private Button searchBookButton;

    @FXML
    private Button addCopyButton;

    @FXML
    private Button addKeywordButton;

    @FXML
    private Button removeKeywordButton;

    @FXML
    private Button addAuthorButton;

    @FXML
    private Button removeAuthorButton;


    /**
     * När nya exemplar läggs till ska de ha en låntyp. Den väljs här
     */
    @FXML
    private SplitMenuButton chooseLoanTypeButton;
    @FXML
    void handleBokOption(ActionEvent event) {
        selectedLoanType = LoanType.BOK;
        chooseLoanTypeButton.setText(selectedLoanType.låntyp);
    }

    @FXML
    void handleKurslitteraturOption(ActionEvent event) {
        selectedLoanType = LoanType.KURSLITTERATUR;
        chooseLoanTypeButton.setText(selectedLoanType.låntyp);
    }

    @FXML
    void handleReferenslitteraturOption(ActionEvent event) {
        selectedLoanType = LoanType.REFERENSLITTERATUR;
        chooseLoanTypeButton.setText(selectedLoanType.låntyp);
    }

    @FXML
    private Button removeBookButton;

    @FXML
    private Button removeCopyButton;

    @FXML
    private Button formOkButton;

    @FXML
    private Button clearFormButton;

    //form buttons
    @FXML
    void clearFormButtonPressed(ActionEvent event) {
        clearForm();
    }

    /**
     * När denna knapp trycks läggs antingen en ny bok till eller så ändras en existerande bok.
     * @param event
     */
    @FXML
    void formOkButtonPressed(ActionEvent event) {
        //se till att kritiska fält innehåller något exklusive whitespace
        if (!FormHasRequiredData()) {
            showErrorPopup("Det måste finnas en titel, ISBN-13 och minst en författare.");
            return;
        }
        Bok bok;
        HashSet<Författare> changedAuthors = new HashSet<>(formAuthorList);
        HashSet<Ämnesord> changedKeywords = new HashSet<>(formKeywordList);
        Pair<Boolean, String> validate;

        switch(formMode) {
            case ADDING:
                //set fields and new book
                bok = new Bok();
                bok.setTitel(titleBoxContents.getText().trim());
                bok.setIsbn13(isbnBoxContents.getText().trim());
                bok.setFörfattare(changedAuthors);
                bok.setÄmnesord(changedKeywords);

                //validate
                validate = InputValidatorService.isValidBok(bok);
                if(!validate.getKey()){
                    showErrorPopup(validate.getValue());
                    return;
                }
                //update data
                bookList.add(bok);
                hasNewBooks = true;
                break;
            case EDITING:
                //set fields
                bok = bookViewTable.getSelectionModel().getSelectedItem();
                bok.setTitel(titleBoxContents.getText().trim());
                bok.setIsbn13(isbnBoxContents.getText().trim());
                bok.setFörfattare(changedAuthors);
                bok.setÄmnesord(changedKeywords);
                //validate
                validate = InputValidatorService.isValidBok(bok);
                if(!validate.getKey()){
                    showErrorPopup(validate.getValue());
                    return;
                }
                //update data
                hasChangedBooks = true;
                bookViewTable.refresh(); //då den inte reagerar på ändrat internt innehåll
                break;
            case NONE:
                System.out.println("AddBookViewController: this should never ever be visible");
            default:
                System.out.println("AddBookViewController: some fucking how, none of the approved states are set");
                break;
        }
        setWindowToDefaultState();
    }

    /**
     * Om en bok är vald i bookviewtable, sätt till edit book state.
     */
    @FXML
    private Button editBookButton;

    @FXML
    void editBookButtonPressed(ActionEvent event) {
        setWindowToEditingExistingBooksState();
    }

    @FXML
    void bookTableClicked(MouseEvent event) {
        if (bookList.isEmpty() || bookViewTable.getSelectionModel().getSelectedItem() == null) return; //förhindra exekvering när listan är tom eller det trycks utanför
        Bok bok = bookViewTable.getSelectionModel().getSelectedItem();

        //form
        titleBoxContents.setText(bok.getTitel());
        isbnBoxContents.setText(bok.getIsbn13());
        idBoxContents.setText(bok.getId() == null ? "" : bok.getId().toString()); //kan vara null

        //författare
        formAuthorList.clear();
        formAuthorList.addAll(bok.getFörfattare());

        //ämnesord
        formKeywordList.clear();
        formKeywordList.addAll(bok.getÄmnesord());

        //exemplar
        exemplarList.clear();
        exemplarList.addAll(bookViewTable.getSelectionModel().getSelectedItem().getExemplars()); //hämta ex från bok

        removeBookButton.setDisable(bookList.isEmpty());
        addCopyButton.setDisable(bookList.isEmpty());
        editBookButton.setDisable(bookList.isEmpty());


    }

    @FXML
    void removeBookTableButtonPressed(ActionEvent event) {
        Bok bok = bookViewTable.getSelectionModel().getSelectedItem();
        if (bok.getId() != null) {
            if (onDeleteUserConfirmation(!bok.getExemplars().isEmpty())) { //visa varning om boken har exemplar
                bookDeletionList.add(bok);
                bookList.remove(bok);
                exemplarList.clear();
            }
        }else{
            //böcker utan id kan tas bort direkt då de inte finns i db
            bookList.remove(bok);
            exemplarList.clear();
            hasNewBooks = !bookList.isEmpty();
        }
        clearForm();
    }

    @FXML
    void exemplarTableClicked(MouseEvent event) {
        removeCopyButton.setDisable(exemplarList.isEmpty());
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        if(formMode == FormMode.ADDING || formMode == FormMode.EDITING) {
            setWindowToDefaultState();
        } else {
            viewLoader.setView(ViewLoader.Views.HANDLE_INVENTORY);
            setWindowToResetState();
        }
    }

    @FXML
    void addExemplarTableButtonPressed(ActionEvent event) {
        //se till att låntyp är vald
        if(selectedLoanType == LoanType.NOT_SELECTED) {
            showErrorPopup("Du måste välja en låntyp.");
            chooseLoanTypeButton.requestFocus();
            return;
        }
        //Skapar ett exemplar för den valda boken med den valda låntypen. Läggs in i databasen när bekräfta ändringar trycks.

        Bok bok = bookViewTable.getSelectionModel().getSelectedItem();

        //skapa ex
        Exemplar nyttEx = new Exemplar();
        nyttEx.setNewLåntyp(selectedLoanType.getLåntyp());
        nyttEx.setBok(bok);

        //lägg till i både synlig lista och bokens lista
        exemplarList.add(nyttEx);
        bok.getExemplars().add(nyttEx);

        hasChangedBooks = true; //nya exemplar räknas som ändrad bok
    }

    @FXML
    void removeExemplarButtonPressed(ActionEvent event) {
        Bok bok = bookViewTable.getSelectionModel().getSelectedItem();
        Exemplar ex = ExemplarViewTable.getSelectionModel().getSelectedItem();

        //om streckkod finns, be användaren bekräfta
        if (ex.getStreckkod() != null) {
            if (onDeleteUserConfirmation(false)) {
                exemplarDeletionList.add(ex); //exemplar till raderingslista
                bok.getExemplars().remove(ex); //ta bort exemplaret från bokens lista
                exemplarList.remove(ex); //ta bort exemplaret från synlig lista
            }
        }else{ //ta bort från synlig och boklista direkt när streckkod saknas
            exemplarList.remove(ex);
            bok.getExemplars().remove(ex);
        }
    }

    @FXML
    void addAuthorButtonPressed(ActionEvent event) throws IOException {

        //input
        String[] names = openNameInputDialog();
        if (DEBUGPRINTOUTS) System.out.println("AddBookViewController: Found "+ names[0] + " " + names[1] + " in NameInputDialog for directors");

        //validate
        if(names[0].isEmpty() || names[1].isEmpty()) {showErrorPopup("För- och efternamnet får inte vara tomt."); return;}


        //add
        formAuthorList.add(bookDatabaseService.findOrCreateAuthor(names));
    }

    @FXML
    void removeAuthorButtonPressed(ActionEvent event) {
        if(formAuthorList.isEmpty()) return;
        formAuthorList.remove(authorTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void addKeywordButtonPressed(ActionEvent event) {
        //metodvariabler
        TextInputDialog dialog = new TextInputDialog();
        Optional<String> ämnesord;

        //ta input med enkel dialog
        dialog.setTitle("Ämnesord");
        dialog.setHeaderText("Skriv in ämnesord");
        dialog.setContentText("Ämnesord:");
        ämnesord = dialog.showAndWait();
        if (ämnesord.isPresent() && ämnesord.get().trim().isEmpty()) {
            showErrorPopup("Fältet får inte vara tomt.");
            return;
        }

        if (DEBUGPRINTOUTS) System.out.println("AddFilmViewController: Found "+ ämnesord.get() + " in TextInputDialog for genre");

        formKeywordList.add(bookDatabaseService.findOrCreateÄmnesord(ämnesord.get().trim())); //se om det redan finns i databas och lägg till
    }

    @FXML
    void removeKeywordButtonPressed(ActionEvent event) {
        if(formKeywordList.isEmpty()) return;
        formKeywordList.remove(keywordTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Lägger till nya böcker eller exemplar i databasen baserat på det som finns i bookList och ExemplarList
     * @param event
     */
    @FXML
    void confirmButtonPressed(ActionEvent event) {
        ArrayList<Bok> processedBooks = new ArrayList<>();
        //
        if (hasNewBooks) {
            for(Bok b : bookList) {
                if (b.getId() == null) { //hantera endast nya böcker och skippa uppdaterade
                    bookDatabaseService.addNewBook(b);
                    processedBooks.add(b);
                }
            }
        }
        if (hasChangedBooks) {
            for(Bok b : bookList) {
                bookDatabaseService.updateBook(b);
            }
        }
        bookList.removeAll(processedBooks);

        if(!bookDeletionList.isEmpty()) {
            for(Bok b : bookDeletionList) {
                bookDatabaseService.deleteBook(b);
            }
        }
        if (!exemplarDeletionList.isEmpty()) {
            for(Exemplar e : exemplarDeletionList) {
                bookDatabaseService.deleteBookCopy(e);
            }
        }
        if (hasChangedBooks || hasNewBooks || !bookDeletionList.isEmpty() || !exemplarDeletionList.isEmpty()) {
            showInformationPopup("Ändringarna skickades till databasen!");
            hasChangedBooks = false;
            hasNewBooks = false;
        } else {
            showInformationPopup("Det finns inga ändringar att skicka.");
        }

        bookList.clear();
        exemplarList.clear(); //tvinga användare att ladda om
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
        viewLoader.loadClosingPopup(ViewLoader.Views.SMALL_SEARCH_WINDOW);
    }

    /**
     * Förbereder klassens tables genom att ställa in deras cellvaluefactories.
     */
    public void initialize() {
        //tables
        //author
        authorTable.setItems(formAuthorList);
        authorFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("förnamn"));
        authorLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("efternamn"));

        //keywords
        keywordTable.setItems(formKeywordList);
        keywordColumn.setCellValueFactory(new PropertyValueFactory<>("ord"));


        //exemplar table
        ExemplarViewTable.setItems(exemplarList);
        BarcodeColumn.setCellValueFactory(new PropertyValueFactory<>("streckkod"));
        LåntypColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLåntyp().getLåntyp()));
        TillgängligColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTillgänglig() ? "Ja" : "Nej"));

        //book table
        bookViewTable.setItems(bookList);
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

    @Override
    public void loadServicesFromState() {
        super.loadServicesFromState();
        //tjänster
        bookDatabaseService = getState().getBookDatabaseService();
    }

    //hjälpmetoder

    /*
     * WINDOWSTATES
     */
    /**
     * Ska motsvara fönstret i sitt default state, men utan att ta bort information från listor. Det gäller att:
     * FormMode is NONE
     * Form is disabled
     * Form buttons are disabled
     *
     * Böcker table is enabled
     * Böcker remove button only enabled if bookList contains something
     *
     * Exemplar table is enabled
     * Exemplar remove button only enabled if exemplarList contains something
     *
     * Search book button is enabled
     * Add new book button is enabled
     * Edit book button is only enabled if bookList contains something
     */
    private void setWindowToDefaultState() {
        //form
        formMode = FormMode.NONE;
        clearForm();
        disableForm(true);

        //book table and buttons
        bookViewTable.setDisable(false);
        removeBookButton.setDisable(bookList.isEmpty());

        //exemplar table
        ExemplarViewTable.setDisable(false);
        chooseLoanTypeButton.setDisable(false);
        addCopyButton.setDisable(false);
        removeCopyButton.setDisable(exemplarList.isEmpty());

        // search/add/remove
        searchBookButton.setDisable(false);
        addNewBookButton.setDisable(false);
        editBookButton.setDisable(bookList.isEmpty());
        confirmButton.setDisable(false);
    }

    /**
     * Status för vyn när ny bok läggs till
     * När en bok läggs till gäller följande:
     * Form is enabled
     * Form buttons are enabled
     * Form is cleared
     * FormMode is ADDING
     *
     * Böcker table is disabled
     * Böcker table buttons are disabled
     *
     * Exemplar table is disabled
     * Exemplar table buttons are disabled
     *
     * Search book button is disabled
     * Add new book button is disabled
     * Change book button is disabled
     */
    private void setWindowToAddingNewBooksState() {
        //form
        formMode = FormMode.ADDING;
        disableForm(false);
        clearForm();

        //book table
        bookViewTable.setDisable(true);
        removeBookButton.setDisable(true);

        //exemplar table
        ExemplarViewTable.setDisable(true);
        chooseLoanTypeButton.setDisable(true);
        addCopyButton.setDisable(true);
        removeCopyButton.setDisable(true);

        //other buttons
        searchBookButton.setDisable(true);
        addNewBookButton.setDisable(true);
        editBookButton.setDisable(true);
        confirmButton.setDisable(true);
    }

    /**
     * När en bok redigeras gäller ungefär samma som att lägga till en ny men formMode är editing och den rensas inte.
     */
    private void setWindowToEditingExistingBooksState() {
        //form
        formMode = FormMode.EDITING;
        disableForm(false);

        //book table
        bookViewTable.setDisable(true);
        removeBookButton.setDisable(true);

        //exemplar table
        ExemplarViewTable.setDisable(true);
        chooseLoanTypeButton.setDisable(true);
        addCopyButton.setDisable(true);
        removeCopyButton.setDisable(true);

        //search/add/edit buttons
        searchBookButton.setDisable(true);
        addNewBookButton.setDisable(true);
        editBookButton.setDisable(true);
        confirmButton.setDisable(true);

    }

    /**
     * Samma som defaultState men all information slängs.
     */
    private void setWindowToResetState() {
        setWindowToDefaultState();

        //purge information
        exemplarList.clear();
        bookList.clear();
        exemplarDeletionList.clear();
        bookDeletionList.clear();

        //statusboleans
        hasNewBooks = false;
        hasChangedBooks = false;

        //status
        chooseLoanTypeButton.setText("Välj låntyp...");
        formMode = FormMode.NONE;

    }

    /*
     * UTILITIES
     */

    /**
     * Liten hjälpmetod som ser till att rätt info finns i fält.
     * TODO ersätt med InputValidatorService
     * @return false om något fält saknar nödvändig data, annars true
     */
    private boolean FormHasRequiredData() {
        if (titleBoxContents.getText().trim().isEmpty()) return false;
        if(isbnBoxContents.getText().trim().isEmpty()) return false;
        if(formAuthorList.isEmpty()) return false;
        return true;
    }

    /**
     * Rensar alla textrutor från innehåll
     */
    private void clearForm() {
        //rensa inte ID för existerande böcker
        if (!(formMode == FormMode.EDITING)) idBoxContents.clear();
        titleBoxContents.clear();
        isbnBoxContents.clear();

        //tabeller
        formAuthorList.clear();
        formKeywordList.clear();

    }

    /**
     * Stänger av eller sätter på alla textrutor.
     * @param state det tillstånd som önskas
     */
    private void disableForm(boolean state) {
        titleBoxContents.setDisable(state);
        isbnBoxContents.setDisable(state);
        addAuthorButton.setDisable(state);
        removeAuthorButton.setDisable(state);
        addKeywordButton.setDisable(state);
        removeKeywordButton.setDisable(state);
        clearFormButton.setDisable(state);
        formOkButton.setDisable(state);
    }

    /*
     * IMPLEMENTED
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg != ApplicationState.UpdateType.BOOK) return;
        if(DEBUGPRINTOUTS) System.out.println("AddBookViewController: Update");
        bookList.clear();
        bookList.addAll(FXCollections.observableArrayList(getState().getBookSearchResults())); //interna boklistan tilldelas från appstate's sökresultat
    }
}
