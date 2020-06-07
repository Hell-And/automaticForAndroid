package com.lixiang.douyin_follow.monitor;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lixiang.douyin_follow.service.DouyinServiceMonitor;
import com.lixiang.douyin_follow.util.AccessibilitUtil;

import java.io.OutputStream;
import java.util.List;

/**
 * Created by lixiang on 2020/6/2.
 * Email: 903087038@qq.com
 * Desc: 抖音点赞关注上翻页实现
 * 坐标暂时写死，以小米mix2，1080*2160 测试
 */
public class DouyinFollowMonitor {

    public static void policy(DouyinServiceMonitor accessibilityServiceMonitor, AccessibilityNodeInfo nodeInfo, String packageName, String className) {
        if (!("com.ss.android.ugc.aweme".equals(packageName))) {
            return;
        }
        //点赞
        //"com.ss.android.ugc.aweme:id/apw" 页面点赞红心按钮id
        //"com.ss.android.ugc.aweme:id/bab" 页面关注加好按钮id
        //可以用findAccessibilityNodeInfosByViewId 方法来查找控件


        //获取页面状态，是直播还是正常小视频
        getAcType(accessibilityServiceMonitor, nodeInfo);
    }

    /*
     * 点赞按钮模拟点击
     * */
    private static void fabulousClick(final DouyinServiceMonitor accessibilityServiceMonitor, final AccessibilityNodeInfo nodeInfo) {
        Rect rect = new Rect();
        //获取点赞红心按钮的坐标 ,返回的数组可能是多个，包含上一个小视频和下一个小视频，需要取出当前页面的id
        List<AccessibilityNodeInfo> accessibilityNodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.aweme:id/apw");

        //没有找找到id时，证明不在小视频页面，防止误点所以return
        if (accessibilityNodeInfos.isEmpty()) return;

        for (AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfos) {
            accessibilityNodeInfo.getBoundsInScreen(rect);
            //第一个满足判断的坐标就是当前页面的id
            if (rect.centerX() > 0 && rect.centerY() > 0) {
                break;
            }
        }
        Log.d("GestureDescription", rect.centerX() + " ->" + rect.centerY());
        Path path = new Path();
        path.moveTo(rect.centerX(), rect.centerY());
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 100L, 200L, false))
                .build();

        boolean result = accessibilityServiceMonitor.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                followClick(accessibilityServiceMonitor, nodeInfo);

            }
        }, null);
//        Log.d("GestureDescription", result + "");
    }

    /*
     * 关注按钮模拟点击
     * */
    private static void followClick(final DouyinServiceMonitor accessibilityServiceMonitor, final AccessibilityNodeInfo nodeInfo) {
        Rect rect = new Rect();
        //获取点赞红心按钮的坐标 ,返回的数组可能是多个，包含上一个小视频和下一个小视频，需要取出当前页面的id
        List<AccessibilityNodeInfo> accessibilityNodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.aweme:id/bab");

        if (accessibilityNodeInfos.isEmpty()) return;
        for (AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfos) {
            accessibilityNodeInfo.getBoundsInScreen(rect);
            //第一个满足判断的坐标就是当前页面的id
            if (rect.centerX() > 0 && rect.centerY() > 0) {
                break;
            }
        }
        Log.d("GestureDescription", rect.centerX() + " ->" + rect.centerY());
        Path path = new Path();
        path.moveTo(rect.centerX(), rect.centerY());
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 100L, 200L, false))
                .build();
        boolean result = accessibilityServiceMonitor.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                new Handler().postDelayed(new Runnable() {
                    @Override

                    public void run() {
                        forceMove(accessibilityServiceMonitor, nodeInfo);
                    }
                }, 500);
            }
        }, null);
//        Log.d("GestureDescription", result + "");
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
        path.moveTo(width / 2, height - 100);
        path.lineTo(width / 2, 100);
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

    public static void getAcType(DouyinServiceMonitor accessibilityServiceMonitor, AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            //如果页面有显示直播中的控件，应该就是好友，就没有点赞和关注按钮
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("直播中");
            if (list.isEmpty()) {
                fabulousClick(accessibilityServiceMonitor, nodeInfo);
            } else {
                forceMove(accessibilityServiceMonitor, nodeInfo);

                /*for (AccessibilityNodeInfo item : list) {
//                这种方式是click实现的点击，而抖音是通过ontouch实现的点击，所以这里isClickable返回的false，这种方式无法模拟点击
                    if (item.isClickable()) {
//                        Log.d(TAG, "keepAppPraise = " + item.getClassName());
                        item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }*/
            }

        }
    }

    /*
     * 使用shell命令操作手机页面，这种方式需要root手机（必须），使用场景限制太大
     * */
    private static void clickWithShellMonitor(AccessibilityNodeInfo nodeInfo) {
        OutputStream os = null;
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        Log.d("KeepAppMonitor", "forceClick: " + rect.left + " " + rect.top + " " + rect.right + " " + rect.bottom);
        int x = (rect.left + rect.right) / 2;
        int y = (rect.top + rect.bottom) / 2;
        //点击命令
        String cmd = "input tap " + String.valueOf(450) + " " + String.valueOf(y + 10);
        try {
            if (os == null) {
                os = Runtime.getRuntime().exec("su").getOutputStream();
            }
            os.write(cmd.getBytes());
            os.flush();//清空缓存
            os.close();//停止流
            os = null;
        } catch (Exception e) {
            Log.e("KeepAppMonitor", e.getMessage());
        }
    }
}
