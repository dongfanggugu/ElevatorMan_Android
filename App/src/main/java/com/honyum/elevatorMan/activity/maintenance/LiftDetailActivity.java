package com.honyum.elevatorMan.activity.maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.LiftInfo;

public class LiftDetailActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lift_detail);
        initTitleBar();
        initView();
    }

    private void initTitleBar() {
        initTitleBar("电梯详情", R.id.title_lift_detail,
                R.drawable.back_normal, backClickListener);;
    }

    private void initView() {
        Intent intent = getIntent();
        LiftInfo info = (LiftInfo) intent.getSerializableExtra("info");
        if (null == info) {
            return;
        }

        ((TextView)findViewById(R.id.tv_code)).setText(info.getNum());
        ((TextView)findViewById(R.id.tv_project)).setText(info.getCommunityName());
        ((TextView)findViewById(R.id.tv_address)).setText(info.getAddress());
        ((TextView)findViewById(R.id.tv_brand)).setText(info.getBrand());
        ((TextView)findViewById(R.id.tv_worker)).setText(getConfig().getName());
        ((TextView)findViewById(R.id.tv_tel)).setText(getConfig().getTel());






    }
}
