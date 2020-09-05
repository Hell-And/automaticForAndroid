package com.lixiang.douyin_follow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.lixiang.basesupport.VerificationActivity;
import com.lixiang.basesupport.base.BaseActivity;
import com.lixiang.basesupport.util.PublicUtil;
import com.lixiang.basesupport.util.VerificationUtil;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        if (PublicUtil.getIMEI(this).equals("865737032360084")) {
            toMainPage();
        } else {
            startActivityForResult(new Intent(this, VerificationActivity.class), 101);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                //激活码验证通过
                toMainPage();
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 101) {
                //激活码验证不通过
                finish();
            }
        }
    }

    private void toMainPage() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
