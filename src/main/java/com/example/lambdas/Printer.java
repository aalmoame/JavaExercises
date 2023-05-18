package com.example.lambdas;

public class Printer {
    String name; // can be any string
	Type type; // enum of 'Laser', 'ThreeDimensional', 'LED', 'DotMatrix'
	int speedPerSecond; // no. of words the printer can print per second

    Printer(String name, Type type, int speedPerSecond) {
        this.name = name;
        this.type = type;
        this.speedPerSecond = speedPerSecond;
    }

    public String getName() {
        return this.name;
    }

    public Type getType() {
        return this.type;
    }

    public int getSpeedPerSecond() {
        return this.speedPerSecond;
    }

    public void setName(String name) {
        this.name = name;
    }

    enum Type {
        Laser,
        ThreeDimensional,
        LED,
        DotMatrix
    }
}
