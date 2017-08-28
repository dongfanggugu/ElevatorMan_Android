package com.honyum.elevatorMan.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.worker.FixDetailActivity;
import com.honyum.elevatorMan.adapter.EBuyOrderListAdapter;
import com.honyum.elevatorMan.adapter.FixTaskListAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.EasyLadderOrderInfo;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.net.EasyLadderOrderResponse;
import com.honyum.elevatorMan.net.EmptyRequest;
import com.honyum.elevatorMan.net.FixRequest;
import com.honyum.elevatorMan.net.FixResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;

import java.util.List;

/**
 * Created by Star on 2017/6/12.
 */


public class EBuyOrderListActivity extends BaseFragmentActivity implements ListItemCallback<EasyLadderOrderInfo> {
    private EBuyOrderListAdapter mEBuyOrderListAdapter;
    private ListView mLv_ebuycontent;
    private List<EasyLadderOrderInfo> mInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebuyorder);
        initTitle();
        initView();
    }

    private void initView() {

        mLv_ebuycontent = (ListView)findViewById(R.id.lv_ebuycontent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestEBuyOrderListInfo();
    }


    private RequestBean getRequestBean(String userId, String token) {

        EmptyRequest request = new EmptyRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        //request.setBody(request.new FixRequestBody().setPage(NetConstant.PAGE).setRows(NetConstant.ROWS));
        return request;
    }

    private void requestEBuyOrderListInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.EASY_LADDER_ORDERBYUSER,
                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                EasyLadderOrderResponse response = EasyLadderOrderResponse.getEasyLadderOrderResponse(result);
                mInfo = response.getBody();
                //获取到了返回的信息
                if (mInfo == null||mInfo.size()==0) {
                    //findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
                    findViewById(R.id.lv_ebuycontent).setVisibility(View.GONE);
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
        mEBuyOrderListAdapter = new EBuyOrderListAdapter(mInfo, this);
        mLv_ebuycontent.setAdapter(mEBuyOrderListAdapter);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {

        initTitleBar("E梯配订单", R.id.title,
                R.drawable.back_normal, backClickListener);
    }


    @Override
    public void performItemCallback(EasyLadderOrderInfo data) {
//        Intent intent = new Intent(this, FixDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("Info", data);
//        intent.putExtras(bundle);
//        startActivity(intent);

    }
}
