package com.honyum.elevatorMan.activity.company;

import android.os.Bundle;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.MaintRecInfo;
import com.honyum.elevatorMan.utils.DataTravelUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Star on 2017/6/19.
 */

public class EMantenanceDetailActivity extends BaseActivityWraper {


    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_worker1)
    TextView tvWorker1;
    @BindView(R.id.tv_plantime)
    TextView tvPlantime;
    @BindView(R.id.tv_weibaotime)
    TextView tvWeibaotime;
    @BindView(R.id.tv_weibaotype)
    TextView tvWeibaotype;

    MaintRecInfo data;

    @Override
    public String getTitleString() {
        return "维保详情";
    }

    @Override
    protected void initView() {

        data = getIntent("Info");
        tvCode.setText(data.getElevatorId());
        tvProject.setText(data.getCommunityName());
        tvAddress.setText(data.getAddress());
        tvWorker1.setText(data.getUserName());
        tvPlantime.setText(data.getPlanTime());
        tvWeibaotime.setText(data.getMaintTime());
        tvWeibaotype.setText(data.getMaintType());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_ementenance_detail;
    }


}
