package com.lixiang.douyin_follow.monitor;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lixiang.douyin_follow.MyApplication;
import com.lixiang.douyin_follow.service.DouyinServiceMonitor;
import com.lixiang.douyin_follow.util.AccessibilitUtil;
import com.lixiang.douyin_follow.util.MMKVutil;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by LoverAnd on 2020/6/5 0005.
 */

public class XiaokeSignMonitor {
    static String TAG = XiaokeSignMonitor.class.getSimpleName();
    //签到页面 更多坐标位置
    //因为签到页面有个定位的遮罩，
    // 这个遮罩消失的时候不会触发onAccessibilityEvent，而输入框以及其他控件节点都在遮罩消失后才可以拿到
    // 不明白为什么，所以另外想办法，随意找一个按钮跳转有个页面然后返回触发onAccessibilityEvent，这时候就可以拿到输入框的节点了
    private static Rect moreRect = new Rect();

    public static void policy(final DouyinServiceMonitor accessibilityServiceMonitor, final AccessibilityNodeInfo nodeInfo, String packageName, String className) {
        if (!("com.facishare.fs".equals(packageName))) {
            return;
        }

        //这里做了个策略，因为锁屏唤醒时，响应不了点击，发现退到手机home页，在进来，就可以响应了
        //所以这里做了一个判断，如果是第一次进来，就先返回home，在AlarmReceiver里有个2s重启的逻辑
        if (MMKVutil.instance.getBoolean("isFirst",true)){
            MMKVutil.instance.putBoolean("isFirst",false);
            accessibilityServiceMonitor.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);//退到home页
        }
        if (className.equals("com.facishare.fs.biz_function.subbiz_outdoorsignin.SendOutdoorSigninActivity")) {
            if (nodeInfo.findAccessibilityNodeInfosByViewId("com.facishare.fs:id/et_content").size() > 0) {
                inputText(accessibilityServiceMonitor, nodeInfo, "签到@杨威1@马智霞@严巧");
                //然后点击签到  测试时别随意打开,防止误签
//            followClick(accessibilityServiceMonitor, nodeInfo, "签到", null);
            } else {
                Log.d(TAG, "policy: " + className);
                int width = AccessibilitUtil.getScreenInfo().get("width");
                moreRect.left = width - width / 4;
                moreRect.right = width;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //这里延时5s  是为了等待定位完成，为了容错 可以加大时长
                        followClick(accessibilityServiceMonitor, nodeInfo, "", moreRect);
                    }
                }, 5000l);
            }
        } else if (className.equals("com.facishare.fs.biz_function.subbiz_outdoorsignin.OutDoorMoreActivity")) {
            //跳转到更多页面，找到返回箭头，模拟返回
            accessibilityServiceMonitor.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);//退到home页
           /* List<AccessibilityNodeInfo> more_Back = nodeInfo.findAccessibilityNodeInfosByViewId("com.facishare.fs:id/title_leftgroup");
            if (more_Back.size() > 0) {
                if (more_Back.get(0).isClickable()) {
                    more_Back.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } else {
                    followClick(accessibilityServiceMonitor, more_Back.get(0), "", null);
                }
            }*/
        } else {
            findWebViewNode(accessibilityServiceMonitor, nodeInfo);
        }
    }

    private static void findWebViewNode(final DouyinServiceMonitor accessibilityServiceMonitor, AccessibilityNodeInfo nodeInfo) {
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            final AccessibilityNodeInfo child = nodeInfo.getChild(i);
            if (child == null) continue;

            List<AccessibilityNodeInfo> topNodeInfo = child.findAccessibilityNodeInfosByViewId("com.facishare.fs:id/title_middlegroup");
            if (topNodeInfo.size() > 0) {
                Rect rect = new Rect();
                topNodeInfo.get(0).getBoundsInScreen(rect);
                moreRect.top = rect.bottom;  //页面title部分的底部
                moreRect.bottom = rect.bottom + (rect.bottom - rect.top); // 页面title部分的底部 加上title部分的高
                Log.d(TAG, "policy: title_middlegroup " + rect.top + " -- " + rect.bottom);
            }


            if ("android.widget.TextView".equals(child.getClassName()) && child.getText() != null) {
                String text = child.getText().toString();
                Log.d(TAG, "findWebViewNode: " + child.getText().toString() + " --- " + child.getViewIdResourceName());
                if (text.equals("外勤")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //这里不延时，不响应点击
                            followClick(accessibilityServiceMonitor, child, "外勤", null);
                        }
                    }, 200l);
                } else if (text.equals("应用")) {
                    followClick(accessibilityServiceMonitor, child, "应用", null);
                }
            }
            if (child.getChildCount() > 0) {
                findWebViewNode(accessibilityServiceMonitor, child);
            }
        }
    }

    /*
     * 关注按钮模拟点击
     * */
    private static void followClick(final DouyinServiceMonitor accessibilityServiceMonitor, AccessibilityNodeInfo nodeInfo, final String viewText, Rect rect) {
        if (rect == null) {
            rect = new Rect();
            nodeInfo.getBoundsInScreen(rect);
        }
        Path path = new Path();
        path.moveTo(rect.centerX(), rect.centerY());
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

       /* GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 100L, 200L, false))
                .build();
        boolean result = accessibilityServiceMonitor.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
        }, null);*/
//        Log.d("GestureDescription", result + ""); com.facishare.fs:id/et_content
//        accessibilityServiceMonitor.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    /**
     * 输入文本
     *
     * @param accessibilityServiceMonitor
     * @param nodeInfo
     * @param text
     */
    private static void inputText(DouyinServiceMonitor accessibilityServiceMonitor, AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) return;
        //签到页面输入框
        findWebViewNode(accessibilityServiceMonitor, nodeInfo);
        final List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.facishare.fs:id/et_content");//签到页面输入框id
        if (list != null && !list.isEmpty()) {
            AccessibilityNodeInfo info = list.get(0);
            //粘贴板
            ClipboardManager clipboard = (ClipboardManager) MyApplication.application.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(null, text);
            clipboard.setPrimaryClip(clip);

            CharSequence txt = info.getText();
            if (txt == null) txt = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Bundle arguments = new Bundle();
                arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0);
                arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, txt.length());
                info.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                info.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, arguments);
                info.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, txt);
                info.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            }
        }
    }
}
