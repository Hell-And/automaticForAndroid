package com.lixiang.douyin_follow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}