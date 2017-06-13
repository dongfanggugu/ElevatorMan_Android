package com.honyum.elevatorMan.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.maintenance.MaintenancePlanAddActivity;
import com.honyum.elevatorMan.activity.maintenance.MaintenanceTaskFinishActivity;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.data.FixTaskInfo;
import com.honyum.elevatorMan.net.FixTaskRequest;
import com.honyum.elevatorMan.net.FixTaskResponse;
import com.honyum.elevatorMan.net.FixWorkArriveRequest;
import com.honyum.elevatorMan.net.FixWorkFinishRequest;
import com.honyum.elevatorMan.net.FixWorkStartRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;

import static com.honyum.elevatorMan.net.base.NetConstant.FIX_ARRIVED;
import static com.honyum.elevatorMan.net.base.NetConstant.FIX_BEFORE_START;
import static com.honyum.elevatorMan.net.base.NetConstant.FIX_FIX_FINISH;
import static com.honyum.elevatorMan.net.base.NetConstant.FIX_LOOK;
import static com.honyum.elevatorMan.net.base.NetConstant.FIX_LOOK_FINISHED;
import static com.honyum.elevatorMan.net.base.NetConstant.FIX_STARTED;

/**
 * Created by Star on 2017/6/12.
 */

public class FixTaskActivity extends BaseActivityWraper {
    private TextView tv_look_eva;
    private FixTaskInfo mFixTaskInfo;
    private FixInfo mFixInfo;
    private TextView tv_look_result;
    private TextView tv_index;
    @Override
    public String getTitleString() {
        return getString(R.string.curr_task);
    }

    @Override
    protected void initView() {
        mFixTaskInfo = getIntent("Info");
        mFixInfo = getIntent("Fixdata");
        tv_index = findView(R.id.tv_index);
        tv_look_result = findView(R.id.tv_look_result);

        tv_look_eva = findView(R.id.tv_look_eva);
        changeBottomButton(mFixTaskInfo.getState());

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_fix_task;
    }

    //当前状态，待确定，点击开始
    private RequestBean getRequestBean(String userId, String token) {

        FixWorkStartRequest request = new FixWorkStartRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new FixWorkStartBody().setRepairOrderProcessId(mFixTaskInfo.getId()));
        return request;
    }

    private void requestFixWorkStartInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_FIX_START,
                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showAppToast(getString(R.string.sucess));
                    changeBottomButton(FIX_STARTED);
                }
            }
        };
        addTask(task);
    }

    //当前状态，已出发，点击到达
    private RequestBean getArriveRequestBean(String userId, String token) {

        FixWorkArriveRequest request = new FixWorkArriveRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new FixWorkArriveBody().setRepairOrderProcessId(mFixTaskInfo.getId()));
        return request;
    }

    private void requestFixWorkArriveInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_FIX_ARRIVE,
                getArriveRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showSimpleToast(getString(R.string.sucess));
                    changeBottomButton(FIX_ARRIVED);
                }
            }
        };
        addTask(task);
    }




    //切换维修状态
    public void changeBottomButton(String state)
    {

        switch (state) {
            case FIX_BEFORE_START:

                tv_index.setText(R.string.before_start);
                tv_index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestFixWorkStartInfo();
                    }
                });
                break;
            case FIX_STARTED:
                tv_index.setText(R.string.arrived);
                tv_index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestFixWorkArriveInfo();
                    }
                });
                break;
            case FIX_ARRIVED:
                tv_index.setText(R.string.fix_look_finish);
                tv_index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FixTaskActivity.this, FixLookFinishActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Info",mFixTaskInfo);
                        bundle.putSerializable("Fixdata",mFixInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case FIX_LOOK_FINISHED:
                tv_index.setText(R.string.fix_finish);
                tv_index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FixTaskActivity.this, FixLookFinishActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Info",mFixTaskInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case FIX_FIX_FINISH:
                tv_index.setVisibility(View.GONE);
                tv_look_result.setVisibility(View.VISIBLE);
                tv_look_result.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FixTaskActivity.this, FixResultLookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Info",mFixTaskInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                tv_look_eva.setVisibility(View.VISIBLE);
                tv_look_eva.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FixTaskActivity.this, FixEvaLookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Info",mFixInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                });
                break;
        }
    }
}
