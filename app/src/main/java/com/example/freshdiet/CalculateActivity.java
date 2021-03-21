package com.example.freshdiet;

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
    private int val, time;
    private HashMap<String, Double> myMap=new HashMap<>();
    private HashMap<String, Double> myMap2=new HashMap<>();
    private ArrayList<String> arrayList;
    private ArrayList<String> exerciseArray
            =new ArrayList<>(Arrays.asList("걷기","달리기","등산",
            "자전거","근력운동","싸이클","볼링","배드민턴", "배구", "축구","농구",
            "테니스","탁구","유도","주짓수", "킥복싱", "스트레칭", "체조",
            "골프", "야구", "스케이트", "스키", "태권도", "럭비", "에어로빅",
            "수영"));
    private ArrayList<String> hobbyArray
            =new ArrayList<>(Arrays.asList("독서","영화/드라마/유튜브 보기",
            "게임", "요리", "드라이브", "미술", "춤", "노래"));
    private ArrayList<String> otherArray;

    public CalculateActivity(double weight, String detail, int time, HashMap<String, Double> map1,HashMap<String, Double> map2){
        this.weight=weight;
        this.time=time;
        this.detail=detail;
        myMap=map1;
        myMap2=map2;
    }


    private void searchData(){
        if(myMap.containsKey(detail)){
            mets=myMap.get(detail);
            calorie=calculate();
        }
        else{
            if(myMap2.containsKey(detail)){
                calorie=myMap2.get(detail);
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
