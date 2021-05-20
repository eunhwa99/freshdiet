package com.example.freshdiet.challenge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.freshdiet.BroadcastD;

import java.util.Calendar;

public class AlarmHATT {
    private Context context;
    private int hour, min, sec, request;
    private String name;
    long timemillis, where;
    Calendar alarmCalendar;


    public AlarmHATT(Context context) {
        this.context=context;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void Alarm(int request, String name, int hour, int min, int where) {
        this.request=request;
        this.name=name;
        this.hour=hour;
        this.min=min;
        this.where=where;
        sec=0;
        alarmCalendar=Calendar.getInstance();

        setAlarm();
    }

    public void setAlarm(){
        //AlarmManager는 device에 미래에 대한 알림같은 행위를 등록할 때 사용
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, BroadcastD.class);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra("noti_title","챌린지");
        intent.putExtra("noti_content", name);
        intent.putExtra("request",request);
        intent.putExtra("hour",hour);
        intent.putExtra("min",min);
        //알람이 발생했을 경우, BradcastD에게 방송을 해주기 위해서 명시적으로 알려줍니다.

        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(Calendar.HOUR_OF_DAY,hour);
        alarmCalendar.set(Calendar.MINUTE,min);
        alarmCalendar.set(Calendar.SECOND,0);
        if(alarmCalendar.before(Calendar.getInstance()) || where==-1)
            alarmCalendar.add(Calendar.DATE,1);

        timemillis=alarmCalendar.getTimeInMillis();


        PendingIntent sender = PendingIntent.getBroadcast(context, request, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){ //23
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){ //19
                am.setExact(AlarmManager.RTC_WAKEUP, timemillis,sender);
            }
            else{
                am.set(AlarmManager.RTC_WAKEUP,timemillis,sender);
            }
        }
        else{
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timemillis,sender);
        }



        //  Toast.makeText(context, "알람이 설정되었습니다.",Toast.LENGTH_SHORT).show();
        // am.setRepeating(AlarmManager.RTC_WAKEUP, timemills, AlarmManager.INTERVAL_DAY,sender);
    }

    public void addDay(){
        alarmCalendar.add(Calendar.DATE,1);
    }

    public void noAlarm(int request){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, BroadcastD.class);
        PendingIntent sender=PendingIntent.getBroadcast(context, request, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(sender!=null) {
            am.cancel(sender);
            sender.cancel();
        }

    }

}
