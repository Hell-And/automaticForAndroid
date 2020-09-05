package com.lixiang.douyin_follow.Broad;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.lixiang.douyin_follow.util.MonitorStatusUtil;

import static android.content.ContentValues.TAG;

public class MonitorStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
//        abortBroadcast();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //重新开启APP
                startTargetApp(context);
            }
        }, 500l);
    }

    public void startTargetApp(Context context) {
        String packageName = MonitorStatusUtil.getInstance().getMonitorPackgeName();
        if (!TextUtils.isEmpty(packageName)) {
            Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(MonitorStatusUtil.getInstance().getMonitorPackgeName());
            LaunchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            Log.d(TAG, "startTargetApp: " + packageName + " 重启");
            context.startActivity(LaunchIntent);
        }
    }
}
