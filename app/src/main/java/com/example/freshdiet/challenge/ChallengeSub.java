package com.example.freshdiet.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
}
