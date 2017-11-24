package com.honyum.elevatorMan.activity.common;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.ElevatorInfo;
import com.honyum.elevatorMan.utils.Utils;

import org.litepal.crud.DataSupport;

import static android.view.View.GONE;

/**
 * Created by Star on 2017/8/24.
 */

public class ShowEleInfoActivity extends BaseActivityWraper {


    private ElevatorInfo ei;
    private String id;

    private TextView tv_elenum;

    private TextView tv_address;

    private TextView tv_error;

    private TextView tv_x;
    private TextView tv_y;

    private TextView tv_getpostion;

    private ImageView iv_sign;
    private ImageView iv_use;
    private ImageView iv_port;


    @Override
    public String getTitleString() {
        return "查看电梯信息";
    }

    @Override
    protected void initView() {

        findView(R.id.fl_map).setVisibility(GONE);
        id = getIntent().getStringExtra("infos");

        ei = DataSupport.where("eleId = ?", id).find(ElevatorInfo.class).get(0);

        if (ei != null) {

            findView(R.id.tv_submit).setVisibility(GONE);
            findView(R.id.tv_save).setVisibility(GONE);
            tv_elenum = findView(R.id.tv_elenum);
            tv_address = findView(R.id.tv_address);
            tv_error = findView(R.id.tv_error);
            tv_x = findView(R.id.tv_x);
            tv_y = findView(R.id.tv_y);
            tv_getpostion = findView(R.id.tv_getpostion);
            iv_use = findView(R.id.iv_use);
            iv_port = findView(R.id.iv_port);
            iv_sign = findView(R.id.iv_sign);
            tv_elenum.setText(ei.getEleId());
            tv_address.setText(ei.getProjectName());
            tv_x.setText(ei.getX() + "");
            tv_y.setText(ei.getY() + "");
            tv_error.setVisibility(GONE);
            tv_getpostion.setVisibility(GONE);
            iv_sign.setImageBitmap(Utils.stringtoBitmap(ei.getSignImage()));
            iv_use.setImageBitmap(Utils.stringtoBitmap(ei.getUseImage()));
            iv_port.setImageBitmap(Utils.stringtoBitmap(ei.getPortImage()));

//            Glide.with(this).load(ei.getSignImage()).override(480, 640).into(iv_sign);
//            Glide.with(this).load(ei.getUseImage()).override(480, 640).into(iv_use);
//            Glide.with(this).load(ei.getPortImage()).override(480, 640).into(iv_port);


        }


    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_update_eve_info;
    }
}
