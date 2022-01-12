package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private ServerSocket serverSocket;

    private ExecutorService executeIt = Executors.newFixedThreadPool(4); // Заменить на коллекцию потоков

    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) { // For received console commands in server

            serverSocket = new ServerSocket(MainController.SERVER_PORT);

            System.out.println(serverSocket.getInetAddress().getHostName() + "/" + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort());

            System.out.println("Server socket created, command console reader for listen to server commands");

            while (!serverSocket.isClosed()) {
                if (bufferedReader.ready()) { // Check console commands in server
                    System.out.println("Main Server found any messages in channel, let's look at them.");

                    String serverCommand = bufferedReader.readLine();

                    if (serverCommand.equalsIgnoreCase("quit")) { // Close server when command is "quit"
                        System.out.println("Main Server initiate exiting...");
                        serverSocket.close();
                        break;
                    }
                }

                // Waiting for the client
                Socket client = serverSocket.accept();

                // New connection in a separate thread
                executeIt.execute(new ClientDialog(client));
                System.out.print("Connection accepted.");
            }

            stop();
        }  catch (SocketException e) {
//            System.out.println("Socket closed");
        } catch (IOException e) {
            e.printStackTrace(); // MessageBox!!
        }
    }

    public void stop() throws IOException {
        executeIt.shutdown();

        serverSocket.close();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
