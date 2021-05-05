package com.example.freshdiet.calorie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.freshdiet.R;

public class FoodInfoPopup  extends Activity {
    TextView foodname, foodkal, foodcarbo, foodprotein, foodfat;
    Intent intent;
    FoodInfo foodInfo;
    Button deletebtn, closebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.foodinfo_popup);

        foodname=findViewById(R.id.infopoptxt);
        foodkal=findViewById(R.id.infopopkal);
        foodcarbo=findViewById(R.id.infopopcarbo);
        foodprotein=findViewById(R.id.infopopprotein);
        foodfat=findViewById(R.id.infopopfat);
        deletebtn=findViewById(R.id.deletebtn2);
        closebtn=findViewById(R.id.closebtn2);

        dealIntent();
        setClick();
    }

    public void dealIntent(){
        intent=getIntent();
        foodInfo= (FoodInfo) intent.getSerializableExtra("foodinfo");

        foodname.setText(foodInfo.name);
        foodkal.setText(String.valueOf(foodInfo.kal));
        foodcarbo.setText(String.valueOf(foodInfo.carbo));
        foodprotein.setText(String.valueOf(foodInfo.protein));
        foodfat.setText(String.valueOf(foodInfo.fat));
    }

    public void setClick(){
        deletebtn.setOnClickListener(view->{
            intent.putExtra("delete",true);
            setResult(RESULT_OK, intent);
            finish();
        });
        closebtn.setOnClickListener(v -> {
            setResult(RESULT_CANCELED,intent);
            finish();
        });
    }
}
