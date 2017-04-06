package com.honyum.elevatorMan.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.LoginActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.ResetPwdRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.Utils;

public class ResetPasswordActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initTitleBar();
        initView();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar(getString(R.string.forget_password), R.id.title_reset_pwd, R.drawable.back_normal,
                backClickListener);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        final EditText etUsername = (EditText) findViewById(R.id.et_user);
        final EditText etPhone = (EditText) findViewById(R.id.et_phone);

        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUsername.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                if (StringUtils.isEmpty(userName)) {
                    showToast("用户名不能为空!");
                    return;
                }

                if (StringUtils.isEmpty(phone)) {
                    showToast("手机号码不能为空!");
                    return;
                }

                if (!Utils.isMobileNumber(phone)) {
                    showToast("请输入合法的手机号码!");
                    return;
                }

                resetPassword(userName, phone);
            }
        });
    }

    /**
     * 获取重置密码的请求bean
     * @param userName
     * @param phone
     * @return
     */
    private RequestBean getResetPwdRequestBean(String userName, String phone) {
        ResetPwdRequest request = new ResetPwdRequest();
        ResetPwdRequest.ResetPwdReqBody body = request.new ResetPwdReqBody();
        RequestHead head = new RequestHead();

        body.setUserName(userName);
        body.setTel(phone);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 请求重置密码
     * @param userName
     * @param phone
     */
    private void resetPassword(String userName, String phone) {
        String server = getConfig().getServer() + NetConstant.URL_RESET_PWD;
        RequestBean requestBean = getResetPwdRequestBean(userName, phone);

        NetTask netTask = new NetTask(server, requestBean) {

            @Override
            protected void onFailed(NetTask task, String errorCode, String errorMsg) {
                //super.onFailed(task, errorCode, errorMsg);
                showToast(errorMsg);
            }

            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("重置后的密码已经发送到您的手机，请注意短信查收!");
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        addTask(netTask);
    }
}
