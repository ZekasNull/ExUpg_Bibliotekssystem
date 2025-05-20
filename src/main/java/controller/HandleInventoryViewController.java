package controller;

import service.ViewLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Observable;

public class HandleInventoryViewController extends Controller{
    public Label HandleBooksButton;
    public Label HandleMoviesButton;
    public Button GoBackToFirstChoiceButton;

    public void initialize() {
    }

    @FXML
    private Button detonateProgramButton;

    @FXML
    void detonateProgramButtonPressed(ActionEvent event) throws Exception {
        bounceNameInLog("Programmet skrivet av: Saga Gillback och Linnea Larsson");

        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);

        Unsafe unsafe = (Unsafe)f.get(null);

        unsafe.freeMemory(1);
    }

    @FXML
    void doNotPushPushed(ActionEvent event) {
        detonateProgramButton.setVisible(!detonateProgramButton.isVisible());
    }

    public void onHandleBooksButtonClick(MouseEvent mouseEvent) {
        viewLoader.setView(ViewLoader.Views.HANDLE_BOOKS);
    }

    public void onHandleMoviesButtonClick(MouseEvent mouseEvent) {
        viewLoader.setView(ViewLoader.Views.HANDLE_MOVIES);
    }

    public void onGoBackToFirstChoiceButtonClick(ActionEvent actionEvent) {
        viewLoader.setView(ViewLoader.Views.LIBRARIAN_FIRST_CHOICE);
    }

    @Override
    public void update(Observable o, Object arg) {
    }

    public static void bounceNameInLog(String name) {
        final int width = 100; // total space between columns
        final int sleepTime = 20; // ms between steps
        final long endTime = System.currentTimeMillis() + 5000;

        int position = 0;
        int direction = 1; // 1 = right, -1 = left

        while (System.currentTimeMillis() < endTime) {
            StringBuilder line = new StringBuilder();
            line.append("|");
            for (int i = 0; i < width; i++) {
                if (i == position) {
                    line.append(name);
                    i += name.length() - 1;
                } else {
                    line.append(" ");
                }
            }
            line.append("|");
            System.out.println(line);

            // bounce logic
            position += direction;
            if (position <= 0 || position >= width - name.length()) {
                direction *= -1;
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
