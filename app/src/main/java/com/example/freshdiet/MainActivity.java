package com.example.freshdiet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static String username, userage, userheight, userweight, usermeta, usergender;
    Button btn1,btn2,btn3,btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1=findViewById(R.id.profilebtn);
        btn2=findViewById(R.id.calendarbtn);
        btn3=findViewById(R.id.caloriebtn);
        btn4=findViewById(R.id.challengbtn);

        getData();
        if(username.equals("Unknown")){
            Intent intent=new Intent(getApplicationContext(), MyProfile.class);
            startActivityForResult(intent,1004);
        }
    }

    public void gotoMenu(View v){
        Intent intent;
        switch(v.getId()){
            case R.id.profilebtn:
                //https://asukim.tistory.com/59
                intent=new Intent(getApplicationContext(), MyProfile.class);
                startActivity(intent);
                break;
            case R.id.calendarbtn:
                intent=new Intent(getApplicationContext(), Calendar.class);
                startActivity(intent);
                break;
            case R.id.caloriebtn:
                break;
            case R.id.challengbtn:
                break;

        }
    }

    private void getData() {
        SharedPreferences sharedPreferences=getSharedPreferences("Profile",MODE_PRIVATE);
        username=sharedPreferences.getString("UserName","Unknown");
        userage = sharedPreferences.getString("UserAge", "Unknown");
        userheight = sharedPreferences.getString( "UserHeight", "Unknown");
        userweight = sharedPreferences.getString("UserWeight","Unknown");
        usermeta = sharedPreferences.getString("UserMeta","Unknown");
        usergender=sharedPreferences.getString("UserGender","Unknown");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1004:
                if(resultCode==RESULT_OK){
                    username=data.getStringExtra("name");
                    userage=data.getStringExtra("age");
                    userheight=data.getStringExtra("height");
                    userweight=data.getStringExtra("weight");
                    usermeta=data.getStringExtra("meta");
                    usergender=data.getStringExtra("gender");
                }
        }
    }
}