package controller;

import d0024e.exupg_bibliotekssystem.MainApplication;
import service.ViewLoader;
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
    private final boolean DEBUGPRINTOUTS = MainApplication.DEBUGPRINTING;

    //tjänster
    private FilmDatabaseService filmDatabaseService;

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
    private Button confirmButton;

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

    /*
     * Form buttons
     */
    @FXML
    void addActorButtonPressed(ActionEvent event) throws IOException {
        String[] names = openNameInputDialog();
        if (names == null) return;
        if (DEBUGPRINTOUTS) System.out.println("AddFilmViewController: Found "+ names[0] + " " + names[1] + " in NameInputDialog for actors");

        skådespelareList.add(filmDatabaseService.findOrCreateActor(names));
    }

    @FXML
    void removeActorButtonPressed(ActionEvent event) {
        if (skådespelareList.isEmpty()) return;
        skådespelareList.remove(actorViewTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void addDirectorButtonPressed(ActionEvent event) throws IOException {
        String[] names = openNameInputDialog();
        if (names == null) return;
        if (DEBUGPRINTOUTS) System.out.println("AddFilmViewController: Found "+ names[0] + " " + names[1] + " in NameInputDialog for directors");

        regissörsList.add(filmDatabaseService.findOrCreateDirector(names));
    }

    @FXML
    void removeDirectorButtonPressed(ActionEvent event) {
        if(regissörsList.isEmpty()) return;
        regissörsList.remove(directorViewTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void addGenreButtonPressed(ActionEvent event) {
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

        genresList.add(filmDatabaseService.findOrCreateGenre(genreNamn.get()));
    }

    @FXML
    void removeGenreButtonPressed(ActionEvent event) {
        if(genresList.isEmpty()) return;
        genresList.remove(genreViewTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void clearFormButtonPressed(ActionEvent event) {
        clearForm();
    }

    @FXML
    void formOkButtonPressed(ActionEvent event) {
        if (titleBoxContents.getText().isEmpty() || produktionslandBoxContents.getText().isEmpty() || agelimitBoxContents.getText().isEmpty()) {
            showErrorPopup("Ett eller flera fält är inte ifyllda");
            return;
        }
        if(!agelimitBoxContents.getText().matches("\\d+")) {
            showErrorPopup("Ett textfält har otillåtet innehåll.");
            agelimitBoxContents.requestFocus();
            return;
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
                if (DEBUGPRINTOUTS) System.out.println("AddFilmViewController: formState is not set to ADDING, EDITING or NONE");
                return;
        }
        setWindowToDefaultState();
    }

    /*
     * Right side
     */
    @FXML
    void filmTableClicked(MouseEvent event) {
        if(filmViewTable.getSelectionModel().getSelectedItem() == null) return; //om det klickades utanför en rad
        Film film = filmViewTable.getSelectionModel().getSelectedItem();
        clearForm();

        titleBoxContents.setText(film.getTitel());
        if(film.getId() != null) idBoxContents.setText(film.getId().toString());
        produktionslandBoxContents.setText(film.getProduktionsland());
        agelimitBoxContents.setText(film.getÅldersgräns().toString());
        regissörsList.addAll(film.getRegissörs());
        skådespelareList.addAll(film.getSkådespelares());
        genresList.addAll(film.getGenres());

        exemplarList.clear();
        exemplarList.addAll(filmViewTable.getSelectionModel().getSelectedItem().getExemplars()); //hämta ex från film
        addExemplarButton.setDisable(filmList.isEmpty());
        removeExemplarButton.setDisable(exemplarList.isEmpty());
        removeFilmButton.setDisable(filmList.isEmpty());
        editFilmButton.setDisable(filmList.isEmpty());
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
            clearForm();
        }

        removeFilmButton.setDisable(filmList.isEmpty());
        editFilmButton.setDisable(filmList.isEmpty());
        exemplarList.clear();
        updateHasNewFilms(); //blir filmlistan tom finns inga nya

    }

    @FXML
    void addExemplarTableButtonPressed(ActionEvent event) {
        if(filmViewTable.getSelectionModel().getSelectedItem() == null) return;
        Film film = filmViewTable.getSelectionModel().getSelectedItem(); //för vald film
        //då det bara kan finnas en typ av exemplar för filmer, skapa det direkt
        Exemplar ex = new Exemplar();
        ex.setNewLåntyp("film");
        ex.setFilm_id(film);

        film.getExemplars().add(ex); //lägg också till i filmens lista för konsistens
        exemplarList.add(ex);
        removeExemplarButton.setDisable(false); //läggs ett ex till kan det tas bort
        hasChangedFilms = true; //nytt ex på en film räknas som att filmen ändrades och hanteras när filmer uppdateras
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
            selectedFilm.getExemplars().remove(selected);
            exemplarDeletionList.add(selected);
            exemplarList.remove(selected);
        }

        removeExemplarButton.setDisable(exemplarList.isEmpty());
    }

    /*
     * Middle bar buttons
     */

    @FXML
    void searchExistingFilmButtonPressed(ActionEvent event) throws IOException {
        clearForm();

        Stage searchWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("smallSearchWindow.fxml"));
        Scene searchwindow = new Scene(loader.load());

        //hämta referens till controller
        SmallSearchWindowController controller = loader.getController();
        controller.setState(getState());
        controller.loadServicesFromState(); //ge referens till appstate
        controller.setStage(searchWindow);
        controller.setMode(SmallSearchWindowController.Mode.FILM);

        searchWindow.setTitle("Search window");
        searchWindow.setScene(searchwindow);
        searchWindow.initModality(Modality.APPLICATION_MODAL);

        //ovanstående som metodanrop i konstruktor?
        searchWindow.show();
    }

    @FXML
    void addNewFilmToListButtonPressed(ActionEvent event) {
        setWindowToAddingFilmState();
    }

    @FXML
    void editFilmButtonPressed(ActionEvent event) {
        setWindowToEditingFilmState();
    }

    /*
     * Bottom right buttons
     */

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        if(formMode == FormMode.ADDING || formMode == FormMode.EDITING) {
            setWindowToDefaultState();
            clearForm();
        } else {
            viewLoader.setView(ViewLoader.Views.HANDLE_INVENTORY);
            setWindowToResetState();
        }
    }

    @FXML
    void confirmButtonPressed(ActionEvent event) {
        ArrayList<Film> processedFilms = new ArrayList<>(); //filmer som har hanterats ska inte gå in i changed
        if (hasNewFilms) {
            if(DEBUGPRINTOUTS) System.out.println("AddFilmViewController: hasNewFilms");
            for (Film f : filmList) {
                if(f.getId() == null) //hantera endast nya filmer
                filmDatabaseService.addNewFilm(f);
                processedFilms.add(f);
            }
        }
        filmList.removeAll(processedFilms); //när nya filmer är klara, ta bort från listan
        if (hasChangedFilms) {
            if(DEBUGPRINTOUTS) System.out.println("AddFilmViewController: hasChangedFilms");
            for (Film f : filmList) {
                filmDatabaseService.updateFilm(f);
            }
        }
        if (!filmDeletionList.isEmpty()) {
            for (Film f : filmDeletionList) {
                filmDatabaseService.deleteFilm(f);
            }
        }
        if(!exemplarDeletionList.isEmpty()) {
            for (Exemplar ex : exemplarDeletionList) {
                filmDatabaseService.deleteFilmCopy(ex);
            }
        }

        if(hasChangedFilms || hasNewFilms) {
            showInformationPopup("Ändringarna skickades till databasen!");
            hasChangedFilms = false;
            hasNewFilms = false;
        }else {
            showInformationPopup("Det finns inga ändringar att skicka.");
        }

        filmList.clear(); //tvinga användaren ladda om uppdaterade filmer och exemplar
        exemplarList.clear();
        setWindowToDefaultState(); //nollställ fönstret
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

    @Override
    public void loadServicesFromState() {
        super.loadServicesFromState();
        //tjänster
        filmDatabaseService = getState().getFilmDatabaseService();
    }

    private void updateHasNewFilms() {
        for (Film f : filmList) {
            if (f.getId() == null) {
                hasNewFilms = true;
                return;
            }
        }
    }

    /*
     * Window states
     */
    private void setWindowToDefaultState() {
        //form
        formMode = FormMode.NONE;
        clearForm();
        disableForm(true);

        //search/add/remove
        searchFilmButton.setDisable(false);
        addNewFilmButton.setDisable(false);
        editFilmButton.setDisable(filmList.isEmpty());

        //right side
        filmViewTable.setDisable(false);
        exemplarViewTable.setDisable(false);
        removeFilmButton.setDisable(filmList.isEmpty());
        addExemplarButton.setDisable(filmList.isEmpty());
        removeExemplarButton.setDisable(exemplarList.isEmpty());

        //confirm button
        confirmButton.setDisable(false);

    }

    private void setWindowToAddingFilmState() {
        //form
        formMode = FormMode.ADDING;
        clearForm();
        disableForm(false);

        //top buttons
        searchFilmButton.setDisable(true);
        addNewFilmButton.setDisable(true);
        editFilmButton.setDisable(true);

        //right side
        filmViewTable.setDisable(true);
        exemplarViewTable.setDisable(true);
        removeFilmButton.setDisable(true);
        removeExemplarButton.setDisable(true);
        addExemplarButton.setDisable(true);

        //confirm
        confirmButton.setDisable(true);
    }

    private void setWindowToEditingFilmState() {
        //form
        formMode = FormMode.EDITING;
        disableForm(false);

        //film table
        filmViewTable.setDisable(true);
        removeFilmButton.setDisable(true);

        //exemplar table
        exemplarViewTable.setDisable(true);
        removeExemplarButton.setDisable(true);
        addExemplarButton.setDisable(true);

        //search/add/edit
        searchFilmButton.setDisable(true);
        addNewFilmButton.setDisable(true);
        editFilmButton.setDisable(true);

        //confirm
        confirmButton.setDisable(true);
    }

    private void setWindowToResetState() {
        setWindowToDefaultState();

        //listor
        filmList.clear();
        exemplarList.clear();
        clearForm();

        //status
        hasNewFilms = false;
        hasChangedFilms = false;

        formMode = FormMode.NONE;
    }


    /*
     * UTILITIES
     */

    private void disableForm(boolean state) {
        titleBoxContents.setDisable(state);
        produktionslandBoxContents.setDisable(state);
        agelimitBoxContents.setDisable(state);

        addDirectorButton.setDisable(state);
        removeDirectorButton.setDisable(state);

        addActorButton.setDisable(state);
        removeActorButton.setDisable(state);

        addGenreButton.setDisable(state);
        removeGenreButton.setDisable(state);

        clearFormButton.setDisable(state);
        formOkButton.setDisable(state);
    }

    private void clearForm() {
        titleBoxContents.clear();
        produktionslandBoxContents.clear();
        agelimitBoxContents.clear();
        skådespelareList.clear();
        genresList.clear();
        regissörsList.clear();
    }

    /*
     * IMPLEMENTED
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg != ApplicationState.UpdateType.FILM) return;
        if (DEBUGPRINTOUTS) System.out.println("AddFilmViewController: Adding films from search result");
        filmList.clear();
        filmList.addAll(getState().getFilmSearchResults());
    }
}
