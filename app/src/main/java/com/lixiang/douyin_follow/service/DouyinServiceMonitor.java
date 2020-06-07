package com.lixiang.douyin_follow.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lixiang.douyin_follow.monitor.AliPayMonitor;
import com.lixiang.douyin_follow.monitor.TaobaoActionMonitor;
import com.lixiang.douyin_follow.monitor.XiaokeSignMonitor;


public class DouyinServiceMonitor extends AccessibilityService {

    private static final String TAG = DouyinServiceMonitor.class.getSimpleName();
    /**
     * 辅助功能
     */
    private boolean isKeepEnable = true;
    private AccessibilityServiceInfo serviceInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return super.onStartCommand(intent, flags, startId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.packageNames = new String[]{"com.ss.android.ugc.aweme", "com.taobao.taobao", "com.facishare.fs","com.eg.android.AlipayGphone"};// 监控的app 抖音包名
        serviceInfo.notificationTimeout = 100;
        serviceInfo.flags = serviceInfo.flags | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        setServiceInfo(serviceInfo);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String packageName = event.getPackageName().toString();
        String className = event.getClassName().toString();
        //Log.d(Config.TAG,"packageName = " + packageName + ", className = " + className);

        switch (eventType) {

            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
//            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END :
                AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                if (isKeepEnable && nodeInfo != null) {

//                    DouyinFollowMonitor.policy(this,nodeInfo, packageName, className);
                    serviceInfo = getServiceInfo();
                    serviceInfo.flags = serviceInfo.flags | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
                    setServiceInfo(serviceInfo);
//                    TaobaoActionMonitor.policy(this,nodeInfo, packageName, className);
                    AliPayMonitor.policy(this,nodeInfo, packageName, className);

//                    XiaokeSignMonitor.policy(this, nodeInfo, packageName, className);
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
                Log.d(TAG, "onAccessibilityEvent: " +className);
                break;
        }
    }

    public Boolean clickHomeKey() {
        return performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    @Override
    public void onInterrupt() {

    }
}
