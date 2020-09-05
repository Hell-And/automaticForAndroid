package com.lixiang.douyin_follow.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lixiang.douyin_follow.monitor.AliPayMonitor;
import com.lixiang.douyin_follow.monitor.DouyinFastFollowMonitor;
import com.lixiang.douyin_follow.monitor.DouyinFollowMonitor;
import com.lixiang.douyin_follow.monitor.HuoshanFastMonitor;
import com.lixiang.douyin_follow.monitor.ShuabaoVideoMonitor;
import com.lixiang.douyin_follow.monitor.TaobaoActionMonitor;
import com.lixiang.douyin_follow.monitor.XiaokeSignMonitor;
import com.lixiang.douyin_follow.util.MonitorStatusUtil;


public class SuperServiceMonitor extends AccessibilityService {

    private static final String TAG = SuperServiceMonitor.class.getSimpleName();
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
        serviceInfo.packageNames = new String[]{
                "com.ss.android.ugc.aweme"//抖音
                , "com.taobao.taobao" //淘宝
                , "com.facishare.fs"  //纷享销客
                , "com.eg.android.AlipayGphone" //支付宝
                , "com.ss.android.ugc.livelite" //火山极速版
                , "com.kuaishou.nebula" //快手极速版
                , "com.ss.android.ugc.aweme.lite" //抖音极速版
                , "com.jm.video" //刷宝短视频
        };// 监控的app 抖音包名
        serviceInfo.notificationTimeout = 100;
        serviceInfo.flags = serviceInfo.flags | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        setServiceInfo(serviceInfo);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {
            int eventType = event.getEventType();
            String packageName = event.getPackageName().toString();
            String className = event.getClassName().toString();
            Log.d(TAG,"packageName = " + packageName + ", className = " + className);
            MonitorStatusUtil.getInstance().setSuperServiceMonitor(this);
            MonitorStatusUtil.getInstance().setMonitorPackgeName(packageName);
            switch (eventType) {
                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                    //当有操作时 就设置当前任务时间
                    MonitorStatusUtil.getInstance().setMonitorContinuedTime(System.currentTimeMillis());
//                    serviceInfo = getServiceInfo();
//                    serviceInfo.flags = serviceInfo.flags | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
//                    setServiceInfo(serviceInfo);
                    TaobaoActionMonitor.getInstance().policy(this, getRootInActiveWindow(), packageName, className);
//                    XiaokeSignMonitor.policy(this, getRootInActiveWindow(), packageName, className);
                    break;
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                case AccessibilityEvent.TYPE_VIEW_SCROLLED:
//            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END :
                    AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                    if (isKeepEnable && nodeInfo != null) {

                        //当有操作时 就设置当前任务时间
                        MonitorStatusUtil.getInstance().setMonitorContinuedTime(System.currentTimeMillis());

                        DouyinFollowMonitor.getInstance().policy(this, nodeInfo, packageName, className);
                        DouyinFastFollowMonitor.getInstance().policy(this, nodeInfo, packageName, className);
                        serviceInfo = getServiceInfo();
                        serviceInfo.flags = serviceInfo.flags | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
                        setServiceInfo(serviceInfo);
                        TaobaoActionMonitor.getInstance().policy(this, nodeInfo, packageName, className);
//                        AliPayMonitor.policy(this, nodeInfo, packageName, className);

                        XiaokeSignMonitor.policy(this, nodeInfo, packageName, className);
                        HuoshanFastMonitor.getInstance().policy(this, nodeInfo, packageName, className);
                        ShuabaoVideoMonitor.getInstance().policy(this, nodeInfo, packageName, className);
                    }
                    break;
                case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
                    Log.d(TAG, "onAccessibilityEvent: " + className);
                    break;
            }
        } catch (Exception e) {

        }
    }

    public Boolean clickHomeKey() {
        return performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    @Override
    public void onInterrupt() {


        
    }

}
