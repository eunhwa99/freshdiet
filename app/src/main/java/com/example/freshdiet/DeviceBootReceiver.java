package com.example.freshdiet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.freshdiet.challenge.AlarmHATT;
import com.example.freshdiet.challenge.ChallengeSub;

public class DeviceBootReceiver extends BroadcastReceiver {
    String[] alarmlist;
    Context mcontext;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext= ChallengeSub.challengContext;
        String action=intent.getAction();
        alarmlist=new String[10];

        if(action.equals("android.intent.action.BOOT_COMPLETED")){
            AlarmHATT alarmHATT = new AlarmHATT(context);
            alarmlist=getAlarm();
            for(int i=0;i<alarmlist.length;i++){

                if(alarmlist[i].equals("")||alarmlist[i]==null){
                    continue;
                }

                String[] temp=alarmlist[i].split(" ");
                String name=temp[0];
                int hour=Integer.parseInt(temp[1]);
                int min=Integer.parseInt(temp[2]);

                alarmHATT.Alarm(i, name, hour,min,0); //alarm 설정
            }
        }
    }

    public String[] getAlarm(){
        SharedPreferences prefs=mcontext.getSharedPreferences("Alarm",Context.MODE_PRIVATE);
        int size = prefs.getInt("Alarmlist" + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString("Alarmlist" + "_" + i, null);
        return array;
    }
}
