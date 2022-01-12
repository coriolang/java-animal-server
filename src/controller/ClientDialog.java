package controller;

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
            System.out.println("Read from client: " + clientCommand);

            String[] splittedCommand = clientCommand.split("&");

            if (splittedCommand[0].contains("get")) {
                sentJson(splittedCommand);
            } else if (splittedCommand[0].contains("create")) {
                create(splittedCommand);
            } else if (splittedCommand[0].contains("feed")) {
                feed(splittedCommand);
            } else if (splittedCommand[0].contains("kill")) {
                kill(splittedCommand);
            } else if (splittedCommand[0].contains("test")) {
                test();
            }
        } catch (IOException e) {
            e.printStackTrace();
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

        String result = MainController.create(type, name, weight);

        dataOutputStream.writeUTF(result);
        dataOutputStream.flush();

        dataOutputStream.close();
    }

    private void kill(String[] splittedCommand) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        int animalId = Integer.parseInt(splittedCommand[1]);

        String result = MainController.kill(animalId);

        dataOutputStream.writeUTF(result);
        dataOutputStream.flush();

        dataOutputStream.close();
    }

    private void feed(String[] splittedCommand) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        int animalId = Integer.parseInt(splittedCommand[1]);
        int foodId = Integer.parseInt(splittedCommand[2]);

        String result = MainController.feed(animalId, foodId);

        dataOutputStream.writeUTF(result);
        dataOutputStream.flush();

        dataOutputStream.close();
    }


    private void sentJson(String[] splittedCommand) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        String json = "";

        if (splittedCommand[1].contains("allAnimals")) {
            json = MainController.jsonAllAnimals();
        } else if (splittedCommand[1].contains("allHerbivores")) {
            json = MainController.jsonAllHerbivores();
        } else if (splittedCommand[1].contains("allPredators")) {
            json = MainController.jsonAllPredators();
        } else if (splittedCommand[1].contains("allGrasses")) {
            json = MainController.jsonAllGrasses();
        } else if (splittedCommand[1].contains("liveAnimals")) {
            json = MainController.jsonAllLiveAnimals();
        } else if (splittedCommand[1].contains("liveHerbivores")) {
            json = MainController.jsonAllLiveHerbivores();
        } else if (splittedCommand[1].contains("livePredators")) {
            json = MainController.jsonAllLivePredators();
        }

        dataOutputStream.writeUTF(json);
        dataOutputStream.flush();

        dataOutputStream.close();
    }
}
