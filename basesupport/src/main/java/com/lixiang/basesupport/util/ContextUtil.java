package com.lixiang.basesupport.util;

import android.content.Context;

import java.lang.reflect.Method;

/**
 * Created by lixiang on 2019/8/6.
 * Email: 903087038@qq.com
 * Desc:
 */
public class ContextUtil {
    /**
     * Context对象
     */

    /**
     * 取得Context对象
     * PS:必须在主线程调用
     * @return Context
     */
    public static Context getContext() {
            synchronized (ContextUtil.class) {
                    try {
                        Class<?> ActivityThread = Class.forName("android.app.ActivityThread");

                        Method method = ActivityThread.getMethod("currentActivityThread");
                        Object currentActivityThread = method.invoke(ActivityThread);//获取currentActivityThread 对象

                        Method method2 = currentActivityThread.getClass().getMethod("getApplication");
                        return (Context)method2.invoke(currentActivityThread);//获取 Context对象

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        return null;
    }
}
