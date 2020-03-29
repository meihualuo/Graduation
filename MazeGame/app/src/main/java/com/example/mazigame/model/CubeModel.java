package com.example.mazigame.model;

public class CubeModel {
    private int weight;
    private int height;

    public CubeModel(int weight, int height) {
        this.weight = weight;
        this.height = height;
    }

    public int getWeighe() {
        return weight;
    }

    public void setWeighe(int weight) {
        this.weight = weight;
    }

    public int getHeighe() {
        return height;
    }

    public void setHeighe(int height) {
        this.height = height;
    }
}
