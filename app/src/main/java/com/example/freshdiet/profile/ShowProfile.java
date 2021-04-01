package com.example.freshdiet.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    TextView nametv, agetv, sextv, weighttv, heighttv, metatv, chtv;
    Button editbtn;
    HashMap<String, Long> challengeMap;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.showprofile, container, false);
       nametv=rootView.findViewById(R.id.nametv);
       agetv=rootView.findViewById(R.id.agetv);
        sextv=rootView.findViewById(R.id.sextv);
        weighttv=rootView.findViewById(R.id.weighttv);
        heighttv=rootView.findViewById(R.id.heighttv);
        metatv=rootView.findViewById(R.id.metatv);
        editbtn=rootView.findViewById(R.id.editprofile);
        chtv=rootView.findViewById(R.id.chtv);

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

            chtv.setText("챌린지 : " + entry.getKey() + "  " + String.valueOf(today-entry.getValue())+"일째");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Long getDay(){
        final Calendar ddayCalendar = Calendar.getInstance();
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        return today;
    }

    public HashMap<String,Long> getMap(){
        HashMap<String,Long> outputMap = new HashMap<String,Long>();
        SharedPreferences pSharedPref = getActivity().getSharedPreferences("Challenge", MODE_PRIVATE);

        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("challenge_day", (new JSONObject()).toString());
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
