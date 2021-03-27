package com.example.freshdiet.challenge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.freshdiet.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ChallengeMain extends Fragment implements ViewFlipperAction.ViewFlipperCallback {
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.challengemain, container, false);

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
            Intent intent;
            TextView tv;
            intent = new Intent(getContext(), ChallengeSub.class);
            tv=rootView.findViewById(R.id.sleeptv1);
            intent.putExtra("curname",tv.getText().toString());
            startActivity(intent);
        });

        sleep2.setOnClickListener(view -> {
            Intent intent;
            TextView tv;
            intent = new Intent(getContext(), ChallengeSub.class);
            tv=rootView.findViewById(R.id.sleeptv2);
            intent.putExtra("curname",tv.getText().toString());
            startActivity(intent);
        });
    }
}



