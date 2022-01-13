import controller.MainController;
import view.MessageBox;
import view.ServerFrame;

import java.io.FileNotFoundException;

public class ServerApp {

    public static void main(String[] args) throws InterruptedException {
        try {
            MainController.startApp();
        } catch (FileNotFoundException e) {
            MessageBox.showError("File with configs not found!");
            return;
        }

        // GUI
        new ServerFrame(MainController.stringResources.getString("APPLICATION_TITLE"));
    }
}
