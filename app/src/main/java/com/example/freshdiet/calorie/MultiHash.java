package com.example.freshdiet.calorie;

import java.io.Serializable;

public class MultiHash implements Serializable {
    double val1, val3, val4, val5, val6;
    String val2; // 단위

    MultiHash(double a, String b, double c, double d, double e, double f){
        this.val1=a;
        this.val2=b;
        this.val3=c;
        this.val4=d;
        this.val5=e;
        this.val6=f;
    }
}
