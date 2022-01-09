package exceptions;

import controller.MainController;

public class IllegalFoodException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = MainController.stringResources.getString("FOOD_EXCEPTION");

    public IllegalFoodException() {
        super(DEFAULT_MESSAGE);
    }

    public IllegalFoodException(String message) {
        super(message);
    }
}
