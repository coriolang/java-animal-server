package model;

import exceptions.IllegalFeedingDeadException;
import exceptions.IllegalFoodException;

public class Herbivore extends Animal implements Food {

    public Herbivore() {
        super();
        setName("Default Herbivore");
    }

    public Herbivore(String name, float weight) {
        super(name, weight);
    }

    @Override
    public void eat(Food food) {
        if (!isAlive) {
            throw new IllegalFeedingDeadException();
        }
        if (!(food instanceof Grass)) {
            throw new IllegalFoodException();
        }
        Grass grass = (Grass)food;

        grass.weight -= 0.5F;
        this.weight += 0.5F;
    }
}
