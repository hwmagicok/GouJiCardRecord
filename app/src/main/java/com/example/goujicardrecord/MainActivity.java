package com.example.goujicardrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int OverLay_Permission_RequestCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startFloatWindowService();
    }

    private void startFloatWindowService() {
        // 查看是否有悬浮窗权限，如果没有弹出窗口提示用户获取权限；如果有就直接启动服务
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "需要悬浮窗权限", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())), OverLay_Permission_RequestCode);
        } else {
            Log.d("MainActivity", "startService");
            startService(new Intent(this, FloatWindowService.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case OverLay_Permission_RequestCode:
                if (Settings.canDrawOverlays(this)) {
                    startService(new Intent(this, FloatWindowService.class));
                } else {
                    Toast.makeText(this, "授权失败，活动结束", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
