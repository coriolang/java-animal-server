package controller;

import view.MessageBox;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Server implements Runnable {

    private ServerSocket serverSocket;
    private int port;
    private TextArea logsTextArea;

    private ArrayList<ClientDialog> clients = new ArrayList<>();

    public Server(int port, TextArea logsTextArea) {
        this.port = port;
        this.logsTextArea = logsTextArea;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            while (!serverSocket.isClosed()) {
                clients.removeIf(cli -> cli.getClientSocket().isClosed());

                Socket client = serverSocket.accept();

                logsTextArea.append(
                        "\n"
                                + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"))
                                + " "
                                + client.getInetAddress().getHostAddress()
                                + " "
                                + MainController.stringResources.getString("CONNECTED")
                );

                ClientDialog clientDialog = new ClientDialog(client);
                clients.add(clientDialog);
                new Thread(clientDialog).start();
            }

            stop();
        }  catch (SocketException e) {
//            MessageBox.showInfo(e.getMessage());
        } catch (IOException e) {
            MessageBox.showError(e.getMessage());
        }
    }

    public void stop() throws IOException {
        for (ClientDialog clientDialog : clients) {
            if (!clientDialog.getClientSocket().isClosed()) {
                clientDialog.getClientSocket().close();
            }
        }
        serverSocket.close();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
