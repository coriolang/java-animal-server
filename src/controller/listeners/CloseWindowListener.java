package controller.listeners;

import controller.MainController;
import view.ServerFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class CloseWindowListener extends WindowAdapter {

    private ServerFrame frame;

    public CloseWindowListener(ServerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            MainController.closeApp();
        } catch (IOException ex) {
            frame.getLogsTextArea().setText(ex.getMessage());
        }
    }
}
