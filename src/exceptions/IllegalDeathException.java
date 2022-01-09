package exceptions;

import controller.MainController;

public class IllegalDeathException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = MainController.stringResources.getString("DEATH_EXCEPTION");

    public IllegalDeathException() {
        super(DEFAULT_MESSAGE);
    }

    public IllegalDeathException(String message) {
        super(message);
    }
}
