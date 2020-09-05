package com.lixiang.basesupport.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.noober.background.BackgroundLibrary;

/**
 * Created by lixiang on 2020/6/8.
 * Email: 903087038@qq.com
 * Desc:
 */
public class BaseActivity extends AppCompatActivity {
   public Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁用横屏
        mContext=this;
    }

    public void showToastText(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}
