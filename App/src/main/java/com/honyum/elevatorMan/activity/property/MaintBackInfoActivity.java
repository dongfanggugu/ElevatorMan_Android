package com.honyum.elevatorMan.activity.property;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.MaintBackRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MaintBackInfoActivity extends BaseFragmentActivity {

    private EditText etRemark;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maint_back_info);
        initTitleBar();
        initView();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar("不合格理由", R.id.title_detail, R.drawable.back_normal,
                backClickListener);
    }
    private void initView() {

        etRemark = (EditText) findViewById(R.id.et_remark);

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maintBack();
            }
        });
    }

    private void maintBack() {

        String remark = etRemark.getText().toString();

        if (StringUtils.isEmpty(remark)) {
            showToast("请填写维保不合格理由");
            return;
        }
        String mainId = getIntent().getStringExtra("main_id");

        MaintBackRequest request = new MaintBackRequest();
        MaintBackRequest.MaintBackReqBody body = new MaintBackRequest.MaintBackReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setMainId(mainId);
        body.setBackReason(remark);

        request.setHead(head);
        request.setBody(body);

        String server = getConfig().getServer() + NetConstant.URL_MAINT_FAILED;
        NetTask task = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("提交维保不合格成功");
                setResult(102);
                finish();
            }
        };
        addTask(task);
    }
}
