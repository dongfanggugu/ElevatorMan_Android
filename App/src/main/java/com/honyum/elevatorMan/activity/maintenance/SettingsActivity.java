package com.honyum.elevatorMan.activity.maintenance;

import android.os.Bundle;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.worker.WorkerBaseActivity;

public class SettingsActivity extends WorkerBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initTitleBar();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar(getString(R.string.settings), R.id.title_settings, R.drawable.navi_setting_normal,
                menuClickListener);
    }
}
