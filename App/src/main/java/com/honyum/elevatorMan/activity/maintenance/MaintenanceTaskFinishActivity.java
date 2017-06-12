package com.honyum.elevatorMan.activity.maintenance;

import android.content.Intent;
import android.os.Bundle;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.MaintenanceServiceFinishRequest;
import com.honyum.elevatorMan.net.MaintenanceServiceResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;

/**
 * Created by Star on 2017/6/10.
 */

public class MaintenanceTaskFinishActivity extends BaseFragmentActivity {

    private String currId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maint_result);
        initTitle();
        initView();
    }
    /**
     * 初始化标题
     */
    private void initTitle() {

        initTitleBar("维保结果提交", R.id.title_order,
                R.drawable.back_normal, backClickListener);
    }

    private void requestMaintOrderProcessWorkerFinish() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_TASK_FINISH,
                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showAppToast(getString(R.string.sucess));
                    //finish();
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());
            }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());
        };
        addTask(task);
    }

    private RequestBean getRequestBean(String userId, String token) {

        MaintenanceServiceFinishRequest request = new MaintenanceServiceFinishRequest();
        request.setHead(new NewRequestHead().setaccessToken(token).setuserId(userId));
        //TODO 这里的上传图片没有做
        request.setBody(request.new MaintenanceServiceFinishBody().setMaintOrderProcessId(currId).setMaintUserFeedback("维修完成").setAfterImg("www.baidu.com").setBeforeImg("www.baidu.com"));
        return request;
    }

    /**
     * 初始化view
     */
    private void initView() {
        Intent it = getIntent();

        currId = it.getStringExtra("Id");
    }
}
