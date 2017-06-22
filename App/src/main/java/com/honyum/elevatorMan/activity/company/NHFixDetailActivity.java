package com.honyum.elevatorMan.activity.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.FixTaskDetailListAdapter;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.FixTaskInfo;
import com.honyum.elevatorMan.data.mydata.NHFixAndTask;
import com.honyum.elevatorMan.net.FixTaskRequest;
import com.honyum.elevatorMan.net.FixTaskResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.view.MyListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Star on 2017/6/15.
 */

public class NHFixDetailActivity extends BaseActivityWraper implements ListItemCallback<FixTaskInfo> {
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_ordertime)
    TextView tvOrdertime;
    @BindView(R.id.tv_apptime)
    TextView tvApptime;
    @BindView(R.id.tv_breaktype)
    TextView tvBreaktype;
    @BindView(R.id.tv_breakdes)
    TextView tvBreakdes;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.iv_image1)
    ImageView ivImage1;
    @BindView(R.id.tv_vill_address)
    TextView tvVillAddress;
    @BindView(R.id.tv_eve_brand)
    TextView tvEveBrand;
    @BindView(R.id.eve_weight)
    TextView eveWeight;
    @BindView(R.id.eve_landing)
    TextView eveLanding;
    @BindView(R.id.tv_nexttime)
    TextView tvNexttime;
    @BindView(R.id.tv_paybill)
    TextView tvPaybill;
    @BindView(R.id.tv_look_eva)
    TextView tvLookEva;
    @BindView(R.id.fix_rec)
    MyListView fixRec;
    @BindView(R.id.rl_fix_title)
    RelativeLayout rlFixTitle;
    private NHFixAndTask mCompanyRepairInfo;
//    private List<FixTaskInfo> mFixTasks;
//    private FixTaskDetailListAdapter mFixTaskDetailListAdapter;

    @Override
    public String getTitleString() {
        return getString(R.string.fixtask);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //requestFixTaskListInfo();
    }

    @Override
    protected void initView() {
        mCompanyRepairInfo = getIntent("Info");
//        if(getIntent().getExtras().getInt("Id") == 1) {
//            tvOrderId.setText(getString(R.string.order_id) + mCompanyRepairInfo.getCode());
//            tvOrdertime.setText(getString(R.string.order_time) + mCompanyRepairInfo.getCreateTime());
//            tvApptime.setText(getString(R.string.app_time) + mCompanyRepairInfo.getRepairTime());
//            tvBreaktype.setText(getString(R.string.breaktype) + mCompanyRepairInfo.getPhenomenon());
//            etRemark.setFocusable(false);
//            etRemark.setText(mCompanyRepairInfo.getRepairTypeInfo().getContent());
//            new GetPicture(mCompanyRepairInfo.getPicture(), ivImage1).execute();
//            tvVillAddress.setText(getString(R.string.vill_address) + mCompanyRepairInfo.getVillaInfo().getAddress());
//            tvEveBrand.setText("电梯品牌：" + mCompanyRepairInfo.getVillaInfo().getBrand());
//            eveWeight.setText(getString(R.string.eve_weight) + mCompanyRepairInfo.getVillaInfo().getWeight() + "KG");
//            eveLanding.setText(getString(R.string.eve_landing) + mCompanyRepairInfo.getVillaInfo().getLayerAmount() + "层");
//        }
//        else
//        {
//            tvOrderId.setText(getString(R.string.order_id) + mCompanyRepairInfo.getRepairOrderInfo().getCode());
//            tvOrdertime.setText(getString(R.string.order_time) + mCompanyRepairInfo.getRepairOrderInfo().getCreateTime());
//            tvApptime.setText(getString(R.string.app_time) + mCompanyRepairInfo.getRepairOrderInfo().getRepairTime());
//            tvBreaktype.setText(getString(R.string.breaktype) + mCompanyRepairInfo.getRepairOrderInfo().getPhenomenon());
//            etRemark.setFocusable(false);
//            etRemark.setText(mCompanyRepairInfo.getRepairTypeInfo().getContent());
//            if(mCompanyRepairInfo.getPicture()!=null)
//            {
//                String s = mCompanyRepairInfo.getPicture().split(",")[0];
//                new GetPicture(s, ivImage1).execute();
//            }
//
//            tvVillAddress.setText(getString(R.string.vill_address) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getAddress());
//            tvEveBrand.setText("电梯品牌：" + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getBrand());
//            eveWeight.setText(getString(R.string.eve_weight) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getWeight() + "KG");
//            eveLanding.setText(getString(R.string.eve_landing) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getLayerAmount() + "层");
//        }
        Intent i = getIntent();
        int i1 = i.getIntExtra("Id",0);
        if(i1==1) {
            tvOrderId.setText(getString(R.string.order_id) + mCompanyRepairInfo.getCode());
            tvOrdertime.setText(getString(R.string.order_time) + mCompanyRepairInfo.getCreateTime());
            tvApptime.setText(getString(R.string.app_time) + mCompanyRepairInfo.getRepairTime());
            tvBreaktype.setText(getString(R.string.breaktype) + mCompanyRepairInfo.getPhenomenon());
            etRemark.setFocusable(false);
            etRemark.setText(mCompanyRepairInfo.getRepairTypeInfo().getContent());
            new GetPicture(mCompanyRepairInfo.getPicture(), ivImage1).execute();
            tvVillAddress.setText(getString(R.string.vill_address) + mCompanyRepairInfo.getVillaInfo().getAddress());
            tvEveBrand.setText("品牌：" + mCompanyRepairInfo.getVillaInfo().getBrand());
            eveWeight.setText(getString(R.string.eve_weight) + mCompanyRepairInfo.getVillaInfo().getWeight());
            eveLanding.setText(getString(R.string.eve_landing) + mCompanyRepairInfo.getVillaInfo().getLayerAmount());
        }
        else
        {
            tvOrderId.setText(getString(R.string.order_id) + mCompanyRepairInfo.getRepairOrderInfo().getCode());
            tvOrdertime.setText(getString(R.string.order_time) + mCompanyRepairInfo.getRepairOrderInfo().getCreateTime());
            tvApptime.setText(getString(R.string.app_time) + mCompanyRepairInfo.getRepairOrderInfo().getRepairTime());
            tvBreaktype.setText(getString(R.string.breaktype) + mCompanyRepairInfo.getRepairOrderInfo().getPhenomenon());
            etRemark.setFocusable(false);
            etRemark.setText(mCompanyRepairInfo.getRepairTypeInfo().getContent());
            if(mCompanyRepairInfo.getPicture()!=null)
            {
                String s = mCompanyRepairInfo.getPicture().split(",")[0];
                new GetPicture(s, ivImage1).execute();
            }
            tvVillAddress.setText(getString(R.string.vill_address) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getAddress());
            tvEveBrand.setText("品牌：" + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getBrand());
            eveWeight.setText(getString(R.string.eve_weight) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getWeight());
            eveLanding.setText(getString(R.string.eve_landing) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getLayerAmount());
        }
        tvNexttime.setVisibility(View.GONE);
        tvPaybill.setVisibility(View.GONE);
        tvLookEva.setVisibility(View.GONE);
        rlFixTitle.setVisibility(View.GONE);
        fixRec.setVisibility(View.GONE);

    }

//    private void fillList() {
//        mFixTaskDetailListAdapter = new FixTaskDetailListAdapter(mFixTasks, this);
//        fixRec.setAdapter(mFixTaskDetailListAdapter);
//    }
//
//    //服务方法区 需要分离出
//    private RequestBean getRequestBean(String userId, String token) {
//
//        FixTaskRequest request = new FixTaskRequest();
//        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
//        request.setBody(request.new FixTaskBody().setRepairOrderId(mCompanyRepairInfo.getId()));
//        return request;
//    }
//
//    private void requestFixTaskListInfo() {
//        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_FIX_TASK,
//                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
//            @Override
//            protected void onResponse(NetTask task, String result) {
//                FixTaskResponse response = FixTaskResponse.getFixTaskResponse(result);
//                mFixTasks = response.getBody();
//                //获取到了返回的信息
//                if (mFixTasks == null || mFixTasks.size() == 0) {
//                    //findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
//                    findViewById(R.id.fix_rec).setVisibility(View.GONE);
//                    return;
//                }
//                fillList();
//            }
//        };
//        addTask(task);
//    }

    //end 服务方法区 需要分离出
    @Override
    protected int getLayoutID() {
        return R.layout.avtivity_fix_task_detail;
    }

    @Override
    public void performItemCallback(FixTaskInfo data) {


    }


}
