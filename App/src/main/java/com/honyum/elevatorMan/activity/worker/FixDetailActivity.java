package com.honyum.elevatorMan.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.FixTaskDetailListAdapter;
import com.honyum.elevatorMan.adapter.FixTaskListAdapter;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.data.FixTaskInfo;
import com.honyum.elevatorMan.net.FixRequest;
import com.honyum.elevatorMan.net.FixResponse;
import com.honyum.elevatorMan.net.FixTaskRequest;
import com.honyum.elevatorMan.net.FixTaskResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Star on 2017/6/12.
 */

//TODO  需要分离Service和Activity,可以将intent需要传递的函数放在全局里。节省INTENT代码
public class FixDetailActivity extends BaseActivityWraper implements ListItemCallback<FixTaskInfo> {

    //Knife 使用了apt的二次编译，而不是反射。不影响效率
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
    ListView fixRec;
    private FixInfo mFixInfo;
    private List<FixTaskInfo> mFixTasks ;
    private FixTaskDetailListAdapter mFixTaskDetailListAdapter;


    @Override
    public String getTitleString() {
        return getString(R.string.fix_task);
    }

    @Override
    protected void initView() {

        mFixInfo = getIntent("Info");
        tvOrderId.setText(getString(R.string.order_id)+mFixInfo.getId());
        tvOrdertime.setText(getString(R.string.order_time)+mFixInfo.getCreateTime());
        tvApptime.setText(getString(R.string.app_time)+mFixInfo.getRepairTime());
        tvBreaktype.setText(getString(R.string.breaktype)+mFixInfo.getRepairTypeInfo().getName());
        etRemark.setText(mFixInfo.getRepairTypeInfo().getContent());
        new GetPicture(mFixInfo.getPicture(),ivImage1).execute();
        tvVillAddress.setText(getString(R.string.vill_address)+mFixInfo.getVillaInfo().getAddress());
        tvEveBrand.setText(getString(R.string.brand)+mFixInfo.getVillaInfo().getBrand());
        eveWeight.setText(getString(R.string.eve_weight)+mFixInfo.getVillaInfo().getWeight());
        eveLanding.setText(getString(R.string.eve_landing)+mFixInfo.getVillaInfo().getLayerAmount());

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestFixTaskListInfo();
    }

    //服务方法区 需要分离出
    private RequestBean getRequestBean(String userId, String token) {

        FixTaskRequest request = new FixTaskRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new FixTaskBody().setRepairOrderId(mFixInfo.getId()));
        return request;
    }

    private void requestFixTaskListInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_FIX_TASK,
                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                FixTaskResponse response = FixTaskResponse.getFixTaskResponse(result);
                mFixTasks = response.getBody();
                //获取到了返回的信息
                if (mFixTasks == null||mFixTasks.size()==0) {
                    //findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
                    findViewById(R.id.fix_rec).setVisibility(View.GONE);
                    return;
                }
                fillList();
            }
        };
        addTask(task);
    }
    //end 服务方法区 需要分离出

    private void fillList() {
        mFixTaskDetailListAdapter = new FixTaskDetailListAdapter(mFixTasks, this);
        fixRec.setAdapter(mFixTaskDetailListAdapter);
    }
    @Override
    protected int getLayoutID() {
        return R.layout.avtivity_fix_task_detail;
    }


    @OnClick({R.id.tv_paybill, R.id.tv_look_eva,R.id.tv_nexttime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_paybill:
                tvPaybill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FixDetailActivity.this, FixPaymentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Info",mFixInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                });
                break;
            case R.id.tv_look_eva:
                tvLookEva.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FixDetailActivity.this, FixEvaLookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Info",mFixInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                });
                break;
            case R.id.tv_nexttime:
                Intent intent = new Intent(FixDetailActivity.this, FixNextTimeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Info",mFixInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void performItemCallback(FixTaskInfo data) {

        Intent intent = new Intent(FixDetailActivity.this, FixTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Info", data);
        bundle.putSerializable("Fixdata",mFixInfo);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
