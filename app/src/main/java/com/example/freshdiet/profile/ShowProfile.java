package com.example.freshdiet.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.freshdiet.MainActivity;
import com.example.freshdiet.R;

public class ShowProfile extends Fragment {
    TextView nametv, agetv, sextv, weighttv, heighttv, metatv;
    Button editbtn;

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
    private void initScreen(){
        nametv.setText(MainActivity.username);
        agetv.setText(MainActivity.userage);
        sextv.setText(MainActivity.usergender);
        weighttv.setText(MainActivity.userweight);
        heighttv.setText(MainActivity.userheight);
        metatv.setText(MainActivity.usermeta);

    }
}
