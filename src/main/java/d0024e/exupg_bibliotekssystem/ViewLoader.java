package d0024e.exupg_bibliotekssystem;

import controller.Controller;
import controller.ShowProfileViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import state.ApplicationState;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewLoader extends Controller {
    private static ApplicationState APPSTATE;
    private static BorderPane rootLayout;

    private static final Map<String, Node> views = new HashMap<>();

    public static void initialize(Stage primaryStage, ApplicationState state) throws IOException {
        APPSTATE = state;
        rootLayout = new BorderPane();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        loadAndStoreView("Hantera böcker", "add-book-view.fxml");
        loadAndStoreView("Hantera filmer", "add-movie-view.fxml");
        loadAndStoreView("Hantera inventarie", "handle-inventory-view.fxml");
        loadAndStoreView("Bibliotikaries första val", "librarian-first-choice-view.fxml");
        loadAndStoreView("Huvudmeny", "main-menu.fxml");
        loadAndStoreView("Ej återlämnade", "show-not-returned-view.fxml");
        loadAndStoreView("Profil", "show-profile-view.fxml");

        setView("Huvudmeny");
    }

    private static void loadAndStoreView(String name, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
        Parent view = loader.load();

        Controller controller = loader.getController();
        controller.setState(APPSTATE);

        if (controller instanceof ShowProfileViewController profileController) {
            profileController.loadData();
        }

        views.put(name, view);
    }

    public static void setView(String name) {
        Node view = views.get(name);
        if (view != null) {
            rootLayout.setCenter(view);
        } else {
            System.err.println("View '" + name + "' not found.");
        }
    }
    public static BorderPane getRootLayout() {
        return rootLayout;
    }
    public void loadPopup(String fxmlFile, String title) throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader(this.APPSTATE.app.getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Controller controller = loader.getController();
            controller.setState(APPSTATE); //ge referens till appstate
            APPSTATE.addObserver(controller); //behöver inte vara observer på state?

            Stage popupStage = new Stage();
            popupStage.setTitle(title);
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.show();
        }catch (Exception e) { e.printStackTrace();}
    }
    @Override
    public void update(java.util.Observable o, Object arg) {
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

