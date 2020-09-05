package com.lixiang.douyin_follow.monitor;

import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lixiang.douyin_follow.config.ConfigMonitor;
import com.lixiang.douyin_follow.service.SuperServiceMonitor;
import com.lixiang.douyin_follow.util.AccessibilitUtil;
import com.lixiang.douyin_follow.util.GestureDescriptionUtil;

import java.util.List;


/*
 * 火山极速版
 * */
public class HuoshanFastMonitor {
    private static final HuoshanFastMonitor instance = new HuoshanFastMonitor();
    private SuperServiceMonitor superServiceMonitor;
    private AccessibilityNodeInfo nodeInfo;
    private int startFlag = 0;
    private int followNumToday = 0;
    private Handler handler = new Handler();
    private Runnable runnableMove = new Runnable() {
        @Override
        public void run() {
            moveMonitor(superServiceMonitor);
//            clickMonitor(superServiceMonitor, nodeInfo);
        }
    };

    public static HuoshanFastMonitor getInstance() {
        return instance;
    }

    public void policy(SuperServiceMonitor accessibilityServiceMonitor, AccessibilityNodeInfo nodeInfo, String packageName, String className) {
        if (!("com.kuaishou.nebula".equals(packageName))) {
            return;
        }
        this.superServiceMonitor = accessibilityServiceMonitor;
        this.nodeInfo = nodeInfo;
        //点赞
        //"com.ss.android.ugc.aweme:id/apw" 页面点赞红心按钮id
        //"com.ss.android.ugc.aweme:id/bab" 页面关注加好按钮id
        //可以用findAccessibilityNodeInfosByViewId 方法来查找控件

        synchronized (HuoshanFastMonitor.class) {
//            clickLike(superServiceMonitor, nodeInfo);
            handler.removeCallbacks(runnableMove);
            handler.postDelayed(runnableMove, ConfigMonitor.delayTime);
        }
    }

    private void clickFollow(final SuperServiceMonitor accessibilityServiceMonitor, final AccessibilityNodeInfo nodeInfo) {

        List<AccessibilityNodeInfo> accessibilityNodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId("com.kuaishou.nebula:id/slide_play_right_follow_button");
        Rect rect = new Rect();
        //没有找找到id时，证明不在小视频页面，防止误点所以return
        if (accessibilityNodeInfos.isEmpty()) {
            handler.removeCallbacks(runnableMove);
            handler.postDelayed(runnableMove, ConfigMonitor.delayTime);
        } else {
            for (AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfos) {
                accessibilityNodeInfo.getBoundsInScreen(rect);
                //第一个满足判断的坐标就是当前页面的id
                if (rect.centerX() > 0 && rect.centerY() > 0) {
                    break;
                }
            }
            GestureDescriptionUtil.clickMonitor(accessibilityServiceMonitor, rect, new GestureDescriptionUtil.OnMonitorDoSomethingFinish() {
                @Override
                public void onCompleted(GestureDescription gestureDescription) {
                    handler.removeCallbacks(runnableMove);
                    handler.postDelayed(runnableMove, ConfigMonitor.delayTime);
                }
            });
        }
    }

    private void clickLike(final SuperServiceMonitor accessibilityServiceMonitor, final AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> accessibilityNodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId("com.kuaishou.nebula:id/like_icon");
        if (accessibilityNodeInfos.isEmpty()) return;
        if (followNumToday > 2) {
            handler.removeCallbacks(runnableMove);
            handler.postDelayed(runnableMove, ConfigMonitor.delayTime);
        } else {
            Rect rect = new Rect();
            for (AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfos) {
                accessibilityNodeInfo.getBoundsInScreen(rect);
                //第一个满足判断的坐标就是当前页面的id
                if (rect.centerX() > 0 && rect.centerY() > 0) {
                    break;
                }
            }
            GestureDescriptionUtil.clickMonitor(accessibilityServiceMonitor, rect, new GestureDescriptionUtil.OnMonitorDoSomethingFinish() {
                @Override
                public void onCompleted(GestureDescription gestureDescription) {
                    clickFollow(accessibilityServiceMonitor, nodeInfo);
                }
            });
            followNumToday++;
        }
    }

    /*private void clickMonitor(final SuperServiceMonitor accessibilityServiceMonitor, final AccessibilityNodeInfo nodeInfo) {
        if (followNumToday < 2) {
//                    List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.kuaishou.nebula:id/like_icon");
//                    if (list.size() > 0){
//                    }else {
//                    }
//                    AccessibilityNodeInfo accessibilityNodeInfo=AccessibilitUtil.findNodeInfosById(nodeInfo, "com.kuaishou.nebula:id/like_icon",1);
            AccessibilityNodeInfo accessibilityNodeInfo = AccessibilitUtil.findNodeInfosById(nodeInfo, "com.kuaishou.nebula:id/like_icon", 1);
            if (accessibilityNodeInfo != null) {
                GestureDescriptionUtil.clickMonitor(accessibilityServiceMonitor, accessibilityNodeInfo, new GestureDescriptionUtil.OnMonitorDoSomethingFinish() {
                    @Override
                    public void onCompleted(GestureDescription gestureDescription) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                followNumToday++;
                                AccessibilityNodeInfo accessibilityNodeInfo = AccessibilitUtil.findNodeInfosById(nodeInfo, "com.kuaishou.nebula:id/slide_play_right_follow_button", 1);
                                if (accessibilityNodeInfo != null) {
                                    GestureDescriptionUtil.clickMonitor(accessibilityServiceMonitor, accessibilityNodeInfo, new GestureDescriptionUtil.OnMonitorDoSomethingFinish() {
                                        @Override
                                        public void onCompleted(GestureDescription gestureDescription) {
                                                handler.removeCallbacks(runnableMove);
                                                handler.postDelayed(runnableMove, 500);

                                        }
                                    });
                                } else {
                                    handler.removeCallbacks(runnableMove);
                                    handler.postDelayed(runnableMove, 500);
                                }
                            }
                        }, 200);

//                            GestureDescriptionUtil.clickMonitor(accessibilityServiceMonitor, AccessibilitUtil.findNodeInfosById(nodeInfo, "com.kuaishou.nebula:id/slide_play_right_follow_button", 1), new GestureDescriptionUtil.OnMonitorDoSomethingFinish() {
//                                @Override
//                                public void onCompleted(GestureDescription gestureDescription) {
//                                    moveMonitor(accessibilityServiceMonitor);
//                                }
//                            });
                    }
                });
            } else {
                handler.removeCallbacks(runnableMove);
                handler.postDelayed(runnableMove, 500);
            }
        } else {
            handler.removeCallbacks(runnableMove);
            handler.postDelayed(runnableMove, 500);
        }
    }

    */
    private void moveMonitor(final SuperServiceMonitor accessibilityServiceMonitor) {
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        Path path = new Path();
//        Log.d("GestureDescription", rect.centerX() + " ->" + rect.centerY());
        int width = AccessibilitUtil.getScreenInfo().get("width");
        int height = AccessibilitUtil.getScreenInfo().get("height");
        path.moveTo(width / 2, height - 40);
        path.lineTo(width / 2, 50);
        startFlag++;
        if (startFlag >= 10) {
            GestureDescriptionUtil.moveMonitor(accessibilityServiceMonitor, width / 2, height / 2 - 100, width / 2, height / 2 + 100, null);
            startFlag = 0;
        } else {
            GestureDescriptionUtil.moveMonitor(accessibilityServiceMonitor, width / 2, height - 40, width / 2, 50, null);
        }
    }
}
