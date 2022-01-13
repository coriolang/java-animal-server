package view;

import controller.MainController;

import javax.swing.*;

public class MessageBox {

    public static void showInfo(String infoMessage) {
        JOptionPane.showMessageDialog(
                null,
                infoMessage,
                MainController.stringResources.getString("APPLICATION_TITLE"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showError(String infoMessage) {
        JOptionPane.showMessageDialog(
                null,
                infoMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
