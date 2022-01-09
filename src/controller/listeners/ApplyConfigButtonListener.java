package controller.listeners;

import view.ServerFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplyConfigButtonListener implements ActionListener {

    private ServerFrame frame;

    public ApplyConfigButtonListener(ServerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.getLogsTextArea().setText("CONFIG APPLIED!!");
    }
}
