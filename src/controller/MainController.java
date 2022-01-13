package controller;

import com.google.gson.Gson;
import exceptions.*;
import model.Animal;
import model.Grass;
import model.Herbivore;
import model.Predator;
import repository.Forest;
import view.MessageBox;
import view.ServerFrame;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.*;

public class MainController {

    private static final String CONFIGS_FILE = FileSystems.getDefault()
            .getPath("src", "resources", "configs.properties").toString();

    private static Server server;

    private static Properties properties;
    private static String repositoryFile;

    private static final int EMPTY_INIT_MODE = 0;
    private static final int DEFAULT_INIT_MODE = 1;
    private static final int FILE_INIT_MODE = 2;

    private static Forest forest;

    public static ResourceBundle stringResources;

    public static void startApp() throws FileNotFoundException {
        try(FileInputStream fis = new FileInputStream(CONFIGS_FILE)) {
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }

        stringResources = ResourceBundle.getBundle(
                "resources.strings",
                new Locale(properties.getProperty("LOCALE"))
        );

        String currentOSName = System.getProperty("os.name");

        int iniType = Integer.parseInt(
                properties.getProperty("INITIALIZATION_TYPE")
        );

        if (currentOSName.contains("Windows")) {
            repositoryFile = properties.getProperty("REPOSITORY_FILE_WINDOWS");
        } else if (currentOSName.contains("Linux")) {
            repositoryFile = properties.getProperty("REPOSITORY_FILE_LINUX");
        } else if (currentOSName.contains("Mac")) {
            repositoryFile = properties.getProperty("REPOSITORY_FILE_MACOS");
        } else {
            repositoryFile = FileSystems.getDefault().getPath("forest.dat").toString();
            iniType = 1;
            MessageBox.showInfo(stringResources.getString("OS_NOT_DEFINED"));
        }

        initialization(iniType);

        forest = Forest.getInstance();
    }

    public static void closeApp() throws IOException {
        Forest.save(repositoryFile);
        System.exit(0);
    }

    public static void startServer(int port, TextArea logsTextArea) {
        server = new Server(port, logsTextArea);
        Thread serverThread = new Thread(server);

        serverThread.start();
    }

    public static void stopServer() throws IOException {
        if (server != null) {
            if (server.getServerSocket() != null) {
                if (!server.getServerSocket().isClosed()) {

                    server.stop();
                }
            }
        }
    }

    private static void saveConfigs() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(CONFIGS_FILE)) {
            properties.store(fos, null);
        } catch (IOException e) {
            throw e;
        }
    }

    public static String applyConfig(int selectedLanguage, int selectedIniType) throws IOException {
        if (selectedLanguage == 0) {
            properties.setProperty("LOCALE", "ru");
        } else if (selectedLanguage == 1) {
            properties.setProperty("LOCALE", "en");
        }

        stringResources = ResourceBundle.getBundle(
                "resources.strings",
                new Locale(properties.getProperty("LOCALE"))
        );

        String initStatus = "";
        switch (selectedIniType) {
            case 0:
                properties.setProperty("INITIALIZATION_TYPE", "0");
                initStatus = MainController.stringResources.getString("EMPTY_INIT_SELECTED");
                break;
            case 1:
                properties.setProperty("INITIALIZATION_TYPE", "1");
                initStatus = MainController.stringResources.getString("DEFAULT_INIT_SELECTED");
                break;
            case 2:
                properties.setProperty("INITIALIZATION_TYPE", "2");
                initStatus = MainController.stringResources.getString("FROM_FILE_INIT_SELECTED");
                break;
        }

        int iniType = Integer.parseInt(
                properties.getProperty("INITIALIZATION_TYPE")
        );

        initialization(iniType);

        forest = Forest.getInstance();

        saveConfigs();

        return initStatus;
    }

    private static void initialization(int iniType) {
        switch (iniType) {
            case EMPTY_INIT_MODE:
                Forest.emptyInit();
                break;
            case DEFAULT_INIT_MODE:
                Forest.defaultInit();
                break;
            case FILE_INIT_MODE:
                try {
                    Forest.load(repositoryFile);
                } catch (IOException | ClassNotFoundException e) {
                    if (e instanceof FileNotFoundException) {
                        MessageBox.showInfo(stringResources.getString("FILE_NOT_FOUND"));
                        Forest.defaultInit();
                    } else {
                        MessageBox.showError(e.getMessage());
                    }
                }
                break;
        }
    }

    public static int getIniType() {
        return Integer.parseInt(properties.getProperty("INITIALIZATION_TYPE"));
    }

    public static ResourceBundle getResourceBundle(String locale) {
        return ResourceBundle.getBundle(
                "resources.strings",
                new Locale(locale)
        );
    }

    public static String getPublicIpAddress() throws IOException {
        URL ipAddress = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                ipAddress.openStream()));
        String publicIpAddress = in.readLine();

        return publicIpAddress;
    }

    public static String getDefaultPort() {
        return properties.getProperty("DEFAULT_PORT");
    }

    public static String create(int type, String name, float weight, String locale) {
        String result = "";

        try {
            switch (type) {
                case 0:
                    Herbivore herbivore = new Herbivore(name, weight);
                    forest.create(herbivore);
                    break;
                case 1:
                    Predator predator = new Predator(name, weight);
                    forest.create(predator);
                    break;
                case 2:
                    Grass grass = new Grass(name, weight);
                    forest.create(grass);
                    break;
            }
            result = getResourceBundle(locale).getString("CREATION_SUCCESSFUL");
        } catch (IllegalWeightException e) {
            result = e.getMessage();
        }

        return result;
    }

    public static String kill(int animalId, String locale) {
        String result = "";

        try {
            Animal animal = forest.findAnimalById(animalId);
            animal.die();
            forest.update(animal);

            result = getResourceBundle(locale).getString("MURDER_SUCCESSFUL");
        } catch (IllegalDeathException | NullPointerException e) {
            result = e.getMessage();
        }

        return result;
    }

    public static String feed(int animalId, int foodId, String locale) {
        String result = "";

        try {
            Animal animal = forest.findAnimalById(animalId);
            if (animal instanceof Herbivore herbivore) {
                Grass grass = forest.findGrassById(foodId);
                herbivore.eat(grass);
                forest.update(herbivore);
                forest.update(grass);

                result = getResourceBundle(locale).getString("FEEDING_SUCCESSFUL");
            } else if (animal instanceof Predator predator){
                Herbivore herbivore = (Herbivore) forest.findAnimalById(foodId);
                predator.eat(herbivore);
                forest.update(predator);
                forest.update(herbivore);

                if (!herbivore.isAlive()) {
                    result = getResourceBundle(locale).getString("HUNT_SUCCESSFUL");
                } else {
                    result = getResourceBundle(locale).getString("HUNT_UNSUCCESSFUL");
                }
            }
        } catch (IllegalCarrionException
                | IllegalDeathException
                | IllegalFeedingDeadException
                | IllegalFoodException
                | NullPointerException e) {
            result = e.getMessage();
        }

        return result;
    }

    public static String jsonAllAnimals() {
        HashMap<Integer, Animal> animalHashMap = forest.getAllAnimals();
        HashMap<Integer, String> stringHashMap = new HashMap<>();

        for (Animal animal : animalHashMap.values()) {
            stringHashMap.put(animal.getId(), animal.getInfo());
        }

        Gson gson = new Gson();
        String json = gson.toJson(stringHashMap);

        return json;
    }

    public static String jsonAllHerbivores() {
        HashMap<Integer, Herbivore> herbivoreHashMap = forest.getAllHerbivores();
        HashMap<Integer, String> stringHashMap = new HashMap<>();

        for (Herbivore herbivore : herbivoreHashMap.values()) {
            stringHashMap.put(herbivore.getId(), herbivore.getInfo());
        }

        Gson gson = new Gson();
        String json = gson.toJson(stringHashMap);

        return json;
    }

    public static String jsonAllPredators() {
        HashMap<Integer, Predator> predatorHashMap = forest.getAllPredators();
        HashMap<Integer, String> stringHashMap = new HashMap<>();

        for (Predator predator : predatorHashMap.values()) {
            stringHashMap.put(predator.getId(), predator.getInfo());
        }

        Gson gson = new Gson();
        String json = gson.toJson(stringHashMap);

        return json;
    }

    public static String jsonAllGrasses() {
        HashMap<Integer, Grass> grassHashMap = forest.getAllGrasses();
        HashMap<Integer, String> stringHashMap = new HashMap<>();

        for (Grass grass : grassHashMap.values()) {
            stringHashMap.put(grass.getId(), grass.getInfo());
        }

        Gson gson = new Gson();
        String json = gson.toJson(stringHashMap);

        return json;
    }

    public static String jsonAllLiveAnimals() {
        HashMap<Integer, Animal> animalHashMap = forest.getAllLiveAnimals();
        HashMap<Integer, String> stringHashMap = new HashMap<>();

        for (Animal animal : animalHashMap.values()) {
            stringHashMap.put(animal.getId(), animal.getInfo());
        }

        Gson gson = new Gson();
        String json = gson.toJson(stringHashMap);

        return json;
    }

    public static String jsonAllLiveHerbivores() {
        HashMap<Integer, Herbivore> herbivoreHashMap = forest.getAllLiveHerbivores();
        HashMap<Integer, String> stringHashMap = new HashMap<>();

        for (Herbivore herbivore : herbivoreHashMap.values()) {
            stringHashMap.put(herbivore.getId(), herbivore.getInfo());
        }

        Gson gson = new Gson();
        String json = gson.toJson(stringHashMap);

        return json;
    }

    public static String jsonAllLivePredators() {
        HashMap<Integer, Predator> predatorHashMap = forest.getAllLivePredators();
        HashMap<Integer, String> stringHashMap = new HashMap<>();

        for (Predator predator : predatorHashMap.values()) {
            stringHashMap.put(predator.getId(), predator.getInfo());
        }

        Gson gson = new Gson();
        String json = gson.toJson(stringHashMap);

        return json;
    }
}
