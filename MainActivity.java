package com.wr.accessibilitytest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wr.accessibilitytest.R;
import com.wr.accessibilitytest.util.SpUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AccessibilityManager.AccessibilityStateChangeListener {

    private TextView textStatus = null;
    private ImageView imageStatus = null;

    private AccessibilityManager accessibilityManager = null;

    private boolean isServiceEnable() {
        List<AccessibilityServiceInfo> accessibilityServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId() == "com.wr.accessibilitytest.service.PlugService") {
                return true;
            }
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpUtil.initSpUtil(this);

        textStatus = findViewById(R.id.text_status);
        imageStatus = findViewById(R.id.image_status);

        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager != null) {
            accessibilityManager.addAccessibilityStateChangeListener(this);
        } else {
            throw new NullPointerException();
        }
        updateServiceStatus();
    }

    void startPlug(View v) {
        try {
            Log.i("wangrun", "startPlug");
            Toast.makeText(this, "点击插件来开启", Toast.LENGTH_SHORT).show();
            Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(accessibleIntent);
        } catch (Exception e) {
            Toast.makeText(this, "遇到一些问题,请手动打开系统设置>无障碍服务>自动插件(ฅ´ω`ฅ)", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    void startSetting(View v) {
        startActivity(new Intent(this, SettingActivity.class));
    }

    private void updateServiceStatus() {
        if (isServiceEnable()) {
            if (textStatus != null && imageStatus != null) {
                textStatus.setText("关闭插件");
                imageStatus.setBackgroundResource(R.mipmap.ic_stop);
            }
        } else {
            if (textStatus != null && imageStatus != null) {
                textStatus.setText("打开插件");
                imageStatus.setBackgroundResource(R.mipmap.ic_start);
            }
        }
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        updateServiceStatus();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
