package com.example.freshdiet.challenge;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.freshdiet.R;

public class ChallengeSub extends Activity {
// https://lcw126.tistory.com/289

    private TextView alarmtime , waketime, curtv;
    private String curname;
    private TimePicker timePicker;
    private Button start_btn;
    int index=0;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengesub);

        timePicker=findViewById(R.id.tp_timepicker);
        curtv=findViewById(R.id.challengetv);
        start_btn=findViewById(R.id.ch_btn);

        dealIntent();

        start_btn.setOnClickListener(view->{
            ChallengeMain.challenge[index]=true;
            intent.putExtra("idx",index);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void dealIntent(){
        intent=getIntent();
        curname=intent.getStringExtra("curname");
        curtv.setText(curname);
        index=intent.getIntExtra("index",0);
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
