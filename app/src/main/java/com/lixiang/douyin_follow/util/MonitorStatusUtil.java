package com.lixiang.douyin_follow.util;

import com.lixiang.douyin_follow.service.SuperServiceMonitor;

public class MonitorStatusUtil {

    private static MonitorStatusUtil instance = null;

    private MonitorStatusUtil() {
    }

    public static synchronized MonitorStatusUtil getInstance() {
        if (instance == null) {
            synchronized (MonitorStatusUtil.class) {
                if (instance == null) {
                    instance = new MonitorStatusUtil();
                    instance.setMonitorContinuedTime(System.currentTimeMillis());
                }
            }
        }
        return instance;
    }

    //任务执行时的时间
    public long monitorContinuedTime;
    public SuperServiceMonitor superServiceMonitor = null;
    public String monitorPackgeName;

    public String getMonitorPackgeName() {
        return monitorPackgeName;
    }

    public void setMonitorPackgeName(String monitorPackgeName) {
        this.monitorPackgeName = monitorPackgeName;
    }

    public long getMonitorContinuedTime() {
        return monitorContinuedTime;
    }

    public void setMonitorContinuedTime(long monitorContinuedTime) {
        this.monitorContinuedTime = monitorContinuedTime;
    }

    public SuperServiceMonitor getSuperServiceMonitor() {
        return superServiceMonitor;
    }

    public void setSuperServiceMonitor(SuperServiceMonitor superServiceMonitor) {
        this.superServiceMonitor = superServiceMonitor;
    }
}
