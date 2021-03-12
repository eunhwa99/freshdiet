package com.example.freshdiet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Calendar extends AppCompatActivity {
    public String fname=null;
    public String str=null;
    public CalendarView calendarView;
    public TextView diaryTextView,calendarText;
    public ScrollView scrollText;
    public Button addbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        calendarView=findViewById(R.id.calendarView);
        diaryTextView=findViewById(R.id.diaryTextView);
        addbtn=findViewById(R.id.addbtn);
        calendarText=findViewById(R.id.calendarText);

        setListener(); // button 이벤트 추가

        //로그인 및 회원가입 엑티비티에서 이름을 받아옴
        Intent intent=getIntent();
        String name=intent.getStringExtra("userName");
        final String userID=intent.getStringExtra("userID");
        calendarText.setText(name+"님의 달력 일기장");

        // 계획표 및 달성 등의 정보 가지고 오기
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);

                diaryTextView.setText(String.format("%d / %d / %d",year,month+1,dayOfMonth));

                checkDay(year,month,dayOfMonth,userID);
            }
        });

    }

    public void setListener(){
        addbtn.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Piechart.class);
                startActivity(intent);

            }
        });
    }



    public void  checkDay(int cYear,int cMonth,int cDay,String userID){
        fname=""+userID+cYear+"-"+(cMonth+1)+""+"-"+cDay+".txt";//저장할 파일 이름설정
        FileInputStream fis=null;//FileStream fis 변수

        try{
            fis=openFileInput(fname);

            byte[] fileData=new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str=new String(fileData);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content="";
            fos.write((content).getBytes());
            fos.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
          //  String content=contextEditText.getText().toString();
           // fos.write((content).getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
