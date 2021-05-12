package com.example.freshdiet;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.freshdiet.challenge.AlarmHATT;

public class BroadcastD extends BroadcastReceiver {
    NotificationCompat.Builder builder;
    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private String CHANNEL_ID = "channel1";
    private String CHANNEL_NAME = "Channel1";
    private String title, content;
    private int request;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {

        //알람 시간이 되었을때 onReceive를 호출함
        title = intent.getStringExtra("noti_title");
        content=intent.getStringExtra("noti_content");
        request=intent.getIntExtra("request",-1);


        AlarmHATT alarmHATT=new AlarmHATT(context);
        alarmHATT.Alarm(request, title, System.currentTimeMillis()+86400000);


        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, request, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel= new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);

            notificationmanager.createNotificationChannel(
                  channel
            );
        }


        builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        Vibrator vib=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(new long[]{1000,2000},-1);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title).setContentText(content)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setVibrate(new long[]{1000,2000})
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        builder.setSound(notification);

        notificationmanager.notify(request, builder.build());

    }
}
