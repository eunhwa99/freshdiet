package com.example.freshdiet.challenge;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.freshdiet.ListViewAdapter;
import com.example.freshdiet.R;

import java.util.ArrayList;

public class ChallengeSub extends AppCompatActivity {

    private ImageView calendariv;
    private TextView datetv, sleeptime, waketime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengesub);

        calendariv=findViewById(R.id.calendariv);
        datetv=findViewById(R.id.datetv);
        sleeptime=findViewById(R.id.sleepTime);
        waketime=findViewById(R.id.wakeTime);

    }

    public void calendarclick(View v){ //달력 사진 눌렀을 때
        if(v.getId()==R.id.calendariv) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string+"년 "+month_string + "월 " + day_string + "일 ");

        datetv.setText(dateMessage);
    }

    public void timepicker(View v){
        TimePickerDialog timePicker;
        TextView tv_tmp = (TextView) v;
        String[] time_tmp = tv_tmp.getText().toString().split(":");

        if(time_tmp[0].equals("--")){
            time_tmp[0] = time_tmp[1] = "0";
        }

        timePicker = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String setTime, hour, min;
                if(minute>=0&&minute<10) min="0"+minute;
                else min=String.valueOf(minute);

                if(hourOfDay>=0&&hourOfDay<10) hour="0"+hourOfDay;
                else hour=String.valueOf(hourOfDay);

                setTime=hour+":"+min;
                TextView tv = (TextView) v;
                tv.setText(setTime);

            }
        }, Integer.parseInt(time_tmp[0]), Integer.parseInt(time_tmp[1]), false);
        timePicker.setButton(TimePickerDialog.BUTTON_NEGATIVE, "삭제", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView tv = (TextView) v;
                tv.setText("--:--");
            }
        });

        timePicker.show();

    }
}
