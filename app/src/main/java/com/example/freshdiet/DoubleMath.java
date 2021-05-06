package com.example.freshdiet;

public class DoubleMath {

    public double calc(double num){
        if(num<=0.0) return 0.0;
        return Math.round(num*100)/100.0;
    }


}
