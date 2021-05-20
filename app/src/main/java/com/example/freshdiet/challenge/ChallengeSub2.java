package com.example.freshdiet.challenge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.freshdiet.R;
import com.kyleduo.switchbutton.SwitchButton;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class ChallengeSub2 extends Activity implements TimePicker.OnTimeChangedListener{

    Intent intent;
    int alarmTime;
    String curname, prizestr;
    int idx, challengeDays;
    int nHourDay, nMinute;
    long millis=-1, hours, minutes;

    private TextView curtv, alarmtv, giveuptv;
    private Button start_btn, cancel_btn;
    private EditText prize, durtv;
    private TimePicker timePicker;
    private boolean timepickervisible=false, clicked=false;
    private LinearLayout timepickerlayout,layout;
    SwitchButton switchButton;

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
        switchButton = (SwitchButton) findViewById(R.id.sb_use_listener2);

        dealIntent();
        setTextView(); // 가지고 온 정보 텍스뷰에 뿌리기
        setClickListener(); // 버튼 리스너 지정
    }


    private void dealIntent(){
        intent=getIntent();
        idx=intent.getIntExtra("index",-1);
        curname=intent.getStringExtra("curname");
        alarmTime=intent.getIntExtra("alarmtime",-1); // 설정한 알람시간 가지고 오기
        if(alarmTime!=-1) switchButton.setChecked(true);

        prizestr=intent.getStringExtra("prize");
        challengeDays=intent.getIntExtra("days",-1);
    }

    private void set_timepicker_text_colour(){
        Resources system;
        system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
        int ampm_numberpicker_id = system.getIdentifier("amPm", "id", "android");

        NumberPicker hour_numberpicker = (NumberPicker) timePicker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) timePicker.findViewById(minute_numberpicker_id);
        NumberPicker ampm_numberpicker = (NumberPicker) timePicker.findViewById(ampm_numberpicker_id);

        set_numberpicker_text_colour(hour_numberpicker);
        set_numberpicker_text_colour(minute_numberpicker);
        set_numberpicker_text_colour(ampm_numberpicker);
    }

    private void set_numberpicker_text_colour(NumberPicker number_picker){
        final int count = number_picker.getChildCount();
        //final int color = getResources().getColor(R.color.text);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
            number_picker.setTextColor(Color.BLACK);
        }
        else {
            for (int i = 0; i < count; i++) {
                View child = number_picker.getChildAt(i);

                try {
                    Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                    wheelpaint_field.setAccessible(true);

                    ((Paint) wheelpaint_field.get(number_picker)).setColor(Color.BLACK);
                    ((EditText) child).setTextColor(Color.BLACK);

                    number_picker.invalidate();
                } catch (NoSuchFieldException e) {
                    //    Log.w("setNumberPickerTextColor", e);

                } catch (IllegalAccessException e) {
                    //   Log.w("setNumberPickerTextColor", e);
                } catch (IllegalArgumentException e) {
                    //  Log.w("setNumberPickerTextColor", e);
                }
            }
        }
    }

    private void setTextView(){
        curtv.setText(curname);
        if(alarmTime!=-1) {
            hours = TimeUnit.MILLISECONDS.toHours(alarmTime);
            minutes = TimeUnit.MILLISECONDS.toMinutes(alarmTime)%60;
            alarmtv.setText(hours+"시 "+minutes+"분 ");
        }
        if(challengeDays!=-1){
            durtv.setText(String.valueOf(challengeDays));
        }

        prize.setText(prizestr);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setClickListener(){
        timePicker.setOnTimeChangedListener(this);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){

                    set_timepicker_text_colour();

                    timePicker.setCurrentHour((int)hours);
                    timePicker.setCurrentMinute((int)minutes);
                    timePicker.setEnabled(true);
                    timepickerlayout.setVisibility(View.VISIBLE);
                }else{
                    timePicker.setVisibility(View.GONE);
                    timePicker.setEnabled(false);
                }
            }
        });

        start_btn.setOnClickListener(view->{ // 수정버튼
            AlarmHATT alarmHATT = new AlarmHATT(ChallengeSub2.this);
            if(durtv.getText().toString()==null|| durtv.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "챌린지 기간을 설정하세요.",Toast.LENGTH_SHORT).show();
            }
            else {
                intent.putExtra("idx", idx);
                intent.putExtra("prize", prize.getText().toString());
                intent.putExtra("days", Integer.parseInt(durtv.getText().toString()));

                if(switchButton.isChecked()){ // 알람 설정됨
                    if (clicked) // 알람 시간 수정되었다면
                    {
                        intent.putExtra("alarmtime", nHourDay * 3600 * 1000 + nMinute * 60 * 1000); // 새로운 알람 시간 저장
                        alarmHATT.noAlarm(idx); // 기존 알람 삭제

                        setAlarm();
                        Toast.makeText(getApplicationContext(), "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else //아니면 그대로 저장
                        intent.putExtra("alarmtime", alarmTime);
                }

                else {
                    if (alarmtv.getText() != " ") { // 그 전에 알람 설정한 건데 취소한 경우
                        alarmHATT.noAlarm(idx); // 기존 알람 삭제
                        Toast.makeText(getApplicationContext(), "알람이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    intent.putExtra("alarmtime",-1);
                }


                setResult(RESULT_OK, intent);
                finish();
            }
        });
        cancel_btn.setOnClickListener(view->{
            setResult(RESULT_CANCELED,intent);
            finish();
        });

        //알람 시간
        alarmtv.setOnClickListener(view->{
            FrameLayout.LayoutParams params;
            if(switchButton.isChecked()) {
                timepickervisible=true;
                timepickerlayout.setVisibility(View.VISIBLE);
                set_timepicker_text_colour();
                if(alarmTime!=-1){
                    timePicker.setCurrentHour((int) hours);
                    timePicker.setCurrentMinute((int)minutes);
                }

            }
            else{
                timepickervisible=false;
                timepickerlayout.setVisibility(View.GONE);

                millis=-1;
            }

            int width=0;
            width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
            params=new FrameLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(params);
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

                    intent.putExtra("idx",idx);

                    Toast.makeText(getApplicationContext(), "포기하였습니다.", Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAlarm(){
        AlarmHATT alarmHATT = new AlarmHATT(getApplicationContext());

        alarmHATT.Alarm(idx, curname, nHourDay, nMinute,0);
     //   alarmHATT.Alarm(idx, curname, alarmCalendar.getTimeInMillis()); //alarm 설정
    }


    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        nHourDay=hourOfDay;
        nMinute=minute;
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR,nHourDay);
        calendar.set(Calendar.MINUTE,nMinute);

        clicked=true;
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
