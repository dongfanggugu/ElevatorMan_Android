package com.honyum.elevatorMan.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.utils.Utils;

public class SettingsActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        initTitleBar();
        initView();
    }

    private void initTitleBar() {
        initTitleBar("设置", R.id.title_settings, R.drawable.back_normal, backClickListener);
    }

    private void initView() {
        findViewById(R.id.ll_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, PersonModifyActivity.class);
                intent.putExtra("category", "pwd");
                startActivity(intent);
            }
        });


        findViewById(R.id.ll_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRemoteVersion(new ICheckVersionCallback() {
                    @Override
                    public void onCheckVersion(int remoteVersion, String url, int isForce, String description) {
                        updateApk(remoteVersion, url, isForce, description);
                    }
                });
            }
        });

        findViewById(R.id.ll_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
            }
        });
    }

    /**
     * 更新apk
     *
     * @param remoteVersion
     * @param url
     * @param isForce
     */
    private void updateApk(int remoteVersion, String url, int isForce, String description) {

        //当没有下载url时，不再进行版本的检测
        if (StringUtils.isEmpty(url)) {
            popMsgAlertWithoutCancel("当前版本已经是最新!", null);
            return;
        }

        if (StringUtils.isEmpty(description)) {
            description = "有新的版本可用";
        }

        int curVersion = Utils.getVersionCode(this);
        if (remoteVersion > curVersion) {
            updateApk(url, isForce, description, null);

        } else {
            popMsgAlertWithoutCancel("当前版本已经是最新!", null);
        }
    }

}
