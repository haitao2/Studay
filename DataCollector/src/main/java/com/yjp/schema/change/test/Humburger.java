package com.yjp.schema.change.test;

public abstract class Humburger {
    private String name;
    public String getName(){
        return this.name;
    }
    public abstract double getPrice();
}
