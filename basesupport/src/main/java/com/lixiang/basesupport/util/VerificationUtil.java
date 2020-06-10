package com.lixiang.basesupport.util;

public class VerificationUtil {
    private static VerificationUtil instance;

    private VerificationUtil() {
    }

    public static VerificationUtil getInstance() {
        if (instance == null) {
            instance = new VerificationUtil();
        }
        return instance;
    }

    public String myActivationRule(String distinguishCode) {
        insertHelpIndex = 0;
        String dateTime = PublicUtil.getFormatDateTime();
//        tvDateTime.setText(dateTime);
        String md5First = PublicUtil.stringToMD5(distinguishCode + dateTime);
        StringBuffer md5Second = new StringBuffer(md5First);
        int md5Length = md5First.length();
        String dateTimeMillis = String.valueOf(PublicUtil.getFormatDateTimeMillis(dateTime));
        int insertIndex = 0;
        for (int length = md5Length; length > 0; length--) {
            md5Second.insert(length - 1, charAt(dateTimeMillis, insertIndex++));
        }
        return md5Second.toString();
    }

    public String getDistinguishCode(String IMEI, String deviceSn) {
        return PublicUtil.stringToMD5(IMEI + deviceSn);
    }

    private int insertHelpIndex = 0;

    private char charAt(String dateTimeMillis, int index) {
        if (index >= dateTimeMillis.length()) {
            return charAt(PublicUtil.stringToMD5(dateTimeMillis), insertHelpIndex++);
        }
        return PublicUtil.stringToMD5(dateTimeMillis).charAt(index);
    }
}