package com.lixiang.basesupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lixiang.basesupport.base.BaseActivity;
import com.lixiang.basesupport.util.PublicUtil;
import com.lixiang.basesupport.util.VerificationUtil;


public class SuperUserActivity extends BaseActivity {
    private EditText etDeviceCode;
    private TextView tvDateTime;
    private TextView tvActivationCode;
    private TextView tvCopyImei;
    private Button btGetVerCoe;
    private Button btVerification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_user);
        initView();
    }

    private void initView() {
        etDeviceCode = (EditText) findViewById(R.id.et_deviceCode);
        tvDateTime = (TextView) findViewById(R.id.tv_dateTime);
        tvActivationCode = (TextView) findViewById(R.id.tv_activationCode);
        tvCopyImei = (TextView) findViewById(R.id.tv_copyImei);
        btGetVerCoe = (Button) findViewById(R.id.bt_getVerCoe);
        btVerification = (Button) findViewById(R.id.bt_verification);

        btGetVerCoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDateTime.setText(PublicUtil.getFormatDateTime());
                tvActivationCode.setText(VerificationUtil.getInstance().myActivationRule());
            }
        });
        findViewById(R.id.tv_copyImei).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PublicUtil.clipString(tvActivationCode.getText().toString());
                showToastText("复制成功");
            }
        });
    }
}
