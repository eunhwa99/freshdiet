package com.example.freshdiet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;

public class TodoList extends AppCompatActivity {
    private ArrayList<String> exerciseArray=new ArrayList<>(Arrays.asList("걷기","달리기","등산","자전거","근력운동","싸이클","볼링","배드민턴","축구","농구","테니스","탁구","유도","주짓수", "스트레칭"));
    private ArrayList<String> hobbyArray=new ArrayList<>(Arrays.asList("독서","영화/드라마/유튜브 보기", "게임", "요리", "드라이브", "미술", "춤", "노래"));

    private ChoiceListViewAdapter adapter;
    private ListView listView;
    private EditText edittxt;
    private TextView txt1;
    private Button addbtn, cancelbtn;
    private String curname; // 체크된 할일

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);

        txt1=findViewById(R.id.todotxt);
        edittxt=findViewById(R.id.todoedittxt);
        addbtn=findViewById(R.id.todoaddbtn);

        addbtn.setOnClickListener(view -> {
            String edittext=edittxt.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("EditText", edittext);
           int i=listView.getCheckedItemPosition();
           curname=(String)listView.getItemAtPosition(i);

            intent.putExtra("ToDo", curname); //체크 박스 표시된 것
            setResult(RESULT_OK, intent);
            finish(); //팝업 닫기
        });

        dealIntent();

    }
    private void dealIntent(){
        Intent intent=getIntent();
        String name=intent.getStringExtra("todo");
        txt1.setText(name);
        if(name.equals("운동")){
            initListView(exerciseArray, 1);
        }
        else{
            initListView(hobbyArray, 2);
        }
    }

    private void initListView(ArrayList<String> curArray, int code){
        adapter=new ChoiceListViewAdapter();

        // 리스트뷰 참조 및 Adpater 달기
        listView=(ListView)findViewById(R.id.listview2);
        listView.setAdapter(adapter);


        String key="";
        if(code==1){
            key="exercise_list";
        }
        else if(code==2) key="hobby_list";

        ArrayList<String> temp=PreferenceManager.getArrayList(getApplicationContext(),key);
        if(temp.size()==0){
            PreferenceManager.setArrayList(TodoList.this, key,curArray);
        }
        else curArray=temp;

        for(int i=0;i<curArray.size();i++){
            String name=curArray.get(i);
            adapter.addItem(name);
        }
    }
}
