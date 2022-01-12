package controller.listeners;

import controller.MainController;
import view.ServerFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StopServerButtonListener implements ActionListener {

    private ServerFrame frame;

    public StopServerButtonListener(ServerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            MainController.stopServer();

            frame.getLogsTextArea().append(
                    "\n"
                            + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"))
                            + " The server stopped"
            );
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
