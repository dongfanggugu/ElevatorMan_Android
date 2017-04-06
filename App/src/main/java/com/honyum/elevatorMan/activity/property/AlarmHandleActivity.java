package com.honyum.elevatorMan.activity.property;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.fragment.CurrentAlarmFragment;
import com.honyum.elevatorMan.fragment.HistoryAlarmFragment;
import com.honyum.elevatorMan.fragment.ProjectAlarmFragment;

public class AlarmHandleActivity extends BaseFragmentActivity {

    private CurrentAlarmFragment currentAlarmFragment;

    private HistoryAlarmFragment historyAlarmFragment;

    private ProjectAlarmFragment projectAlarmFragment;

    private FragmentManager fragmentManager;

    private FragmentTransaction fragmentTransaction;

    private Fragment preFragment;

    private ImageView preIv;

    private TextView preTv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_handle);

        initFragment();
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();

        initCurFragment();

        findViewById(R.id.ll_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCurFragment();
            }
        });

        findViewById(R.id.ll_history_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initHisFragment();
            }
        });

        findViewById(R.id.ll_pro_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initProFragment();
            }
        });
    }

    private void initCurFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();

        if (currentAlarmFragment == null) {
            currentAlarmFragment = new CurrentAlarmFragment();
            fragmentTransaction.add(R.id.frameLayout, currentAlarmFragment);
        }

        if (preFragment != null) {
            fragmentTransaction.hide(preFragment);
        }

        if (preIv != null && preTv != null) {
            preIv.setSelected(false);
            preTv.setSelected(false);
        }

        fragmentTransaction.show(currentAlarmFragment);
        fragmentTransaction.commit();

        preIv = (ImageView) findViewById(R.id.iv_alarm);
        preTv = (TextView) findViewById(R.id.tv_alarm);

        preIv.setSelected(true);
        preTv.setSelected(true);

        preFragment = currentAlarmFragment;
    }

    private void initHisFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();

        if (historyAlarmFragment == null) {
            historyAlarmFragment = new HistoryAlarmFragment();
            fragmentTransaction.add(R.id.frameLayout, historyAlarmFragment);
        }

        if (preFragment != null) {
            fragmentTransaction.hide(preFragment);
        }

        if (preIv != null && preTv != null) {
            preIv.setSelected(false);
            preTv.setSelected(false);
        }

        fragmentTransaction.show(historyAlarmFragment);
        fragmentTransaction.commit();

        preIv = (ImageView) findViewById(R.id.iv_history_alarm);
        preTv = (TextView) findViewById(R.id.tv_history_alarm);

        preIv.setSelected(true);
        preTv.setSelected(true);

        preFragment = historyAlarmFragment;
    }

    private void initProFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();

        if (projectAlarmFragment == null) {
            projectAlarmFragment = new ProjectAlarmFragment();
            fragmentTransaction.add(R.id.frameLayout, projectAlarmFragment);
        }

        if (preFragment != null) {
            fragmentTransaction.hide(preFragment);
        }

        if (preIv != null && preTv != null) {
            preIv.setSelected(false);
            preTv.setSelected(false);
        }

        fragmentTransaction.show(projectAlarmFragment);
        fragmentTransaction.commit();

        preIv = (ImageView) findViewById(R.id.iv_pro_alarm);
        preTv = (TextView) findViewById(R.id.tv_pro_alarm);

        preIv.setSelected(true);
        preTv.setSelected(true);

        preFragment = projectAlarmFragment;
    }
}
