package com.example.freshdiet.plan;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.freshdiet.MainActivity;
import com.example.freshdiet.R;
import com.example.freshdiet.calorie.ShowFoodList;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Calendar extends Fragment {
    public CalendarView calendarView;
    public TextView diaryTextView,calendarText, meta_cal, act_cal, eat_cal, rest_cal;
    public Button addbtn, foodinfobtn;
    private String selectedDay;

    public String curDate2;
    public static String selectedDay2;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    MainActivity mainActivity;
    Fragment curFragment;

    private String username, userage, userheight, userweight, usermeta;
    private String usergender; //성별


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
        foodinfobtn=rootView.findViewById(R.id.foodinfobtn);

        context=getActivity();
        curFragment=getActivity().getSupportFragmentManager().findFragmentById(R.id.container);

        getData();
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
                MainActivity.fragmentStack.push(curFragment);

                FragmentTransaction transaction = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("selectedDay",selectedDay);
                bundle.putString("selectedDay2",selectedDay2);
                MakePlan makeplan=new MakePlan();
                makeplan.setArguments(bundle);
                transaction.replace(R.id.container, makeplan);
                transaction.commit();
            }
        });

        foodinfobtn.setOnClickListener(view->{
            MainActivity.fragmentStack.push(curFragment);
            FragmentTransaction transaction = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
            Bundle bundle=new Bundle();
            ShowFoodList showFoodList=new ShowFoodList();
            showFoodList.setArguments(bundle);
            transaction.replace(R.id.container, showFoodList);
            transaction.commit();
        });


    }

    public void initScreen() {
        calendarText.setText(username+"님의 달력");
        SimpleDateFormat format = new SimpleDateFormat( "yyyy / M / d");
        SimpleDateFormat format2=new SimpleDateFormat("yyyyMd");
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
                selectedDay2=String.valueOf(year)+String.valueOf(month+1)+String.valueOf(dayOfMonth);
                getData(selectedDay2);
            }
        });
    }

    private void getData(String date){


        SharedPreferences sharedPreferences=context.getSharedPreferences(date+"act", MODE_PRIVATE);
        String str=sharedPreferences.getString("act_calorie","0.0");
        double tmp=Double.parseDouble(str);
        act_cal.setText(str+"(kcal)");
        meta_cal.setText(usermeta+"(kcal)");
        str=sharedPreferences.getString("eat_calorie","0.0");
        double tmp2= Double.parseDouble(str);
        eat_cal.setText(str+"(kcal)");
        rest_cal.setText(String.valueOf(tmp2-tmp)+"(kcal)");

    }

    private void getData(){
        SharedPreferences sharedPreferences=context.getSharedPreferences("Profile",MODE_PRIVATE);
        username=sharedPreferences.getString("UserName","Unknown");
        userage = sharedPreferences.getString("UserAge", "Unknown");
        userheight = sharedPreferences.getString( "UserHeight", "Unknown");
        userweight = sharedPreferences.getString("UserWeight","Unknown");
        usermeta = sharedPreferences.getString("UserMeta","Unknown");
        usergender=sharedPreferences.getString("UserGender","Unknown");
    }

}
