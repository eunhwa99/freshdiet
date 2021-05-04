package com.example.freshdiet.calorie;

public class FoodInfo {
    double kal, carbo, protein, fat, gram;
    String name, unit;

    public FoodInfo(String name, double gram, String unit, double kal, double carbo, double protein, double fat){
        this.name=name;
        this.unit=unit;
        this.kal=kal;
        this.carbo=carbo;
        this.protein=protein;
        this.fat=fat;
    }

    public double getKal(){
        return kal;
    }
    public double getCarbo(){
        return carbo;
    }
    public double getProtein(){
        return protein;
    }
    public double getFat(){
        return fat;
    }
}
