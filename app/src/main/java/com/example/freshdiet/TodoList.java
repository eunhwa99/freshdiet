package com.example.freshdiet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.ToDoubleBiFunction;

public class TodoList extends AppCompatActivity {
    private ArrayList<String> exerciseArray=new ArrayList<>(Arrays.asList("걷기(보통)", "걷기(빠르게)","달리기","등산",
            "자전거","근력운동","싸이클","볼링","배드민턴", "배구", "축구","농구",
            "테니스","탁구","유도","주짓수", "킥복싱", "요가", "체조",
            "골프", "야구", "스케이트", "스키", "태권도", "럭비", "에어로빅",
            "수영","줄넘기"));
    private ArrayList<String> hobbyArray=new ArrayList<>(Arrays.asList("독서","영화/드라마/유튜브 보기", "게임", "요리", "드라이브", "미술", "춤", "노래"));
    private ArrayList<String> otherArray=new ArrayList<>(
            Arrays.asList("가사/노동"));

    private HashMap<String, Double> exerciseMap, hobbyMap, otherMap,exerciseMap2, hobbyMap2, otherMap2;
    private HashMap<String,Double> curMap, curMap2;

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

        exerciseMap=new HashMap<>();
        hobbyMap=new HashMap<>();
        otherMap=new HashMap<>();

        setClickListener();
        dealIntent();
        getData();
    }

    private void setClickListener(){
        addbtn.setOnClickListener(view -> {
            if(!FLAG) {
                String edittext = edittxt.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("EditText", edittext);
                int i = listView.getCheckedItemPosition();
                if(i>-1)
                     curname = (String) listView.getItemAtPosition(i);

                intent.putExtra("ToDo", curname); //체크 박스 표시된 것
                if(name.equals("운동")) curMap2=exerciseMap2;
                else if(name.equals("취미/여가")) curMap2=hobbyMap2;
                else curMap2=otherMap2;
                CalculateActivity calculateActivity=new CalculateActivity(Double.parseDouble(MyProfile.userweight), curname,MakePlan.time,curMap,curMap2);

                double calorie=0.0;
                calorie=calculateActivity.getCalorie();
                intent.putExtra("calorie",calorie);
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
                                    exerciseMap2.put(category, Double.parseDouble(calorie)); // 사용자가 직접 추가
                                    saveData(exerciseArray, exerciseMap2,1);
                                    adapter.addItem(category);
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(TodoList.this,"동일 항목이 존재합니다.",Toast.LENGTH_SHORT).show();
                                }
                            } else if (name.equals("취미/여가")) {
                                if(check(hobbyArray,category)) {
                                    hobbyArray.add(category);
                                    hobbyMap2.put(category, Double.parseDouble(calorie));
                                    saveData(hobbyArray, hobbyMap2,2);
                                    adapter.addItem(category);
                                    adapter.notifyDataSetChanged();
                                }
                                else  Toast.makeText(TodoList.this,"동일 항목이 존재합니다.",Toast.LENGTH_SHORT).show();
                            } else if (name.equals("기타")) {
                                if(check(otherArray,category)) {
                                    otherArray.add(category);
                                    otherMap2.put(category, Double.parseDouble(calorie));
                                    saveData(otherArray, otherMap2,3);
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
                                if(exerciseMap.containsKey(deleteitem)) exerciseMap.remove(deleteitem);
                                else if(exerciseMap2.containsKey(deleteitem)) exerciseMap2.remove(deleteitem);
                                saveData(exerciseArray, exerciseMap2,1);
                            } else if (name.equals("취미/여가")) {
                                hobbyArray.remove(i);
                                if(hobbyMap.containsKey(deleteitem)) hobbyMap.remove(deleteitem);
                                else if(hobbyMap2.containsKey(deleteitem)) hobbyMap2.remove(deleteitem);
                                saveData(hobbyArray, hobbyMap2,2);
                            } else if (name.equals("기타")) {
                                otherArray.remove(i);
                                if(otherMap.containsKey(deleteitem)) otherMap.remove(deleteitem);
                                else if(otherMap2.containsKey(deleteitem)) otherMap2.remove(deleteitem);
                                saveData(otherArray, otherMap2,3);
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
            addData(1);
            initListView(exerciseArray, 1);
        }
        else if(name.equals("취미/여가")){
            addData(2);
            initListView(hobbyArray, 2);
        }
        else if(name.equals("기타")){
            addData(3);
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

    private void getData(){
        exerciseMap2=getMap("exercise2");

        hobbyMap2=getMap("hobby2");

        otherMap2=getMap("other2");
    }

    private void addData(int val){
        String str="";
        Double mets=0.0;
        if(val==1){
            str="exercise";
        }
        else if(val==2){
            str="hobby";
        }
        else if(val==3){
            str="other";}

        curMap=getMap(str);
        if(curMap.size()==0) {

            if (val == 1) {
                for (int i = 0; i < exerciseArray.size(); i++) {
                    String cur = exerciseArray.get(i);

                    switch (cur) {
                        case "걷기(보통)":
                            mets = 2.8;
                            break;
                        case "요가":
                        case "볼링":
                        case "배구":
                            mets = 3.0;
                            break;
                        case "체조":
                        case "골프":
                            mets = 3.5;
                            break;
                        case "걷기(빠르게)":
                            mets = 3.8;
                            break;
                        case "탁구":
                            mets = 4.0;
                            break;
                        case "배드민턴":
                            mets = 4.5;
                            break;
                        case "야구":
                        case "자전거":
                            mets = 5.0;
                            break;
                        case "농구":
                        case "근력운동":
                            mets = 6.0;
                            break;
                        case "에어로빅":
                            mets = 7.0;
                            break;
                        case "축구":
                        case "테니스":
                        case "스케이트":
                        case "스키":
                            mets = 7.0;
                            break;
                        case "등산":
                            mets = 7.5;
                            break;
                        case "싸이클":
                        case "달리기":
                            mets = 8.0;
                            break;
                        case "유도":
                        case "태권도":
                        case "럭비":
                        case "킥복싱":
                        case "주짓수":
                        case "수영":
                            mets = 10.0;
                            break;
                        case "줄넘기":
                            mets = 11.0;
                            break;

                    }
                    exerciseMap.put(cur, mets);
                }
                curMap = exerciseMap;

            } else if (val == 2) {
                for (int i = 0; i < hobbyArray.size(); i++) {
                    String cur = hobbyArray.get(i);
                    //"독서","영화/드라마/유튜브 보기",
                    //            "게임", "요리", "드라이브", "미술", "춤", "노래"
                    switch (cur) {
                        case "영화/드라마/유튜브 보기":
                            mets = 0.9;
                            break;
                        case "게임":
                        case "독서":
                        case "드라이브":
                        case "노래":
                        case "미술":
                            mets = 1.8;
                            break;
                        case "요리":
                            mets = 2.0;
                            break;
                        case "춤":
                            mets = 5.0;
                            break;
                    }
                    hobbyMap.put(cur, mets);
                }
                curMap = hobbyMap;

            } else if (val == 3) {
                for (int i = 0; i < otherArray.size(); i++) {
                    String cur = otherArray.get(i);
                    switch (cur) {
                        case "가사/노동":
                            mets = 2.8;
                            break;
                    }
                    curMap = otherMap;
                }
            }
            setMap(str, curMap);
        }
    }

    private void saveData(ArrayList<String> curArray, HashMap<String, Double> curMap, int code){
        String key1="", key2="";
        if(code==1){
            key1="exercise_list";
            key2="exercise2";
        }
        else if(code==2) {
            key1 = "hobby_list";
            key2="hobby2";
        }
        else if(code==3) {
            key1 = "other_list";
            key2="other2";
        }
        PreferenceManager.setArrayList(TodoList.this, key1,curArray);
        setMap(key2, curMap);
    }


    private boolean check(ArrayList<String> curArray, String cate){
        for(int i=0;i<curArray.size();i++){
            String str=curArray.get(i);
            if(str.equals(cate)) return false;
        }
        return true;
    }

    public void setMap(String str,  HashMap<String,Double> inputMap){
        SharedPreferences pSharedPref = getSharedPreferences(str, Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.putString(str+"Map", jsonString);
            editor.commit();
        }
    }

    public  HashMap<String,Double> getMap(String str){
        HashMap<String,Double> outputMap = new HashMap<String,Double>();
        SharedPreferences pSharedPref = getSharedPreferences(str, Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString(str+"Map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    Double value = (Double) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
}
