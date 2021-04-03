package com.example.freshdiet.challenge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.freshdiet.PreferenceManager;
import com.example.freshdiet.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class ChallengeMain extends Fragment implements ViewFlipperAction.ViewFlipperCallback {
    final int CHALLENGE_NUM=7;
    // Millisecond 형태의 하루(24 시간)
    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    //뷰플리퍼
    ViewFlipper flipper;
    //인덱스
    List<TextView> indexes;
    ViewGroup rootView;
    TextView index0;
    TextView index1;
    TextView index2;
    LinearLayout sleep1,sleep2, exer1,exer2,exer3,eat1,eat2;

    View view1,view2,view3;
    Context Ccontext;
    public static boolean[] challenge;//=new boolean[CHANLLENGE_NUM+1];

    HashMap<String, Long> hashMap;
    HashMap<String, Long> alarmtimeMap;

    ActivityResultLauncher<Intent> startActivityResult1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data=result.getData();
                        int idx=data.getIntExtra("idx",-1);
                        Long time=data.getLongExtra("alarmtime",-1);
                        if(idx!=-1){
                            String text=getTitle(idx);
                            if(hashMap.containsKey(text)){
                                if(challenge[idx]==false){ // false --> 키 삭제
                                    hashMap.remove(text);
                                }
                                else{ // 수정되었다.
                                   alarmtimeMap.put(text, time);
                                }
                            }else{ // 해쉬맵에 키가 없다면
                                if(challenge[idx]==true){ //challenge 체크가 true 이면 현재 날짜와 저장
                                    Long day=getDay();
                                    hashMap.put(text,day);
                                    alarmtimeMap.put(text, time);
                                }
                            }
                        }
                        initScreen();
                       saveData();
                       setMap(hashMap,"Challenge");
                       setMap(alarmtimeMap,"Alarm");

                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.challengemain, container, false);

        Ccontext=container.getContext();
        getData();

        //UI
        flipper = (ViewFlipper)rootView.findViewById(R.id.flipper);
        index0 = rootView.findViewById(R.id.txtIndex0);
        index0.setTextColor(Color.BLACK);
        index1 = rootView.findViewById(R.id.txtIndex1);
        index2 = rootView.findViewById(R.id.txtIndex2);
        //인덱스리스트
        indexes = new ArrayList<>();
        indexes.add(index0);
        indexes.add(index1);
        indexes.add(index2);

        //xml을 inflate 하여 flipper view에 추가하기
        //inflate
        LayoutInflater inflater2 = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        view1 = inflater2.inflate(R.layout.viewflipper1, flipper, false);
        view2 = inflater2.inflate(R.layout.viewflipper2, flipper, false);
        view3 = inflater2.inflate(R.layout.viewflipper3, flipper, false);

        sleep1=view1.findViewById(R.id.sleeplayout1);
        sleep2=view1.findViewById(R.id.sleeplayout2);
        exer1=view2.findViewById(R.id.exerlayout1);
        exer2=view2.findViewById(R.id.exerlayout2);
        exer3=view2.findViewById(R.id.exerlayout3);
        eat1=view3.findViewById(R.id.eatlayout1);
        eat2=view3.findViewById(R.id.eatlayout2);

        //inflate 한 view 추가
        flipper.addView(view1);
        flipper.addView(view2);
        flipper.addView(view3);

        //리스너설정 - 좌우 터치시 화면넘어가기
        //flipper.setOnTouchListener(new ViewFlipperAction(this, flipper));
        hashMap=new HashMap<>();
        initScreen();
        setClickListener();

        return rootView;
    }

    //인덱스 업데이트
    @Override
    public void onFlipperActionCallback(int position) {
       // Log.d("ddd", ""+position);
        for(int i=0; i<indexes.size(); i++){
            TextView index = indexes.get(i);
            //현재화면의 인덱스 위치면 녹색
            if(i == position){
                index.setTextColor(Color.BLACK);
            }
            //그외
            else{
                index.setTextColor(Color.GRAY);
            }
        }
    }

    private void initScreen(){
        HashMap<String, Long>temp=getMap("Challenge");
        if(temp.size()!=0) hashMap=temp;

        temp=getMap("Alarm");
        if(temp.size()!=0) alarmtimeMap=temp;

       if(challenge[0]){
           sleep1.setBackgroundColor(Color.RED);
       }
       else sleep1.setBackgroundColor(Color.GRAY);
        if(challenge[1]){
            sleep2.setBackgroundColor(Color.RED);
        }
        else sleep2.setBackgroundColor(Color.GRAY);
        if(challenge[2]){
            exer1.setBackgroundColor(Color.RED);
        }
        else exer1.setBackgroundColor(Color.GRAY);
        if(challenge[3]){
            exer2.setBackgroundColor(Color.RED);
        }
        else exer2.setBackgroundColor(Color.GRAY);
        if(challenge[4]){
            exer3.setBackgroundColor(Color.RED);
        }
        else exer3.setBackgroundColor(Color.GRAY);
        if(challenge[5]){
            eat1.setBackgroundColor(Color.RED);
        }
        else eat1.setBackgroundColor(Color.GRAY);
        if(challenge[6]){
            eat2.setBackgroundColor(Color.RED);
        }
        else eat2.setBackgroundColor(Color.GRAY);

    }

    private String getTitle(int idx){
        String str="";
        TextView tv;
        tv=rootView.findViewById(R.id.sleeptv1);
        switch(idx){
            case 0:
                tv=rootView.findViewById(R.id.sleeptv1);
                break;
            case 1:
                tv=rootView.findViewById(R.id.sleeptv2);break;
            case 2:
                tv=rootView.findViewById(R.id.exertv1);break;
            case 3:
                tv=rootView.findViewById(R.id.exertv2);break;
            case 4:
                tv=rootView.findViewById(R.id.exertv3);break;
            case 5:
                tv=rootView.findViewById(R.id.foodtv1);break;
            case 6:
                tv=rootView.findViewById(R.id.foodtv2);break;
        }
        str=tv.getText().toString();
        return str;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Long getDay() {

        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        return today;

    }


    private void setClickListener(){

        index0.setOnClickListener(view->{
            flipper.setDisplayedChild(0);
            onFlipperActionCallback(0);
        });
        index1.setOnClickListener(view->{
            flipper.setDisplayedChild(1);
            onFlipperActionCallback(1);
        });
        index2.setOnClickListener(view->{
            flipper.setDisplayedChild(2);
            onFlipperActionCallback(2);
        });


        sleep1.setOnClickListener(view->{
            makeIntent(0);
        });

        sleep2.setOnClickListener(view -> {
            makeIntent(1);
        });

        exer1.setOnClickListener(view->{
            makeIntent(2);
        });
        exer2.setOnClickListener(view->{
            makeIntent(3);
        });
        exer3.setOnClickListener(view->{
            makeIntent(4);
        });
        eat1.setOnClickListener(view->{
            makeIntent(5);
        });
        eat2.setOnClickListener(view->{
            makeIntent(6);
        });
    }

    private void makeIntent(int idx){
        Intent intent;
        String title=getTitle(idx);
        if(!challenge[idx])
            intent=new Intent(getContext(), ChallengeSub.class);
        else {
            intent = new Intent(getContext(), ChallengeSub2.class);
            Long time=alarmtimeMap.get(title); // 알람 시간 가지고 오기
            intent.putExtra("alarmtime",time);
        }

        intent.putExtra("index",idx);
        intent.putExtra("curname",title);
        startActivityResult1.launch(intent);
    }

    private void getData(){
        boolean[] temp=PreferenceManager.getBooleanArray(Ccontext,"challenge_check");
        if(temp.length!=0){
            challenge=temp;
        }
        else challenge=new boolean[CHALLENGE_NUM+1];
    }
    private void saveData(){
        PreferenceManager.setBooleanArray(Ccontext, "challenge_check",challenge);
    }

    public void setMap(HashMap<String, Long> inputMap,String str){

        SharedPreferences pSharedPref = getActivity().getSharedPreferences(str, MODE_PRIVATE);

        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(str+"day").apply();
            editor.putString(str+"day", jsonString);
            editor.commit();
        }
    }

    public  HashMap<String,Long> getMap(String str){
        HashMap<String,Long> outputMap = new HashMap<String,Long>();
        SharedPreferences pSharedPref = getActivity().getSharedPreferences(str, MODE_PRIVATE);

        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString(str+"day", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = String.valueOf(jsonObject.get(key));
                    outputMap.put(key, Long.parseLong(value));
                }
            }
        }catch(Exception e){
            String sr=e.getMessage();
            Log.i("오류", sr);
            e.printStackTrace();
        }


        return outputMap;
    }

}



