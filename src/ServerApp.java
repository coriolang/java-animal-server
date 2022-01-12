import controller.MainController;
import view.ServerFrame;

import java.io.FileNotFoundException;

public class ServerApp {

    public static void main(String[] args) throws InterruptedException {
        try {
            MainController.startApp();
        } catch (FileNotFoundException e) {
            // Вывести Месседж Бокс с ошибкой
            System.out.println("File with configs not found!");
            return;
        }

        // GUI
        new ServerFrame(MainController.stringResources.getString("APPLICATION_TITLE"));
    }
}
