package com.honyum.elevatorMan.activity.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.CompanyNHFixListAdapter;
import com.honyum.elevatorMan.adapter.CompanyNHMentenanceListAdapter;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.mydata.NHFixAndTask;
import com.honyum.elevatorMan.data.newdata.ComapnyMentenanceInfo;
import com.honyum.elevatorMan.data.newdata.CompanyRepairInfo;
import com.honyum.elevatorMan.data.newdata1.RepairInfo;
import com.honyum.elevatorMan.net.GetNHFixOrderCompanyResponse;
import com.honyum.elevatorMan.net.GetNHFixOrderResponse;
import com.honyum.elevatorMan.net.GetNHMentenanceOrderRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Star on 2017/6/15.
 */

public class NHFixOrderListActivity extends BaseActivityWraper implements ListItemCallback<NHFixAndTask> {

    @BindView(R.id.lv_fix_his_list)
    ListView lv_fix_his_list;
    List<NHFixAndTask> datas;
    private CompanyNHFixListAdapter mCompanyNHFixListAdapter;
    @Override
    public String getTitleString() {
        return getString(R.string.fix_order);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNHFixrderListInfo();
    }

    private RequestBean getNHFixOrderListRequestBean(String userId, String token) {

        GetNHMentenanceOrderRequest request = new GetNHMentenanceOrderRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new GetNHMentenanceOrderBody().setBranchId(getConfig().getBranchId()));
        return request;
    }

    private void requestNHFixrderListInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_COMPANY_FIX_LIST,
                getNHFixOrderListRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                GetNHFixOrderCompanyResponse response = GetNHFixOrderCompanyResponse.getNHFixOrderResponse(result);
                datas = response.getBody();
                if (datas == null || datas.size() == 0) {
                    findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
                    lv_fix_his_list.setVisibility(View.GONE);
                    return;
                }
                fillList();
            }
        };
        addTask(task);
    }
    private void fillList()
    {
        mCompanyNHFixListAdapter = new CompanyNHFixListAdapter(datas,this);
        lv_fix_his_list.setAdapter(mCompanyNHFixListAdapter);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_company_nhfix_list;
    }

    @Override
    public void performItemCallback(NHFixAndTask data) {
        //处理点击事件
        Intent intent = new Intent(this, NHFixDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Info", data);
        intent.putExtra("Id", 1);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
