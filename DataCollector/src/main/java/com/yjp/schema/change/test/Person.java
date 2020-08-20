package com.yjp.schema.change.test;

public class Person implements Human {

    public void wearClothes() {
        // TODO Auto-generated method stub
        System.out.println("穿什么呢。。");
    }

    public void walkToWhere() {
        // TODO Auto-generated method stub
        System.out.println("去哪里呢。。");
    }

    public static void main(String[] args) {
        Human person = new Person();
        Decorator decorator = new Decorator_two(new Decorator_first(new Decorator_zero(person)));
        decorator.walkToWhere();
        decorator.wearClothes();
    }
}
