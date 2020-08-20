package com.yjp.schema.change.test;

public class Decorator_two extends Decorator {
    public Decorator_two(Human human) {
        super(human);
    }

    public void findClothes() {
        System.out.println("进房子。。");
    }

    public void findTheTarget() {
        System.out.println("书房找找Map。。");
    }

    @Override
    public void walkToWhere() {
        super.walkToWhere();
        findTheTarget();
    }

    @Override
    public void wearClothes() {
        super.wearClothes();
        findClothes();
    }
}
