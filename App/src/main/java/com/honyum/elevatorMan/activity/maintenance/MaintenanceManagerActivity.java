package com.honyum.elevatorMan.activity.maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;

public class MaintenanceManagerActivity extends BaseFragmentActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_manager);

        initTitleBar();
        initView();
    }

    private void initTitleBar() {

        initTitleBar(getString(R.string.property_maintenance), R.id.title_maintence_manager,
                R.drawable.back_normal, backClickListener);
    }

    private void initView() {
        findViewById(R.id.ll_my_lift).setOnClickListener(this);
        findViewById(R.id.ll_lift_plan).setOnClickListener(this);
        findViewById(R.id.ll_lift_upload).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_my_lift:
                startActivity(new Intent(MaintenanceManagerActivity.this, LiftActivity.class));
                break;
            case R.id.ll_lift_plan:
                startActivity(new Intent(MaintenanceManagerActivity.this, LiftPlanActivity.class));
                break;
            case R.id.ll_lift_upload:
                startActivity(new Intent(MaintenanceManagerActivity.this, LiftCompleteActivity.class));
                break;
        }
    }
}
