package com.honyum.elevatorMan.activity.maintenance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.MaintenanceTaskInfo;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;

/**
 * Created by Star on 2017/6/12.
 */

public class MaintenanceEvaResult extends BaseFragmentActivity {

    

    private MaintenanceTaskInfo mMaintenanceTaskInfo;
    private RatingBar ratingbar2;
    private EditText et_remark;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_eva);
        initTitle();
        initView();
    }

    private void initView() {
        Intent it = getIntent();
        mMaintenanceTaskInfo = (MaintenanceTaskInfo) it.getSerializableExtra("Info");

        ratingbar2 = (RatingBar)findViewById(R.id.ratingbar2);
        ratingbar2.setNumStars(Integer.valueOf(mMaintenanceTaskInfo.getEvaluateResult()));
        ratingbar2.setFocusable(false);
        et_remark = (EditText)findViewById(R.id.et_remark);
        et_remark.setText(mMaintenanceTaskInfo.getEvaluateContent());
        et_remark.setFocusable(false);


    }

    /**
     * 初始化标题
     */
    private void initTitle() {


        initTitleBar("评价结果查看", R.id.title_order,
                R.drawable.back_normal, backClickListener);
    }
}
