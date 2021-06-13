package com.example.freshdiet.calorie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshdiet.R;

import java.util.ArrayList;

/**
 * 음식 추가 이름 너무 길면 x 표 없어짐 (하프앤하프 피자)
 */

public class FoodPopup extends Activity {
   Button closebtn;
   Intent intent;
   FoodAdapter mAdapter, mAdapter2;
   LinearLayoutManager mLayoutManager, mLayoutManager2;
   RecyclerView foodrv;
   ArrayList<FoodListItem> foodlist;
   ArrayList<FoodInfo> foodinfolist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.food_popup);
        foodrv=findViewById(R.id.food_rv1);
        closebtn=findViewById(R.id.closebtn);

        dealIntent();
        initRecylerView();

    }

    public void dealIntent(){
        intent=getIntent();
        foodlist=FoodDetail.foodlist;
        foodinfolist=FoodDetail.foodinfolist;
    }


    public void setClick(){

        mAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
            //    FoodListItem data=mAdapter.getItem(pos);
                foodlist.remove(pos);
                foodinfolist.remove(pos);
                mAdapter.notifyDataSetChanged();

            }
        });

        closebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }
    public void initRecylerView(){
        mLayoutManager = new LinearLayoutManager(FoodPopup.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // 기본값이 VERTICAL
        foodrv.setLayoutManager(mLayoutManager);
        mAdapter=new FoodAdapter(1,foodlist);


        // set Adapter
        foodrv.setAdapter(mAdapter);
        setClick();
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
