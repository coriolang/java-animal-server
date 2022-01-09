package exceptions;

import controller.MainController;

public class IllegalWeightException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = MainController.stringResources.getString("WEIGHT_EXCEPTION");

    public IllegalWeightException() {
        super(DEFAULT_MESSAGE);
    }

    public IllegalWeightException(String message) {
        super(message);
    }
}
