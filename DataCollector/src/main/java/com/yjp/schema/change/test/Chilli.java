package com.yjp.schema.change.test;

public class Chilli extends Condiment {
    Humburger humburger;

    public Chilli(Humburger humburger) {
        this.humburger = humburger;
    }

    public String getName() {
        return this.humburger.getName() + "加辣椒";
    }

    public double getPrice() {
        return this.humburger.getPrice();
    }
}
