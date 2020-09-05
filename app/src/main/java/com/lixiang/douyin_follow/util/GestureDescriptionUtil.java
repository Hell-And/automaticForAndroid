package com.lixiang.douyin_follow.util;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lixiang.douyin_follow.service.SuperServiceMonitor;

public class GestureDescriptionUtil {
    static String TAG = GestureDescriptionUtil.class.getSimpleName();

    /*
    模拟点击
    * */
    public static void clickMonitor(SuperServiceMonitor accessibilityServiceMonitor, AccessibilityNodeInfo nodeInfo, final OnMonitorDoSomethingFinish onMonitorDoSomethingFinish) {
        if (nodeInfo == null) return;
//        boolean isclick = false;
//        AccessibilityNodeInfo accessibilityNodeInfo = nodeInfo;
//        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
//            while (accessibilityNodeInfo != null) {
//                if (accessibilityNodeInfo.isClickable()) {
//                    isclick = accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    break;
//                }
//                accessibilityNodeInfo = accessibilityNodeInfo.getParent();
//            }
//        }
//        if (isclick) return;
        ;
        if (nodeInfo.isClickable()) {
            if (!nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                Rect rect = new Rect();
                nodeInfo.getBoundsInScreen(rect);
                clickMonitor(accessibilityServiceMonitor, rect, onMonitorDoSomethingFinish);
            }
        } else {
            Rect rect = new Rect();
            nodeInfo.getBoundsInScreen(rect);
            clickMonitor(accessibilityServiceMonitor, rect, onMonitorDoSomethingFinish);
        }
    }

    /*
     * 模拟点击
     * */
    public static void clickMonitor(SuperServiceMonitor accessibilityServiceMonitor, Rect rect, final OnMonitorDoSomethingFinish onMonitorDoSomethingFinish) {
        Path path = new Path();
        Log.d(TAG, "clickMonitor: " + rect.centerX() + " -- " + rect.centerY());
        if (rect.centerX() < 0 || rect.centerY() < 0) return;
        path.moveTo(rect.centerX(), rect.centerY());
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 100L, 200L, false))
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
        /*GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 100l, 100l));
        GestureDescription gesture = builder.build();
        boolean isDispatched = accessibilityServiceMonitor.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "onCompleted: 完成..........");
                if (onMonitorDoSomethingFinish != null) {
                    onMonitorDoSomethingFinish.onCompleted(gestureDescription);
                }
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "onCompleted: 取消..........");
                if (onMonitorDoSomethingFinish != null) {
                    onMonitorDoSomethingFinish.onCompleted(gestureDescription);
                }
            }
        }, null);*/
    }


    /*
     * 模拟滑动
     * */
    public static void moveMonitor(SuperServiceMonitor accessibilityServiceMonitor, Rect rect1, Rect rect2, final OnMonitorDoSomethingFinish onMonitorDoSomethingFinish) {
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

    public static void moveMonitor(SuperServiceMonitor accessibilityServiceMonitor, int startX, int startY, int endX, int endY, final OnMonitorDoSomethingFinish onMonitorDoSomethingFinish) {
        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);
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
