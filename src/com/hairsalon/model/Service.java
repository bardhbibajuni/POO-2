package com.hairsalon.model;

public class Service {
    private int id;
    private String name;
    private double price;
    private int duration;

    public Service(int id, String name, double price, int duration) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }
}