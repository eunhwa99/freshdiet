package com.example.freshdiet.challenge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.freshdiet.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * 1. 레이아웃 크기 동적 변환
 * 2. 알람 상자 숫자 크기 줄이기
 * 3. timepicker 글자 검정색으로 바꾸기
 */

public class ChallengeSub2 extends Activity implements TimePicker.OnTimeChangedListener{

    Intent intent;
    int alarmTime;
    String curname, prizestr;
    int idx;
    int nHourDay, nMinute;
    long millis=-1;

    private TextView curtv, durtv, alarmtv, giveuptv;
    private Button start_btn, cancel_btn;
    private EditText prize;
    private TimePicker timePicker;
    private boolean timepickervisible=false, clicked=false;
    private LinearLayout timepickerlayout,layout;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengesub2);

        layout=findViewById(R.id.sub2layout);
        timepickerlayout=findViewById(R.id.timepickerlayout);
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
        alarmTime=intent.getIntExtra("alarmtime",-1); // 설정한 알람시간 가지고 오기
        prizestr=intent.getStringExtra("prize");
    }

    @SuppressLint("SetTextI18n")
    private void setTextView(){
        curtv.setText(curname);
        if(alarmTime!=-1) {
            long hours = TimeUnit.MILLISECONDS.toHours(alarmTime);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(alarmTime)%60;
            alarmtv.setText(hours+"시 "+minutes+"분 ");
        }

        prize.setText(prizestr);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setClickListener(){
        timePicker.setOnTimeChangedListener(this);
        start_btn.setOnClickListener(view->{
            intent.putExtra("idx",idx);
            intent.putExtra("prize", prize.getText().toString());
            if(clicked)
                intent.putExtra("alarmtime",nHourDay*3600*1000+nMinute*60*1000);
            else
                intent.putExtra("alarmtime",alarmTime);

            if(millis!=-1){
                AlarmHATT alarmHATT = new AlarmHATT(ChallengeSub2.this);
                alarmHATT.noAlarm(idx); //
                alarmHATT.Alarm(idx, curname, millis);
            }

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
                timepickervisible=true;
                timepickerlayout.setVisibility(View.VISIBLE);
                giveuptv.setVisibility(View.INVISIBLE);
            }
            else{
                timepickervisible=false;
                timepickerlayout.setVisibility(View.INVISIBLE);
                giveuptv.setVisibility(View.VISIBLE);
                millis=-1;
            }
          //  ViewGroup.MarginLayoutParams params=new ViewGroup.MarginLayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);
          //  layout.setLayoutParams(params);
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

        clicked=true;
        Toast.makeText(getApplicationContext(),nHourDay+"시"+nMinute+"분",Toast.LENGTH_SHORT).show();
        millis= calendar.getTimeInMillis();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}
