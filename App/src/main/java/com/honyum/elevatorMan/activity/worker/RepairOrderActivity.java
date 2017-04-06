package com.honyum.elevatorMan.activity.worker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.fragment.RepairHistoryFragment;
import com.honyum.elevatorMan.fragment.RepairUnderwayFragment;

public class RepairOrderActivity extends BaseFragmentActivity {


    private RepairUnderwayFragment underwayFragment;

    private RepairHistoryFragment historyFragment;

    private FragmentManager fragmentManager;

    private FragmentTransaction fragmentTransaction;

    private Fragment preFragment;

    private ImageView preIv;

    private TextView preTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_order);

        initTitleBar("业主报修", R.id.title, R.drawable.back_normal, backClickListener);

        initFragment();
    }

    private void initFragment() {

        fragmentManager = getSupportFragmentManager();

        initUnderwayFragment();

        findViewById(R.id.ll_mt_underway).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUnderwayFragment();
            }
        });

        findViewById(R.id.ll_mt_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initHistoryFragment();
            }
        });
    }

    private void initHistoryFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();

        if (historyFragment == null) {
            historyFragment = new RepairHistoryFragment();
            fragmentTransaction.add(R.id.frameLayout, historyFragment);
        }

        if (preFragment != null) {
            fragmentTransaction.hide(preFragment);
        }

        if (preIv != null && preTv != null) {
            preIv.setSelected(false);
            preTv.setSelected(false);
        }

        fragmentTransaction.show(historyFragment);
        fragmentTransaction.commit();

        preIv = (ImageView) findViewById(R.id.iv_mt_history);
        preTv = (TextView) findViewById(R.id.tv_mt_history);

        preIv.setSelected(true);
        preTv.setSelected(true);

        preFragment = historyFragment;
    }

    private void initUnderwayFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();

        if (underwayFragment == null) {
            underwayFragment = new RepairUnderwayFragment();
            fragmentTransaction.add(R.id.frameLayout, underwayFragment);
        }

        if (preFragment != null) {
            fragmentTransaction.hide(preFragment);
        }

        if (preIv != null && preTv != null) {
            preIv.setSelected(false);
            preTv.setSelected(false);
        }

        fragmentTransaction.show(underwayFragment);
        fragmentTransaction.commit();

        preIv = (ImageView) findViewById(R.id.iv_mt_underway);
        preTv = (TextView) findViewById(R.id.tv_mt_underway);

        preIv.setSelected(true);
        preTv.setSelected(true);

        preFragment = underwayFragment;
    }
}
