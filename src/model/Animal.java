package model;

import exceptions.IllegalDeathException;
import exceptions.IllegalWeightException;

import java.io.Serializable;

public abstract class Animal implements Serializable {

    protected int id;
    protected String name;
    protected boolean isAlive;
    protected float weight;

    public Animal() {
        this.id = 0;
        this.name = "Default Animal";
        this.weight = 0.0F;
        this.isAlive = true;
    }

    public Animal(String name, float weight) {
        if (weight <= 0)
            throw new IllegalWeightException();

        this.id = 0;
        this.name = name;
        this.weight = weight;
        this.isAlive = true;
    }

    public void die() {
        if (!isAlive)
            throw new IllegalDeathException();

        this.isAlive = false;
    }

    abstract public void eat(Food food);

    public String getInfo() {
        return "id = " + id
                + "; name = " + name
                + "; isAlive = " + isAlive
                + "; weight = " + weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        if (!isAlive)
            throw new IllegalDeathException();

        isAlive = alive;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        if (weight <= 0)
            throw new IllegalWeightException();

        this.weight = weight;
    }
}
