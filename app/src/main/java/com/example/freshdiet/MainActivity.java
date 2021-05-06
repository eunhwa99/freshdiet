package com.example.freshdiet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.freshdiet.calorie.FoodMain;
import com.example.freshdiet.challenge.ChallengeMain;
import com.example.freshdiet.plan.Calendar;
import com.example.freshdiet.plan.MakePlan;
import com.example.freshdiet.profile.MyProfile;
import com.example.freshdiet.profile.ShowProfile;
import com.google.android.material.navigation.NavigationView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,FragmentCallback {


    private DrawerLayout mDrawerLayout;
    private Context context = this;
    public static String username, userage, userheight, userweight, usermeta, usergender;

    Fragment calendarf, makeplanf, profilef, challengef,settingsf, foodmainf;
    Toolbar toolbar;
    static FragmentManager manager;
    private boolean isMembersVisible = false;
    NavigationView navigationView;
    public static Stack<Fragment> fragmentStack;


    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data=result.getData();
                        getData();
                        initScreen();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentStack=new Stack<>();

        getData();
        if(username.equals("Unknown")){
            Intent intent=new Intent(getApplicationContext(), MyProfile.class);
            startActivityResult.launch(intent);

        }

        else {
            initScreen();
        }
    }

    private void initScreen(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
      //  assert actionBar!=null;
        assert actionBar != null;
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
            actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24); //뒤로가기 버튼 이미지 지정
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        calendarf = new Calendar();

        profilef = new ShowProfile();
        makeplanf = new MakePlan();
        challengef = new ChallengeMain();
        foodmainf = new FoodMain();
        // fooddessertf=new FoodDessert();

        manager = getSupportFragmentManager();

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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragmentStack.push(calendarf);

        switch (position){
            case 0:
                fragment=profilef;

                break;
            case 1:
                fragment = calendarf;
                fragmentStack.pop();
              //  toolbar.setTitle("계획표");
                break;

            case 2:
                fragment=foodmainf;
                break;

            case 4:
                fragment=challengef;

                break;
            case 5:
                fragment=settingsf;

            default:
                break;
        }
      //  getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        transaction.replace(R.id.container,fragment).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        int id = item.getItemId();
      //  String title = item.getTitle().toString();
        while(!fragmentStack.isEmpty())
            fragmentStack.pop();

        if(id == R.id.menu0){
            onChangedFragment(0, null);
        }
        else if(id == R.id.menu1){
            onChangedFragment(1, null);
        }

        else if(id==R.id.menu3){
           onChangedFragment(2,null);
        }

        else if(id==R.id.menu4){
            onChangedFragment(4, null);
        }
        else if(id==R.id.menu5){
            onChangedFragment(5, null);
        }

        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(!fragmentStack.isEmpty()){
            Fragment nextFragment = fragmentStack.pop();
            manager.beginTransaction().replace(R.id.container, nextFragment).commit();
        }
        else{
            super.onBackPressed();
        }
    }

    public static void changeText(double cal){
        MakePlan mp=(MakePlan)manager.findFragmentById(R.id.container);
        mp.updateCalorie(cal);
    }


}


