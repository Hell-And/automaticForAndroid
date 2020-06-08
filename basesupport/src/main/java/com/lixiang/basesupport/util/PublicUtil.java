package com.lixiang.basesupport.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by lixiang on 2020/6/8.
 * Email: 903087038@qq.com
 * Desc:
 */
public class PublicUtil {
    private static final String TAG = PublicUtil.class.getSimpleName();
    /*
     * 获取序列号
     * */

    @SuppressLint({"NewApi", "MissingPermission"})
    public static String getDeviceSN() {

        String serial = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0+
                serial = Build.getSerial();
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//8.0+
                serial = Build.SERIAL;
            } else {//8.0-
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("e", "读取设备序列号异常：" + e.toString());
        }
        return serial;
    }


    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context){
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                imei = tm.getDeviceId();
            }else {
                Method method = tm.getClass().getMethod("getImei");
                imei = (String) method.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }

    public static String getFormatDateTime() {
//        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d1 = new Date();
        return format.format(d1);
    }

    public static Long getFormatDateTimeMillis(String serverTime) {
//        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return format.parse(serverTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "getFormatDateTimeMillis: ", e);
        }
        return 0l;
    }

    /**
     * 将字符串转成MD5值
     *
     * @param string
     * @return
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static void clipString(String text){
        //复制识别号
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) ContextUtil.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }
}
