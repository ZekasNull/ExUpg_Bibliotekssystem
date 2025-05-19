package service;

import controller.Controller;
import controller.SmallSearchWindowController;
import d0024e.exupg_bibliotekssystem.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import state.ApplicationState;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewLoader {
    //debug
    private static final boolean DEBUGPRINTS = MainApplication.DEBUGPRINTING;

    private BorderPane rootLayout;
    private ApplicationState applicationState;
    private Stage primaryStage;

    private Map<String, Scene> views = new HashMap<>();

    public enum Views {
        MAIN_MENU("Huvudmeny", "/d0024e/exupg_bibliotekssystem/main-menu.fxml"),
        HANDLE_BOOKS("Hantera böcker", "/d0024e/exupg_bibliotekssystem/add-book-view.fxml"),
        HANDLE_MOVIES("Hantera filmer", "/d0024e/exupg_bibliotekssystem/add-movie-view.fxml"),
        HANDLE_INVENTORY("Hantera inventarie", "/d0024e/exupg_bibliotekssystem/handle-inventory-view.fxml"),
        LIBRARIAN_FIRST_CHOICE("Bibliotekariens första val", "/d0024e/exupg_bibliotekssystem/librarian-first-choice-view.fxml"),
        NOT_RETURNED("Ej återlämnade", "/d0024e/exupg_bibliotekssystem/show-not-returned-view.fxml"),
        PROFILE("Profil", "/d0024e/exupg_bibliotekssystem/show-profile-view.fxml"),
        SMALL_SEARCH_WINDOW("Minisökruta", "/d0024e/exupg_bibliotekssystem/smallSearchWindow.fxml"),
        LOGIN_WINDOW("Log in view", "/d0024e/exupg_bibliotekssystem/login-view.fxml");

        private final String title;
        private final String fxmlPath;

        Views(String title, String fxmlPath) {
            this.title = title;
            this.fxmlPath = fxmlPath;
        }

        public String getTitle() {
            return title;
        }

        public String getFxmlPath() {
            return fxmlPath;
        }
    }


    public ViewLoader(ApplicationState state){
        this.applicationState = state;
    }


    public void initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.show();
    }

    public void prepareViews() throws IOException {
        loadAndStoreView(Views.MAIN_MENU.getTitle(), Views.MAIN_MENU.getFxmlPath());
        loadAndStoreView(Views.HANDLE_MOVIES.getTitle(), Views.HANDLE_MOVIES.getFxmlPath());
        loadAndStoreView(Views.HANDLE_INVENTORY.getTitle(), Views.HANDLE_INVENTORY.getFxmlPath());
        loadAndStoreView(Views.LIBRARIAN_FIRST_CHOICE.getTitle(), Views.LIBRARIAN_FIRST_CHOICE.getFxmlPath());
        loadAndStoreView(Views.NOT_RETURNED.getTitle(), Views.NOT_RETURNED.getFxmlPath());
        loadAndStoreView(Views.PROFILE.getTitle(), Views.PROFILE.getFxmlPath());
        loadAndStoreView(Views.MAIN_MENU.getTitle(), Views.MAIN_MENU.getFxmlPath());
    }

    private void loadAndStoreView(String name, String fxmlPath) throws IOException {
        if (DEBUGPRINTS) System.out.println("ViewLoader: preparing and storing view named: "+name);
        FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load());

        Controller controller = loader.getController();
        controller.setState(applicationState);
        controller.loadServicesFromState();

        views.put(name, scene);
    }

    public void setView(Views view) {
        Scene scene = views.get(view.getTitle());
        if (scene != null) {
            //rootLayout.setCenter(view);
            primaryStage.setScene(scene);
        } else {
            System.err.println("View '" + view.getTitle() + "' not found.");
        }
    }

    public void loadPopup(Views views) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(views.getFxmlPath()));
            Scene scene = new Scene(loader.load());

            Controller controller = loader.getController();
            controller.setState(applicationState);
            controller.loadServicesFromState(); //ladda tjänster

            Stage popupStage = new Stage();
            popupStage.setTitle(views.getTitle());
            popupStage.setScene(scene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.show();
    }

    public void loadClosingPopup(Views views) throws IOException {
        Stage searchWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(views.getFxmlPath()));
        Scene searchwindow = new Scene(loader.load());

        //hämta referens till controller
        SmallSearchWindowController controller = loader.getController();
        controller.setState(applicationState);
        controller.loadServicesFromState(); //ge referens till appstate
        controller.setStage(searchWindow);
        controller.setMode(SmallSearchWindowController.Mode.BOK);

        searchWindow.setTitle(views.getTitle());
        searchWindow.setScene(searchwindow);
        searchWindow.initModality(Modality.APPLICATION_MODAL);

        //ovanstående som metodanrop i konstruktor?
        searchWindow.show();
    }

}
    /*public ViewLoader(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.APPSTATE = ApplicationState.getInstance();
    }

    public void loadScene(String fxmlFile, String title) {
        try{
            FXMLLoader loader = new FXMLLoader(this.APPSTATE.app.getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());

            Controller controller = loader.getController();
            controller.setState(APPSTATE);
            if (controller instanceof ShowProfileViewController) {
                ((ShowProfileViewController) loader.getController()).loadData();
            }

            APPSTATE.addObserver(controller);

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }*/

