package com.example.freshdiet;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class Piechart extends AppCompatActivity {
    PieChart pieChart;
    int[] colorArray=new int[] {Color.LTGRAY, Color.BLUE, Color.RED};

    private TextView startTime, endTime;
    private ListView listView;
    private ListViewAdapter adapter;

    ArrayList<String> listArray=new ArrayList<>(Arrays.asList("공부","운동","취미/여가","식사","숙면","기타"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piechart);

        pieChart=(PieChart)findViewById(R.id.piechart);
        startTime=(TextView)findViewById(R.id.startTime);
        endTime=(TextView)findViewById(R.id.endTime);

        initListView();

        PieDataSet pieDataSet = new PieDataSet(data1(), "Fresh Diary" );
        pieDataSet.setColors(colorArray); //색 지정

        PieData pieData=new PieData(pieDataSet); // grouping the data set from entry to chart
        pieChart.setDrawEntryLabels(true);
        pieData.setDrawValues(true); //showing the value of the entries, default true if not set
        pieData.setValueTextSize(30);

        pieChart.setDrawHoleEnabled(false); //가운데 원형 뚫는 것
        pieChart.setRotationEnabled(false); //원형차트 고정

        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    private ArrayList<PieEntry> data1(){
        ArrayList<PieEntry> datavalue=new ArrayList<>();

        datavalue.add(new PieEntry(30, "하이"));
        datavalue.add(new PieEntry(30, "하이2"));
        datavalue.add(new PieEntry(30, "하이3"));
        return datavalue;
    }

    private void initListView(){
        adapter=new ListViewAdapter();

        // 리스트뷰 참조 및 Adpater 달기
        listView=(ListView)findViewById(R.id.listview1);
        listView.setAdapter(adapter);

        // listview에 클릭 이벤트 핸들러 정의
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name=(String)adapterView.getItemAtPosition(i);

                //String name=item.getName();
                Intent intent;
                switch(name){
                    case "공부": case "식사": case "숙면":
                        intent=new Intent(getApplicationContext(), Popup.class);
                        startActivity(intent);
                        break;
                    case "운동": case "취미/여가":
                        intent=new Intent(getApplicationContext(), TodoList.class);
                        intent.putExtra("todo", name);
                        startActivityForResult(intent,1004);
                        break;

                    case "기타":
                        break;

                }
            }
        });



        ArrayList<String> templist=PreferenceManager.getArrayList(getApplicationContext(),"activity_list");
        if(templist.size()==0){
            PreferenceManager.setArrayList(Piechart.this, "activity_list",listArray);
        }
        else listArray=templist;

        for(int i=0;i<listArray.size();i++){
            String name=listArray.get(i);
            adapter.addItem(name);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1004:
                if (resultCode == RESULT_OK) {
                    // 계획표 추가
                } else {

                }
                break;
        }
    }


    public void pickTime(View v){
        TimePickerDialog timePicker;
        TextView tv_tmp = (TextView) v;
        String[] time_tmp = tv_tmp.getText().toString().split(":");

        if(time_tmp[0].equals("--")){
            time_tmp[0] = time_tmp[1] = "0";
        }

        timePicker = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String setTime, hour, min;
                if(minute==0) min="00";
                else min=String.valueOf(minute);

                if(hourOfDay==0) hour="00";
                else hour=String.valueOf(hourOfDay);

                setTime=hour+":"+min;
                TextView tv = (TextView) v;
                tv.setText(setTime);
            }
        }, Integer.parseInt(time_tmp[0]), Integer.parseInt(time_tmp[1]), false);
        timePicker.setButton(TimePickerDialog.BUTTON_NEGATIVE, "삭제", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView tv = (TextView) v;
                tv.setText("--:--");
            }
        });
        timePicker.show();
    }
}
