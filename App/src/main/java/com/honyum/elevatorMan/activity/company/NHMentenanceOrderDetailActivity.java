package com.honyum.elevatorMan.activity.company;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.mydata.NHorderAndTask;
import com.honyum.elevatorMan.data.newdata.ComapnyMentenanceInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Star on 2017/6/15.
 */

public class NHMentenanceOrderDetailActivity extends BaseActivityWraper {
    @BindView(R.id.tv_oorderNum)
    TextView tvOorderNum;
    @BindView(R.id.tv_ordertime)
    TextView tvOrdertime;
    @BindView(R.id.tv_sevicetype)
    TextView tvSevicetype;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_brand)
    TextView tvBrand;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_oneprice)
    TextView tvOneprice;
    @BindView(R.id.tv_cutprice)
    TextView tvCutprice;
    @BindView(R.id.tv_finalpay)
    TextView tvFinalpay;
    @BindView(R.id.tv_paytype)
    TextView tvPaytype;
    @BindView(R.id.tv_paystate)
    TextView tvPaystate;
    @BindView(R.id.tv_paytime)
    TextView tvPaytime;

    NHorderAndTask mComapnyMentenanceInfo;
    @Override
    public String getTitleString() {
        return getString(R.string.orderdetail);
    }

    @Override
    protected void initView() {
        mComapnyMentenanceInfo = getIntent("Info");
        tvOorderNum.setText("订单编号："+mComapnyMentenanceInfo.getMaintOrderId());
        tvOrdertime.setText("下单时间："+mComapnyMentenanceInfo.getCreateTime());
        tvSevicetype.setText("服务类型："+mComapnyMentenanceInfo.getMainttypeInfo().getName());
        etRemark.setFocusable(false);
        etRemark.setText(mComapnyMentenanceInfo.getMainttypeInfo().getContent());

        tvBrand.setText("电梯品牌："+mComapnyMentenanceInfo.getVillaInfo().getBrand());
        tvAddress.setText("别墅地址："+mComapnyMentenanceInfo.getVillaInfo().getAddress());

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_company_orderdetail;
    }


}
