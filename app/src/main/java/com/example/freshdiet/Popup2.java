package com.example.freshdiet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.freshdiet.plan.CalculateActivity;
import com.example.freshdiet.plan.MakePlan;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class Popup2 extends Activity {
    private LinearLayout typeLayout, memoLayout;
    private TextView curText, typeText, calorietext; // 현재 선택한 항목
    private EditText memoText;
    private Button modifyBtn, deleteBtn;
    private Intent intent;
    private String curtext, curMemo, curType;
    private int curindex, time;
    FragmentManager manager;
    MakePlan makePlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup2);

        typeLayout=findViewById(R.id.typelayout);
        memoLayout=findViewById(R.id.memolayout);

        curText=findViewById(R.id.curText);
        typeText = findViewById(R.id.edittxt2);
        memoText=findViewById(R.id.edittxt3);
        modifyBtn = findViewById(R.id.modifybtn);
        deleteBtn = findViewById(R.id.deletebtn);

       // calorietext=((MakePlan)MakePlan.Mcontext).updateCalorie();

        intent=getIntent();

        curtext=intent.getStringExtra("Todo"); // 공부, 운동, 취미/여가
        curindex=intent.getIntExtra("TodoIndex",-1);
        curMemo=intent.getStringExtra("Memo");
        curType=intent.getStringExtra("Type"); // 달리기, 영화 보기 등

        curText.setText(curtext); //popup창 제목

        if(curType==null) {
            typeLayout.setVisibility(View.GONE);
        }
        else{
            typeText.setText(curType);
        }
        memoLayout.setVisibility(View.VISIBLE);
        memoText.setText(curMemo);


        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modify();

                MakePlan.saveData();

                finish(); //팝업 닫기
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                delete();

                MakePlan.saveData();

                finish();
            }
        });
    }

    private void modify(){
        String[] temp;
        if(curType==null){ // 공부, 숙면
           temp=MakePlan.timeArray.get(curindex).split(":");
           temp[5]=memoText.getText().toString();
           String str="";
           for(int i=0;i<7;i++){
               str+=temp[i];
               if(i!=6) str+=":";
           }

           MakePlan.timeArray.set(curindex, str);
        }
        else{
            temp=MakePlan.timeArray2.get(curindex).split(":");
            temp[6]=memoText.getText().toString();
            String str="";
            for(int i=0;i<8;i++){
                str+=temp[i];
                if(i!=7) str+=":";
            }

            MakePlan.timeArray2.set(curindex, str);
        }
    }

    private void delete(){
        String[] temp;

        double calorie=0.0;
        CalculateActivity calculateActivity;

        if(curType==null) {
            temp=MakePlan.timeArray.get(curindex).split(":");
            deleteChecked(temp);
            MakePlan.timeArray.remove(curindex);
            double cal=0.0, mets=0.0;
            if(curtext.equals("숙면")){
                mets=0.9;
            }
            else if(curtext.equals("공부")){
                mets=1.8;
            }
            calculateActivity=new CalculateActivity(Double.parseDouble(MakePlan.userweight), time, mets);

        }
        else {
            String str="";
            temp=MakePlan.timeArray2.get(curindex).split(":");
            deleteChecked(temp);
            MakePlan.timeArray2.remove(curindex);

            if(curtext.equals("운동")) str="exercise";
            else if(curtext.equals("취미/여가")) str="hobby";
            else str="other";

            HashMap<String, Double> curMap=getMap(str);
            HashMap<String,Double> curMap2=getMap(str+"2");

            calculateActivity=new CalculateActivity(Double.parseDouble(MakePlan.userweight), curType,time,curMap,curMap2);

        }
        calorie=calculateActivity.getCalorie();
        calorie=Math.round(calorie*100)/100.0;

        MainActivity.changeText(-calorie);
    }

    private void deleteChecked(String[] curstr){
        int starthour=Integer.parseInt(curstr[0]), startmin=Integer.parseInt(curstr[1]);
        int endhour=Integer.parseInt(curstr[2]), endmin=Integer.parseInt(curstr[3]);

        if(endhour<starthour){
            for(int i=starthour*60+startmin;i<24*60;i++)
                MakePlan.checkArray[i]=false;
            for(int i=0;i<endhour*60+endmin;i++)
                MakePlan.checkArray[i]=false;
        }
        else {
            for (int i = starthour*60 + startmin; i < endhour*60+endmin+1; i++) {
                MakePlan.checkArray[i]=false;
            }
        }

        if(starthour>endhour){
            endhour+=24;
        }
        time=endhour*60+endmin-starthour*60-startmin;
    }

    public HashMap<String,Double> getMap(String str){
        HashMap<String,Double> outputMap = new HashMap<String,Double>();
        SharedPreferences pSharedPref = getSharedPreferences(str, MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString(str+"Map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = String.valueOf(jsonObject.get(key));
                    outputMap.put(key, Double.parseDouble(value));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}