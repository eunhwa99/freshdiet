package com.example.freshdiet.calorie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.freshdiet.DoubleMath;
import com.example.freshdiet.PreferenceManager;
import com.example.freshdiet.R;
import com.example.freshdiet.plan.Calendar;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/*
- 사용자 음식 직접 입력
 */

public class FoodMain extends Fragment{
    ViewGroup rootView;
    FoodListViewAdapter adapter;
    TextView resulttxt;
    ListView listview = null ;
    ArrayList<FoodListItem> mainList=new ArrayList<>();
    String clicked_food, company, curdate;
    HashMap<String, MultiHash> infoMap=new HashMap<>();

    ArrayList<FoodInfo> foodInfoArrayList1, foodInfoArrayList2;
    Context context;
    DoubleMath doubleMath=new DoubleMath();

    double eat_calorie, eat_carbo, eat_protein, eat_fat;

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data=result.getData();
                        foodInfoArrayList2=(ArrayList<FoodInfo>) data.getSerializableExtra("added_foodinfo");
                        calculate(foodInfoArrayList2);
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.foodmain, container, false);
        resulttxt=rootView.findViewById(R.id.resulttxt);
        curdate= Calendar.selectedDay2;
        context=Calendar.context;

        getFoodList();
        getData();


        return rootView;
    }
    private void getFoodList(){

        try {
            FoodListItem item=new FoodListItem();
            MultiHash multiHash;
            InputStreamReader is = new InputStreamReader(getResources().getAssets().open("my_excel.csv"));
            BufferedReader reader = new BufferedReader(is);
            CSVReader read = new CSVReader(reader);
            String[] nextLine = null;
            nextLine=read.readNext(); // 한 줄 건너 뛰기.
            while ((nextLine = read.readNext()) != null){
                int len=nextLine.length;
                String key="";
                double val1=0.0, val3=0.0, val4=0.0, val5=0.0, val6=0.0;
                String val2=""; // 단위
                for (int i = 0; i < len; i++) {
                    if(i==0){
                        item=new FoodListItem();
                        item.setTitle(nextLine[i]);
                    }
                    else if(i==1){
                        item.setDesc(nextLine[i]);
                        mainList.add(item);
                        key=item.getTitle()+item.getDesc();
                    }
                    else if(i==2){
                        val1=Double.parseDouble(nextLine[i]);
                    }
                    else if(i==3){
                        val2=nextLine[i];
                    }
                    else if(i==4){ //에너지
                        val3=Double.parseDouble(nextLine[i]);
                    }
                    else if(i==5){ //단백질
                        val4=Double.parseDouble(nextLine[i]);
                    }
                    else if(i==6){ //지방
                        val5=Double.parseDouble(nextLine[i]);
                    }
                    else if(i==7){ //탄수화물
                        val6=Double.parseDouble(nextLine[i]);
                    }
                }
                multiHash=new MultiHash(val1, val2, val3, val4, val5, val6);
                infoMap.put(key, multiHash);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        listview = (ListView)rootView.findViewById(R.id.food_listview);
        adapter=new FoodListViewAdapter(mainList);
        listview.setAdapter(adapter);

        setClicked(); //클릭 기능
        setTextChanged(); // 검색 기능
    }

    private void setClicked(){

        // listview에 클릭 이벤트 핸들러 정의
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FoodListItem data=(FoodListItem)adapterView.getItemAtPosition(i);

                clicked_food=data.getTitle();
                company=data.getDesc();

                MultiHash multiHash=infoMap.get(clicked_food+company);

                Intent intent;
                intent=new Intent(getContext(), FoodDetail.class);
                intent.putExtra("food",clicked_food);
                intent.putExtra("food2", company);
                intent.putExtra("val1", multiHash.val1);
                intent.putExtra("val2", multiHash.val2);
                intent.putExtra("val3", multiHash.val3);
                intent.putExtra("val4", multiHash.val4);
                intent.putExtra("val5", multiHash.val5);
                intent.putExtra("val6", multiHash.val6);
                startActivityResult.launch(intent);

            }
        });

    }

    private void setTextChanged(){

        EditText editTextFilter = (EditText)rootView.findViewById(R.id.editTextFilter) ;

        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                //String filterText = edit.toString().split(" ") ;
                String filterText = edit.toString();
                if(filterText.length()>0) {
                    adapter.getFilter().filter(filterText, new Filter.FilterListener() {
                        @Override
                        public void onFilterComplete(int count) {
                            if (count == 0)
                                resulttxt.setText("검색 결과가 없습니다.\n단어 사이에 공백을 추가해 검색하거나 기본 메뉴에 토핑을 추가해보세요.");
                            else
                                resulttxt.setText("검색 결과");
                        }
                    });
                }
                else
                    resulttxt.setText("검색 결과");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;
    }


    public void calculate(ArrayList<FoodInfo> arrayList){

        for(FoodInfo item:arrayList){
            eat_calorie+=item.kal;
            eat_carbo+=item.carbo;
            eat_protein+=item.protein;
            eat_fat+=item.fat;

            eat_calorie=doubleMath.calc(eat_calorie);
            eat_carbo=doubleMath.calc(eat_carbo);
            eat_protein=doubleMath.calc(eat_protein);
            eat_fat=doubleMath.calc(eat_fat);

            foodInfoArrayList1.add(item);
        }

        saveData();
    }

    // 먹은 음식 저장
    public void saveData(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(curdate+"act", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("eat_calorie",String.valueOf(eat_calorie));
        editor.putString("eat_carbo", String.valueOf(eat_carbo));
        editor.putString("eat_protein",String.valueOf(eat_protein));
        editor.putString("eat_fat", String.valueOf(eat_fat));
        editor.apply();

        PreferenceManager.setFoodArrayList(context,curdate+"food", foodInfoArrayList1);

    }

    public void getData(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(curdate+"act", Context.MODE_PRIVATE);
        eat_calorie=Double.parseDouble(sharedPreferences.getString("eat_calorie","0.0"));
        eat_carbo=Double.parseDouble(sharedPreferences.getString("eat_carbo","0.0"));
        eat_protein=Double.parseDouble(sharedPreferences.getString("eat_protein","0.0"));
        eat_fat=Double.parseDouble(sharedPreferences.getString("eat_fat","0.0"));

       foodInfoArrayList1=PreferenceManager.getFoodArrayList(context,curdate+"food");
       if(foodInfoArrayList1==null)
           foodInfoArrayList1=new ArrayList<>();

    }

}
