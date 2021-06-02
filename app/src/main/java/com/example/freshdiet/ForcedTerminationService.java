package com.example.freshdiet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ForcedTerminationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) { //핸들링 하는 부분
        sendMessage();
        stopSelf(); //서비스 종료
    }

    private void sendMessage() {
        Intent intent = new Intent("naminsik");
        intent.putExtra("message", "전달하고자 하는 데이터");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
