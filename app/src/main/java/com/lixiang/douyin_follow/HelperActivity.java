package com.lixiang.douyin_follow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.lixiang.basesupport.SuperUserActivity;
import com.lixiang.basesupport.base.BaseActivity;
import com.lixiang.basesupport.util.PublicUtil;

public class HelperActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private NavigationView designNavigationView;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        initView();
    }

    private void initView() {
        designNavigationView = (NavigationView) findViewById(R.id.design_navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        initMune();
        designNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.getActionView();
                Switch sw = (Switch) menuItem.getActionView();
                switch (menuItem.getItemId()) {
                    case R.id.menu_super_user:
                        //进入生成激活码页面
                        startActivity(new Intent(mContext, SuperUserActivity.class));
                        break;
                    case R.id.menu_douyin:
                        sw.setChecked(!sw.isChecked());
                        break;
                    case R.id.menu_taobao:
                        sw.setChecked(!sw.isChecked());
                        break;
                    case R.id.menu_ali:
                        sw.setChecked(!sw.isChecked());
                        break;
                    case R.id.menu_xiaoke:
                        sw.setChecked(!sw.isChecked());
                        break;
                }
                //关闭侧滑菜单
//                drawerLayout.closeDrawers();
                return false;
            }
        });
    }

    private void initMune() {
        Menu menu = designNavigationView.getMenu();
        ((Switch) menu.findItem(R.id.menu_douyin).getActionView()).setOnCheckedChangeListener(this);
        ((Switch) menu.findItem(R.id.menu_taobao).getActionView()).setOnCheckedChangeListener(this);
        ((Switch) menu.findItem(R.id.menu_ali).getActionView()).setOnCheckedChangeListener(this);
        ((Switch) menu.findItem(R.id.menu_xiaoke).getActionView()).setOnCheckedChangeListener(this);

        if (!PublicUtil.getIMEI(mContext).equals("865737032360084")){
            menu.findItem(R.id.menu_super_user).setVisible(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        finish();
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.menu_douyin:
                showToastText(compoundButton.getId() + " " + R.id.menu_douyin);
                break;
            case R.id.menu_taobao:
                showToastText(compoundButton.getId() + " " + R.id.menu_douyin);
                break;
            case R.id.menu_ali:
                showToastText(compoundButton.getId() + " " + R.id.menu_douyin);
                break;
            case R.id.menu_xiaoke:
                showToastText(compoundButton.getId() + " " + R.id.menu_douyin);
                break;
        }
//        drawerLayout.closeDrawers();
    }
}