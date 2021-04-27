package com.example.freshdiet.calorie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.freshdiet.R;

public class FoodDetail extends AppCompatActivity {
    ViewGroup rootView;
    TextView kal, carbo, protein, fat, foodname, foodname2;
    Button gram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fooddetail);

        foodname=findViewById(R.id.foodtxt);
        foodname2=findViewById(R.id.foodtxt2);
        kal=findViewById(R.id.kaltxt);
        carbo=findViewById(R.id.valtxt1);
        protein=findViewById(R.id.valtxt2);
        fat=findViewById(R.id.valtxt3);
        gram=findViewById(R.id.grambtn);

        dealIntent();
    }

    @SuppressLint("SetTextI18n")
    public void dealIntent(){
        Intent intent=getIntent();
        foodname.setText(intent.getStringExtra("food"));
        foodname2.setText(intent.getStringExtra("food2"));

        Double tmp=intent.getDoubleExtra("val1",0.0);
        String tmp2=intent.getStringExtra("val2");
        tmp2=tmp2.toLowerCase();
        gram.setText(tmp+tmp2);

        kal.setText(String.valueOf(intent.getDoubleExtra("val3",0.0)));
        protein.setText(String.valueOf(intent.getDoubleExtra("val4",0.0)));
        fat.setText(String.valueOf(intent.getDoubleExtra("val5",0.0)));
        carbo.setText(String.valueOf(intent.getDoubleExtra("val6",0.0)));

    }

}
