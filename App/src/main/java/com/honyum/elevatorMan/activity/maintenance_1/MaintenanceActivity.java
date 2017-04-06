package com.honyum.elevatorMan.activity.maintenance_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.PersonActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.fragment.maintenance_1.ElevatorManagementFragment;
import com.honyum.elevatorMan.fragment.maintenance_1.MtManagementFragment;
import com.honyum.elevatorMan.fragment.maintenance_1.MtStatisticsFragment;
import com.honyum.elevatorMan.fragment.maintenance_1.MtTodayFragment;

public class MaintenanceActivity extends BaseFragmentActivity {


    private MtTodayFragment mtTodayFragment;

    private MtManagementFragment mtManagementFragment;

    private ElevatorManagementFragment elevatorManagementFragment;

    private MtStatisticsFragment mtStatisticsFragment;

    private FragmentManager fragmentManager;

    private FragmentTransaction fragmentTransaction;

    private TextView preTvTab;

    private ImageView preIvTab;

    private Fragment preFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance2);

        initView();
    }


    private void initView() {

        fragmentManager = getSupportFragmentManager();

        initMtFragment();

        findViewById(R.id.ll_mt_today).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMtFragment();
            }
        });

        findViewById(R.id.ll_mt_management).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMmFragment();
            }
        });

        findViewById(R.id.ll_elevator_management).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initEmFragment();
            }
        });

        findViewById(R.id.ll_mt_statistics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMsFragment();
            }
        });

        findViewById(R.id.ll_personal_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaintenanceActivity.this, PersonActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initMsFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();

        if (mtStatisticsFragment == null) {
            mtStatisticsFragment = new MtStatisticsFragment();
            fragmentTransaction.add(R.id.frameLayout, mtStatisticsFragment);
        }

        if (preFragment != null) {
            fragmentTransaction.hide(preFragment);
        }

        fragmentTransaction.show(mtStatisticsFragment);
        fragmentTransaction.commit();

        if (preTvTab != null && preIvTab != null) {
            preTvTab.setSelected(false);
            preIvTab.setSelected(false);
        }

        ImageView ivTab = (ImageView) findViewById(R.id.iv_mt_statistics);
        TextView tvTab = (TextView) findViewById(R.id.tv_mt_statistics);

        ivTab.setSelected(true);
        tvTab.setSelected(true);

        preIvTab = ivTab;
        preTvTab = tvTab;
        preFragment = mtStatisticsFragment;
    }

    private void initEmFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();

        if (elevatorManagementFragment == null) {
            elevatorManagementFragment = new ElevatorManagementFragment();
            fragmentTransaction.add(R.id.frameLayout, elevatorManagementFragment);
        }

        if (preFragment != null) {
            fragmentTransaction.hide(preFragment);
        }

        fragmentTransaction.show(elevatorManagementFragment);
        fragmentTransaction.commit();

        if (preTvTab != null && preIvTab != null) {
            preTvTab.setSelected(false);
            preIvTab.setSelected(false);
        }

        ImageView ivTab = (ImageView) findViewById(R.id.iv_elevator_management);
        TextView tvTab = (TextView) findViewById(R.id.tv_elevator_management);

        ivTab.setSelected(true);
        tvTab.setSelected(true);

        preIvTab = ivTab;
        preTvTab = tvTab;
        preFragment = elevatorManagementFragment;
    }

    private void initMmFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();

        if (mtManagementFragment == null) {
            mtManagementFragment = new MtManagementFragment();
            fragmentTransaction.add(R.id.frameLayout, mtManagementFragment);
        }

        if (preFragment != null) {
            fragmentTransaction.hide(preFragment);
        }

        fragmentTransaction.show(mtManagementFragment);
        fragmentTransaction.commit();

        if (preTvTab != null && preIvTab != null) {
            preTvTab.setSelected(false);
            preIvTab.setSelected(false);
        }

        ImageView ivTab = (ImageView) findViewById(R.id.iv_mt_management);
        TextView tvTab = (TextView) findViewById(R.id.tv_mt_management);

        ivTab.setSelected(true);
        tvTab.setSelected(true);

        preIvTab = ivTab;
        preTvTab = tvTab;
        preFragment = mtManagementFragment;
    }

    private void initMtFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();

        if (mtTodayFragment == null) {
            mtTodayFragment = new MtTodayFragment();
            fragmentTransaction.add(R.id.frameLayout, mtTodayFragment);
        }

        if (preFragment != null) {
            fragmentTransaction.hide(preFragment);
        }

        fragmentTransaction.show(mtTodayFragment);
        fragmentTransaction.commit();

        if (preTvTab != null && preIvTab != null) {
            preTvTab.setSelected(false);
            preIvTab.setSelected(false);
        }

        ImageView ivTab = (ImageView) findViewById(R.id.iv_mt_today);
        TextView tvTab = (TextView) findViewById(R.id.tv_mt_today);

        ivTab.setSelected(true);
        tvTab.setSelected(true);

        preIvTab = ivTab;
        preTvTab = tvTab;
        preFragment = mtTodayFragment;
    }
}
