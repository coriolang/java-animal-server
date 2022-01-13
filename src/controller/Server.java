package controller;

import view.MessageBox;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server implements Runnable {

    private ServerSocket serverSocket;
    private int port;

    private ArrayList<ClientDialog> clients = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();

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
