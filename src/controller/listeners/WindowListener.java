package controller.listeners;

import controller.MainController;
import view.ServerFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class WindowListener extends WindowAdapter {

    private ServerFrame frame;

    public WindowListener(ServerFrame frame) {
        this.frame = frame;
    }

    public void windowOpened(WindowEvent e) {
        try {
            String publicIpAddress = MainController.getPublicIpAddress();

            frame.getIpAddressTextLabel().setText(publicIpAddress);
            frame.getPortTextLabel().setText(String.valueOf(MainController.SERVER_PORT));
        } catch (IOException ex) {
            frame.getLogsTextArea().setText(ex.getMessage()); // Заменить на месседж бокс
        }
    }

    public void windowClosing(WindowEvent e) {
        try {
            MainController.stopServer();
            MainController.closeApp();
        } catch (IOException ex) {
            frame.getLogsTextArea().setText(ex.getMessage()); // Заменить на месседж бокс
        }
    }
}
