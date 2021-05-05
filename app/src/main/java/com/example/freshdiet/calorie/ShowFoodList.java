package com.example.freshdiet.calorie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.freshdiet.PreferenceManager;
import com.example.freshdiet.R;
import com.example.freshdiet.plan.Calendar;

import java.util.ArrayList;

import static com.example.freshdiet.plan.Calendar.curDate2;

public class ShowFoodList extends Fragment {
    public ArrayList<FoodInfo> arrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.food_info_recyclerview, container, false);

        arrayList= PreferenceManager.getFoodArrayList(Calendar.context, curDate2+"food");

        return rootView;
    }

}
