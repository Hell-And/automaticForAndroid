package com.lixiang.douyin_follow.monitor;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lixiang.douyin_follow.service.DouyinServiceMonitor;
import com.lixiang.douyin_follow.util.AccessibilitUtil;

/*
* 火山极速版
* */
public class HuoshanFastMonitor {
    public static void policy(DouyinServiceMonitor accessibilityServiceMonitor, AccessibilityNodeInfo nodeInfo, String packageName, String className) {
        if (!("com.kuaishou.nebula".equals(packageName))) {
            return;
        }
        //点赞
        //"com.ss.android.ugc.aweme:id/apw" 页面点赞红心按钮id
        //"com.ss.android.ugc.aweme:id/bab" 页面关注加好按钮id
        //可以用findAccessibilityNodeInfosByViewId 方法来查找控件


        //获取页面状态，是直播还是正常小视频
        forceMove(accessibilityServiceMonitor, nodeInfo);
    }

    /*
     * 模拟上翻页
     * */
    private static void forceMove(DouyinServiceMonitor accessibilityServiceMonitor, AccessibilityNodeInfo nodeInfo) {
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        Path path = new Path();
//        Log.d("GestureDescription", rect.centerX() + " ->" + rect.centerY());
        int width = AccessibilitUtil.getScreenInfo().get("width");
        int height = AccessibilitUtil.getScreenInfo().get("height");
        path.moveTo(width / 2, height - 40);
        path.lineTo(width / 2, 50);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 100L, 200L, false))
                .build();

        boolean result = accessibilityServiceMonitor.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
        }, null);
        Log.d("GestureDescription", result + "");

    }
}
