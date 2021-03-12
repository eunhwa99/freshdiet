package com.example.freshdiet;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Popup extends Activity{
    private EditText editText;
    private Button addBtn,deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup);

        editText=findViewById(R.id.edittxt);
        addBtn=findViewById(R.id.popaddbtn);
        deleteBtn=findViewById(R.id.popdeletebtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish(); //팝업 닫기
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
