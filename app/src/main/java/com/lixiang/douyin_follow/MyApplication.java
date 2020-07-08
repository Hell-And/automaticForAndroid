package com.lixiang.douyin_follow;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;

/**
 * Created by lixiang on 2020/6/2.
 * Email: 903087038@qq.com
 * Desc:
 */
public class MyApplication extends Application {
    public static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
        application = this;

        CrashReport.initCrashReport(getApplicationContext(), "0a2cca7054", true);
    }
}
