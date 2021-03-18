package com.example.freshdiet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Popup2 extends Activity {
    private LinearLayout typeLayout, memoLayout;
    private TextView curText, typeText; // 현재 선택한 항목
    private EditText memoText;
    private Button modifyBtn, deleteBtn;
    private Intent intent;
    private String curtext, curMemo, curType;
    private int curindex;

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

        intent=getIntent();

        curtext=intent.getStringExtra("Todo");
        curindex=intent.getIntExtra("TodoIndex",-1);
        curMemo=intent.getStringExtra("Memo");
        curType=intent.getStringExtra("Type");

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
                ((MakePlan)MakePlan.Mcontext).saveData();
                finish(); //팝업 닫기
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                delete();

                ((MakePlan)MakePlan.Mcontext).saveData();
                finish();
            }
        });
    }

    private void modify(){
        String[] temp;
        if(curType==null){
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
                if(i!=6) str+=":";
            }

            MakePlan.timeArray2.set(curindex, str);
        }
    }

    private void delete(){
        String[] temp;

        if(curType==null) {
            temp=MakePlan.timeArray.get(curindex).split(":");
            MakePlan.timeArray.remove(curindex);
        }
        else {
            temp=MakePlan.timeArray2.get(curindex).split(":");
            MakePlan.timeArray2.remove(curindex);
        }
        deleteChecked(temp);
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

    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}