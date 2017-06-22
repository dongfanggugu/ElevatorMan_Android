package com.honyum.elevatorMan.activity.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.CompanyNHMentenanceListAdapter;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.mydata.NHorderAndTask;
import com.honyum.elevatorMan.data.newdata.ComapnyMentenanceInfo;
import com.honyum.elevatorMan.net.GetMaintListByEleRequest;
import com.honyum.elevatorMan.net.GetMiantListByEleResponse;
import com.honyum.elevatorMan.net.GetNHMentenanceCompanyOrderResponse;
import com.honyum.elevatorMan.net.GetNHMentenanceOrderRequest;
import com.honyum.elevatorMan.net.GetNHMentenanceOrderResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Star on 2017/6/15.
 */

public class NHMentenanceOrderListActivity extends BaseActivityWraper implements ListItemCallback<NHorderAndTask>{
    @BindView(R.id.lv_nhmentenance_his_list)
    ListView lv_nhmentenance_his_list;
    List<NHorderAndTask> datas;
    private CompanyNHMentenanceListAdapter mCompanyNHMentenanceListAdapter;

    @Override
    public String getTitleString() {
        return getString(R.string.hismen);
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNHMentenanceOrderListInfo();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_company_nhmentenance_his_list;
    }

    private void fillList()
    {
        mCompanyNHMentenanceListAdapter = new CompanyNHMentenanceListAdapter(datas,this);
        lv_nhmentenance_his_list.setAdapter(mCompanyNHMentenanceListAdapter);
    }

    @Override
    public void performItemCallback(NHorderAndTask data) {
        //处理点击事件
        Intent intent = new Intent(this, NHMentenanceOrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Info", data);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private RequestBean getNHMentenanceOrderListRequestBean(String userId, String token) {

        GetNHMentenanceOrderRequest request = new GetNHMentenanceOrderRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new GetNHMentenanceOrderBody().setBranchId(getConfig().getBranchId()));
        return request;
    }

    private void requestNHMentenanceOrderListInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_COMPANY_NHMENTENANCE_LIST,
                getNHMentenanceOrderListRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                GetNHMentenanceCompanyOrderResponse response = GetNHMentenanceCompanyOrderResponse.getNHMentenanceOrderResponse(result);
                datas = response.getBody();
                if (datas == null || datas.size() == 0) {
                    findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
                    lv_nhmentenance_his_list.setVisibility(View.GONE);
                    return;
                }
                fillList();
            }
        };
        addTask(task);
    }
}
