package com.lixiang.douyin_follow.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.lixiang.douyin_follow.model.TimeMonitor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AlarManagerUtil {
    public static AlarmManager alarmManager;
    private static PendingIntent pendingIntent;
    private static Activity activityA;
    private static LinkedHashMap<String, TimeMonitor> timeMonitorMap = new LinkedHashMap<>();
    private static LinkedList<String> mapKey = new LinkedList<>();
    private static String thisTimerKey;
    private static int timerTaskIndex = -1;

    public static void addTimer(String key, TimeMonitor timeMonitor) {
        mapKey.add(key);
        timeMonitorMap.put(key, timeMonitor);
    }

    public static void removeTimer(String key) {
        mapKey.remove(key);
        timeMonitorMap.remove(key);
    }

    public static void timedTack(Activity activity) {
        activityA = activity;
        alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        timerTaskIndex++;
        if (timerTaskIndex == mapKey.size()) {
            timerTaskIndex = 0;
        }
        TimeMonitor timeMonitor = timeMonitorMap.get(mapKey.get(timerTaskIndex));
        int hour = timeMonitor.getHour();
        int minute = timeMonitor.getMinute();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 00);
        Intent alarmIntent = new Intent("fenxiangxiaoke");
        Bundle bundle = new Bundle();
        bundle.putInt("hour", hour);
        bundle.putInt("minute", minute);
        alarmIntent.putExtra("timer", bundle);
        alarmIntent.setPackage(activity.getPackageName());
        pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        long TaskTimer = calendar.getTimeInMillis();
        long autoTime = calendar.getTimeInMillis() - System.currentTimeMillis();
        if (autoTime < 0) {
            TaskTimer += 1000 * 60 * 60 * 24;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, TaskTimer, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//  4.4
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, TaskTimer, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, TaskTimer, pendingIntent);
        }
    }

    public static void cancelTimetacker() {
        try {
            alarmManager.cancel(pendingIntent);
//            Toasty.info(activityA, "今日校园自动签到已关闭", Toast.LENGTH_SHORT, true).show();
        } catch (Exception e) {
            e.printStackTrace();
//            Toasty.error(activityA,"取消失败！",Toasty.LENGTH_SHORT,true).show();
        }


    }


}
