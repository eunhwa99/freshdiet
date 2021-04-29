package com.example.freshdiet.calorie;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshdiet.R;

import java.util.ArrayList;

public class FoodDetail extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    RecyclerView toppinglv;
    HorizontalAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    TextView kal, carbo, protein, fat, foodname, foodname2;
    Button gram;
    Double amount, kal_amount, carbo_amount, protein_amount, fat_amount;
    String unit;
    String floating[]={"1","1/2","1/3","1/4","1/5","1/6","1/7","1/8"};
    ArrayList<String> toppings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fooddetail);

        toolbar=findViewById(R.id.toolbar2);
        foodname=findViewById(R.id.foodtxt);
        foodname2=findViewById(R.id.foodtxt2);
        kal=findViewById(R.id.kaltxt);
        carbo=findViewById(R.id.valtxt1);
        protein=findViewById(R.id.valtxt2);
        fat=findViewById(R.id.valtxt3);
        gram=findViewById(R.id.grambtn);
        toppinglv=findViewById(R.id.toppinglv);

        initRecylerView();

        setClicklistview();

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
            show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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

    public void show(){
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

               double val=amount*tmp;
               val=Math.round(val*10)/10.0;
                amount=val;

               val=kal_amount*tmp;
               val=Math.round(val*10)/10.0;
                kal_amount=val;

                val=carbo_amount*tmp;
                val=Math.round(val*10)/10.0;
                carbo_amount=val;

                val=protein_amount*tmp;
                val=Math.round(val*10)/10.0;
                protein_amount=val;

                val=fat_amount*tmp;
                val=Math.round(val*10)/10.0;
                fat_amount=val;

                gram.setText(amount+unit);
                kal.setText(String.valueOf(kal_amount));
                fat.setText(String.valueOf(fat_amount));
                carbo.setText(String.valueOf(carbo_amount));
                protein.setText(String.valueOf(protein_amount));
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

    public void initRecylerView(){
        mLayoutManager = new LinearLayoutManager(FoodDetail.this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL

        toppinglv.setLayoutManager(mLayoutManager);
        mAdapter=new HorizontalAdapter();

        ArrayList<HorizontalData> data = new ArrayList<>();
        int i = 0;
        while (i < 10) {
            data.add(new HorizontalData(i+"번째 데이터"));
            i++;
        }

        // set Data
        mAdapter.setData(data);

        // set Adapter
        toppinglv.setAdapter(mAdapter);

    }

    public void setClicklistview(){

    }



}
