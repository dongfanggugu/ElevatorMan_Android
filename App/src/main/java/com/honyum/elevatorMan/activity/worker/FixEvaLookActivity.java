package com.honyum.elevatorMan.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.FixInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Star on 2017/6/12.
 */

public class FixEvaLookActivity extends BaseActivityWraper {


    @BindView(R.id.ratingbar2)
    RatingBar ratingbar2;
    @BindView(R.id.et_remark)
    EditText etRemark;
    private FixInfo mFixInfo;

    @Override
    public String getTitleString() {
        return getString(R.string.fix_look_eva);
    }

    @Override
    protected void initView() {
        mFixInfo =  getIntent("Info");
        etRemark.setText(mFixInfo.getEvaluateInfo());
        ratingbar2.setRating(Integer.valueOf(mFixInfo.getEvaluate()));
        ratingbar2.setFocusable(false);
        etRemark.setFocusable(false);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_look_eva;
    }

}
