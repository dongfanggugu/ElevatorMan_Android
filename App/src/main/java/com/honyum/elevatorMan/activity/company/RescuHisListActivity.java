package com.honyum.elevatorMan.activity.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.worker.FixDetailActivity;
import com.honyum.elevatorMan.adapter.CompanyRescuListAdapter;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.AlarmInfo1;
import com.honyum.elevatorMan.net.GetAlarmListRequest;
import com.honyum.elevatorMan.net.GetAlarmListResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Star on 2017/6/15.
 */

public class RescuHisListActivity extends BaseActivityWraper implements ListItemCallback<AlarmInfo1> {

    @BindView(R.id.lv_rescu_his_list)
    ListView lvRescuHisList;
    List<AlarmInfo1> datas;
    private CompanyRescuListAdapter mCompanyRescuListAdapter;

    @Override
    public String getTitleString() {
        return getString(R.string.rescu_his);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestAlarmListInfo("3");
    }

    @Override
    protected void initView() {


    }
    private RequestBean getAlarmListRequesttBean(String userId, String token, String brandchId, String history) {

        GetAlarmListRequest request = new GetAlarmListRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new GetAlarmListBody().setBranchId(brandchId).setHistory(history));
        return request;
    }

    private void requestAlarmListInfo(String his) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_ALARM_LIST_BRANCH,
                getAlarmListRequesttBean(getConfig().getUserId(), getConfig().getToken(), getConfig().getBranchId(), his)) {
            @Override
            protected void onResponse(NetTask task, String result) {

                GetAlarmListResponse response = GetAlarmListResponse.getAlarmListResponse(result);
                System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!@!@!" + result);
                datas = response.getBody();
                //获取到了返回的信息
                if (datas == null || datas.size() == 0) {
                    findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
                    findViewById(R.id.lv_rescu_his_list).setVisibility(View.GONE);
                    return;
                }
                fillList();
            }
        };
        addTask(task);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_company_save_his_list;
    }

    private void fillList()
    {
        mCompanyRescuListAdapter = new CompanyRescuListAdapter(datas,this);
        lvRescuHisList.setAdapter(mCompanyRescuListAdapter);
    }

    @Override
    public void performItemCallback(AlarmInfo1 data) {
        //处理点击事件
        Intent intent = new Intent(this, RescuHisDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Info", data);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
