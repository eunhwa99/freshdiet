package com.example.freshdiet.challenge;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.freshdiet.R;
import com.kyleduo.switchbutton.SwitchButton;

import java.lang.reflect.Field;
import java.util.Calendar;

/*
알람: https://1d1cblog.tistory.com/48
 */
public class ChallengeSub extends Activity implements TimePicker.OnTimeChangedListener{


    private TimePicker timePicker;
    private TextView  curtv;
    private EditText editText, prizetext;
    private String curname;
    private Button start_btn, cancel_btn;
    int index=0, nHourDay, nMinute;
    long millis;
    Intent intent;
    SwitchButton switchButton;
    boolean timepickerchecked;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengesub);

        timePicker=findViewById(R.id.tp_timepicker);
        curtv=findViewById(R.id.challengetv);
        editText=findViewById(R.id.duration);
        prizetext=findViewById(R.id.etxtprize);
        start_btn=findViewById(R.id.ch_btn);
        cancel_btn=findViewById(R.id.cancel_btn);
        switchButton = (SwitchButton) findViewById(R.id.sb_use_listener);
        timePicker=findViewById(R.id.tp_timepicker);
        timepickerchecked=false;
        dealIntent();
        registerListener();

        timePicker.setCurrentHour(0);
        timePicker.setCurrentMinute(0);
    }

    private void dealIntent(){
        intent=getIntent();
        curname=intent.getStringExtra("curname");
        curtv.setText(curname);
        index=intent.getIntExtra("index",0);
    }
    private void set_timepicker_text_colour(int color){
        Resources system;
        system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
        int ampm_numberpicker_id = system.getIdentifier("amPm", "id", "android");

        NumberPicker hour_numberpicker = (NumberPicker) timePicker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) timePicker.findViewById(minute_numberpicker_id);
        NumberPicker ampm_numberpicker = (NumberPicker) timePicker.findViewById(ampm_numberpicker_id);

        set_numberpicker_text_colour(hour_numberpicker,color);
        set_numberpicker_text_colour(minute_numberpicker,color);
        set_numberpicker_text_colour(ampm_numberpicker,color);

    }



    private void set_numberpicker_text_colour(NumberPicker number_picker, int color){
        final int count = number_picker.getChildCount();
        //final int color = getResources().getColor(R.color.text);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
            number_picker.setTextColor(color);
        }
        else {
            for (int i = 0; i < count; i++) {
                View child = number_picker.getChildAt(i);

                try {
                    Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                    wheelpaint_field.setAccessible(true);

                    ((Paint) wheelpaint_field.get(number_picker)).setColor(color);
                    ((EditText) child).setTextColor(color);

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerListener(){
        timePicker.setOnTimeChangedListener(this);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    timepickerchecked=true;
                    set_timepicker_text_colour(Color.BLACK);

                    timePicker.setCurrentHour(0);
                    timePicker.setCurrentMinute(0);
                    timePicker.setEnabled(true);
                }else{
                    timepickerchecked=false;

                    set_timepicker_text_colour(Color.LTGRAY);
                    timePicker.setEnabled(false);
                }
            }
        });
        start_btn.setOnClickListener(view->{
            if(editText.getText().toString()==null || editText.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "챌린지 기간을 설정하세요.",Toast.LENGTH_SHORT).show();
            }
            else {
                ChallengeMain.challenge[index] = true;
                intent.putExtra("idx", index);
                intent.putExtra("prize", prizetext.getText().toString());
                intent.putExtra("days", Integer.parseInt(editText.getText().toString()));

                if (timepickerchecked) {
                    intent.putExtra("alarmtime", nHourDay * 3600 * 1000 + nMinute * 60 * 1000);
                    setAlarm();
                }
                Toast.makeText(getApplicationContext(), "챌린지가 시작되었습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        cancel_btn.setOnClickListener(view->{
            setResult(RESULT_CANCELED,intent);
            finish();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAlarm(){
        AlarmHATT alarmHATT = new AlarmHATT(ChallengeSub.this);
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(Calendar.HOUR_OF_DAY,nHourDay);
        alarmCalendar.set(Calendar.MINUTE,nMinute);
        millis=alarmCalendar.getTimeInMillis();
        alarmHATT.Alarm(index, curname, millis); //alarm 설정
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        nHourDay=hourOfDay;
        nMinute=minute;
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,nHourDay);
        calendar.set(Calendar.MINUTE,nMinute);

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
