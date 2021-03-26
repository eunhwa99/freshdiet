package com.example.freshdiet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshdiet.challenge.ChallengeMain;
import com.example.freshdiet.plan.Calendar;
import com.example.freshdiet.plan.MakePlan;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;

//https://wonpaper.tistory.com/164
public class MainActivity extends AppCompatActivity {

    //http://blog.naver.com/PostView.nhn?blogId=qbxlvnf11&logNo=221641795446&categoryNo=37&parentCategoryNo=0&viewDate=&currentPage=1&postListTopCurrentPage=1&from=search

        private DrawerLayout mDrawerLayout;
        private Context context = this;
      public static String username, userage, userheight, userweight, usermeta, usergender;
    public CalendarView calendarView;
    public TextView diaryTextView,calendarText, meta_cal, act_cal, eat_cal, rest_cal;
    public Button addbtn;
    private String selectedDay, selectedDay2;
    public static String curDate2;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        calendarView=findViewById(R.id.calendarView);
        diaryTextView=findViewById(R.id.diaryTextView);
        addbtn=findViewById(R.id.addbtn);
        calendarText=findViewById(R.id.calendarText);
        meta_cal=findViewById(R.id.cal_meta);
        act_cal=findViewById(R.id.cal_act);
        eat_cal=findViewById(R.id.cal_eat);
        rest_cal=findViewById(R.id.cal_rest);

            getData();
            setListener(); // button 이벤트 추가
        initScreen(); // 화면 초기화



        Toolbar toolbar =  findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
            actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24); //뒤로가기 버튼 이미지 지정

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();

                    int id = menuItem.getItemId();
                    String title = menuItem.getTitle().toString();

                    if(id == R.id.account){
                        Toast.makeText(context, title + ": 계정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if(id == R.id.setting){
                        Toast.makeText(context, title + ": 설정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if(id == R.id.logout){
                        Toast.makeText(context, title + ": 로그아웃 시도중", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
            });
        }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    return true;
                }
            }
            return super.onOptionsItemSelected(item);
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

    public void setListener(){
        addbtn.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MakePlan.class);
                intent.putExtra("selectedDay",selectedDay);
                intent.putExtra("selectedDay2",selectedDay2);
                startActivity(intent);

            }
        });
    }

    public void initScreen() {
        calendarText.setText(username+"님의 달력 일기장");
        SimpleDateFormat format = new SimpleDateFormat( "yyyy / MM / dd");
        SimpleDateFormat format2=new SimpleDateFormat("yyyyMMdd");
        Date time = new Date();
        String curDate = format.format(time);
        curDate2=format2.format(time);
        diaryTextView.setText(curDate);
        selectedDay=curDate;
        selectedDay2=curDate2;
        getData(curDate2);

        // 계획표 및 달성 등의 정보 가지고 오기
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d",year,month+1,dayOfMonth));
                selectedDay=diaryTextView.getText().toString();
            }
        });
    }

    private void getData(String date){
        SharedPreferences sharedPreferences=getSharedPreferences(date+"act", MODE_PRIVATE);
        String str=sharedPreferences.getString("act_calorie","0.0");
        act_cal.setText(str+"(kcal)");
        meta_cal.setText(MainActivity.usermeta+"(kcal)");
        /**
         * 섭취 칼로리, 잔여 칼로리 표시
         */
    }


}


 /*   public static String username, userage, userheight, userweight, usermeta, usergender;
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
                intent=new Intent(getApplicationContext(), ChallengeMain.class);
                startActivity(intent);
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
    }*/
