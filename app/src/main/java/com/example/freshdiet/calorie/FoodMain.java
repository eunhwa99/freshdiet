package com.example.freshdiet.calorie;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.freshdiet.R;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/*
- 토핑 추가, 토핑도 gram 조절 (4.29)
- 추가했을 때 섭취 칼로리 증가 + 추가한 음식 보여주기 (4.30)
- 검색 정보 없을 때 유사 리스트 띄우기 (주말)
- 디저트/본식 나눌거?

 */
//  requestUrl="http://openapi.foodsafetykorea.go.kr/api/"+serviceKey+"/I2790/xml/6/21";
//   String serviceKey="3b0058815936482daa41";
public class FoodMain extends Fragment{
    ViewGroup rootView;
    FoodListViewAdapter adapter;
    ListView listview = null ;
    ArrayList<FoodListItem> mainList=new ArrayList<>();
    String clicked_food, company;
    HashMap<String, MultiHash> infoMap=new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.foodmain, container, false);

        getFoodList();

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
                startActivity(intent);

            }
        });

    }

    private void setTextChanged(){
        EditText editTextFilter = (EditText)rootView.findViewById(R.id.editTextFilter) ;
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;
                ((FoodListViewAdapter)listview.getAdapter()).getFilter().filter(filterText) ;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;
    }






}
