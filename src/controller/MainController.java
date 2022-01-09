package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainController {

    private static final String CONFIGS_FILE = FileSystems.getDefault()
            .getPath("src", "resources", "configs.properties").toString();

    private static Properties properties;
    private static String repositoryFile;

    private static final int EMPTY_INIT_MODE = 0;
    private static final int DEFAULT_INIT_MODE = 1;
    private static final int FILE_INIT_MODE = 2;

    public static ResourceBundle stringResources;

//    private static Forest forest;

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

//        switch (iniType) {
//            case EMPTY_INIT_MODE:
//                Forest.emptyInit();
//                break;
//            case DEFAULT_INIT_MODE:
//                Forest.defaultInit();
//                break;
//            case FILE_INIT_MODE:
//                try {
//                    Forest.load(repositoryFile);
//                } catch (IOException | ClassNotFoundException e) {
//                    if (e instanceof FileNotFoundException) {
//                        System.out.println(stringResources.getString("FILE_NOT_FOUND"));
//                        Forest.defaultInit();
//                    } else {
//                        System.out.println(e.getMessage());
//                    }
//                }
//                break;
//        }
//
//        forest = Forest.getInstance();
    }

    public static void closeApp() throws IOException {
//        Forest.save(MainController.repositoryFile);
        System.exit(0);
    }

    private static void saveConfigs() throws IOException {
        try(FileOutputStream fos = new FileOutputStream(CONFIGS_FILE)) {
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
}
