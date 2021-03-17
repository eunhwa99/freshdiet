package com.example.freshdiet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class Popup2 extends Activity {
    private EditText editText;
    private Button modifyBtn, deleteBtn;
    private Intent intent;
    private String curtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup2);

        editText = findViewById(R.id.edittxt2);
        modifyBtn = findViewById(R.id.modifybtn);
        deleteBtn = findViewById(R.id.deletebtn);

        intent=getIntent();

        curtext=intent.getStringExtra("Todo");
        editText.setText(curtext);

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // String edittext = editText.getText().toString();
                //intent.putExtra("EditText", edittext);
                //setResult(RESULT_OK, intent);
                finish(); //팝업 닫기
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               // setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

  /*  @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }*/

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}