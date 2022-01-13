package controller;

import view.MessageBox;

import java.io.*;
import java.net.Socket;

public class ClientDialog implements Runnable {

    private Socket clientSocket;

    public ClientDialog(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        ) {
            String clientCommand = dataInputStream.readUTF();

            String response = MainController.response(clientCommand);

            dataOutputStream.writeUTF(response);
            dataOutputStream.flush();

            clientSocket.close();
        } catch (IOException e) {
            MessageBox.showError(e.getMessage());
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
