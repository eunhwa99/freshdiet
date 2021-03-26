package com.example.freshdiet.challenge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshdiet.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChallengeMain extends AppCompatActivity implements ViewFlipperAction.ViewFlipperCallback {
    //뷰플리퍼
    ViewFlipper flipper;
    //인덱스
    List<TextView> indexes;

    TextView index0;
    TextView index1;
    TextView index2;

    View view1,view2,view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengemain);

        //UI
        flipper = (ViewFlipper)findViewById(R.id.flipper);
        index0 = findViewById(R.id.txtIndex0);
        index0.setTextColor(Color.BLACK);
        index1 = findViewById(R.id.txtIndex1);
        index2 = findViewById(R.id.txtIndex2);

        //인덱스리스트
        indexes = new ArrayList<>();
        indexes.add(index0);
        indexes.add(index1);
        indexes.add(index2);

        //xml을 inflate 하여 flipper view에 추가하기
        //inflate
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        view1 = inflater.inflate(R.layout.viewflipper1, flipper, false);
        view2 = inflater.inflate(R.layout.viewflipper2, flipper, false);
        view3 = inflater.inflate(R.layout.viewflipper3, flipper, false);
        //inflate 한 view 추가
        flipper.addView(view1);
        flipper.addView(view2);
        flipper.addView(view3);

        //리스너설정 - 좌우 터치시 화면넘어가기
        //flipper.setOnTouchListener(new ViewFlipperAction(this, flipper));
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

    public void Go(View v){
        Intent intent;
        TextView tv;
        switch(v.getId()){
            case R.id.sleeplayout1:
                intent = new Intent(this, ChallengeSub.class);
                tv=findViewById(R.id.sleeptv1);
                intent.putExtra("curname",tv.getText().toString());
                startActivity(intent);
                break;
            case R.id.sleeplayout2:
                intent = new Intent(this, ChallengeSub.class);
                tv=findViewById(R.id.sleeptv2);
                intent.putExtra("curname",tv.getText().toString());
                startActivity(intent);
                break;
            case R.id.exerlayout1:
                break;
            case R.id.exerlayout2:
                break;
            case R.id.exerlayout3:
                break;
            case R.id.eatlayout1:
                break;
            case R.id.eatlayout2:
                break;

        }
    }

    public void Flip(View v){
        TextView txtview=findViewById(v.getId());
        int idx=0;
        if (index0.equals(txtview)) {
           idx=0;
        } else if (index1.equals(txtview)) {
           idx=1;
        } else if (index2.equals(txtview)) {
            idx=2;
        }
        flipper.setDisplayedChild(idx);
        onFlipperActionCallback(idx);
    }
}



