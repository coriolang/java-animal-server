package controller.listeners;

import view.ServerFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopServerButtonListener implements ActionListener {

    private ServerFrame frame;

    public StopServerButtonListener(ServerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.getLogsTextArea().setText("SERVER STOPPED!!");
    }
}
