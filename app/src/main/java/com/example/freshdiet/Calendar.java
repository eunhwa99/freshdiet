package com.example.freshdiet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar extends AppCompatActivity {
    public String fname=null;
    public String str=null;
    public CalendarView calendarView;
    public TextView diaryTextView,calendarText, meta_cal, act_cal, eat_cal, rest_cal;
    public Button addbtn;
    private String selectedDay, selectedDay2;
    public static String curDate2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        calendarView=findViewById(R.id.calendarView);
        diaryTextView=findViewById(R.id.diaryTextView);
        addbtn=findViewById(R.id.addbtn);
        calendarText=findViewById(R.id.calendarText);
        meta_cal=findViewById(R.id.cal_meta);
        act_cal=findViewById(R.id.cal_act);
        eat_cal=findViewById(R.id.cal_eat);
        rest_cal=findViewById(R.id.cal_rest);

        setListener(); // button 이벤트 추가
        initScreen(); // 화면 초기화



    }

    public void setListener(){
        addbtn.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MakePlan.class);
                intent.putExtra("selectedDay",selectedDay);
                intent.putExtra("selectedDay2",selectedDay2);
                startActivity(intent);

            }
        });
    }

    public void initScreen() {
        calendarText.setText(MyProfile.username+"님의 달력 일기장");
        SimpleDateFormat format = new SimpleDateFormat( "yyyy / MM / dd");
        SimpleDateFormat format2=new SimpleDateFormat("yyyyMMdd");
        Date time = new Date();
        String curDate = format.format(time);
        curDate2=format2.format(time);
        diaryTextView.setText(curDate);
        selectedDay=curDate;
        selectedDay2=curDate2;
        getData(curDate2);

        // 계획표 및 달성 등의 정보 가지고 오기
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d",year,month+1,dayOfMonth));
                selectedDay=diaryTextView.getText().toString();
            }
        });
    }

    private void getData(String date){
        SharedPreferences sharedPreferences=getSharedPreferences(date+"act", MODE_PRIVATE);
        String str=sharedPreferences.getString("act_calorie","0.0");
        act_cal.setText(str+"(kcal)");
        meta_cal.setText(MyProfile.usermeta+"(kcal)");
        /**
         * 섭취 칼로리, 잔여 칼로리 표시
         */
    }

}
