package controller.listeners;

import controller.MainController;
import view.MessageBox;
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
            frame.getPortTextField().setText(MainController.getDefaultPort());

            frame.getStopServerButton().setEnabled(false);

            frame.getInitializationTypeChoice().select(MainController.getIniType());
        } catch (IOException ex) {
            MessageBox.showError(ex.getMessage());
        }
    }

    public void windowClosing(WindowEvent e) {
        try {
            MainController.stopServer();
            MainController.closeApp();
        } catch (IOException ex) {
            MessageBox.showError(ex.getMessage());
        }
    }
}
