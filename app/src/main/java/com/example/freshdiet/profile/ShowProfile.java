package com.example.freshdiet.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.freshdiet.MainActivity;
import com.example.freshdiet.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ShowProfile extends Fragment {
    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    LinearLayout chtv, daytv;
    TextView nametv, agetv, sextv, weighttv, heighttv, metatv;
    Button editbtn;
    HashMap<String, Long> challengeMap;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.showprofile, container, false);
        chtv=rootView.findViewById(R.id.chtv);
        daytv= rootView.findViewById(R.id.daytv);
        nametv=rootView.findViewById(R.id.nametv);
        agetv=rootView.findViewById(R.id.agetv);
        sextv=rootView.findViewById(R.id.sextv);
        weighttv=rootView.findViewById(R.id.weighttv);
        heighttv=rootView.findViewById(R.id.heighttv);
        metatv=rootView.findViewById(R.id.metatv);
        editbtn=rootView.findViewById(R.id.editprofile);


        challengeMap=getMap();
        setClick();
        initScreen();
        return rootView;
    }

    private void setClick(){
        editbtn.setOnClickListener(view -> {
            Intent intent=new Intent(getContext(), MyProfile.class);
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initScreen(){
        nametv.setText(MainActivity.username);
        agetv.setText(MainActivity.userage);
        sextv.setText(MainActivity.usergender);
        weighttv.setText(MainActivity.userweight);
        heighttv.setText(MainActivity.userheight);
        metatv.setText(MainActivity.usermeta);

        for(Map.Entry<String, Long> entry : challengeMap.entrySet()){
            Long today=getDay();
            createTextView(entry.getKey(), today-entry.getValue());
          //  chtv.setText(entry.getKey());
          //  chtv.setTextColor(Color.BLACK);
          //  daytv.setText(today-entry.getValue()+"일째");
           // daytv.setTextColor(Color.RED);
        }

    }

    private void createTextView(String challenge, Long day){
        TextView txtview=new TextView(getActivity());
        txtview.setText(challenge);
        txtview.setTextSize(15);
        txtview.setTypeface(null, Typeface.BOLD);
        //txtview.setId(0);
        LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        txtview.setLayoutParams(param);
        chtv.addView(txtview);

        TextView txtview2=new TextView(getActivity());
        txtview2.setText(day+" 일 진행");
        txtview2.setTextSize(15);
        txtview2.setTypeface(null, Typeface.BOLD);

       // txtview2.setId(0);
       txtview2.setTextColor(Color.RED);
        txtview2.setLayoutParams(param);
        daytv.addView(txtview2);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Long getDay(){
       // final Calendar ddayCalendar = Calendar.getInstance();
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        return today;
    }

    public HashMap<String,Long> getMap(){
        HashMap<String,Long> outputMap = new HashMap<String,Long>();
        SharedPreferences pSharedPref = getActivity().getSharedPreferences("Challenge", MODE_PRIVATE);

        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("Challenge_day", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = String.valueOf(jsonObject.get(key));
                    outputMap.put(key, Long.parseLong(value));
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
