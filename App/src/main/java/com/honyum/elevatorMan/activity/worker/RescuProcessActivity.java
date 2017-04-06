package com.honyum.elevatorMan.activity.worker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.ChatActivity;
import com.honyum.elevatorMan.activity.common.MainpageActivity;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.net.ReportExceptRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

public class RescuProcessActivity extends WorkerBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescu_process);

        Intent intent = getIntent();

        //获取此次报警事件的id
        String alarmId = intent.getStringExtra("alarm_id");
        initTitleBar();
        initView(alarmId);
    }

    /**
     * 初始化view
     */
    private void initView(final String alarmId) {
        Button btnComplete = (Button) findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(buttonClickListener);
        btnComplete.setTag(alarmId);

        Button btnUnComplete = (Button) findViewById(R.id.btn_uncomplete);
        btnUnComplete.setOnClickListener(buttonClickListener);
        btnUnComplete.setTag(alarmId);

    }

    /**
     * 按钮点击事件
     */
    private OnClickListener buttonClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub

            String alarmId = (String) view.getTag();
            switch (view.getId()) {
                case R.id.btn_complete:
                    alarmComplete(alarmId);
                    break;
                case R.id.btn_uncomplete:
                    popExceptWindow(alarmId);
            }
        }

    };

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar(getString(R.string.title_rescu_process),
                R.id.title_rescu_process, R.drawable.back_normal,
                backClickListener);

        initTitleBar(R.id.title_rescu_process, getString(R.string.title_rescu_process), R.drawable.icon_bbs,
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (null == getIntent()) {
                            return;
                        }

                        AlarmInfo alarmInfo = (AlarmInfo) getIntent().getSerializableExtra("alarm_info");

                        if (null == alarmInfo) {
                            return;
                        }

                        if (!alarmInfo.getState().equals(Constant.ALARM_STATE_ASSIGNED)
                                || !alarmInfo.getState().equals(Constant.ALARM_STATE_ARRIVED)) {
                            showToast("该报警已经完成,无法进入电梯交流群组");
                            return;
                        }

                        Intent intent = new Intent(RescuProcessActivity.this, ChatActivity.class);

                        String alarmId = intent.getStringExtra("alarm_id");
                        intent.putExtra("alarm_id", alarmId);

                        intent.putExtra("enter_mode", ChatActivity.MODE_WORKER);
                        startActivity(intent);
                    }
                });

//		String from = intent.getStringExtra("from");
//
//		if (StringUtils.isEmpty(from)) {
//			initTitleBar(getString(R.string.title_rescu_process),
//					R.id.title_rescu_process, R.drawable.navi_setting_normal,
//					menuClickListener);
//		} else if (from.equals(AlarmListActivity.TAG)) {
//			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//			setExitFlag(false);
//			initTitleBar(getString(R.string.title_rescu_process),
//					R.id.title_rescu_process, R.drawable.back_normal,
//					backClickListener);
//		}

    }

    /**
     * 救援事件完成
     */
    private void alarmComplete(final String alarmId) {

        Intent intent = new Intent(RescuProcessActivity.this, RescuSubmitActivity.class);
        intent.putExtra("alarm_id", alarmId);
        startActivity(intent);
    }


    /**
     * 意外情况的监听
     */
    private void popExceptWindow(String alarmId) {
        View view = View.inflate(this, R.layout.layout_except, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        initExceptView(view, dialog, alarmId);
        dialog.show();
    }

    /**
     * 初始化意外情况弹出框
     *
     * @param view
     * @param dialog
     */
    private void initExceptView(final View view, final AlertDialog dialog, final String alarmId) {

        OnClickListener exceptClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                switch (v.getId()) {
                    case R.id.btn_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.btn_confirm:
                        EditText etException = (EditText) view.findViewById(R.id.et_exception);
                        String remark = etException.getText().toString();
                        if (StringUtils.isEmpty(remark)) {
                            showToast("请填写无法到达的理由!");
                            return;
                        }
                        dialog.dismiss();
                        reportExcept(alarmId, remark);
                }
            }

        };

        // 添加按钮的监听
        view.findViewById(R.id.btn_cancel).setOnClickListener(
                exceptClickListener);
        view.findViewById(R.id.btn_confirm).setOnClickListener(
                exceptClickListener);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText("无法完成救援任务说明");
    }


    /**
     * 上报意外情况
     *
     * @param alarmId
     * @param remark
     */
    private void reportExcept(String alarmId, String remark) {
        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_WORKER_EXCEPT,
                getReportExceptRequest(alarmId, remark)) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub
                Toast.makeText(RescuProcessActivity.this, "无法完成救援已经提交,感谢您的参与", Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent(RescuProcessActivity.this, MainpageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }

        };
        addTask(netTask);
    }

    /**
     * 上报意外情况
     *
     * @param alarmId
     * @param remark
     * @return
     */
    private RequestBean getReportExceptRequest(String alarmId, String remark) {
        ReportExceptRequest request = new ReportExceptRequest();
        ReportExceptRequest.ReportExceptReqBody body = request.new ReportExceptReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setAlarmId(alarmId);
        body.setRemark(remark);

        request.setHead(head);
        request.setBody(body);

        return request;
    }


}