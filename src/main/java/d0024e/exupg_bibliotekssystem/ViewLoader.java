package d0024e.exupg_bibliotekssystem;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import state.ApplicationState;

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
    @Override
    public void update(java.util.Observable o, Object arg) {

    }
}
