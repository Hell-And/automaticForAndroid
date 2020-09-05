package com.lixiang.douyin_follow.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.lixiang.douyin_follow.Broad.MonitorStatusReceiver;
import com.lixiang.douyin_follow.util.MonitorStatusUtil;

import androidx.annotation.Nullable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TimerIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.lixiang.douyin_follow.service.action.FOO";
    private static final String ACTION_BAZ = "com.lixiang.douyin_follow.service.action.BAZ";
    private String TAG = TimerIntentService.class.getSimpleName();
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.lixiang.douyin_follow.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.lixiang.douyin_follow.service.extra.PARAM2";

    private Handler handler = new Handler();
    private Runnable runnableMove = new Runnable() {
        @Override
        public void run() {
//            while (true) {
//                //用这种方式判断一下 页面是否长时间没有操作了，如果是的那就退出应用，尝试重启应用
//                if (System.currentTimeMillis() - MonitorStatusUtil.getInstance().getMonitorContinuedTime() > 1000 * 60) {
//                    MonitorStatusUtil.getInstance().getSuperServiceMonitor().performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
//                    Intent intent = new Intent();
//                    intent.setAction("lixiang.nosomething.timer.over");
//                    sendBroadcast(intent);//发送标准广播
//                    Log.d(TAG, "run: "+"发送广播");
//                    break;
//                }
//            }
        }
    };
    private MonitorStatusReceiver monitorStatusReceiver;

    public TimerIntentService() {
        super("TimerIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        monitorStatusReceiver = new MonitorStatusReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("lixiang.nosomething.timer.over");
        //当网络发生变化的时候，系统广播会发出值为android.net.conn.CONNECTIVITY_CHANGE这样的一条广播
        registerReceiver(monitorStatusReceiver, intentFilter);
        Log.d(TAG, "onCreate: " + "timer启动");
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        handler.removeCallbacks(runnableMove);
        return super.onStartCommand(intent, START_STICKY_COMPATIBILITY, startId);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, TimerIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, TimerIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            while (true) {
                //用这种方式判断一下 页面是否长时间没有操作了，如果是的那就退出应用，尝试重启应用
                if (System.currentTimeMillis() - MonitorStatusUtil.getInstance().getMonitorContinuedTime() > 1000 * 20) {
                    MonitorStatusUtil.getInstance().setMonitorContinuedTime(System.currentTimeMillis());
//                    MonitorStatusUtil.getInstance().getSuperServiceMonitor().performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                    if (MonitorStatusUtil.getInstance().getSuperServiceMonitor() != null){
                        MonitorStatusUtil.getInstance().getSuperServiceMonitor().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        MonitorStatusUtil.getInstance().getSuperServiceMonitor().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        MonitorStatusUtil.getInstance().getSuperServiceMonitor().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        MonitorStatusUtil.getInstance().getSuperServiceMonitor().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    }
                    Intent newIntent = new Intent();
                    newIntent.setAction("lixiang.nosomething.timer.over");
                    //第一个参数为包的路径，第二个参数为类名
//                    newIntent.setComponent(new ComponentName("com.lixiang.douyin_follow.Broad", "com.lixiang.douyin_follow.Broad.MonitorStatusReceiver"));
//                    sendBroadcast(newIntent);//发送标准广播
                    Log.d(TAG, "run: " + "发送广播");
//                    break;
                }
            }

//            handler.removeCallbacks(runnableMove);
//            handler.post(runnableMove);
//            final String action = intent.getAction();
//            if (ACTION_FOO.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionFoo(param1, param2);
//            } else if (ACTION_BAZ.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(param1, param2);
//            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(monitorStatusReceiver);
    }
}
