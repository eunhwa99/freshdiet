package com.example.freshdiet.calorie;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.freshdiet.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class FoodMain extends Fragment {
    ViewGroup rootView;
    FoodListViewAdapter adapter;
    ListView listview = null ;
    ArrayList<FoodListItem> mainList=new ArrayList<>();
    private String requestUrl;
    private String serviceKey="3b0058815936482daa41";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.foodmain, container, false);

        requestUrl="http://openapi.foodsafetykorea.go.kr/api/"+serviceKey+"/I2790/xml/6/21";


        getFoodList();

        return rootView;
    }
    private void getFoodList(){

        try {
            InputStream is = getContext().getResources().getAssets().open("my_excel.xls");
            Workbook wb = Workbook.getWorkbook(is);


            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    FoodListItem item=new FoodListItem();
                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();

                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet.getCell(col, row).getContents();

                            if(col==0){
                                item=new FoodListItem();
                                item.setTitle(contents);
                            }
                            else if(col==colTotal-1){
                                item.setDesc(contents);
                                mainList.add(item);
                            }
                            Log.d("Main",col+"번째: "+contents);
                        }

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        listview = (ListView)rootView.findViewById(R.id.food_listview);
        adapter=new FoodListViewAdapter(mainList);
        listview.setAdapter(adapter);

        setTextChanged(); // 검색 기능
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
