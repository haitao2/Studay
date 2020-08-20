package com.yjp.schema.change.test;

public class Lettuce extends Condiment {
    Humburger humburger;

    public Lettuce(Humburger humburger) {
        this.humburger = humburger;
    }

    public String getName() {
        return humburger.getName() + "生菜";
    }

    public double getPrice() {
        return humburger.getPrice() + 1.5;
    }
}
