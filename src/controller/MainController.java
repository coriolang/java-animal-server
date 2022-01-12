package controller;

import com.google.gson.Gson;
import exceptions.*;
import model.Animal;
import model.Grass;
import model.Herbivore;
import model.Predator;
import repository.Forest;

import java.io.*;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.*;

public class MainController {

    private static final String CONFIGS_FILE = FileSystems.getDefault()
            .getPath("src", "resources", "configs.properties").toString();

    public static final int SERVER_PORT = 1356;

    private static Server server;

    private static Properties properties;
    private static String repositoryFile;

    private static final int EMPTY_INIT_MODE = 0;
    private static final int DEFAULT_INIT_MODE = 1;
    private static final int FILE_INIT_MODE = 2;

    public static ResourceBundle stringResources;

    private static Forest forest;

    public static void startApp() throws FileNotFoundException {
        try(FileInputStream fis = new FileInputStream(CONFIGS_FILE)) {
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }

        int iniType = Integer.parseInt(
                properties.getProperty("INITIALIZATION_TYPE")
        );

        String currentOSName = System.getProperty("os.name");

        if (currentOSName.contains("Windows")) {
            repositoryFile = properties.getProperty("REPOSITORY_FILE_WINDOWS");
        } else if (currentOSName.contains("Linux")) {
            repositoryFile = properties.getProperty("REPOSITORY_FILE_LINUX");
        } else if (currentOSName.contains("Mac")) {
            repositoryFile = properties.getProperty("REPOSITORY_FILE_MACOS");
        } else {
            repositoryFile = FileSystems.getDefault().getPath("forest.dat").toString();
            iniType = 1;
            System.out.println("Operating system not defined. Default initialization.");
        }

        stringResources = ResourceBundle.getBundle(
                "resources.strings",
                new Locale(properties.getProperty("LOCALE"))
        );

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
                        System.out.println(stringResources.getString("FILE_NOT_FOUND"));
                        Forest.defaultInit();
                    } else {
                        System.out.println(e.getMessage());
                    }
                }
                break;
        }

        forest = Forest.getInstance();

        System.out.println(iniType);
        System.out.println(repositoryFile);
    }

    public static void closeApp() throws IOException {
        Forest.save(repositoryFile);
        System.exit(0);
    }

    public static void startServer() {
        server = new Server();
        Thread serverThread = new Thread(server);

        serverThread.start();

        System.out.println("Server started!");
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

    public static String applyConfig(int selectedLanguage) throws IOException {
        String languageStatus = stringResources.getString("LANGUAGE_NOT_SELECTED");

        if (selectedLanguage == 0) {
            properties.setProperty("LOCALE", "ru");
        } else if (selectedLanguage == 1) {
            properties.setProperty("LOCALE", "en");
        }

        saveConfigs();

        stringResources = ResourceBundle.getBundle(
                "resources.strings",
                new Locale(properties.getProperty("LOCALE"))
        );

        if (selectedLanguage == 0) {
            languageStatus = stringResources.getString("SELECTED_RUSSIAN");
        } else if (selectedLanguage == 1) {
            languageStatus = stringResources.getString("SELECTED_ENGLISH");
        }

        return languageStatus;
    }

    public static String getPublicIpAddress() throws IOException {
        URL ipAddress = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                ipAddress.openStream()));
        String publicIpAddress = in.readLine();

        return publicIpAddress;
    }

    public static String create(int type, String name, float weight) {
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
            result = "Creation successful!!";
        } catch (IllegalWeightException e) {
            result = e.getMessage();
        }

        return result;
    }

    public static String kill(int animalId) {
        String result = "";

        try {
            Animal animal = forest.findAnimalById(animalId);
            animal.die();
            forest.update(animal);

            result = "Murder successful...";
        } catch (IllegalDeathException | NullPointerException e) {
            result = e.getMessage();
        }

        return result;
    }

    public static String feed(int animalId, int foodId) {
        String result = "";

        try {
            Animal animal = forest.findAnimalById(animalId);
            if (animal instanceof Herbivore herbivore) {
                Grass grass = forest.findGrassById(foodId);
                herbivore.eat(grass);
                forest.update(herbivore);
                forest.update(grass);

                result = "Feeding successful!!";
            } else if (animal instanceof Predator predator){
                Herbivore herbivore = (Herbivore) forest.findAnimalById(foodId);
                predator.eat(herbivore);
                forest.update(predator);
                forest.update(herbivore);

                if (!herbivore.isAlive()) {
                    result = "Successful hunt.";
                } else {
                    result = "Unsuccessful hunt!!";
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
