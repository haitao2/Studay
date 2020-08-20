package com.yjp.schema.change.test;

public class ChickenBurger extends Humburger{

    public double getPrice() {
        return 10;
    }

    @Override
    public String getName() {
        return "鸡腿汉堡";
    }

}
