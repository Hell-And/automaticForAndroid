package com.lixiang.douyin_follow.util;

import com.tencent.mmkv.MMKV;

public class MMKVutil {
    public static MMKVutil instance = new MMKVutil();
    MMKV kv = MMKV.defaultMMKV();

    public boolean putBoolean(String key, boolean bool) {
        return kv.encode(key, bool);
    }

    public boolean getBoolean(String key, boolean defaultBool) {
        return kv.decodeBool(key, defaultBool);
    }
}
