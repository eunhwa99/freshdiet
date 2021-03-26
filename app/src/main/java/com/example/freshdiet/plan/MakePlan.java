package com.example.freshdiet.plan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

//import com.larswerkman.holocolorpicker.ColorPicker;
import com.example.freshdiet.ListViewAdapter;
import com.example.freshdiet.MainActivity;
import com.example.freshdiet.Popup;
import com.example.freshdiet.PreferenceManager;
import com.example.freshdiet.R;

import petrov.kristiyan.colorpicker.ColorPicker;

import java.util.ArrayList;
import java.util.Arrays;

public class MakePlan extends Fragment {
    private TextView startTime;
    private TextView endTime;
    private TextView calorietxt;
    private ListView listView;
    private ListViewAdapter adapter;
    private String curname, memotext, detail;
    private String curDate;
    private String curDate2; //현재 날짜

    ArrayList<String> listArray=new ArrayList<>(Arrays.asList("공부","운동","취미/여가","식사","숙면","기타"));
    public static ArrayList<String> timeArray=new ArrayList<>();
    public static ArrayList<String> timeArray2=new ArrayList<>();
    public static Context Mcontext;

    public int starthour,startmin,endhour, endmin;

    public static int time;

    int color=Color.WHITE;
    private final int COLOR_ACTIVITY = 1, POPUP_ACTIVITY=2, NOPOPUP_ACTIVITY=3;

    public static boolean[] checkArray=new boolean[24*60+1];

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.makeplan, container, false);
        startTime=rootView.findViewById(R.id.startTime);
        endTime=rootView.findViewById(R.id.endTime);
        calorietxt=rootView.findViewById(R.id.calorietxt);
        listView=rootView.findViewById(R.id.listview1);


        Mcontext=getContext();

        Bundle bundle = getArguments();
        if(bundle != null){
            curDate=bundle.getString("selectedDay");
            curDate2=bundle.getString("selectedDay2");
        }

        String str;
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(curDate2+"act",Context.MODE_PRIVATE);
        str=sharedPreferences.getString("act_calorie", "0.0");

        calorietxt.setText(str);

        initListView();
        getData(); // preference에 저장된 데이터 가지고 오기
        setListener();

        return rootView;
    }

   /* @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makeplan);

       // layout=findViewById(R.id.makeplanlayout);
        startTime=(TextView)findViewById(R.id.startTime);
        endTime=(TextView)findViewById(R.id.endTime);
        calorietxt=findViewById(R.id.calorietxt);


        Mcontext=MakePlan.this;

        Intent intent=getIntent();
        curDate=intent.getStringExtra("selectedDay");

        curDate2=intent.getStringExtra("selectedDay2");
        String str;
        SharedPreferences sharedPreferences=getSharedPreferences(curDate2+"act",MODE_PRIVATE);
        str=sharedPreferences.getString("act_calorie", "0.0");

        calorietxt.setText(str);

        initListView();
        getData(); // preference에 저장된 데이터 가지고 오기
    }*/



    private void initListView(){
        adapter=new ListViewAdapter(listArray);

        // 리스트뷰 참조 및 Adpater 달기
        listView.setAdapter(adapter);

        // listview에 클릭 이벤트 핸들러 정의
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 curname=(String)adapterView.getItemAtPosition(i);
                Intent intent;
                if(startTime.getText().toString().equals("--:--")){
                    Toast.makeText(getContext(),"시작 시간을 입력하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(endTime.getText().toString().equals("--:--")){
                    Toast.makeText(getContext(),"종료 시간을 입력하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(alreadyUsed()){
                    Toast.makeText(getContext(), "시간이 중복됩니다. 다시 설정해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                switch(curname){
                    case "공부": case "식사": case "숙면":
                    //    intent=new Intent(getContext(), Popup.class);
                    //    startActivityForResult(intent,POPUP_ACTIVITY);
                        getParentFragmentManager().setFragmentResultListener("requestKey", getViewLifecycleOwner(), new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                                // We use a String here, but any type that can be put in a Bundle is supported
                                String result = bundle.getString("bundleKey");
                                // Do something with the result
                            }
                        });

                        break;
                    case "운동": case "취미/여가": case "기타":
                     //   intent=new Intent(getApplicationContext(), TodoList.class);
                     //   intent.putExtra("todo", curname);
                     //   startActivityForResult(intent,NOPOPUP_ACTIVITY);

                        break;

                }
            }
        });


        ArrayList<String> templist= PreferenceManager.getArrayList(getContext(),"activity_list");
        if(templist.size()==0){
            PreferenceManager.setArrayList(getContext(), "activity_list",listArray);
        }
        else listArray=templist;

        for(int i=0;i<listArray.size();i++){
            String name=listArray.get(i);
            adapter.addItem(name);
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case POPUP_ACTIVITY: // 공부, 숙면
                if (resultCode == RESULT_OK) {
                    memotext=data.getStringExtra("EditText"); // 메모 내용 가지고 오기
                    // 계획표 추가
                    openColorPicker(0);

                    double cal=0.0, mets=0.0;
                    if(curname.equals("숙면")){
                        mets=0.9;
                    }
                    else if(curname.equals("공부")){
                        mets=1.8;
                    }
                    CalculateActivity calculateActivity=new CalculateActivity(Double.parseDouble(MainActivity.userweight), time, mets);
                   cal=calculateActivity.getCalorie();
                    cal=Math.round(cal*100)/100.0;
                   updateCalorie(cal);
                }
                break;
            case NOPOPUP_ACTIVITY:
                if (resultCode == RESULT_OK) { //운동, 취미
                   memotext=data.getStringExtra("EditText");
                   detail=data.getStringExtra("ToDo"); // detail 아무것도 없으면 표시 X
                    // 계획표 추가
                    openColorPicker(1);

                    double cal=0.0;
                    cal=data.getDoubleExtra("calorie",0.0);
                    updateCalorie(cal);

                }
                break;
        }
    }*/

    public void addSchedule(String memo, String nopop, int what){


        if(starthour>endhour){
            if(starthour>=12&&starthour<=23&&endhour>=12&&endhour<=23 || starthour>=0&&starthour<=12&&endhour>=0&&endhour<=12){
                Toast.makeText(getContext(), "시작 시간과 끝 시간이 잘못되었습니다. 다시 설정해주세요.", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else if(starthour==endhour){
            if(startmin>endmin){
                Toast.makeText(getContext(), "시작 시간과 끝 시간이 잘못되었습니다. 다시 설정해주세요.", Toast.LENGTH_LONG).show();
                return;
            }
        }


            if (what == 0) {
                timeArray.add(starthour + ":" + startmin + ":" + endhour + ":" + endmin + ":" + curname + ":" + memo + ":" + color);
            } else if (what == 1) {
                timeArray2.add(starthour + ":" + startmin + ":" + endhour + ":" + endmin + ":" + curname + ":" + nopop + ":" + memo + ":" + color);
            }

            //시간 중복 안되게 체크하기
            checkUsedTime();
            saveData();
    }

    private void setListener(){
        startTime.setOnClickListener(view -> {
            pickTime(startTime);
        });
        endTime.setOnClickListener(view->{
            pickTime(endTime);
        });
    }

    private Boolean alreadyUsed(){
        String[] starttime= startTime.getText().toString().split(":");
        String[] endtime=endTime.getText().toString().split(":");

        int[] start=new int[2];
        int[] end=new int[2];

        for(int i=0;i<2;i++){
            switch(starttime[i]){
                case "00":
                    start[i]=0;
                    break;
                case "01":
                    start[i]=1;
                    break;
                case "02":
                    start[i]=2;
                    break;
                case "03":
                    start[i]=3;
                    break;
                case "04":
                    start[i]=4;
                    break;
                case "05":
                    start[i]=5;
                    break;
                case "06":
                    start[i]=6;
                    break;
                case "07":
                    start[i]=7;
                    break;
                case "08":
                    start[i]=8;
                    break;
                case "09":
                    start[i]=9;
                    break;
                default:
                    start[i]=Integer.parseInt(starttime[i]);
                    break;
            }
            switch(endtime[i]){
                case "00":
                    end[i]=0;
                    break;
                case "01":
                    end[i]=1;
                    break;
                case "02":
                    end[i]=2;
                    break;
                case "03":
                    end[i]=3;
                    break;
                case "04":
                    end[i]=4;
                    break;
                case "05":
                    end[i]=5;
                    break;
                case "06":
                    end[i]=6;
                    break;
                case "07":
                    end[i]=7;
                    break;
                case "08":
                    end[i]=8;
                    break;
                case "09":
                    end[i]=9;
                    break;
                default:
                    end[i]=Integer.parseInt(endtime[i]);
                    break;
            }

        }
        starthour=start[0]; startmin=start[1];
        endhour=end[0]; endmin=end[1];

        if(endhour<starthour){
            for(int i=starthour*60+startmin + 1;i<24*60;i++)
                if(checkArray[i])
                    return true;
            for(int i=0;i<endhour*60+endmin;i++)
                if(checkArray[i])
                    return true;
            time=(endhour+12)*60+endmin-starthour*60-startmin;
        }
        else {
            for (int i = starthour*60 + startmin+1; i < endhour*60+endmin; i++) {
                if(checkArray[i])
                    return true;
            }
            time=endhour*60+endmin-starthour*60-startmin;
        }
        return false;
    }

    public void checkUsedTime(){
        if(endhour<starthour){
            for(int i=starthour*60+startmin;i<24*60;i++)
                checkArray[i]=true;
            for(int i=0;i<endhour*60+endmin;i++)
                checkArray[i]=true;

        }
        else {
            for (int i = starthour*60 + startmin; i < endhour*60+endmin+1; i++) {
                checkArray[i]=true;
            }

        }
    }


    public void pickTime(View v){
        TimePickerDialog timePicker;
        TextView tv_tmp = (TextView) v;
        String[] time_tmp = tv_tmp.getText().toString().split(":");

        if(time_tmp[0].equals("--")){
            time_tmp[0] = time_tmp[1] = "0";
        }

        timePicker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener(){
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

    public void getData(){
        timeArray=PreferenceManager.getArrayList(getContext(),curDate);
        timeArray2=PreferenceManager.getArrayList(getContext(), curDate+"_2");
        boolean[] temp=PreferenceManager.getBooleanArray(getContext(), curDate+"check");
        if(temp.length!=0){
            checkArray=temp;
        }

    }
    public void saveData(){
        PreferenceManager.setArrayList(getContext(), curDate, timeArray);
        PreferenceManager.setArrayList(getContext(), curDate+"_2", timeArray2);
        PreferenceManager.setBooleanArray(getContext(), curDate+"check",checkArray);
    }

    private void openColorPicker(int what){
        final ColorPicker colorPicker = new ColorPicker((Activity) getContext());  // ColorPicker 객체 생성
        ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list

        colors.add("#ffab91");
        colors.add("#F48FB1");
        colors.add("#ce93d8");
        colors.add("#b39ddb");
        colors.add("#9fa8da");
        colors.add("#90caf9");
        colors.add("#81d4fa");
        colors.add("#80deea");
        colors.add("#80cbc4");
        colors.add("#c5e1a5");
        colors.add("#e6ee9c");
        colors.add("#fff59d");
        colors.add("#ffe082");
        colors.add("#ffcc80");
        colors.add("#bcaaa4");

        colorPicker.setColors(colors)  // 만들어둔 list 적용
                .setColumns(5)  // 5열로 설정
                .setRoundColorButton(true)  // 원형 버튼으로 설정
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int newcolor) {
                        color=newcolor;
                        if(what==0) {
                            addSchedule(memotext, memotext, what);
                        }
                        else {
                            addSchedule(memotext, detail, what);
                        }
                    }

                    @Override
                    public void onCancel() {
                        // Cancel 버튼 클릭 시 이벤트
                    }
                }).show();  // dialog 생성
    }

    public void updateCalorie(double cal){
        String cur=calorietxt.getText().toString();
        double curcal=0.0;
        if(cur.equals("")||cur==null){
            curcal=cal;
        }
        else curcal=Double.parseDouble(cur)+cal;

        calorietxt.setText(String.valueOf(curcal));

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(curDate2+"act",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("act_calorie",String.valueOf(curcal));
        editor.commit();
    }



}
