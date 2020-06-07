package com.lixiang.douyin_follow.util;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lixiang.douyin_follow.service.DouyinServiceMonitor;

public class GestureDescriptionUtil {
    static String TAG = GestureDescriptionUtil.class.getSimpleName();

    /*
    模拟点击
    * */
    public static void clickMonitor(DouyinServiceMonitor accessibilityServiceMonitor, AccessibilityNodeInfo nodeInfo, final OnMonitorDoSomethingFinish onMonitorDoSomethingFinish) {
//        if (nodeInfo.isClickable()) {
//            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        } else {
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        clickMonitor(accessibilityServiceMonitor, rect, onMonitorDoSomethingFinish);
//        }
    }

    /*
     * 模拟点击
     * */
    public static void clickMonitor(DouyinServiceMonitor accessibilityServiceMonitor, Rect rect, final OnMonitorDoSomethingFinish onMonitorDoSomethingFinish) {
        Path path = new Path();
        Log.d(TAG, "clickMonitor: " + rect.centerX() + " -- " + rect.centerY());
        if (rect.centerX() < 0 || rect.centerY() < 0) return;
        path.moveTo(rect.centerX(), rect.centerY());
//        GestureDescription.Builder builder = new GestureDescription.Builder();
//        GestureDescription gestureDescription = builder
//                .addStroke(new GestureDescription.StrokeDescription(path, 100L, 200L, false))
//                .build();

//        boolean result = accessibilityServiceMonitor.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
//            @Override
//            public void onCompleted(GestureDescription gestureDescription) {
//                super.onCompleted(gestureDescription);
////                followClick(accessibilityServiceMonitor, nodeInfo);
//                if (onMonitorDoSomethingFinish != null)
//                    onMonitorDoSomethingFinish.onCompleted(gestureDescription);
//            }
//        }, null);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 100l, 100l));
        GestureDescription gesture = builder.build();
        boolean isDispatched = accessibilityServiceMonitor.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "onCompleted: 完成..........");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "onCompleted: 取消..........");
            }
        }, null);
    }


    /*
     * 模拟滑动
     * */
    public static void moveMonitor(DouyinServiceMonitor accessibilityServiceMonitor, Rect rect1, Rect rect2, final OnMonitorDoSomethingFinish onMonitorDoSomethingFinish) {
        Path path = new Path();
        path.moveTo(rect1.centerX(), rect1.centerY());
        path.lineTo(rect2.centerX(), rect2.centerY());
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 10L, 20L, false))
                .build();

        boolean result = accessibilityServiceMonitor.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
//                followClick(accessibilityServiceMonitor, nodeInfo);
                if (onMonitorDoSomethingFinish != null)
                    onMonitorDoSomethingFinish.onCompleted(gestureDescription);
            }
        }, null);
    }

    public interface OnMonitorDoSomethingFinish {
        void onCompleted(GestureDescription gestureDescription);
    }
}
