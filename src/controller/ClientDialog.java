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
        try (DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());) {
            String clientCommand = dataInputStream.readUTF();

            String[] splittedCommand = clientCommand.split("&");

            switch (splittedCommand[0]) {
                case "get":
                    sentJson(splittedCommand);
                    break;
                case "create":
                    create(splittedCommand);
                    break;
                case "feed":
                    feed(splittedCommand);
                    break;
                case "kill":
                    kill(splittedCommand);
                    break;
                case "test":
                    test();
                    break;
            }

            clientSocket.close();
        } catch (IOException e) {
            MessageBox.showError(e.getMessage());
        }
    }

    private void test() throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        dataOutputStream.writeInt(1);
        dataOutputStream.flush();

        dataOutputStream.close();
    }

    private void create(String[] splittedCommand) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        int type = Integer.parseInt(splittedCommand[1]);
        String name = splittedCommand[2];
        float weight = Float.parseFloat(splittedCommand[3]);
        String locale = splittedCommand[4];

        String result = MainController.create(type, name, weight, locale);

        dataOutputStream.writeUTF(result);
        dataOutputStream.flush();

        dataOutputStream.close();
    }

    private void kill(String[] splittedCommand) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        int animalId = Integer.parseInt(splittedCommand[1]);
        String locale = splittedCommand[2];

        String result = MainController.kill(animalId, locale);

        dataOutputStream.writeUTF(result);
        dataOutputStream.flush();

        dataOutputStream.close();
    }

    private void feed(String[] splittedCommand) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        int animalId = Integer.parseInt(splittedCommand[1]);
        int foodId = Integer.parseInt(splittedCommand[2]);
        String locale = splittedCommand[3];

        String result = MainController.feed(animalId, foodId, locale);

        dataOutputStream.writeUTF(result);
        dataOutputStream.flush();

        dataOutputStream.close();
    }


    private void sentJson(String[] splittedCommand) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        String json = "";

        switch (splittedCommand[1]) {
            case "allAnimals":
                json = MainController.jsonAllAnimals();
                break;
            case "allHerbivores":
                json = MainController.jsonAllHerbivores();
                break;
            case "allPredators":
                json = MainController.jsonAllPredators();
                break;
            case "allGrasses":
                json = MainController.jsonAllGrasses();
                break;
            case "liveAnimals":
                json = MainController.jsonAllLiveAnimals();
                break;
            case "liveHerbivores":
                json = MainController.jsonAllLiveHerbivores();
                break;
            case "livePredators":
                json = MainController.jsonAllLivePredators();
                break;
        }

        dataOutputStream.writeUTF(json);
        dataOutputStream.flush();

        dataOutputStream.close();
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
