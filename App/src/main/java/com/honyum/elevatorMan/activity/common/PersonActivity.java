package com.honyum.elevatorMan.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.constant.Constant;

public class PersonActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        initTitleBar();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar(getString(R.string.person_center), R.id.title_person,
                R.drawable.back_normal, backClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        ((TextView) findViewById(R.id.tv_user_name)).setText(getConfig().getUserName());
        ((TextView) findViewById(R.id.tv_age)).setText("" + getConfig().getAge());
        ((TextView) findViewById(R.id.tv_sex)).setText(getConfig().getSex() == 0 ? "女" : "男");
        ((TextView) findViewById(R.id.tv_company)).setText(getConfig().getBranchName());
        ((TextView) findViewById(R.id.tv_operation_code)).setText(getConfig().getOperationCard());

        if (!StringUtils.isEmpty(getConfig().getHAddress())) {
            ((TextView) findViewById(R.id.tv_home_place)).setText(getConfig().getHAddress());
        }

        if (!StringUtils.isEmpty(getConfig().getWAddress())) {
            ((TextView) findViewById(R.id.tv_work_place)).setText(getConfig().getWAddress());
        }
        loadIcon((ImageView) findViewById(R.id.img_user_icon));


        //个人基本信息
        findViewById(R.id.ll_basic_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonActivity.this, PersonBasicInfoActivity.class));
            }
        });

        //物业不显示操作证号
        if (getConfig().getRole().equals(Constant.PROPERTY)) {
            findViewById(R.id.ll_operation).setVisibility(View.GONE);
            findViewById(R.id.ll_address_1).setVisibility(View.GONE);
            findViewById(R.id.ll_permanent_address).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.ll_permanent_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, PermanentAddressActivity.class);
                startActivity(intent);
            }
        });

        //操作证号
        findViewById(R.id.ll_operation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, PersonModifyActivity.class);
                intent.putExtra("category", "operation");
                startActivity(intent);
            }
        });

        //家庭住址
        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, AddressActivity.class);
                intent.putExtra("category", "home");
                startActivity(intent);
            }
        });

        //工作地址
        findViewById(R.id.ll_work_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, AddressActivity.class);
                intent.putExtra("category", "work");
                startActivity(intent);
            }
        });

        //设置
        findViewById(R.id.ll_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        //退出登陆
        findViewById(R.id.tv_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }


    /**
     * 退出登录
     */
//    public void logout() {
//
//        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_LOG_OUT,
//                getLogoutRequest()) {
//            @Override
//            protected void onResponse(NetTask task, String result) {
//                clearUserInfo();
//
//                //退出登录时，取消之前设置的闹钟
//                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                for (PendingIntent pendingIntent : RemindUtils.mRemindList) {
//                    manager.cancel(pendingIntent);
//                }
//                //关闭定位服务
//                Intent sIntent = new Intent(PersonActivity.this, LocationService.class);
//                stopService(sIntent);
//
//                //启动
//                Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                SysActivityManager.getInstance().exit();
//            }
//
//            @Override
//            protected void onFailed(NetTask task, String errorCode, String errorMsg) {
//                //super.onFailed(task, errorCode, errorMsg);
//                clearUserInfo();
//                Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                SysActivityManager.getInstance().exit();
//            }
//        };
//        addTask(netTask);
//
//    }
//
//    /**
//     * 退出登录请求bean
//     * @return
//     */
//    private RequestBean getLogoutRequest() {
//        VersionCheckRequest request = new VersionCheckRequest();
//        RequestHead head = new RequestHead();
//
//        head.setAccessToken(getConfig().getToken());
//        head.setUserId(getConfig().getUserId());
//
//        request.setHead(head);
//
//        return request;
//    }

}
