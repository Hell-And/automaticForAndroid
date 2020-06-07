package com.lixiang.douyin_follow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lixiang.douyin_follow.model.TimeMonitor;
import com.lixiang.douyin_follow.service.DouyinServiceMonitor;
import com.lixiang.douyin_follow.util.AccessibilitUtil;
import com.lixiang.douyin_follow.util.AlarManagerUtil;
import com.lixiang.douyin_follow.util.MMKVutil;

public class MainActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener, CompoundButton.OnCheckedChangeListener {
    String TAG = MainActivity.class.getSimpleName();
    private Button btnSettings;
    private TimePicker timePickerSign, timepick_signOut;
    private Switch sw_cpdaily_xiaoke, sw_signOut_xiaoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //锁屏唤醒
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main);
        if (System.currentTimeMillis() >= 1592622280000l) {
            finish();
        }
        MMKVutil.instance.putBoolean("isFirst", true);
        initView();
        //启动服务
        startService();
    }

    private void initView() {
        sw_cpdaily_xiaoke = findViewById(R.id.sw_sign_xiaoke);
        sw_signOut_xiaoke = findViewById(R.id.sw_signOut_xiaoke);
        sw_cpdaily_xiaoke.setOnCheckedChangeListener(this);
        timePickerSign = findViewById(R.id.timepick_sign);
        timepick_signOut = findViewById(R.id.timepick_signOut);
        //显示为24小时
        timePickerSign.setIs24HourView(true);
        //设置点击事件不弹键盘
        timePickerSign.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        //监听timePicker变化
        timePickerSign.setOnTimeChangedListener(this);

        //显示为24小时
        timepick_signOut.setIs24HourView(true);
        //设置点击事件不弹键盘
        timepick_signOut.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        //监听timePicker变化
        timepick_signOut.setOnTimeChangedListener(this);

        btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开设置页面
                AccessibilitUtil.showSettingsUI(MainActivity.this);
            }
        });
    }

    private void updatePermission() {
        if (AccessibilitUtil.isAccessibilitySettingsOn(this, DouyinServiceMonitor.class.getCanonicalName())) {
            btnSettings.setEnabled(false);
        } else {
            btnSettings.setEnabled(true);
        }
    }

    private void startService() {
        Intent mIntent = new Intent(this, DouyinServiceMonitor.class);
        startService(mIntent);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //检查权限是否打开
        updatePermission();
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        switch (view.getId()) {
            case R.id.timepick_sign:
                this.signHourOfDay = hourOfDay;
                this.signMinute = minute;
                break;
            case R.id.timepick_signOut:
                this.signOutHourOfDay = hourOfDay;
                this.signOutMinute = minute;
                break;
        }
        Log.d(TAG, "onTimeChanged: " + hourOfDay + " : " + minute);
    }

    private int signHourOfDay, signMinute, signOutHourOfDay, signOutMinute;
    private boolean isStartTimeTask;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.sw_sign_xiaoke:
                if (isChecked) {
                    if (buttonView.isPressed()) { //判断是否是人为单击
                        AlarManagerUtil.addTimer("am", new TimeMonitor(signHourOfDay, signMinute));
                        AlarManagerUtil.timedTack(MainActivity.this);
                    }
                } else {
                    AlarManagerUtil.removeTimer("am");
                }
                break;
            case R.id.sw_signOut_xiaoke:
                if (isChecked) {
                    if (buttonView.isPressed()) { //判断是否是人为单击
                        AlarManagerUtil.addTimer("pm", new TimeMonitor(signHourOfDay, signMinute));
                        AlarManagerUtil.timedTack(MainActivity.this);
                    }
                } else {
                    AlarManagerUtil.removeTimer("pm");
                }
                break;
        }
    }

    public void showText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
