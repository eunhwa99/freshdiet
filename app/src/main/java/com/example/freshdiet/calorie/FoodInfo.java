package com.example.freshdiet.calorie;

import java.io.Serializable;

public class FoodInfo implements Serializable {
    double kal, carbo, protein, fat, gram;
    String name, unit;

    public FoodInfo(String name, double gram, String unit, double kal, double carbo, double protein, double fat){
        this.name=name;
        this.gram=gram;
        this.unit=unit;
        this.kal=kal;
        this.carbo=carbo;
        this.protein=protein;
        this.fat=fat;
    }

}
