package com.yjp.schema.change.test;

public class Decorator_first extends Decorator {
    public Decorator_first(Human human) {
        super(human);
    }

    public void goClothespress() {
        System.out.println("进房子。。");
    }

    public void findPlaceOnMap() {
        System.out.println("书房找找Map。。");
    }

    @Override
    public void walkToWhere() {
        super.walkToWhere();
        findPlaceOnMap();
    }

    @Override
    public void wearClothes() {
        super.wearClothes();
        goClothespress();
    }
}
