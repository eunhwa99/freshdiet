package com.example.freshdiet.challenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.freshdiet.R;

public class ChallengeSub2 extends Activity{

    Intent intent;
    Long alarmTime;
    String curname;
    int idx;

    private TextView curtv, durtv;
    private Button start_btn, cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengesub2);

        curtv=findViewById(R.id.challengetv);
        durtv=findViewById(R.id.duration);
        start_btn=findViewById(R.id.ch_btn);
        cancel_btn=findViewById(R.id.cancel_btn);

        dealIntent();
    }

    private void dealIntent(){
        intent=getIntent();
        idx=intent.getIntExtra("index",0);
        curname=intent.getStringExtra("curname");
        alarmTime=intent.getLongExtra("alarmtime",-1); // 설정한 알람시간 가지고 오기
    }
}
