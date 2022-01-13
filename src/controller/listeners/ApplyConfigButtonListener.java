package controller.listeners;

import controller.MainController;
import view.MessageBox;
import view.ServerFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ApplyConfigButtonListener implements ActionListener {

    private ServerFrame frame;

    public ApplyConfigButtonListener(ServerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedLanguage = frame.getLanguageChoice().getSelectedIndex();
        int selectedIniType = frame.getInitializationTypeChoice().getSelectedIndex();
        String initStatus;

        try {
            initStatus = MainController.applyConfig(selectedLanguage, selectedIniType);
        } catch (IOException ex) {
            MessageBox.showError(ex.getMessage());
            return;
        }

        frame.updateStrings();
        frame.getInitializationTypeChoice().select(MainController.getIniType());

        String logMessage = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"))
                + " "
                + initStatus;

        if (frame.getLogsTextArea().getText().length() > 0) {
            frame.getLogsTextArea().append("\n" + logMessage);
        } else {
            frame.getLogsTextArea().setText(logMessage);
        }
    }
}
