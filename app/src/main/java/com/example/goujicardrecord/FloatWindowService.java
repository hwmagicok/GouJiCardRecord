package com.example.goujicardrecord;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

public class FloatWindowService extends Service {

    private static CardRecordWindow cardRecordWindow = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (cardRecordWindow == null) {
            showFloatWindow();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    private void showFloatWindow() {
        Log.d("FloatWindowService", "showFloatWindow in");
        if (Settings.canDrawOverlays(this)) {
            cardRecordWindow = new CardRecordWindow(this);
            cardRecordWindow.CreateCardRecordWindow();
        }


    }

}
