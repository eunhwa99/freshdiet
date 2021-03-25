package com.example.freshdiet.plan;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CalculateActivity {
    private double weight ,mets, calorie;
    private String detail;
    private int time;
    private HashMap<String, Double> myMap=new HashMap<>();
    private HashMap<String, Double> myMap2=new HashMap<>();

    public CalculateActivity(double weight, String detail, int time, HashMap<String, Double> map1,HashMap<String, Double> map2){
        this.weight=weight;
        this.time=time;
        this.detail=detail;
        myMap=map1;
        myMap2=map2;
    }

    public CalculateActivity(double weight, int time, double mets){
        this.weight=weight;
        this.time=time;
        this.mets=mets;
    }


    private void searchData(){
        if(detail==null||detail.equals("")){
            calorie=calculate();
        }
        else { //운동, 취미, 기타
            if (myMap.containsKey(detail)) {
                mets = myMap.get(detail);
                calorie = calculate();
            } else {
                if (myMap2.containsKey(detail)) {
                    double temp = time / 10.0; //10분 단위 --> 나누기
                    calorie = myMap2.get(detail) * temp;
                }
            }
        }
    }

    private double calculate(){

        return mets*(3.5*weight*time)*5/1000.0;
    }

    public double getCalorie(){
        searchData();
        return calorie;
    }



}
