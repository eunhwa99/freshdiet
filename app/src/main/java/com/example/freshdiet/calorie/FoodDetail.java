package com.example.freshdiet.calorie;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshdiet.R;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FoodDetail extends AppCompatActivity{
    ScrollView sv;
    Toolbar toolbar;
    ActionBar actionBar;
    RecyclerView toppinglv,toppinglv2;
    HorizontalAdapter mAdapter, mAdapter2;
    LinearLayoutManager mLayoutManager, mLayoutManager2;
    LinearLayout tp1ly, tp1ly2, tp2ly, tp2ly2;

    TextView kal, carbo, protein, fat, foodname, foodname2;
    TextView toppingtxt,toppingtxt2, kaltxt2, kaltxt3,tvaltxt1, tvaltxt2, tvaltxt3, tvaltxt4, tvaltxt5, tvaltxt6;
    Button gram, gram2, gram3;
    Button addbtn1, addbtn2, addbtn3, floating_btn;
    double amount, kal_amount, carbo_amount, protein_amount, fat_amount;
    double amount2, kal_amount2, carbo_amount2, protein_amount2, fat_amount2;
    double amount3, kal_amount3, carbo_amount3, protein_amount3, fat_amount3;
    String unit,unit2, unit3;
    String floating[]={"1","1/2","1/3","1/4","1/5","1/6","1/7","1/8"};

    HashMap<String, MultiHash> toppingMap1=new HashMap<>();
    HashMap<String, MultiHash> toppingMap2=new HashMap<>();
    //HashMap<String, MultiHash> foodinfoMap=new HashMap<>();

    ArrayList<FoodInfo> foodinfolist=new ArrayList<>();

    public static ArrayList<FoodListItem> foodlist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fooddetail);

        sv=findViewById(R.id.foodsv);
        toolbar=findViewById(R.id.toolbar2);
        foodname=findViewById(R.id.foodtxt);
        foodname2=findViewById(R.id.foodtxt2);
        kal=findViewById(R.id.kaltxt);
        carbo=findViewById(R.id.valtxt1);
        protein=findViewById(R.id.valtxt2);
        fat=findViewById(R.id.valtxt3);

        gram=findViewById(R.id.grambtn);
        gram2=findViewById(R.id.grambtn2);
        gram3=findViewById(R.id.grambtn3);

        toppinglv=findViewById(R.id.toppinglv);
        toppinglv2=findViewById(R.id.toppinglv2);

        tp1ly=findViewById(R.id.topping1ly);
        tp1ly2=findViewById(R.id.topping1ly2);
        tp2ly=findViewById(R.id.topping2ly);
        tp2ly2=findViewById(R.id.topping2ly2);

        toppingtxt=findViewById(R.id.toppingtxt);
        toppingtxt2=findViewById(R.id.toppingtxt2);
        kaltxt2=findViewById(R.id.kaltxt2); //토핑1 칼로리
        kaltxt3=findViewById(R.id.kaltxt3); //토핑2 칼로리
        tvaltxt1=findViewById(R.id.tvaltxt1); // 토핑1 탄수화물
        tvaltxt2=findViewById(R.id.tvaltxt2); //토핑1 단백질
        tvaltxt3=findViewById(R.id.tvaltxt3); //토핑1 지방
        tvaltxt4=findViewById(R.id.tvaltxt4); //토핑2 탄수화물
        tvaltxt5=findViewById(R.id.tvaltxt5); //토핑2 단백질
        tvaltxt6=findViewById(R.id.tvaltxt6); //토핑2 지방

        addbtn1=findViewById(R.id.addbtn1);
        addbtn2=findViewById(R.id.addbtn2);
        addbtn3=findViewById(R.id.addbtn3);

        floating_btn=findViewById(R.id.floating_btn);

       // toolbar.bringToFront();
        scrollDown();
        initRecylerView();

        setToolbar();
        setButtonClicked();
        dealIntent();
    }

    public void setToolbar(){
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        assert actionBar != null;

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 없애줌
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼
    }

    public void setButtonClicked(){
        gram.setOnClickListener(view->{
            show(1);
        });
        gram2.setOnClickListener(view->{
            show(2);
        });
        gram3.setOnClickListener(view->{
            show(3);
        });

        addbtn1.setOnClickListener(view->{
            addFood(foodname.getText().toString(),amount, unit);
            foodinfolist.add(new FoodInfo(foodname.getText().toString(),amount,unit, kal_amount,carbo_amount,protein_amount,fat_amount));
            //foodinfoMap.put(foodname.getText().toString(),new MultiHash(amount,unit, kal_amount,carbo_amount,protein_amount,fat_amount));
        });
        addbtn2.setOnClickListener(view->{
            addFood(toppingtxt.getText().toString(),amount2, unit2);
            foodinfolist.add(new FoodInfo(toppingtxt.getText().toString(),amount2,unit2, kal_amount2,carbo_amount2,protein_amount2,fat_amount2));
            //foodinfoMap.put(toppingtxt.getText().toString(),new MultiHash(amount2,unit2, kal_amount2,carbo_amount2,protein_amount2,fat_amount2));
        });
        addbtn3.setOnClickListener(view->{
            addFood(toppingtxt2.getText().toString(),amount3, unit3);
            foodinfolist.add(new FoodInfo(toppingtxt2.getText().toString(),amount3,unit3, kal_amount3,carbo_amount3,protein_amount3,fat_amount3));
           // foodinfoMap.put(toppingtxt2.getText().toString(),new MultiHash(amount3,unit3, kal_amount3,carbo_amount3,protein_amount3,fat_amount3));
        });

        floating_btn.setOnClickListener(view->{
            Intent intent=new Intent();
            intent.putExtra("added_foodinfo",foodinfolist);
            Toast.makeText(FoodDetail.this, "추가되었습니다.",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, intent);
            finish(); //팝업 닫기
        });
    }


    @SuppressLint("SetTextI18n")
    public void dealIntent(){
        Intent intent=getIntent();
        foodname.setText(intent.getStringExtra("food"));
        foodname2.setText(intent.getStringExtra("food2"));

        amount=intent.getDoubleExtra("val1",0.0);
        unit=intent.getStringExtra("val2");
        unit=unit.toLowerCase();
        gram.setText(amount+unit);

        kal_amount=intent.getDoubleExtra("val3",0.0);
        kal.setText(String.valueOf(kal_amount));

        protein_amount=intent.getDoubleExtra("val4",0.0);
        protein.setText(String.valueOf(protein_amount));

        fat_amount=intent.getDoubleExtra("val5",0.0);
        fat.setText(String.valueOf(fat_amount));

        carbo_amount=intent.getDoubleExtra("val6",0.0);
        carbo.setText(String.valueOf(carbo_amount));

    }

    public void show(int num){
        final Dialog d = new Dialog(FoodDetail.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        d.setContentView(R.layout.numberpicker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);

        np.setMaxValue(floating.length-1);
        np.setMinValue(0);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(floating);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                double tmp=0.0;
               switch(floating[np.getValue()]){
                   case "1/8":
                       tmp=0.125;
                       break;
                   case "1/7":
                       tmp=1.0/7.0;
                       break;
                   case "1/6":
                       tmp=1.0/6.0;
                       break;
                   case "1/5":
                       tmp=0.2;
                       break;
                   case "1/4":
                       tmp=0.25;
                       break;
                   case "1/3":
                       tmp=1.0/3.0;
                       break;
                   case "1/2":
                       tmp=0.5;
                       break;
                   case "1":
                       tmp=1.0;
                       break;
               }

               if(num==1) {
                   amount=change(amount, tmp);
                   kal_amount = change(kal_amount,tmp);
                   carbo_amount = change(carbo_amount,tmp);
                   protein_amount=change(protein_amount,tmp);
                   fat_amount = change(fat_amount,tmp);

                   gram.setText(amount + unit);
                   kal.setText(String.valueOf(kal_amount));
                   fat.setText(String.valueOf(fat_amount));
                   carbo.setText(String.valueOf(carbo_amount));
                   protein.setText(String.valueOf(protein_amount));
               }
               else if(num==2){
                   amount2=change(amount2, tmp);
                   kal_amount2 = change(kal_amount2,tmp);
                   carbo_amount2 = change(carbo_amount2,tmp);
                   protein_amount2=change(protein_amount2,tmp);
                   fat_amount2 = change(fat_amount2,tmp);

                   gram2.setText(amount2 + unit2);
                   kaltxt2.setText(String.valueOf(kal_amount2));
                   tvaltxt1.setText(String.valueOf(carbo_amount2));
                   tvaltxt2.setText(String.valueOf(protein_amount2));
                   tvaltxt3.setText(String.valueOf(fat_amount2));
               }
               else if(num==3){
                   amount3=change(amount3, tmp);
                   kal_amount3 = change(kal_amount3,tmp);
                   carbo_amount3 = change(carbo_amount3,tmp);
                   protein_amount3=change(protein_amount3,tmp);
                   fat_amount3 = change(fat_amount3,tmp);

                   gram3.setText(amount3 + unit3);
                   kaltxt3.setText(String.valueOf(kal_amount3));
                   tvaltxt4.setText(String.valueOf(carbo_amount3));
                   tvaltxt5.setText(String.valueOf(protein_amount3));
                   tvaltxt6.setText(String.valueOf(fat_amount3));

               }
                d.dismiss();
            }

        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }

        });

        d.show();
    }

    public double change(double d1, double tmp){
        double val=d1*tmp;
        val=Math.round(val*10)/10.0;
        return val;
    }

    public void initRecylerView(){
        mLayoutManager = new LinearLayoutManager(FoodDetail.this);
        mLayoutManager2=new LinearLayoutManager(FoodDetail.this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

        toppinglv.setLayoutManager(mLayoutManager);
        toppinglv2.setLayoutManager(mLayoutManager2);

        mAdapter=new HorizontalAdapter();
        mAdapter2=new HorizontalAdapter();

        ArrayList<HorizontalData> data =getData("topping1", toppingMap1);
        ArrayList<HorizontalData> data2=getData("topping2",toppingMap2);

        // set Data
        mAdapter.setData(data);
        mAdapter2.setData(data2);

        // set Adapter
        toppinglv.setAdapter(mAdapter);
        toppinglv2.setAdapter(mAdapter2);
        setClick();
    }


    public ArrayList<HorizontalData> getData(String str, HashMap map){
        ArrayList<HorizontalData> data = new ArrayList<>();
        MultiHash multiHash;
        try {
            InputStreamReader is = new InputStreamReader(getResources().getAssets().open(str+".csv"));
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
                        data.add(new HorizontalData(nextLine[i]));
                        key=nextLine[i];
                    }
                    else if(i==1){
                        val1=Double.parseDouble(nextLine[i]); // 1회 제공량
                    }
                    else if(i==2){
                        val2=nextLine[i]; //1회 제공량 단위
                    }
                    else if(i==3){ //에너지
                        val3=Double.parseDouble(nextLine[i]);
                    }
                    else if(i==4){ //단백질
                        val4=Double.parseDouble(nextLine[i]);
                    }
                    else if(i==5){ //지방
                        val5=Double.parseDouble(nextLine[i]);
                    }
                    else if(i==6){ //탄수화물
                        val6=Double.parseDouble(nextLine[i]);
                    }
                }
                multiHash=new MultiHash(val1, val2, val3, val4, val5, val6);
                map.put(key, multiHash);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void setClick(){
        mAdapter.setOnItemClickListener(new HorizontalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
               // HorizontalData data=(HorizontalData) v.getI(pos);
                String data=mAdapter.getItem(pos).getText();
                MultiHash multiHash=toppingMap1.get(data);

                if(tp1ly.getVisibility()==View.VISIBLE){
                    if(data.equals(toppingtxt.getText().toString())){
                        tp1ly.setVisibility(View.GONE);
                        tp1ly2.setVisibility(View.GONE);
                        return;
                    }
                }

               else {
                    tp1ly.setVisibility(View.VISIBLE);
                    tp1ly2.setVisibility(View.VISIBLE);
                }

                toppingtxt.setText(data);
                kaltxt2.setText(String.valueOf(multiHash.val3));
                tvaltxt1.setText(String.valueOf(multiHash.val6));
                tvaltxt2.setText(String.valueOf(multiHash.val4));
                tvaltxt3.setText(String.valueOf(multiHash.val5));

                amount2=multiHash.val1;
                unit2=multiHash.val2;
                kal_amount2=multiHash.val3;
                carbo_amount2=multiHash.val6;
                protein_amount2=multiHash.val4;
                fat_amount2=multiHash.val5;

                gram2.setText(multiHash.val1+multiHash.val2);
            }
        });

        mAdapter2.setOnItemClickListener(new HorizontalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                // HorizontalData data=(HorizontalData) v.getI(pos);
                String data=mAdapter2.getItem(pos).getText();
                MultiHash multiHash=toppingMap2.get(data);


                if(tp2ly.getVisibility()==View.VISIBLE){
                    if(data.equals(toppingtxt2.getText().toString())){
                        tp2ly.setVisibility(View.GONE);
                        tp2ly2.setVisibility(View.GONE);
                        return;
                    }
                }

                else {
                    tp2ly.setVisibility(View.VISIBLE);
                    tp2ly2.setVisibility(View.VISIBLE);
                }


                toppingtxt2.setText(data);
                kaltxt3.setText(String.valueOf(multiHash.val3));
                tvaltxt4.setText(String.valueOf(multiHash.val6));
                tvaltxt5.setText(String.valueOf(multiHash.val4));
                tvaltxt6.setText(String.valueOf(multiHash.val5));

                amount3=multiHash.val1;
                unit3=multiHash.val2;
                kal_amount3=multiHash.val3;
                carbo_amount3=multiHash.val6;
                protein_amount3=multiHash.val4;
                fat_amount3=multiHash.val5;

                gram3.setText(multiHash.val1+multiHash.val2);
            }
        });
    }

    public void scrollDown(){ //동적으로 스크롤 변할 때 끝까지 내려가도록
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    /**
     * Toolbar 메뉴
     */
    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.food_added:
                Intent intent=new Intent(FoodDetail.this, FoodPopup.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
              //  Toast.makeText(getApplicationContext(), "나머지 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);

        }
    }

    public void addFood(String name, double v1, String v2){ // 음식 이름, 음식 양, 단위
        FoodListItem foodListItem=new FoodListItem();
        foodListItem.setTitle(name);
        foodListItem.setDesc(v1+v2);
        foodlist.add(foodListItem);

        Toast.makeText(FoodDetail.this, "추가되었습니다.",Toast.LENGTH_SHORT).show();
    }



}
