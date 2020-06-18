package com.lixiang.douyin_follow.monitor;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lixiang.douyin_follow.service.DouyinServiceMonitor;
import com.lixiang.douyin_follow.util.GestureDescriptionUtil;

import java.util.List;

/**
 * Created by lixiang on 2020/6/5.
 * Email: 903087038@qq.com
 * Desc:
 */
public class TaobaoActionMonitor {
    private static String TAG = TaobaoActionMonitor.class.getSimpleName();
    private static Handler handler = new Handler();

    //    && (className.equals("com.taobao.browser.BrowserActivity") || className.equals("com.taobao.browser.exbrowser.BrowserUpperActivity"))
    public static synchronized void policy(DouyinServiceMonitor accessibilityServiceMonitor, final AccessibilityNodeInfo nodeInfo, String packageName, String className) {
       synchronized (TaobaoActionMonitor.class){
           if (!("com.taobao.taobao".equals(packageName))
//                || className.equals("com.taobao.android.home.component.view.viewpager.ViewPager")
//                || className.equals("com.taobao.tao.welcome.Welcome")
//                || className.equals("com.taobao.tao.TBMainActivity")
           ) {
               return;
           }
           List<AccessibilityNodeInfo> homeFlag = nodeInfo.findAccessibilityNodeInfosByText("数码家电买买买");
           List<AccessibilityNodeInfo> homeFlag1 = nodeInfo.findAccessibilityNodeInfosByText("进口好货买买买");
           List<AccessibilityNodeInfo> homeFlag2 = nodeInfo.findAccessibilityNodeInfosByText("爱家好物买买买");
           if (homeFlag.size() > 0 || homeFlag1.size() > 0 || homeFlag2.size() > 0) {
               AccessibilityNodeInfo homeFlagNode = homeFlag.get(0);
               Rect rect = new Rect();
               homeFlagNode.getBoundsInScreen(rect);
               rect.top = rect.bottom;
               rect.bottom = rect.bottom + 50;
               GestureDescriptionUtil.clickMonitor(accessibilityServiceMonitor, rect, null);
           } else {

               List<AccessibilityNodeInfo> finishFlag = nodeInfo.findAccessibilityNodeInfosByText("任务已完成");
               List<AccessibilityNodeInfo> finishFlag1 = nodeInfo.findAccessibilityNodeInfosByText("任务完成");
               List<AccessibilityNodeInfo> finishFlag2 = nodeInfo.findAccessibilityNodeInfosByText("领取失败");
               List<AccessibilityNodeInfo> finishFlag3 = nodeInfo.findAccessibilityNodeInfosByText("返回重试");
               List<AccessibilityNodeInfo> finishFlag4 = nodeInfo.findAccessibilityNodeInfosByText("今日已达上限");
               Log.d(TAG, "policy: " + finishFlag1.size());
               if (finishFlag.size() > 0
                       || finishFlag1.size() > 0
                       || finishFlag2.size() > 0
                       || finishFlag3.size() > 0
                       || finishFlag4.size() > 0) {
                   accessibilityServiceMonitor.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
               } else {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
                   if (nodeInfo != null) findWebViewNode(accessibilityServiceMonitor, nodeInfo);
//                }
//            }, 1000l);
               }
           }

       }
        //获取页面状态，是直播还是正常小视频
//        fabulousClick(accessibilityServiceMonitor, nodeInfo);
    }


    private static void findWebViewNode(DouyinServiceMonitor accessibilityServiceMonitor, final AccessibilityNodeInfo nodeInfo) {
//        AccessibilityNodeInfo accessibilityNodeInfoWebView = null;
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
//            List<AccessibilityNodeInfo> childBottom = nodeInfo.findAccessibilityNodeInfosByViewId("taskBottomSheet");
            final AccessibilityNodeInfo child = nodeInfo.getChild(i);
            if (child == null) continue;
//            Log.d(TAG,child.getClassName().toString()+" - "+ childBottom.size());
            if (child.getText() != null) {
                String text = child.getText().toString();
                if ("android.widget.Button".equals(child.getClassName())) {
//                Log.d("findWebViewNode--", "找到android.widget.Button");
//                getRecordButton(child);
//        Log.d(TAG, "getRecordButton: "+ Log.d(TAG, charSequence.toString()));
                    if (text.equals("做任务，领喵币")) {
                        if (child.isClickable()) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                }
                            }, 1000l);
                            break;
                        }
                    } else if (text.equals("去浏览") || text.equals("去观看") || text.equals("去兑换")) {
                        if (child.isClickable()) {
                            child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            break;
                        }
                    }
//                return;
                } else if (text.contains("任务已完成")
                        || text.contains("今日已达上限")
                        || text.contains("返回重试")
                        || text.contains("领取失败")) {
                    accessibilityServiceMonitor.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    return;
                }
            }

            if (child.getChildCount() > 0) {
                findWebViewNode(accessibilityServiceMonitor, child);
            }
        }
    }

    private static void getRecordButton(AccessibilityNodeInfo accessibilityNodeInfoWebView) {
        CharSequence charSequence = accessibilityNodeInfoWebView.getText();
//        Log.d(TAG, "getRecordButton: "+ Log.d(TAG, charSequence.toString()));
        if ("已完成".equals(charSequence.toString())) {
            Rect rect = new Rect();
            accessibilityNodeInfoWebView.getBoundsInScreen(rect);
//            Log.d(TAG, "getRecordButton: "+rect.centerX()+"--"+rect.centerY());
        }
        if (charSequence != null && charSequence.toString().equals("做任务，领喵币")) {
            if (accessibilityNodeInfoWebView.isClickable()) {
                accessibilityNodeInfoWebView.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

        } else if (charSequence != null && charSequence.toString().equals("去浏览")) {
            if (accessibilityNodeInfoWebView.isClickable()) {
                accessibilityNodeInfoWebView.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
//        for (int i = 0; i < accessibilityNodeInfoWebView.getChildCount(); i++) {
//            CharSequence charSequence= accessibilityNodeInfoWebView.getChild(i).getContentDescription();
//            if (charSequence!=null){
//                Log.d(TAG, charSequence.toString());
//            }
//        }
//        com.android.talkback.TalkBackAnalytics;
    }

    private static void getRecordListView(AccessibilityNodeInfo accessibilityNodeInfoListView) {
        int childCount = accessibilityNodeInfoListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (accessibilityNodeInfoListView.getChild(i).getClassName().toString().equals("android.widget.Button")) {
//                Log.d(TAG, "getRecordListView: "+accessibilityNodeInfoListView.getChild(i).getText());
                String text = accessibilityNodeInfoListView.getChild(i).getText().toString();
                if (text == null) continue;
                if ("去浏览".equals(text)) {
                    if (accessibilityNodeInfoListView.isClickable()) {
                        accessibilityNodeInfoListView.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        break;
                    }
                }
            }/*else {
                getRecordListView(accessibilityNodeInfoListView);
            }*/
        }
    }

    /*
     * 点赞按钮模拟点击
     * */
    private static void fabulousClick(final DouyinServiceMonitor accessibilityServiceMonitor, final AccessibilityNodeInfo nodeInfo) {
        Rect rect = new Rect();
//        //获取点赞红心按钮的坐标 ,返回的数组可能是多个，包含上一个小视频和下一个小视频，需要取出当前页面的id
//        List<AccessibilityNodeInfo> accessibilityNodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.aweme:id/apw");
//
//        //没有找找到id时，证明不在小视频页面，防止误点所以return
//        if (accessibilityNodeInfos.isEmpty()) return;
//
//        for (AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfos) {
//            accessibilityNodeInfo.getBoundsInScreen(rect);
//            //第一个满足判断的坐标就是当前页面的id
//            if (rect.centerX() > 0 && rect.centerY() > 0) {
//                break;
//            }
//        }
        Log.d("GestureDescription", rect.centerX() + " ->" + rect.centerY());
        Path path = new Path();
        path.moveTo(950, 1955);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 100L, 200L, false))
                .build();

        boolean result = accessibilityServiceMonitor.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
//                followClick(accessibilityServiceMonitor, nodeInfo);

            }
        }, null);
//        Log.d("GestureDescription", result + "");
    }

}
