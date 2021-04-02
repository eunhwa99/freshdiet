package com.example.freshdiet.challenge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class AlarmHATT {
    private Context context;
    public AlarmHATT(Context context) {
        this.context=context;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void Alarm(int request, String name, long timemills) {
        //AlarmManager는 device에 미래에 대한 알림같은 행위를 등록할 때 사용
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ChallengeSub.BroadcastD.class);
        intent.putExtra("noti_title","챌린지");
        intent.putExtra("noti_content", name);
        intent.putExtra("request",request);
        //알람이 발생했을 경우, BradcastD에게 방송을 해주기 위해서 명시적으로 알려줍니다.

        PendingIntent sender = PendingIntent.getBroadcast(context, request, intent, 0);
       // Calendar calendar = Calendar.getInstance();
        //알람시간 calendar에 set해주기

      //  calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 12, 0);
        //알람 예약
        am.set(AlarmManager.RTC_WAKEUP, timemills, sender);
    }

}
