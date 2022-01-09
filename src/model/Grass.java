package model;

import exceptions.IllegalWeightException;

import java.io.Serializable;

public class Grass implements Food, Serializable {

    protected int id;
    protected String name;
    protected float weight;

    public Grass() {
        this.id = 0;
        this.name = "Default Grass";
        this.weight = 0.0F;
    }

    public Grass(String name, float weight) {
        if (weight <= 0)
            throw new IllegalWeightException();

        this.id = 0;
        this.name = name;
        this.weight = weight;
    }

    public String getInfo() {
        return "id = " + id
                + "; name = " + name
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        if (weight <= 0)
            throw new IllegalWeightException();

        this.weight = weight;
    }
}
