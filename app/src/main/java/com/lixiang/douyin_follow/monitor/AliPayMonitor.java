package com.lixiang.douyin_follow.monitor;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.media.ImageReader;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.view.Window;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lixiang.douyin_follow.service.DouyinServiceMonitor;
import com.lixiang.douyin_follow.util.AccessibilitUtil;
import com.lixiang.douyin_follow.util.GestureDescriptionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付宝能量收取
 * Created by LoverAnd on 2020/6/5 0005.
 */
public class AliPayMonitor {
    static String TAG = AliPayMonitor.class.getSimpleName();
    private static Rect moreRect = new Rect();
    private static Handler handler = new Handler();

    private static Rect userImageRect = new Rect();
    private static int noClickNodeCenterY;

    public static void policy(final DouyinServiceMonitor accessibilityServiceMonitor, final AccessibilityNodeInfo nodeInfo, String packageName, String className) {
        if (!("com.eg.android.AlipayGphone".equals(packageName))
                || className.equals("com.eg.android.AlipayGphone.AlipayLogin")
        ) {
            return;
        }
        Log.d(TAG, "policy: " + className);

       /* if (className.equals("com.alipay.mobile.nebulax.integration.mpaas.activity.NebulaActivity$Main")) {

            int width = AccessibilitUtil.getScreenInfo().get("width");
            int height = AccessibilitUtil.getScreenInfo().get("height");
            GestureDescriptionUtil.moveMonitor(accessibilityServiceMonitor,
                    new Rect(0, height - 100, width, height),
                    new Rect(0, height / 2, width, height - 100),
                    null);
        }*/
        findWebViewNode(accessibilityServiceMonitor, nodeInfo);


        //放在for循环里面只会点击最后一个，所以把需要点击的节点存起来，然后异步执行点击
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: " + clickNode.size());
                for (AccessibilityNodeInfo accessibilityNodeInfo : clickNode) {
                    GestureDescriptionUtil.clickMonitor(accessibilityServiceMonitor, accessibilityNodeInfo, null);
                    try {
                        Thread.sleep(200l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                //收完一个退出页面
                if (clickNode.size() > 0) {
                    clickNode.clear();
                    accessibilityServiceMonitor.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);//退到home页
                }
            }
        });
//        List<AccessibilityNodeInfo> more_Back = nodeInfo.findAccessibilityNodeInfosByViewId("com.facishare.fs:id/title_leftgroup");
//        if (more_Back.size() > 0) {
//        }
    }

    private static List<AccessibilityNodeInfo> clickNode = new ArrayList<>();

    private static void findWebViewNode(final DouyinServiceMonitor accessibilityServiceMonitor, AccessibilityNodeInfo nodeInfo) {
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            final AccessibilityNodeInfo child = nodeInfo.getChild(i);
            if (child == null) continue;
            CharSequence textChar = child.getText();
            if (textChar != null) {
                String text = textChar.toString();

                //右侧头像节点
                if (text.equals("我的大树养成记录")) {
                    Log.d(TAG, "findWebViewNode: 我的大树养成记录");
                    child.getBoundsInScreen(userImageRect);
                }
//                List<AccessibilityNodeInfo> accessibilityNodeInfos = nodeInfo.findAccessibilityNodeInfosByText("我的大树养成记录");
//                Log.d(TAG, "policy: " + accessibilityNodeInfos.size());
//                if (accessibilityNodeInfos.size() > 0) {
//                    //左边有个按钮也没有文字，会与能量球的逻辑重叠，应该过滤出来不让点击
//                    //按钮就在头像下方，只是一个在屏幕右方，一个在左侧
//                    accessibilityNodeInfos.get(0).getBoundsInScreen(userImageRect);
//                }

                //下方好友排行
                AccessibilityNodeInfo parentNode = child.getParent();
                if (parentNode != null && parentNode.getParent() != null
                        && parentNode.getParent().getClassName().equals("android.widget.ListView")) {
                    //对于可以收取能量的好友右侧小图标是可见的，也有可能显示的是倒计时
                    if (child.isVisibleToUser()) {
                        //判断是不是数字，是数字可能就是右上角的倒计时，也不排除有人昵称是纯数字, 右上角的倒计时有个符号，先替换掉
                        boolean isNum = AccessibilitUtil.isNumeric(text.replace("’", "").replace(" ", ""));
                        if (isNum) {
                            //是纯数字 先排除昵称是纯数字的情况，用坐标来判断
//                            Rect rectNum = new Rect();
//                            child.getBoundsInScreen(rectNum);
//                            //这里判断中心点的x坐标，100是一个估计值，使用monitor.bat可以了解到，昵称view的中心点大概在屏幕中心
//                            if (rectNum.centerX() > AccessibilitUtil.getScreenInfo().get("width") - 100) {
//                                //这里应该是有倒计时显示的item
////                                break;
//                            }
                        } else {
                            //先排除左侧的头像，它也是可见且没有文字
                            Rect rect = new Rect();
                            child.getBoundsInScreen(rect);
                            if (rect.centerX() > AccessibilitUtil.getScreenInfo().get("width") - 100) {
                                //这种情况就是，既显示了，又不是数字（不是倒计时），
                                // 只剩两种情况: 1,可以收取，显示的是绿色的小手图标。2，可以帮忙收取,显示的是黄色的爱心图标
                                //暂时还无法区分具体是哪一种
                                if (isClickNode(child)) {
                                    Log.d(TAG, "findWebViewNode: getActionList " + child.getActionList().size());

                                    GestureDescriptionUtil.clickMonitor(accessibilityServiceMonitor, child, null);

                                    //准备一个bitmap对象，用来将copy出来的区域绘制到此对象中
//                                    int pixel = Bitmap.getPixel(100, 100);
//
//                                    final ImageReader imageReader = ImageReader.newInstance(size.x, size.y, PixelFormat.RGBA_8888, 1);
//                                    mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
//                                            size.x, size.y, metrics.densityDpi,
//                                            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
//                                            imageReader.getSurface(), null, null);
//
//                                    GBData.reader = imageReader;

                                    break;
                                }
                            }
                        }
                    }
                }
            }


            if ("android.widget.Button".equals(child.getClassName())) {
                //能量球是button 而且文字是空

                if (child.getText() == null || child.getText().toString().replace(" ", "").isEmpty()) {
                    Log.d(TAG, "findWebViewNode: " + "收");
                    if (isClickNode(child) && !clickNode.contains(child)) {
//                        if (child.getParent().getParent().getClassName().equals(""))
                        clickNode.add(child);
                    }
                    ;

//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            GestureDescriptionUtil.clickMonitor(accessibilityServiceMonitor, child, null);
//                        }
//                    }, 500l);
                } else if (child.getText().toString().contains("收集能量")) {
                    clickNode.add(child);
//                    GestureDescriptionUtil.clickMonitor(accessibilityServiceMonitor, child, null);
                } else {
                    Log.d(TAG, "findWebViewNode: getText " + child.getText().toString());
                }
            }

            if (child.getChildCount() > 0) {
                findWebViewNode(accessibilityServiceMonitor, child);
            }
        }
    }

    /*
     * 判断节点是否需要点击
     * */
    private static boolean isClickNode(AccessibilityNodeInfo nodeInfo) {
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        if (rect.centerY() < 0
                || rect.centerY() >= AccessibilitUtil.getScreenInfo().get("height")
                || rect.centerX() < 0
                || rect.centerX() >= AccessibilitUtil.getScreenInfo().get("width"))
            return false;
        //头像就在不需要点击按钮的上方 50是一个预估值,如果超过50 大致可以确定是能量球了
        return rect.top - userImageRect.bottom > 60;
    }
}
