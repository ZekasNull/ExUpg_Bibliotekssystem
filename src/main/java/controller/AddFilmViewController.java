package controller;

import d0024e.exupg_bibliotekssystem.MainApplication;
import javafx.util.Pair;
import service.FilmDatabaseService;
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
import service.InputValidatorService;
import state.ApplicationState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Optional;

public class AddFilmViewController extends Controller {
    //debug
    private final boolean DEBUGPRINTOUTS = MainApplication.DEBUGPRINTS;

    //lists
    private ObservableList<Exemplar> exemplarList = FXCollections.observableArrayList();
    private ObservableList<Film> filmList = FXCollections.observableArrayList();
    private ObservableList<Skådespelare> skådespelareList = FXCollections.observableArrayList();
    private ObservableList<Regissör> regissörsList = FXCollections.observableArrayList();
    private ObservableList<Genre> genresList = FXCollections.observableArrayList();

    //deletionlist
    private ArrayList<Exemplar> exemplarDeletionList = new ArrayList<>();
    private ArrayList<Film> filmDeletionList = new ArrayList<>();

    //formstate
    private enum FormMode {ADDING, EDITING, NONE}
    private FormMode formMode = FormMode.NONE;

    //on confirm checks
    private boolean
            hasNewFilms,
            hasChangedFilms = false;

    //exemplartable
    @FXML
    private TableView<Exemplar> exemplarViewTable;

    @FXML
    private TableColumn<Exemplar, ?> BarcodeColumn;

    @FXML
    private TableColumn<Exemplar, String> LåntypColumn;

    @FXML
    private TableColumn<Exemplar, String> TillgängligColumn;

    //filmviewtable
    @FXML
    private TableView<Film> filmViewTable;

    @FXML
    private TableColumn<?, ?> titleColumn;

    //form text fields
    //tables
    //columns
    @FXML
    private TextField idBoxContents;

    @FXML
    private TextField produktionslandBoxContents;

    @FXML
    private TextField agelimitBoxContents;

    @FXML
    private TableView<Genre> genreViewTable;

    @FXML
    private TableColumn<?, ?> genreColumn;

    @FXML
    private TableView<Regissör> directorViewTable;

    @FXML
    private TableColumn<?, ?> directorFirstName;

    @FXML
    private TableColumn<?, ?> directorLastName;

    @FXML
    private TableView<Skådespelare> actorViewTable;

    @FXML
    private TableColumn<?, ?> actorFirstName;

    @FXML
    private TableColumn<?, ?> actorLastName;

    //buttons
    @FXML
    private Button addActorButton;

    @FXML
    private Button addDirectorButton;

    @FXML
    private Button addExemplarButton;

    @FXML
    private Button addGenreButton;

    @FXML
    private Button addNewFilmButton;

    @FXML
    private Button clearFormButton;

    @FXML
    private Button editFilmButton;

    @FXML
    private Button formOkButton;

    @FXML
    private Button removeActorButton;

    @FXML
    private Button removeFilmButton;

    @FXML
    private Button removeDirectorButton;

    @FXML
    private Button removeExemplarButton;

    @FXML
    private Button removeGenreButton;

    @FXML
    private Button searchFilmButton;

    @FXML
    private TextField titleBoxContents;

    @FXML
    void addActorButtonPressed(ActionEvent event) throws IOException {
        FilmDatabaseService dbs = new FilmDatabaseService(); //FIXME ENDAST TEST
        String[] names = openNameInputDialog();
        if (DEBUGPRINTOUTS) System.out.println("AddFilmViewController: Found "+ names[0] + " " + names[1] + " in NameInputDialog for actors");

        skådespelareList.add(dbs.findOrCreateActor(names));
    }

    @FXML
    void addDirectorButtonPressed(ActionEvent event) throws IOException {
        FilmDatabaseService dbs = new FilmDatabaseService(); //FIXME ENDAST TEST
        String[] names = openNameInputDialog();
        if (DEBUGPRINTOUTS) System.out.println("AddFilmViewController: Found "+ names[0] + " " + names[1] + " in NameInputDialog for directors");

        regissörsList.add(dbs.findOrCreateDirector(names));
    }

    @FXML
    void addExemplarTableButtonPressed(ActionEvent event) {
        Film film = filmViewTable.getSelectionModel().getSelectedItem(); //för vald film
        //då det bara kan finnas en typ av exemplar för filmer, skapa det direkt
        Exemplar ex = new Exemplar();
        ex.setNewLåntyp("film");
        ex.setFilm_id(film);

        film.getExemplars().add(ex); //lägg också till i filmens lista för konsistens
        exemplarList.add(ex);
        hasChangedFilms = true; //nytt ex på en film räknas som att filmen ändrades och hanteras när filmer uppdateras
    }

    @FXML
    void addGenreButtonPressed(ActionEvent event) throws IOException {
        FilmDatabaseService dbs = new FilmDatabaseService(); //FIXME ENDAST TEST
        Optional<String> genreNamn;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Genre");
        dialog.setHeaderText("Skriv in genre");
        dialog.setContentText("Genre:");
        genreNamn = dialog.showAndWait();
        if (genreNamn.isEmpty()) {
            if (DEBUGPRINTOUTS) System.out.println("AddFilmViewController: No genre entered in TextInputDialog");
            return;
        }

        if (DEBUGPRINTOUTS) System.out.println("AddFilmViewController: Found "+ genreNamn.get() + " in TextInputDialog for genre");

        genresList.add(dbs.findOrCreateGenre(genreNamn.get()));
    }

    @FXML
    void addNewFilmToListButtonPressed(ActionEvent event) {
        setWindowToAddingFilmState();
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        setWindowToDefaultState();
    }

    @FXML
    void clearFormButtonPressed(ActionEvent event) {
        purgeFormLists();
    }

    @FXML
    void confirmButtonPressed(ActionEvent event) {
        FilmDatabaseService dbs = new FilmDatabaseService(); //FIXME ENDAST TEST
        ArrayList<Film> processedFilms = new ArrayList<>(); //filmer som har hanterats ska inte gå in i changed
        if (hasNewFilms) {
            for (Film f : filmList) {
                if(f.getId() == null) //hantera endast nya filmer
                dbs.addNewFilm(f);
                processedFilms.add(f);
            }
        }
        filmList.removeAll(processedFilms); //när nya filmer är klara, ta bort från listan
        if (hasChangedFilms) {
            for (Film f : filmList) {
                dbs.updateFilm(f);
            }
        }
        if (!filmDeletionList.isEmpty()) {
            for (Film f : filmDeletionList) {
                dbs.deleteFilm(f);
            }
        }
        if(!exemplarDeletionList.isEmpty()) {
            for (Exemplar ex : exemplarDeletionList) {
                dbs.deleteFilmCopy(ex);
            }
        }

        if(hasChangedFilms || hasNewFilms) {
            showInformationPopup("Ändringarna skickades till databasen!");
        }else {
            showInformationPopup("Det finns inga ändringar att skicka.");
        }

        setWindowToDefaultState(); //nollställ fönstret
        filmList.clear(); //tvinga användaren ladda om uppdaterade filmer och exemplar
        exemplarList.clear();
    }

    @FXML
    void editFilmButtonPressed(ActionEvent event) {
        formMode = FormMode.EDITING;
        disableForm(false);
        updateFormView();
        searchFilmButton.setDisable(true);
        addNewFilmButton.setDisable(true);
        filmViewTable.setDisable(true);
    }

    @FXML
    void filmTableClicked(MouseEvent event) {
        if(filmViewTable.getSelectionModel().getSelectedItem() == null) return; //om det klickades utanför en rad

        updateFormView();
        exemplarList.clear();
        exemplarList.addAll(filmViewTable.getSelectionModel().getSelectedItem().getExemplars()); //hämta ex från film
        addExemplarButton.setDisable(filmList.isEmpty());
        removeExemplarButton.setDisable(filmList.isEmpty());
        removeFilmButton.setDisable(filmList.isEmpty());
        editFilmButton.setDisable(filmList.isEmpty());
    }

    @FXML
    void formOkButtonPressed(ActionEvent event) {
        if (titleBoxContents.getText().isEmpty() || produktionslandBoxContents.getText().isEmpty() || agelimitBoxContents.getText().isEmpty()) {
            showErrorPopup("Ett eller flera fält är inte ifyllda");
        }
        if(!agelimitBoxContents.getText().matches("\\d+")) {
            showErrorPopup("Ett textfält har otillåtet innehåll.");
            agelimitBoxContents.requestFocus();
        }

        //new sets
        HashSet<Regissör> regissörs = new HashSet<>(regissörsList);
        HashSet<Skådespelare> skådespelares = new HashSet<>(skådespelareList);
        HashSet<Genre> genres = new HashSet<>(genresList);

        //film
        Film film;
        Pair<Boolean, String> validate;

        switch (formMode){
            case ADDING:
                film = new Film();
                //update fields
                film.setTitel(titleBoxContents.getText());
                film.setProduktionsland(produktionslandBoxContents.getText());
                film.setÅldersgräns(Integer.parseInt(agelimitBoxContents.getText()));
                film.setRegissörs(regissörs);
                film.setSkådespelares(skådespelares);
                film.setGenres(genres);
                //validate
                validate = InputValidatorService.isValidFilm(film);
                if(!validate.getKey()){
                    showErrorPopup(validate.getValue());
                    return;
                }

                filmList.add(film);
                updateHasNewFilms();
                break;
            case EDITING:
                film = filmViewTable.getSelectionModel().getSelectedItem();
                film.setTitel(titleBoxContents.getText());
                film.setProduktionsland(produktionslandBoxContents.getText());
                //FIXME antar snällt att användaren bara skrev siffror
                film.setÅldersgräns(Integer.parseInt(agelimitBoxContents.getText()));
                film.setRegissörs(regissörs);
                film.setSkådespelares(skådespelares);
                film.setGenres(genres);

                //validate
                validate = InputValidatorService.isValidFilm(film);
                if(!validate.getKey()){
                    showErrorPopup(validate.getValue());
                    return;
                }
                //update data
                hasChangedFilms = true;
                filmViewTable.refresh();
                break;
            case NONE: //ska vara omöjlig att nå om state är none
            default:
                System.out.println("AddFilmViewController: formState is not set to ADDING, EDITING or NONE");
                return;
        }
        setWindowToDefaultState();
    }

    @FXML
    void removeActorButtonPressed(ActionEvent event) {
        if (skådespelareList.isEmpty()) return;
        skådespelareList.remove(actorViewTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void removeFilmTableButtonPressed(ActionEvent event) {
        Film selected = filmViewTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        //ta bort filmen direkt om id saknas
        if(selected.getId() == null) {
            filmList.remove(selected);
        }else if (onDeleteUserConfirmation(!selected.getExemplars().isEmpty())) { //annars om id finns måste användaren bekräfta
            filmDeletionList.add(selected);
            filmList.remove(selected);
            purgeFormLists();
        }

        removeFilmButton.setDisable(filmList.isEmpty());
        updateHasNewFilms(); //blir filmlistan tom finns inga nya

    }

    @FXML
    void removeDirectorButtonPressed(ActionEvent event) {
        if(regissörsList.isEmpty()) return;
        regissörsList.remove(directorViewTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void removeExemplarButtonPressed(ActionEvent event) {
        Film selectedFilm = filmViewTable.getSelectionModel().getSelectedItem();
        Exemplar selected = exemplarViewTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (selected.getStreckkod() == null) {
            exemplarList.remove(selected);
            selectedFilm.getExemplars().remove(selected);
        } else if (onDeleteUserConfirmation(false)) {
            exemplarDeletionList.add(selected);
            exemplarList.remove(selected);
        }

        removeExemplarButton.setDisable(exemplarList.isEmpty());
    }

    @FXML
    void removeGenreButtonPressed(ActionEvent event) {
        if(genresList.isEmpty()) return;
        genresList.remove(genreViewTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void searchExistingFilmButtonPressed(ActionEvent event) throws IOException {
        purgeFormLists();

        Stage searchWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("smallSearchWindow.fxml"));
        Scene searchwindow = new Scene(loader.load());

        //hämta referens till controller
        SmallSearchWindowController controller = loader.getController();
        controller.setState(getState()); //ge referens till appstate
        controller.setStage(searchWindow);
        controller.setMode(SmallSearchWindowController.Mode.FILM);

        searchWindow.setTitle("Search window");
        searchWindow.setScene(searchwindow);
        searchWindow.initModality(Modality.APPLICATION_MODAL);

        //ovanstående som metodanrop i konstruktor?
        searchWindow.show();
    }

    public void initialize() {

        //director tableview
        directorViewTable.setItems(regissörsList);
        directorFirstName.setCellValueFactory(new PropertyValueFactory<>("förnamn"));
        directorLastName.setCellValueFactory(new PropertyValueFactory<>("efternamn"));

        //actor tableview
        actorViewTable.setItems(skådespelareList);
        actorFirstName.setCellValueFactory(new PropertyValueFactory<>("förnamn"));
        actorLastName.setCellValueFactory(new PropertyValueFactory<>("efternamn"));

        //genre tableview
        genreViewTable.setItems(genresList);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genreNamn"));

        //filmtable
        filmViewTable.setItems(filmList);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titel"));

        //exemplar
        exemplarViewTable.setItems(exemplarList);
        BarcodeColumn.setCellValueFactory(new PropertyValueFactory<>("streckkod"));
        LåntypColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLåntyp().getLåntyp()));
        TillgängligColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTillgänglig() ? "Ja" : "Nej"));


    }

    private void setWindowToDefaultState() {
        searchFilmButton.setDisable(false);
        addNewFilmButton.setDisable(false);
        editFilmButton.setDisable(filmList.isEmpty());
        removeFilmButton.setDisable(true);
        removeExemplarButton.setDisable(true);
        addExemplarButton.setDisable(true);
        filmViewTable.setDisable(false);

        disableForm(true);
        purgeFormLists();

        //purge deletion lists
        filmDeletionList.clear();
        exemplarDeletionList.clear();
    }

    private void setWindowToAddingFilmState() {
        searchFilmButton.setDisable(true);
        addNewFilmButton.setDisable(true);
        disableForm(false);
        formMode = FormMode.ADDING;
    }

    private String[] openNameInputDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("nameInputDialog.fxml"));
        Scene inputDialog = new Scene(loader.load());
        Stage popupDialog = new Stage();

        //controller references
        NameInputDialogController controller = loader.getController();
        controller.setStage(popupDialog);

        //setup window
        popupDialog.setTitle("Namn");
        popupDialog.setScene(inputDialog);
        popupDialog.setResizable(false);
        popupDialog.showAndWait();

        //when closed
        return controller.getNames();
    }

    private void disableForm(boolean disable) {
        titleBoxContents.setDisable(disable);
        produktionslandBoxContents.setDisable(disable);
        agelimitBoxContents.setDisable(disable);

        addDirectorButton.setDisable(disable);
        removeDirectorButton.setDisable(disable);

        addActorButton.setDisable(disable);
        removeActorButton.setDisable(disable);

        addGenreButton.setDisable(disable);
        removeGenreButton.setDisable(disable);

        clearFormButton.setDisable(disable);
        formOkButton.setDisable(disable);
    }

    private void purgeFormLists() {
        titleBoxContents.clear();
        produktionslandBoxContents.clear();
        agelimitBoxContents.clear();
        skådespelareList.clear();
        genresList.clear();
        regissörsList.clear();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg != ApplicationState.UpdateType.FILM) return;
        if (DEBUGPRINTOUTS) System.out.println("AddFilmViewController: Adding films from search result");
        filmList.clear();
        filmList.addAll(getState().getFilmSearchResults());
    }

    private void updateHasNewFilms() {
        for (Film f : filmList) {
            if (f.getId() == null) {
                hasNewFilms = true;
                return;
            }
        }
    }

    private void updateFormView() {
        Film film = filmViewTable.getSelectionModel().getSelectedItem();
        purgeFormLists();
        titleBoxContents.setText(film.getTitel());
        if(film.getId() != null) idBoxContents.setText(film.getId().toString());
        produktionslandBoxContents.setText(film.getProduktionsland());
        agelimitBoxContents.setText(film.getÅldersgräns().toString());
        regissörsList.addAll(film.getRegissörs());
        skådespelareList.addAll(film.getSkådespelares());
        genresList.addAll(film.getGenres());
    }

    private void showInformationPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(message);

        alert.showAndWait();
    }

    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fel");
        alert.setHeaderText(message);

        alert.showAndWait();
    }

    private boolean onDeleteUserConfirmation(boolean hasChildren)
    {
        String additionalWarning = hasChildren ? " Alla exemplar kommer också att raderas" : "";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bekräfta");
        alert.setHeaderText("Är du säker?");
        alert.setContentText("Vill du verkligen radera detta objekt?" + additionalWarning);

        // Set button types explicitly
        ButtonType yesButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Nej", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show and wait for user input
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == yesButton;
    }
}
