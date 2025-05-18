package d0024e.exupg_bibliotekssystem;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import state.ApplicationState;
import java.io.IOException;

public class ViewLoader extends Controller {
    private Stage primaryStage;
    private ApplicationState APPSTATE;

    public ViewLoader(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.APPSTATE = ApplicationState.getInstance();
    }

    public void loadScene(String fxmlFile, String title) {
        try{
            FXMLLoader loader = new FXMLLoader(this.APPSTATE.app.getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());

            Controller controller = loader.getController();
            controller.setState(APPSTATE);

            APPSTATE.addObserver(controller);

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }
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
