package d0024e.exupg_bibliotekssystem;

import javafx.application.Application;
import javafx.stage.Stage;
import service.ViewLoader;
import state.ApplicationState;

public class MainApplication extends Application {
    public static final boolean DEBUGPRINTING = true; //styr debug i hela programmet, ideally
    private ApplicationState applicationState;

    public MainApplication() {
    }

    @Override
    public void init() {
        this.applicationState = new ApplicationState();
    }

    @Override //Starts the program and sets up the primaryStage
    public void start(Stage var1) throws Exception {
        ViewLoader viewLoader = applicationState.getViewLoaderService();
        viewLoader.initialize(var1);
        viewLoader.prepareViews();
        viewLoader.setView(ViewLoader.Views.MAIN_MENU);
        //test
        //viewLoader.loadPopup(ViewLoader.Views.SMALL_SEARCH_WINDOW);
    }

    public static void main(String[] args) {
        /*
        För att kolla att allt fungerar:
        Lägga till, ändra objekt:
        1. Logga in som Josef
        2. Skapa en ny film med ett exemplar ("Testfilm")
        3. Skapa en ny bok med två exemplar ("Testbok")
        4. ändra filmen på något sätt
        5. Logga ut

        Söka, låna, återlämna, kvittoutskrift:
        6. Logga in som Sven Svensson
        7. Sök efter bok
        8. Köa lån av bok
        9. Sök efter film
        10. Köa lån av film
        11. Försök låna (ska misslyckas)
        12. Ta bort ett objekt och försök igen (ska gå)
        13. Gå till profil och se lån
        14.  Återlämna lån.

        Ej återlämnade objekt, ta bort objekt:
        15. Logga in som Josef
        16. Visa ej återlämnade lån (bör vara ett)
        17. Ta bort testfilm
        18. Ta bort testbok
        19. Försök söka efter test
        20. Logga ut.

        21+: Försök göra avsiktiga fel och se vad som inte hanteras

         */
        launch(args);
    }
}