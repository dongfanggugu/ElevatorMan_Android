package com.honyum.elevatorMan.activity.worker;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.FixTaskInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Star on 2017/6/12.
 */

public class FixResultLookActivity extends BaseActivityWraper {


    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.iv_image1)
    ImageView ivImage1;
    @BindView(R.id.iv_image2)
    ImageView ivImage2;
    @BindView(R.id.iv_image3)
    ImageView ivImage3;
    @BindView(R.id.iv_image4)
    ImageView ivImage4;
    @BindView(R.id.tv_fix_complete)
    TextView tvFixComplete;
    @BindView(R.id.tv_fix_complete1)
    TextView tvFixComplete1;

    private FixTaskInfo mFixTaskInfo;


    @Override
    public String getTitleString() {
        return getString(R.string.fix_result_look);
    }

    @Override
    protected void initView() {
        tvFixComplete.setVisibility(View.GONE);
        tvFixComplete1.setVisibility(View.GONE);
        mFixTaskInfo = getIntent("Info");
        etRemark.setFocusable(false);
        etRemark.setText(mFixTaskInfo.getFinishResult());
        String[] s = mFixTaskInfo.getPictures().split(",");
        ImageView[] imageIndex = new ImageView[]{ivImage1,ivImage2,ivImage3,ivImage4};
        if(s!=null)
        {
            for (int i = 0 ; i<s.length;i++)
            {
                new GetPicture(s[i],imageIndex[i]).execute();
            }
        }


       // new GetPicture(mMaintenanceTaskInfo.getAfterImg(),iv_after_image).execute();

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_fix_result;
    }


}
