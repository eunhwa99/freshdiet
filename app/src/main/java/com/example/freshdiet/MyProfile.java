package com.example.freshdiet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    Button inputBtn;
    EditText nametxt, agetxt, heighttxt, weighttxt, metatxt;
    RadioGroup sexRadio;
    RadioButton manRadio, womanRadio;

    private String username, userage, userheight, userweight, usermeta;
    private String gender; //성별
    private boolean[] check = new boolean[6]; //checkname, checkage, checksex, checkheight,checkweight,checkmeta;
    private String[] str = {"이름", "나이", "성별", "키", "몸무게"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        inputBtn = findViewById(R.id.inputbtn);

        nametxt = findViewById(R.id.nameedit);
        agetxt = findViewById(R.id.age);
        heighttxt = findViewById(R.id.height);
        weighttxt = findViewById(R.id.weight);
        metatxt = findViewById(R.id.metabolism);

        sexRadio = findViewById(R.id.sexRadio);
        sexRadio.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        manRadio = findViewById(R.id.man);
        womanRadio = findViewById(R.id.woman);

        clickListener(); // 버튼 클릭 리스너 지정

    }



    // 라디오 그룹 클릭 리스너
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            check[2] = true;
            if (i == R.id.man) {
                gender = "man";

            } else if (i == R.id.woman) {
                gender = "woman";
            }
        }
    };


    private void saveData() {
        SharedPreferences sharedPreferences=getSharedPreferences("Profile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserName", username);
        editor.putString("UserAge", userage);
        editor.putString( "UserHeight", userheight);
        editor.putString( "UserWeight", userweight);
        editor.putString( "UserMeta", usermeta); //기초대사량
        editor.putString("UserGender", gender);
        editor.commit();
    }

    private void clickListener() {
        inputBtn.setOnClickListener(new View.OnClickListener() {
            private boolean flag = true;

            @Override
            public void onClick(View view) {
                getEditText();

                for (int i = 0; i < 5; i++) { // 5: 기초대사량
                    if (!check[i]) {
                        flag = false;
                        makeAlertDialog("알림", str[i] + "를 입력하세요.");
                        break;
                    }
                }

                if (flag == true) {
                    if (check[5] == false)
                    {
                        Double temp=metabolism();
                        if(temp!=-1) {
                            usermeta = String.valueOf(temp);
                        }
                    }

                    saveData();
                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.putExtra("name",username);
                    intent.putExtra("age",userage);
                    intent.putExtra("gender",gender);
                    intent.putExtra("height",userheight);
                    intent.putExtra("weight",userweight);
                    intent.putExtra("meta",usermeta);
                    setResult(RESULT_OK, intent);
                    finish(); //팝업 닫기
                }
            }
        });

    }

    private void makeAlertDialog(String title, String message) {
        AlertDialog.Builder builder;
        AlertDialog alertDialog;

        builder = new AlertDialog.Builder(MyProfile.this);

        builder.setTitle(title).setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void getEditText() {

        if (nametxt.getText().toString().equals("") || nametxt.getText().toString() == null)
            check[0] = false;
        else {
            check[0] = true;
            username = nametxt.getText().toString();
        }

        if (agetxt.getText().toString().equals("") || agetxt.getText().toString() == null)
            check[1] = false;
        else {
            check[1] = true;
            userage = agetxt.getText().toString();
        }

        if (heighttxt.getText().toString().equals("") || heighttxt.getText().toString() == null)
            check[3] = false;
        else {
            check[3] = true;
            userheight = heighttxt.getText().toString();
        }
        if (weighttxt.getText().toString().equals("") || weighttxt.getText().toString() == null)
            check[4] = false;
        else {
            check[4] = true;
            userweight = weighttxt.getText().toString();
        }

        if (metatxt.getText().toString().equals("") || metatxt.getText().toString() == null)
            check[5] = false;
        else {
            check[5] = true;
            usermeta = metatxt.getText().toString();
        }

    }

    private double metabolism() {
        double height = Double.parseDouble(userheight);
        double weight = Double.parseDouble(userweight);
        int age = Integer.parseInt(userage);

        if (gender.equals("man")) {
            return (66.47 + (13.75 * weight) + (5 * height) - (6.76 * age));
        } else if (gender.equals("woman")) {
            return (665.1 + (9.56 * weight) + (1.85 * height) - (4.68 * age));
        }
        return -1;

    }


}
