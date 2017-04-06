package com.honyum.elevatorMan.activity.property;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.PersonActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.SysActivityManager;

public class PropertyMainPageActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property1);

        initTitleBar(R.id.title, "电梯易管家");

        initView();
    }

    private void initView() {
        findViewById(R.id.ll_current_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToCurAlarm();
            }
        });

        findViewById(R.id.ll_project_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToProAlarm();
            }
        });

        findViewById(R.id.ll_elevator_mall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToElevatorMall();
            }
        });

        findViewById(R.id.ll_maintenance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToProjectList();
            }
        });

        findViewById(R.id.ll_personal_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToPerson();
            }
        });

        findViewById(R.id.ll_near_maintenance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToNearMt();
            }
        });

        findViewById(R.id.ll_value_added_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToValueAddedService();
            }
        });
    }


    /**
     * 跳转增值服务
     */
    private void jumpToValueAddedService() {
    }


    /**
     * 跳转附近维保
     */
    private void jumpToNearMt() {
        Intent intent = new Intent(this, NearMaintenanceActivity.class);
        startActivity(intent);
    }


    /**
     * 跳到个人信息
     */
    private void jumpToPerson() {
        Intent intent = new Intent(this, PersonActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到维保管理
     */
    private void jumpToProjectList() {
        Intent intent = new Intent(this, PropertyMaintenanceActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到电梯商城
     */
    private void jumpToElevatorMall() {
    }


    /**
     * 跳到项目报警
     */
    private void jumpToProAlarm() {
        Intent intent = new Intent(this, PropertyActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到当前处理的报警
     */
    private void jumpToCurAlarm() {

        Intent intent = new Intent(this, AlarmHandleActivity.class);
        startActivity(intent);

        /*String alarmId = getConfig().getAlarmId();
        String alarmState = getConfig().getAlarmState();

        if (StringUtils.isEmpty(alarmId) || StringUtils.isEmpty(alarmState)) {
            showToast(getString(R.string.no_alarm));
            return;
        }

        Intent intent = new Intent(this, AlarmTraceActivity.class);
        intent.putExtra("alarm_id", alarmId);

        if (alarmState.equals("1")) {  //已经出发
            intent.setAction(Constant.ACTION_WORKER_ASSIGNED);
        } else if (alarmState.equals("2")) {    //已经到达
            intent.setAction(Constant.ACTION_WORKER_ARRIVED);
        } else if (alarmState.equals("3")) {    //已经完成
            intent.setAction(Constant.ACTION_ALARM_COMPLETE);
            intent.putExtra("msg", getString(R.string.property_complete));
        } else if (alarmState.equals(Constant.ALARM_STATE_START)) {    //发生报警
            intent.setAction(Constant.ACTION_ALARM_PROPERTY);
        }
        startActivity(intent);*/
    }

    @Override
    public void onBackPressed() {
        popMsgAlertWithCancel(getString(R.string.exit_confirm), new IConfirmCallback() {
            @Override
            public void onConfirm() {
                PropertyMainPageActivity.super.onBackPressed();
                SysActivityManager.getInstance().exit();
            }
        }, "否", "是", getString(R.string.exit_confirm));
    }
}
