package com.example.freshdiet.calorie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.freshdiet.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class Toppings extends Activity {
    EditText name, amount, kal, carbo, protein, fat;
    HashMap<String, MultiHash> toppingMap=new HashMap<>();
    RadioGroup toppingradio;
    RadioButton mainradio, dessertradio;
    Button cancel, add;
    int type;
    String typestring;

    String name2;
    double amount2, kal2,carbo2,protein2, fat2;

    boolean[] check=new boolean[8];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.toppings);

        name=findViewById(R.id.toppingname);
        amount=findViewById(R.id.toppingamount);
        kal=findViewById(R.id.toppingkal);
        carbo=findViewById(R.id.toppingcarbo);
        protein=findViewById(R.id.toppingprotein);
        fat=findViewById(R.id.toppingfat);

        toppingradio=findViewById(R.id.toppingradio);
        toppingradio.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        mainradio=findViewById(R.id.mainradio);
        dessertradio=findViewById(R.id.dessertradio);

        cancel=findViewById(R.id.toppingcancel);
        add=findViewById(R.id.toppingadd);

        setListener();
    }

    // 라디오 그룹 클릭 리스너
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
          check[0]=true;
            if (i == R.id.mainradio) {
                type = 1;
                typestring="topping1";
                toppingMap=getMap("topping1");

            } else if (i == R.id.dessertradio) {
                type = 2;
                typestring="topping2";
                toppingMap=getMap("topping2");
            }
        }
    };

    public void setListener(){
        Intent intent=new Intent(getApplicationContext(), FoodDetail.class);

        cancel.setOnClickListener(view->{
            setResult(RESULT_CANCELED, intent);
            finish();
        });

        add.setOnClickListener(view->{
            int state=checked();
            if(state!=-1){
                showMessage(state);
            }
            else{
                getInfo();

                MultiHash multiHash=new MultiHash(amount2, "g", kal2, carbo2, protein2, fat2);
                toppingMap.put(name2, multiHash);
                setMap(typestring, toppingMap);
                Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_SHORT).show();

                intent.putExtra("type",type);
                setResult(RESULT_OK, intent);

                finish();
                toppingMap=getMap("topping1");
            }
        });
    }

    public int checked(){

        if(!check[0]) return 0;
        name2=name.getText().toString();
        if(name2.isEmpty()) return 1;
        if((amount.getText().toString().isEmpty())) return 2;
        if(kal.getText().toString().isEmpty()) return 3;
        if(carbo.getText().toString().isEmpty()) return 4;
        if(protein.getText().toString().isEmpty()) return 5;
        if(fat.getText().toString().isEmpty()) return 6;

        return -1;
    }

    public void showMessage(int state){
        String message="";
        if(state==0){
            message="토핑 종류를 선택하세요.";
        }
        else if(state==1){
            message="토핑 이름을 적어주세요.";
        }
        else if(state==2){
            message="토핑 제공량을 적어주세요.";
        }
        else if(state==3){
            message="토핑 칼로리를 적어주세요.";
        }
        else if(state==4){
            message="토핑 탄수화물을 적어주세요.";
        }
        else if(state==5){
            message="토핑 단백질을 적어주세요.";
        }
        else if(state==6){
            message="토핑 지방을 적어주세요.";
        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void getInfo(){
        name2=name.getText().toString();
        amount2=Double.parseDouble(amount.getText().toString());
        kal2=Double.parseDouble(kal.getText().toString());
        carbo2=Double.parseDouble(carbo.getText().toString());
        protein2=Double.parseDouble(protein.getText().toString());
        fat2=Double.parseDouble(fat.getText().toString());

    }

    public void setMap(String str,  HashMap<String,MultiHash> inputMap){
        SharedPreferences pSharedPref = getSharedPreferences(str, MODE_PRIVATE);
        if (pSharedPref != null){
           // JSONObject jsonObject = new JSONObject(inputMap);

            Gson gson = new Gson();
            String jsonString = gson.toJson(inputMap);

         //   String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(str+"Map").apply();
            editor.putString(str+"Map", jsonString);
            editor.commit();
        }
    }

    public  HashMap<String,MultiHash> getMap(String str){
        HashMap<String,MultiHash> outputMap = new HashMap<String,MultiHash>();
        SharedPreferences pSharedPref = getSharedPreferences(str, MODE_PRIVATE);

        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString(str+"Map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String val=String.valueOf(jsonObject.get(key));
                    Gson gson=new Gson();

                    MultiHash value = gson.fromJson(val, MultiHash.class);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            String sr=e.getMessage();
            Log.i("오류", sr);
            e.printStackTrace();
        }


        return outputMap;
    }

}
