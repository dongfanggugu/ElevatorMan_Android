package com.honyum.elevatorMan.activity.company;

import android.os.Bundle;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.AlarmInfo1;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Star on 2017/6/15.
 */

public class RescuHisDetailActivity extends BaseActivityWraper {

    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_lift_add)
    TextView tvLiftAdd;
    @BindView(R.id.tv_lift_code)
    TextView tvLiftCode;
    @BindView(R.id.tv_lift_date)
    TextView tvLiftDate;
    @BindView(R.id.tv_lift_saved)
    TextView tvLiftSaved;
    @BindView(R.id.tv_lift_injured)
    TextView tvLiftInjured;

    private AlarmInfo1 mAlarmInfo1;

    @Override
    public String getTitleString() {
        return getString(R.string.save_detail);
    }

    @Override
    protected void initView() {

        mAlarmInfo1=  getIntent("Info");
        tvProject.setText(mAlarmInfo1.getCommunityName());
        tvLiftAdd.setText(mAlarmInfo1.getCommunityAddress());
        tvLiftCode.setText(mAlarmInfo1.getLiftNum());
        tvLiftDate.setText(mAlarmInfo1.getAlarmTime());
        tvLiftSaved.setText(mAlarmInfo1.getSavedCount()+"");
        tvLiftInjured.setText(mAlarmInfo1.getInjureCount()+"");
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_company_save_detail;
    }


}
