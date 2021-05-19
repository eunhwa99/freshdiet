package com.example.freshdiet.calorie;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.freshdiet.R;

public class Toppings extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup2);


    }
}
