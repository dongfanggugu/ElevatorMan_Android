package com.honyum.elevatorMan.activity.worker;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.RepairTaskInfo;
import com.honyum.elevatorMan.net.CommitRepairDescribeRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.Utils;

public class RepairInfoActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_info);

        initView();
    }

    private void initView() {

        boolean isUnderWay = getIntent().getBooleanExtra("underway", true);

        final RepairTaskInfo info = (RepairTaskInfo) getIntent().getSerializableExtra("info");

        final EditText etWorkerDescribe = (EditText) findViewById(R.id.et_worker_describe);

        if (isUnderWay) {
            initTitleBar(R.id.title, "保修单详情", R.drawable.back_normal, backClickListener,
                    R.drawable.icon_confirm, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            submitRepairResult(info.getId(), etWorkerDescribe.getText().toString());
                        }
                    });
        } else {
            initTitleBar("保修单详情", R.id.title, R.drawable.back_normal, backClickListener);

            TextView tvWorkerDescribe = (TextView) findViewById(R.id.tv_worker_describe);
            tvWorkerDescribe.setText(info.getFinishResult());
            etWorkerDescribe.setVisibility(View.GONE);
            tvWorkerDescribe.setVisibility(View.VISIBLE);

            findViewById(R.id.ll_owner_rating).setVisibility(View.VISIBLE);
            RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
            rb.setRating(Utils.getInt(info.getEvaluate()));

            TextView tvRating = (TextView) findViewById(R.id.tv_owner_rating);
            tvRating.setText(info.getEvaluateInfo());
        }

        TextView tvTime = (TextView) findViewById(R.id.tv_rp_time);
        tvTime.setText(info.getCreateTime());

        TextView tvName = (TextView) findViewById(R.id.tv_owner_name);
        if (TextUtils.isEmpty(info.getName())) {
            tvName.setText(info.getUserName());
        } else {
            tvName.setText(info.getName());
        }

        TextView tvTel = (TextView) findViewById(R.id.tv_owner_tel);
        tvTel.setText(info.getTel());

        TextView tvBrand = (TextView) findViewById(R.id.tv_et_brand);
        tvBrand.setText(info.getBrand());

        TextView tvAddress = (TextView) findViewById(R.id.tv_rp_address);
        tvAddress.setText(info.getAddress());

        TextView tvOwnerDescribe = (TextView) findViewById(R.id.tv_owner_describe);
        tvOwnerDescribe.setText(info.getPhenomenon());
    }

    private void submitRepairResult(String id, String describe) {

        if (TextUtils.isEmpty(describe)) {
            showToast("维修描述不可为空");
            return;
        }

        String server = getConfig().getServer() + NetConstant.COMMIT_REPAIR_DESCRIBE;

        CommitRepairDescribeRequest request = new CommitRepairDescribeRequest();
        RequestHead head = new RequestHead();
        CommitRepairDescribeRequest.CommitRepairDescribeReqBody body
                = request.new CommitRepairDescribeReqBody();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setRepairId(id);
        body.setFinishResult(describe);

        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("提交成功!!");
                onBackPressed();
            }
        };

        addTask(netTask);
    }
}
