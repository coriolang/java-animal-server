package controller.listeners;

import controller.MainController;
import view.MessageBox;
import view.ServerFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StartServerButtonListener implements ActionListener {

    private ServerFrame frame;

    public StartServerButtonListener(ServerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (frame.getPortTextField().getText().isEmpty()
                || frame.getPortTextField().getText().isBlank()) {

            MessageBox.showError(MainController.stringResources.getString("INCORRECT_PORT"));
            return;
        }

        int port;
        try {
            port = Integer.parseInt((frame.getPortTextField().getText()));
        } catch (NumberFormatException ex) {
            MessageBox.showError(MainController.stringResources.getString("INCORRECT_PORT"));
            return;
        }
        MainController.startServer(port, frame.getLogsTextArea());

        String logMessage = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"))
                + " "
                + MainController.stringResources.getString("SERVER_STARTED");

        if (frame.getLogsTextArea().getText().length() > 0) {
            frame.getLogsTextArea().append("\n" + logMessage);
        } else {
            frame.getLogsTextArea().setText(logMessage);
        }

        frame.getPortTextField().setEnabled(false);
        frame.getStartServerButton().setEnabled(false);
        frame.getStopServerButton().setEnabled(true);
        frame.getLanguageChoice().setEnabled(false);
        frame.getInitializationTypeChoice().setEnabled(false);
        frame.getApplyConfigButton().setEnabled(false);
    }
}
