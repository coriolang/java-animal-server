package model;

import exceptions.IllegalCarrionException;
import exceptions.IllegalFeedingDeadException;
import exceptions.IllegalFoodException;

public class Predator extends Animal {

    public Predator() {
        super();
        setName("Default Predator");
    }

    public Predator(String name, float weight) {
        super(name, weight);
    }

    private boolean hunt() {
        return Math.random() > 0.5;
    }

    @Override
    public void eat(Food food) {
        if (!isAlive) {
            throw new IllegalFeedingDeadException();
        }
        if (!(food instanceof Herbivore)) {
            throw new IllegalFoodException();
        }
        Herbivore herbivore = (Herbivore)food;
        if (!herbivore.isAlive) {
            throw new IllegalCarrionException();
        }
        if (!hunt()) {
            return;
        }

        herbivore.weight -= 2.0F;
        this.weight += 2.0F;
        herbivore.die();
    }
}
