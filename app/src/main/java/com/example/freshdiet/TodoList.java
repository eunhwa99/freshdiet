package com.example.freshdiet;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;

public class TodoList extends AppCompatActivity {
    private ArrayList<String> exerciseArray=new ArrayList<>(Arrays.asList("걷기","달리기","등산","자전거","근력운동","싸이클","볼링","배드민턴","축구","농구","테니스","탁구","유도","주짓수", "스트레칭"));
    private ArrayList<String> hobbyArray=new ArrayList<>(Arrays.asList("독서","영화/드라마/유튜브 보기", "게임", "요리", "드라이브", "미술", "춤", "노래"));
    private ArrayList<String> otherArray=new ArrayList<>();

    private ChoiceListViewAdapter adapter;
    private ListView listView;
    private EditText edittxt,editcalorie, editcategory;
    private TextView txt1;
    private Button addbtn, editbtn, cancelbtn;
    private String curname; // 체크된 할일
    private String name; //운동, 취미, 기타
    private LinearLayout editlayout,editlayout2;
    private boolean FLAG=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);

        editlayout=findViewById(R.id.editlayout);
        editlayout2=findViewById(R.id.editlayout2);
        txt1=findViewById(R.id.todotxt);
        edittxt=findViewById(R.id.todoedittxt);
        editcalorie=findViewById(R.id.editcalorie);
        editcategory=findViewById(R.id.editcategory);
        addbtn=findViewById(R.id.todoaddbtn);
        cancelbtn=findViewById(R.id.todocancelbtn);
        editbtn=findViewById(R.id.editbtn);

        setClickListener();
        dealIntent();

    }

    private void setClickListener(){
        addbtn.setOnClickListener(view -> {
            if(!FLAG) {
                String edittext = edittxt.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("EditText", edittext);
                int i = listView.getCheckedItemPosition();
                curname = (String) listView.getItemAtPosition(i);

                intent.putExtra("ToDo", curname); //체크 박스 표시된 것
                setResult(RESULT_OK, intent);
                finish(); //팝업 닫기
            }
            else{
                String calorie=editcalorie.getText().toString();
                String category=editcategory.getText().toString();

                if(category.equals("")||category==null){
                    Toast.makeText(TodoList.this, "항목명을 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else if(calorie.equals("")||calorie==null){
                    Toast.makeText(TodoList.this, "10분당 소모 칼로리를 입력하세요.",Toast.LENGTH_SHORT).show();
                }

                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("알림").setMessage("추가하시겠습니까?");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getApplicationContext(), "추가되었습니다..", Toast.LENGTH_SHORT).show();
                            if (name.equals("운동")) {
                                if(check(exerciseArray,category)) {
                                    exerciseArray.add(category);
                                    saveData(exerciseArray, 1);
                                    adapter.addItem(category);
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(TodoList.this,"동일 항목이 존재합니다.",Toast.LENGTH_SHORT).show();
                                }
                            } else if (name.equals("취미/여가")) {
                                if(check(hobbyArray,category)) {
                                    hobbyArray.add(category);
                                    saveData(hobbyArray, 2);
                                    adapter.addItem(category);
                                    adapter.notifyDataSetChanged();
                                }
                                else  Toast.makeText(TodoList.this,"동일 항목이 존재합니다.",Toast.LENGTH_SHORT).show();
                            } else if (name.equals("기타")) {
                                if(check(otherArray,category)) {
                                    otherArray.add(category);
                                    saveData(otherArray, 3);
                                    adapter.addItem(category);
                                    adapter.notifyDataSetChanged();
                                }
                                else  Toast.makeText(TodoList.this,"동일 항목이 존재합니다.",Toast.LENGTH_SHORT).show();
                            }
                            editcalorie.setText("");
                            editcategory.setText("");

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {

                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        cancelbtn.setOnClickListener(view->{
            if(!FLAG){
                finish();
            }
            else{
                editlayout2.setVisibility(View.VISIBLE);
                editlayout.setVisibility(View.GONE);
                FLAG=false;
                Drawable myIcon = getResources().getDrawable(R.drawable.ic_baseline_edit_24);
                editbtn.setBackground(myIcon);
            }
        });

        editbtn.setOnClickListener(view->{
            if(!FLAG) {
                editlayout2.setVisibility(View.GONE);
                editlayout.setVisibility(View.VISIBLE);
                FLAG = true;
                Drawable myIcon = getResources().getDrawable(R.drawable.ic_baseline_delete_24);
                editbtn.setBackground(myIcon);
            }
            else{ //리스트뷰 삭제

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("알림").setMessage("삭제하시겠습니까?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        int i = listView.getCheckedItemPosition();
                        String deleteitem=(String)listView.getItemAtPosition(i);
                        if(i>-1) {
                            if (name.equals("운동")) {
                                exerciseArray.remove(i);
                                saveData(exerciseArray, 1);
                            } else if (name.equals("취미/여가")) {
                                hobbyArray.remove(i);
                                saveData(hobbyArray, 2);
                            } else if (name.equals("기타")) {
                                otherArray.remove(i);
                                saveData(otherArray, 3);
                            }


                            // listview 선택 초기화.
                            listView.clearChoices();
                            // listview 갱신.
                            adapter.removeItem(i);
                            adapter.notifyDataSetChanged();

                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }


    private void dealIntent(){
        Intent intent=getIntent();
        name=intent.getStringExtra("todo");
        txt1.setText(name);
        if(name.equals("운동")){
            initListView(exerciseArray, 1);
        }
        else if(name.equals("취미/여가")){
            initListView(hobbyArray, 2);
        }
        else if(name.equals("기타")){
            initListView(otherArray,3);
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
        else if(code==3) key="other_list";

        ArrayList<String> temp=PreferenceManager.getArrayList(TodoList.this,key);
        if(temp.size()==0){
            PreferenceManager.setArrayList(TodoList.this, key,curArray);
        }
        else curArray=temp;

        for(int i=0;i<curArray.size();i++){
            String name=curArray.get(i);
            adapter.addItem(name);
        }
    }

    private void saveData(ArrayList<String> curArray, int code){
        String key="";
        if(code==1){
            key="exercise_list";
        }
        else if(code==2) key="hobby_list";
        else if(code==3) key="other_list";
       // ArrayList<String> temp=PreferenceManager.getArrayList(TodoList.this, key);

        PreferenceManager.setArrayList(TodoList.this, key,curArray);

    }

    private boolean check(ArrayList<String> curArray, String cate){
        for(int i=0;i<curArray.size();i++){
            String str=curArray.get(i);
            if(str.equals(cate)) return false;
        }
        return true;
    }

}
