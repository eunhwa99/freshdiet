package com.example.freshdiet.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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
    private String username, userage, userheight, userweight, usermeta;
    private String usergender; //성별

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        getData();
                        challengeMap=getMap();
                        setClick();
                        initScreen();
                    }
                }
            });

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


        getData();
        challengeMap=getMap();
        setClick();
        initScreen();
        return rootView;
    }

    private void setClick(){
        editbtn.setOnClickListener(view -> {
            Intent intent=new Intent(getContext(), MyProfile.class);
            //startActivity(intent);
            startActivityResult.launch(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initScreen(){
        nametv.setText(username);
        agetv.setText(userage);
        sextv.setText(usergender);
        weighttv.setText(userweight);
        heighttv.setText(userheight);
        metatv.setText(usermeta);

        for(Map.Entry<String, Long> entry : challengeMap.entrySet()){
            Long today=getDay();
            createTextView(entry.getKey(), today-entry.getValue());

        }

    }

    private void createTextView(String challenge, Long day){
        TextView txtview=new TextView(getActivity());
        txtview.setText(challenge);
        txtview.setTextSize(15);
        txtview.setTypeface(null, Typeface.BOLD);
        DisplayMetrics dm=getResources().getDisplayMetrics();
        int size=Math.round(10*dm.density);

        LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        param.topMargin=size;
        txtview.setLayoutParams(param);
        chtv.addView(txtview);

        TextView txtview2=new TextView(getActivity());
        txtview2.setText(day+" 일 째");
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
                String jsonString = pSharedPref.getString("Challengeday", (new JSONObject()).toString());
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

    private void getData() {
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Profile",MODE_PRIVATE);
        username=sharedPreferences.getString("UserName","Unknown");
        userage = sharedPreferences.getString("UserAge", "Unknown");
        userheight = sharedPreferences.getString( "UserHeight", "Unknown");
        userweight = sharedPreferences.getString("UserWeight","Unknown");
        usermeta = sharedPreferences.getString("UserMeta","Unknown");
        usergender=sharedPreferences.getString("UserGender","Unknown");
    }

}
