package com.example.freshdiet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn1,btn2,btn3,btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1=findViewById(R.id.profilebtn);
        btn2=findViewById(R.id.calendarbtn);
        btn3=findViewById(R.id.caloriebtn);
        btn4=findViewById(R.id.challengbtn);

    }

    public void gotoMenu(View v){
        Intent intent;
        switch(v.getId()){
            case R.id.profilebtn:
                //https://asukim.tistory.com/59
                intent=new Intent(getApplicationContext(), MyProfile.class);
                startActivity(intent);
                break;
            case R.id.calendarbtn:
                intent=new Intent(getApplicationContext(), Calendar.class);
                startActivity(intent);
                break;
            case R.id.caloriebtn:
                break;
            case R.id.challengbtn:
                break;

        }
    }
}