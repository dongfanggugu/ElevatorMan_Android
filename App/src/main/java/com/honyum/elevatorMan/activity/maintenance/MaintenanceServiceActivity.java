package com.honyum.elevatorMan.activity.maintenance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.MSTaskListAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.MaintenanceServiceInfo;
import com.honyum.elevatorMan.data.MaintenanceTaskInfo;
import com.honyum.elevatorMan.net.MaintenanceServiceResponse;
import com.honyum.elevatorMan.net.MaintenanceServiceTaskRequest;
import com.honyum.elevatorMan.net.MiantenanceServiceTaskResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;

import java.util.List;

import static com.honyum.elevatorMan.net.base.NetConstant.ADD_STATE;

public class MaintenanceServiceActivity extends BaseFragmentActivity implements View.OnClickListener,ListItemCallback<MaintenanceTaskInfo> {

    private TextView tv_cellname;
    private MaintenanceServiceInfo currInfo;
    private Boolean isFirst;
    private List<MaintenanceServiceInfo> msInfoList;
    private List<MaintenanceTaskInfo> msTInfoList;
    private TextView tv_main_type;
    private TextView left_time;
    private ListView lv_detail;
    private MSTaskListAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_service);
        initTitleBar();
        isFirst = true;
        initView();
    }

    private void initView() {
        //tv_cellname
        tv_cellname = (TextView) findViewById(R.id.tv_cellname);
        tv_main_type = (TextView) findViewById(R.id.tv_main_type);
        //tv_main_type  left_time  bt_selectPos
        left_time = (TextView) findViewById(R.id.left_time);
        lv_detail = (ListView) findViewById(R.id.lv_detail);
        findViewById(R.id.tv_plan).setOnClickListener(this);
        findViewById(R.id.bt_selectPos).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestMaintenServiceInfo();
    }

    private void requestMaintenServiceInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_SERVICE,
                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                MaintenanceServiceResponse response = MaintenanceServiceResponse.getMaintServiceInfoResponse(result);
                msInfoList = response.getBody();
                //获取到了返回的信息
                if (msInfoList == null) {
                    findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_content).setVisibility(View.GONE);
                    return;
                }
                fillView();
                requestMaintenTaskList();


                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);
    }

    private void requestMaintenTaskList() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_TASK,
                getFixRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                MiantenanceServiceTaskResponse response = MiantenanceServiceTaskResponse.getMaintTaskInfoResponse(result);
                Log.e("!!!!!!!!!!!!!!", "onResponse: "+ result);
                msTInfoList = response.getBody();
                //获取"taskCode": "20170603155058"   "maintUserFeedback": "完成", maintUserInfo.name
                //获取到了返回的信息
                if (msTInfoList == null) {
                    return;
                }
                fillTaskList();

                //requestMaintenFixList();




            }
        };
        addTask(task);
    }

    private void fillTaskList() {
        mAdapter = new MSTaskListAdapter(msTInfoList,this);
        lv_detail.setAdapter(mAdapter);
    }



    //TODO 这里的page 和row 暂时设置为1 - 100 调整
    private RequestBean getFixRequestBean(String userId, String token) {

        MaintenanceServiceTaskRequest request = new MaintenanceServiceTaskRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new MaintenanceTaskBody().setMaintOrderId(currInfo.getId()).setPage(NetConstant.PAGE).setRows(NetConstant.ROWS));
        return request;
    }

    private void fillView() {
        if (isFirst) {
            MaintenanceServiceInfo mi = msInfoList.get(0);
            currInfo = mi;
            isFirst = false;
            //更新UI
            tv_cellname.setText(mi.getVillaInfo().getCellName());
            tv_main_type.setText(mi.getMaintypeInfo().getName());
            left_time.setText(mi.getExpireTime());


        } else {
            for (MaintenanceServiceInfo mi : msInfoList) {

                if (!StringUtils.isEmpty(currInfo.getId()))
                    if (mi.getId().equals(currInfo.getId())) {
                        tv_cellname.setText(mi.getVillaInfo().getCellName());
                        tv_main_type.setText(mi.getMaintypeInfo().getName());
                        Log.e("!!!!!!!!!!!!!!", "onResponse: " + left_time.getText());
                        left_time.setText(mi.getExpireTime());
                        //更新UI，
                        break;

                    }

            }
        }
    }

    private RequestBean getRequestBean(String userId, String token) {

        RequestBean request = new RequestBean();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        //request.setBody(body);
        return request;
    }

    private void initTitleBar() {
        initTitleBar("维保服务", R.id.title_service,
                R.drawable.back_normal, backClickListener);
    }

    @Override
    public void onClick(View v) {
        // Dialog
        switch (v.getId()) {
            case R.id.bt_selectPos:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String[] items = new String[msInfoList.size()];
                for (int i = 0; i < msInfoList.size(); i++) {
                    items[i] = msInfoList.get(i).getVillaInfo().getCellName();
                }
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currInfo = msInfoList.get(which);
                        fillView();
                    }
                });
                builder.show();
                break;
            case R.id.tv_plan:
                junmpToAddServicePlan();
                break;
        }
    }

    //跳转到维保业务详情页面，根据状态判断显示的按钮。 这里自定义//0待确认 1已确认 2已出发 3已到达 4已完成 5已评价  添加  -1 为新添加计划
    private void junmpToAddServicePlan() {
        Intent intent = new Intent(this, MaintenancePlanAddActivity.class);
        intent.putExtra("State", ADD_STATE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Info", currInfo);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void performItemCallback(MaintenanceTaskInfo data) {
        Intent intent = new Intent(this, MaintenancePlanAddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Info", data);
        intent.putExtras(bundle);
        intent.putExtra("State", data.getState());
        startActivity(intent);
    }
}
