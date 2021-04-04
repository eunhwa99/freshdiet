package com.example.freshdiet.challenge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.freshdiet.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ChallengeSub2 extends Activity implements TimePicker.OnTimeChangedListener{

    Intent intent;
    Long alarmTime;
    String curname, prizestr;
    int idx;
    int nHourDay, nMinute;
    long millis=-1;

    private TextView curtv, durtv, alarmtv, giveuptv;
    private Button start_btn, cancel_btn;
    private EditText prize;
    private TimePicker timePicker;
    private boolean timepickervisible=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengesub2);

        curtv=findViewById(R.id.challengetv); //챌린지 이름
        durtv=findViewById(R.id.duration); //챌린지 기간
        alarmtv=findViewById(R.id.alarmtv); // 알람 시간
        giveuptv=findViewById(R.id.giveuptv);
        prize=findViewById(R.id.etxtprize); // 보상
        start_btn=findViewById(R.id.ch_btn);
        cancel_btn=findViewById(R.id.cancel_btn);
        timePicker=findViewById(R.id.tp_timepicker);

        dealIntent();
        setTextView(); // 가지고 온 정보 텍스뷰에 뿌리기
        setClickListener(); // 버튼 리스너 지정
    }


    private void dealIntent(){
        intent=getIntent();
        idx=intent.getIntExtra("index",-1);
        curname=intent.getStringExtra("curname");
        alarmTime=intent.getLongExtra("alarmtime",-1); // 설정한 알람시간 가지고 오기
        prizestr=intent.getStringExtra("prize");
    }

    @SuppressLint("SetTextI18n")
    private void setTextView(){
        curtv.setText(curname);
        if(alarmTime!=-1) {
            long hours = TimeUnit.MILLISECONDS.toHours(alarmTime);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(alarmTime);
            alarmtv.setText(hours+"시 "+minutes+"분 ");
        }
        prize.setText(prizestr);
    }

    private void setClickListener(){
        start_btn.setOnClickListener(view->{
            intent.putExtra("idx",idx);
            intent.putExtra("prize", prize.getText().toString());
            intent.putExtra("alarmtime",millis);

            setResult(RESULT_OK,intent);
            finish();
        });
        cancel_btn.setOnClickListener(view->{
            setResult(RESULT_CANCELED,intent);
            finish();
        });

        //알람 시간
        alarmtv.setOnClickListener(view->{
            if(!timepickervisible) {
                timePicker.setVisibility(View.VISIBLE);
                giveuptv.setVisibility(View.INVISIBLE);
            }
            else{
                timePicker.setVisibility(View.INVISIBLE);
                giveuptv.setVisibility(View.VISIBLE);
            }

        });
        // 포기 버튼
        giveuptv.setOnClickListener(view->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("포기선언!").setMessage("정말 포기할래요?");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    ChallengeMain.challenge[idx]=false;
                    Toast.makeText(getApplicationContext(), "처리되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });
    }


    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        nHourDay=hourOfDay;
        nMinute=minute;
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR,nHourDay);
        calendar.set(Calendar.MINUTE,nMinute);

        millis= calendar.getTimeInMillis();
    }
}
