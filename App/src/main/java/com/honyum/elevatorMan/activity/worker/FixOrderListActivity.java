package com.honyum.elevatorMan.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.maintenance.MaintenancePlanAddActivity;
import com.honyum.elevatorMan.adapter.FixTaskListAdapter;
import com.honyum.elevatorMan.adapter.MSTaskListAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.data.MaintenanceTaskInfo;
import com.honyum.elevatorMan.net.FixRequest;
import com.honyum.elevatorMan.net.FixResponse;
import com.honyum.elevatorMan.net.MaintenanceServiceResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.util.List;

/**
 * Created by Star on 2017/6/12.
 */


public class FixOrderListActivity extends BaseFragmentActivity implements ListItemCallback<FixInfo> {
    private FixTaskListAdapter mFixTaskListAdapter;
    private ListView fix_list;
    private List<FixInfo> mFixInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_list);
        initTitle();
        initView();
    }

    private void initView() {

        fix_list = (ListView)findViewById(R.id.fix_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestFixListInfo();
    }


    private RequestBean getRequestBean(String userId, String token) {

        FixRequest request = new FixRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new FixRequestBody().setPage(NetConstant.PAGE).setRows(NetConstant.ROWS));
        return request;
    }

    private void requestFixListInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_FIX_LIST,
                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                FixResponse response = FixResponse.getFixResponse(result);
                mFixInfo = response.getBody();
                //获取到了返回的信息
                if (mFixInfo == null||mFixInfo.size()==0) {
                    findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
                    findViewById(R.id.fix_list).setVisibility(View.GONE);
                    return;
                }
                fillList();
              //  requestMaintenTaskList();


                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);
    }

    private void fillList() {
        mFixTaskListAdapter = new FixTaskListAdapter(mFixInfo, this);
        fix_list.setAdapter(mFixTaskListAdapter);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {

        initTitleBar("怡墅维修", R.id.title,
                R.drawable.back_normal, backClickListener);
    }


    @Override
    public void performItemCallback(FixInfo data) {
        Intent intent = new Intent(this, FixDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Info", data);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
