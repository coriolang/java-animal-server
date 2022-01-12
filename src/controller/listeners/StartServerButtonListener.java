package controller.listeners;

import controller.MainController;
import view.ServerFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StartServerButtonListener implements ActionListener {

    private ServerFrame frame;

    public StartServerButtonListener(ServerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainController.startServer();

        frame.getLogsTextArea().setText(
                LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"))
                        + " The server is running"
        );
    }
}
