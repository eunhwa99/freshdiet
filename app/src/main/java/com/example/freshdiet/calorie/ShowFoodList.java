package com.example.freshdiet.calorie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshdiet.DoubleMath;
import com.example.freshdiet.PreferenceManager;
import com.example.freshdiet.R;
import com.example.freshdiet.plan.Calendar;

import java.util.ArrayList;

import static com.example.freshdiet.plan.Calendar.context;
import static com.example.freshdiet.plan.Calendar.selectedDay2;

public class ShowFoodList extends Fragment {
    public ArrayList<FoodInfo> arrayList;
    RecyclerView foodrv;
    FoodAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    TextView infokal, infocarbo, infoprotein, infofat;
    double eat_calorie, eat_carbo, eat_protein, eat_fat;
    int position;
    DoubleMath doubleMath=new DoubleMath();

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data=result.getData();
                        boolean delete=data.getBooleanExtra("delete",false);
                        if(delete){ //삭제
                            FoodInfo foodInfo=arrayList.get(position);
                            eat_calorie-=foodInfo.kal;
                            eat_carbo-=foodInfo.carbo;
                            eat_protein-=foodInfo.protein;
                            eat_fat-=foodInfo.fat;

                            eat_calorie=doubleMath.calc(eat_calorie);
                            eat_carbo=doubleMath.calc(eat_carbo);
                            eat_protein=doubleMath.calc(eat_protein);
                            eat_fat=doubleMath.calc(eat_fat);

                            arrayList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            setTextview();
                            saveData();
                        }
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.food_info_recyclerview, container, false);

        foodrv=rootView.findViewById(R.id.foodrv);
        infokal=rootView.findViewById(R.id.infokal);
        infocarbo=rootView.findViewById(R.id.infocarbo);
        infoprotein=rootView.findViewById(R.id.infoprotein);
        infofat=rootView.findViewById(R.id.infofat);

        getData();
        setTextview();
        initRecycleView();

        return rootView;
    }

    public void initRecycleView(){
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // 기본값이 VERTICAL
        foodrv.setLayoutManager(mLayoutManager);
        mAdapter=new FoodAdapter(2,arrayList);
        // set Adapter
        foodrv.setAdapter(mAdapter);

        setClick();
    }

    public void setTextview(){
        infokal.setText(String.valueOf(eat_calorie));
        infocarbo.setText(String.valueOf(eat_carbo));
        infoprotein.setText(String.valueOf(eat_protein));
        infofat.setText(String.valueOf(eat_fat));
    }

    public void setClick(){
        mAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                /**
                 * 팝업창 띄우기
                 */
                FoodInfo foodInfo=arrayList.get(pos);
                position=pos;
                Intent intent=new Intent(getContext(), FoodInfoPopup.class);
                intent.putExtra("foodinfo", foodInfo);
                startActivityResult.launch(intent);
            }
        });

    }
    public void getData(){
        SharedPreferences sharedPreferences= context.getSharedPreferences(selectedDay2+"act", Context.MODE_PRIVATE);
        eat_calorie=Double.parseDouble(sharedPreferences.getString("eat_calorie","0.0"));
        eat_carbo=Double.parseDouble(sharedPreferences.getString("eat_carbo","0.0"));
        eat_protein=Double.parseDouble(sharedPreferences.getString("eat_protein","0.0"));
        eat_fat=Double.parseDouble(sharedPreferences.getString("eat_fat","0.0"));

        arrayList= PreferenceManager.getFoodArrayList(Calendar.context, selectedDay2+"food");
        if(arrayList==null)
            arrayList=new ArrayList<>();
    }

    public void saveData(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(selectedDay2+"act", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("eat_calorie",String.valueOf(eat_calorie));
        editor.putString("eat_carbo", String.valueOf(eat_carbo));
        editor.putString("eat_protein",String.valueOf(eat_protein));
        editor.putString("eat_fat", String.valueOf(eat_fat));
        editor.apply();

        PreferenceManager.setFoodArrayList(context,selectedDay2+"food", arrayList);
    }

}
