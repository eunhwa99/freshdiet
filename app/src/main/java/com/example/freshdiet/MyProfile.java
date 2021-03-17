package com.example.freshdiet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MyProfile extends AppCompatActivity {
    Button inputBtn, helpBtn;
    EditText nametxt, agetxt, heighttxt, weighttxt, metatxt;
    RadioGroup sexRadio;
    RadioButton manRadio, womanRadio;

    private String username, userage, userheight, userweight, usermeta;
    private boolean[] check=new boolean[6]; //checkname, checkage, checksex, checkheight,checkweight,checkmeta;
    private String[] str={"이름", "나이", "성별", "키", "몸무게"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        inputBtn=findViewById(R.id.inputbtn);
        helpBtn=findViewById(R.id.helpbtn);

        nametxt=findViewById(R.id.nameedit);
        agetxt=findViewById(R.id.age);
        heighttxt=findViewById(R.id.height);
        weighttxt=findViewById(R.id.weight);
        metatxt=findViewById(R.id.metabolism);

        sexRadio=findViewById(R.id.sexRadio);
        manRadio=findViewById(R.id.man);
        womanRadio=findViewById(R.id.woman);

        getData();
        clickListener(); // 버튼 클릭 리스너 지정

    }

    // 라디오 그룹 클릭 리스너
   RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
       @Override
       public void onCheckedChanged(RadioGroup radioGroup, int i) {
           check[2]=true;
           if(i==R.id.man){

           }
           else if(i==R.id.woman){
           }
       }
   };

    private void getData(){
         check=PreferenceManager.getBooleanArray(MyProfile.this, "MyProfile");

    }

    private void saveData(){
        PreferenceManager.setBooleanArray(MyProfile.this, "MyProfile",check);
    }

    private void clickListener(){
        inputBtn.setOnClickListener(new View.OnClickListener() {
            private boolean flag=true;
            @Override
            public void onClick(View view) {
                getEditText();

                for(int i=0;i<6;i++){
                    if(!check[i]){
                       flag=false;
                       makeAlertDialog("알림", str[i]+"를 입력하세요.");
                        break;
                    }
                }
                if(flag==true) {
                    saveData();
                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void makeAlertDialog(String title, String message){
        AlertDialog.Builder builder;
        AlertDialog alertDialog;

        builder = new AlertDialog.Builder(MyProfile.this);

        builder.setTitle(title).setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void getEditText(){

        if(nametxt.getText().toString().equals("")||nametxt.getText().toString()==null) check[0]=false;
        else check[0]=true;

        if(agetxt.getText().toString().equals("")||agetxt.getText().toString()==null) check[1]=false;
        else check[1]=true;

        if(heighttxt.getText().toString().equals("")||heighttxt.getText().toString()==null) check[2]=false;
        else check[2]=true;
        if(weighttxt.getText().toString().equals("")||weighttxt.getText().toString()==null) check[3]=false;
        else check[3]=true;

    }


}
