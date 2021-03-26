package com.example.freshdiet.plan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.freshdiet.MainActivity;
import com.example.freshdiet.R;
import com.example.freshdiet.plan.MakePlan;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar extends Fragment {
    public String fname=null;
    public String str=null;
    public CalendarView calendarView;
    public TextView diaryTextView,calendarText, meta_cal, act_cal, eat_cal, rest_cal;
    public Button addbtn;
    private String selectedDay, selectedDay2;
    public static String curDate2;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.calendar, container, false);

        //UI
        calendarView=rootView.findViewById(R.id.calendarView);
        diaryTextView=rootView.findViewById(R.id.diaryTextView);
        addbtn=rootView.findViewById(R.id.addbtn);
        calendarText=rootView.findViewById(R.id.calendarText);
        meta_cal=rootView.findViewById(R.id.cal_meta);
        act_cal=rootView.findViewById(R.id.cal_act);
        eat_cal=rootView.findViewById(R.id.cal_eat);
        rest_cal=rootView.findViewById(R.id.cal_rest);

        setListener(); // button 이벤트 추가
        initScreen(); // 화면 초기화



        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    public void setListener(){
        addbtn.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("selectedDay",selectedDay);
                bundle.putString("selectedDay2",selectedDay2);
                MakePlan makeplan=new MakePlan();
                makeplan.setArguments(bundle);
                transaction.replace(R.id.container, makeplan);
                transaction.commit();
            }
        });
    }

    public void initScreen() {
        calendarText.setText(MainActivity.username+"님의 달력 일기장");
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
        Context context=getActivity();
        SharedPreferences sharedPreferences=context.getSharedPreferences(date+"act", Context.MODE_PRIVATE);
        String str=sharedPreferences.getString("act_calorie","0.0");
        act_cal.setText(str+"(kcal)");
        meta_cal.setText(MainActivity.usermeta+"(kcal)");
        /**
         * 섭취 칼로리, 잔여 칼로리 표시
         */
    }

}
