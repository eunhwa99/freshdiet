package com.example.freshdiet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

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
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,FragmentCallback{

    //http://blog.naver.com/PostView.nhn?blogId=qbxlvnf11&logNo=221641795446&categoryNo=37&parentCategoryNo=0&viewDate=&currentPage=1&postListTopCurrentPage=1&from=search

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    public static String username, userage, userheight, userweight, usermeta, usergender;

    Fragment calendarf, profilef, calorief, challengef;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData();
        if(username.equals("Unknown")){
            Intent intent=new Intent(getApplicationContext(), MyProfile.class);
            startActivityForResult(intent,1004);
        }

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24); //뒤로가기 버튼 이미지 지정

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        calendarf=new Calendar();
        //profilef=new Fragment();
        //calendarf=new Fragment();
        //challengef=new Fragment();
        // calendar 화면을 첫 번째 fragment로 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.container, calendarf).commit();
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

    @Override
    public void onChangedFragment(int position, Bundle bundle) {
        Fragment fragment = null;

        switch (position){
            case 1:
                fragment = profilef;
                toolbar.setTitle("첫 번째 화면");
                break;
            case 2:
                fragment = calorief;
                toolbar.setTitle("두 번째 화면");
                break;
            case 3:
                fragment = challengef;
                toolbar.setTitle("세 번째 화면");
                break;
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        mDrawerLayout.closeDrawers();

        int id = item.getItemId();
        String title = item.getTitle().toString();

        if(id == R.id.account){
            onChangedFragment(1, null);
            Toast.makeText(context, title + ": 계정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.setting){
            onChangedFragment(2, null);
            Toast.makeText(context, title + ": 설정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.logout){
            onChangedFragment(3, null);
            Toast.makeText(context, title + ": 로그아웃 시도중", Toast.LENGTH_SHORT).show();
        }
        else{
            onChangedFragment(4, null);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
