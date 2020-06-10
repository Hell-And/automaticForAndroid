package com.lixiang.basesupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lixiang.basesupport.base.BaseActivity;
import com.lixiang.basesupport.util.PublicUtil;
import com.lixiang.basesupport.util.VerificationUtil;
import com.lixiang.basesupport.widget.BlockView;

public class VerificationActivity extends BaseActivity {
    String TAG = VerificationActivity.class.getSimpleName();
    private TextView tvDeviceNo, tvDateTime, tvActivationCode, tvResult, tv_copyImei;
    private Button btGetVerCoe, btVerification;
    private boolean hasPermission = false;
    private String distinguishCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        initView();
        requestPermis();
        setViewData();
    }

    private void requestPermis() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
        } else {
            hasPermission = true;
        }
    }

    private void initView() {
        tvDeviceNo = findViewById(R.id.tv_deviceCode);
        tvDateTime = findViewById(R.id.tv_dateTime);
        tvResult = findViewById(R.id.tv_result);
        tvActivationCode = findViewById(R.id.tv_activationCode);
        btGetVerCoe = findViewById(R.id.bt_getVerCoe);
        btVerification = findViewById(R.id.bt_verification);

        findViewById(R.id.tv_copyImei).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublicUtil.clipString(PublicUtil.getIMEI(mContext));
                showToastText("复制成功");
            }
        });

        btVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateTime = PublicUtil.getFormatDateTime();
                tvDateTime.setText(dateTime);

                if (!hasPermission) {
                    tvResult.setText("必要权限未打开！");
                    requestPermis();
                    return;
                }

                String userActivationCode = tvActivationCode.getText().toString();
                if (TextUtils.isEmpty(userActivationCode)) {
                    tvResult.setText("请输入激活码");
                    return;
                }
                if (!userActivationCode.equals(VerificationUtil.getInstance().myActivationRule(distinguishCode))) {
                    tvResult.setText("激活码不正确！");
                    return;
                }
                //成功
                setResult(RESULT_OK, getIntent());
                finish();

            }
        });

        btGetVerCoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    private void setViewData() {
        tvDateTime.setText(PublicUtil.getFormatDateTime());
        setDistinguishCodeText();
    }

    private void setDistinguishCodeText() {
        if (hasPermission){
            String IMEI = PublicUtil.getIMEI(mContext);
            String deviceSn = PublicUtil.getDeviceSN();
            distinguishCode = VerificationUtil.getInstance().getDistinguishCode(IMEI, deviceSn);
            tvDeviceNo.setText(distinguishCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    hasPermission = true;
                    setDistinguishCodeText();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, getIntent());
        super.onBackPressed();
    }
}
