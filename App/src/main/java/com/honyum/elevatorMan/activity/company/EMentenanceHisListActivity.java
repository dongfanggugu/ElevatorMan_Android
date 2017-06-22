package com.honyum.elevatorMan.activity.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.CompanyMentenanceListAdapter;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.MaintRecInfo;
import com.honyum.elevatorMan.net.GetMaintListByEleRequest;
import com.honyum.elevatorMan.net.GetMiantListByEleResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Star on 2017/6/15.
 */

public class EMentenanceHisListActivity extends BaseActivityWraper implements ListItemCallback<MaintRecInfo> {

    @BindView(R.id.lv_mentenance_his_list)
    ListView lvRescuHisList;
    List<MaintRecInfo> datas;
    private String eleId;


    private CompanyMentenanceListAdapter mCompanyMentenanceListAdapter;

    @Override
    public String getTitleString() {
        return "历史订单";
    }

    @Override
    protected void initView() {

        eleId = getIntent("Id");

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestMaintListByEleInfo(eleId);
    }

    private RequestBean getMaintListByEleRequestBean(String userId, String token, String ele) {

        GetMaintListByEleRequest request = new GetMaintListByEleRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new GetMaintListByEleBody().setElevatorId(ele));
        return request;
    }

    private void requestMaintListByEleInfo(String ele) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_LIST_ELE,
                getMaintListByEleRequestBean(getConfig().getUserId(), getConfig().getToken(), ele)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                GetMiantListByEleResponse response = GetMiantListByEleResponse.getMiantListByEleResponse(result);
                datas = response.getBody();
                if (datas == null || datas.size() == 0) {
                    findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
                    findViewById(R.id.lv_mentenance_his_list).setVisibility(View.GONE);
                    return;
                }
                fillList();
            }
        };
        addTask(task);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_company_mentenance_his_list;
    }

    private void fillList() {
        mCompanyMentenanceListAdapter = new CompanyMentenanceListAdapter(datas,this);
        lvRescuHisList.setAdapter(mCompanyMentenanceListAdapter);
    }

    @Override
    public void performItemCallback(MaintRecInfo data) {
        //处理点击事件
        Intent intent = new Intent(this, EMantenanceDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Info", data);
        intent.putExtras(bundle);
        startActivity(intent);

    }

}

