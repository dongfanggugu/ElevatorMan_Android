package com.honyum.elevatorMan.base;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.LoginActivity;
import com.honyum.elevatorMan.activity.PicturePickActivity;
import com.honyum.elevatorMan.activity.RegisterStepOneActivity;
import com.honyum.elevatorMan.activity.RegisterStepTwoActivity;
import com.honyum.elevatorMan.activity.WelcomeActivity;
import com.honyum.elevatorMan.activity.common.MainPage1Activity;
import com.honyum.elevatorMan.activity.common.MainpageActivity;
import com.honyum.elevatorMan.activity.common.NavigationActivity;
import com.honyum.elevatorMan.activity.common.ResetPasswordActivity;
import com.honyum.elevatorMan.activity.maintenance_1.MaintenanceActivity;
import com.honyum.elevatorMan.activity.property.AlarmTraceActivity;
import com.honyum.elevatorMan.activity.property.PropertyMainPageActivity;
import com.honyum.elevatorMan.activity.worker.WorkerActivity;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.listener.MyPhoneStateListener;
import com.honyum.elevatorMan.listener.OnCallStateListener;
import com.honyum.elevatorMan.net.AlarmListRequest;
import com.honyum.elevatorMan.net.LiftVideoRequest;
import com.honyum.elevatorMan.net.LiftVideoResponse;
import com.honyum.elevatorMan.net.LoginResponse;
import com.honyum.elevatorMan.net.VersionCheckRequest;
import com.honyum.elevatorMan.net.VersionCheckResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NetWorkManager;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.receiver.ForeMsgReceiver;
import com.honyum.elevatorMan.service.LocationService;
import com.honyum.elevatorMan.utils.DownloadFilesTask;
import com.honyum.elevatorMan.utils.RemindUtils;
import com.honyum.elevatorMan.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class BaseFragmentActivity extends SlidingFragmentActivity
        implements ForeMsgReceiver.IReceiveForeMsg {

    private SlidingMenu slidingMenu;

    private static boolean isForeground = false;

    private ForeMsgReceiver mForeMsgReceiver;


    //表示上次点击的时间，用来解决快速点击多次的问题
    private long mLastClickTime = 0;


    //用来维护消息弹出框，每个单独的弹出框维护单独的报警事件
    private static Map<String, AlertDialog> mAlertDialogMap = new HashMap<String, AlertDialog>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SysActivityManager.getInstance().addActivity(this);
        getMyApplication().setBaseActivity(this);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        isForeground = true;
        registerForeMsg();

        //当是欢迎和登陆页面和注册页面时不需要进行判断
        if (this instanceof LoginActivity
                || this instanceof WelcomeActivity
                || this instanceof RegisterStepOneActivity
                || this instanceof RegisterStepTwoActivity
                || this instanceof PicturePickActivity
                || this instanceof ResetPasswordActivity
                || this instanceof NavigationActivity
                || this instanceof MaintenanceActivity) {
            return;
        }

        String userId = getConfig().getUserId();
        String token = getConfig().getToken();
        String role = getConfig().getRole();
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)
                || StringUtils.isEmpty(role)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        }
    }


    /**
     * 处理网路请求的错误信息
     */
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            Log.i("handler", "msg:" + msg.arg1);

            if (0 == msg.arg1) { // 请求成功，不做操作
                handlerCallback();
            } else if (-9 == msg.arg1) {
                // 保存错误编码
                getConfig().setErrorCode("" + msg.arg1);

                //清除登陆信息
                clearUserInfo();

                // 跳转到登陆页面
                SysActivityManager.getInstance().exit();
                Intent intent = new Intent(BaseFragmentActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            } else {
                showToast((String) msg.obj);

                //如果是欢迎页面，当网络请求失败后，需要进入登陆页面
                if (BaseFragmentActivity.this instanceof WelcomeActivity) {
                    SysActivityManager.getInstance().exit();
                    Intent intent = new Intent(BaseFragmentActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                }
            }

        }
    };

    /**
     * mHandler接收消息后的回调
     */
    protected void handlerCallback() {
        Log.i("zhenhao", "handler callback");
    }

    /**
     * 获取handler
     *
     * @return
     */
    public Handler getHandler() {
        return mHandler;
    }

    /**
     * 初始化滑动菜单
     *
     * @param resId
     */
    public void initSlidingMenu(int resId) {
        setBehindContentView(resId);
        slidingMenu = getSlidingMenu();
        slidingMenu.setBehindOffset(getWidth() / 3);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    }

    /**
     * 获取屏幕宽度像素
     *
     * @return
     */
    public int getWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 设置标题
     *
     * @param titleId             控件id
     * @param title               标题内容
     * @param leftImg             左按钮图标resId
     * @param onClickLeftListener 左按钮点击事件
     */
    public void initTitleBar(String title, int titleId, int leftImg,
                             OnClickListener onClickLeftListener) {
        initTitleBar(titleId, title, leftImg, onClickLeftListener, 0, null);
    }

    /**
     * 设置标题
     *
     * @param titleId              控件id
     * @param title                标题内容
     * @param rightImg             右按钮图标resId
     * @param onClickRightListener 右按钮点击事件
     */
    public void initTitleBar(int titleId, String title, int rightImg,
                             OnClickListener onClickRightListener) {
        initTitleBar(titleId, title, 0, null, rightImg, onClickRightListener);
    }

    /**
     * 设置标题
     *
     * @param titleId 控件id
     * @param title   标题内容
     */
    public void initTitleBar(int titleId, String title) {
        initTitleBar(titleId, title, 0, null, 0, null);
    }

    public void setTitle(int titleId, String title) {
        View titleView = findViewById(titleId);

        ((TextView) titleView.findViewById(R.id.tv_title)).setText(title);
    }

    /**
     * 设置标题
     *
     * @param titleId              控件id
     * @param title                标题内容
     * @param leftImg              左按钮图标resId
     * @param onClickLeftListener  左按钮点击事件
     * @param rightImg             右按钮图标resId
     * @param onClickRightListener 右按钮点击事件
     */
    public void initTitleBar(int titleId, String title,
                             int leftImg, OnClickListener onClickLeftListener,
                             int rightImg, OnClickListener onClickRightListener) {
        View titleView = findViewById(titleId);

        ((TextView) titleView.findViewById(R.id.tv_title)).setText(title);

        if (leftImg != 0) {
            ImageView leftButton = (ImageView) titleView
                    .findViewById(R.id.btn_back);
            leftButton.setImageResource(leftImg);
            leftButton.setVisibility(View.VISIBLE);
            leftButton.setOnClickListener(onClickLeftListener);
        }

        if (rightImg != 0) {
            ImageView rightButton = (ImageView) titleView
                    .findViewById(R.id.btn_query);
            rightButton.setImageResource(rightImg);
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setOnClickListener(onClickRightListener);
        }
    }

    /**
     * 添加不带进度条的网络请求任务
     *
     * @param task
     */
    public void addBackGroundTask(NetTask task) {
        addTask(task, false, null, null);
    }

    /**
     * 添加带有进度条的网络请求任务
     *
     * @param task
     */
    public void addTask(NetTask task) {
        addTask(task, true, null, null);
    }

    public void addTask(NetTask task, String title) {
        addTask(task, true, title, null);
    }

    public void addTask(NetTask task, String title, String info) {
        addTask(task, true, title, info);
    }

    /**
     * 发送任务,选择是否显示进度条
     */
    public void addTask(NetTask task, boolean showProgress, String title,
                        String info) {
        NetWorkManager netManager = NetWorkManager.getInstance();
        task.setBaseActivity(this);
        if (showProgress) {
            task.setSyncRequest();
        }
        if (!netManager.addTast(task, title, info)) {
            Toast.makeText(this, "网络连接错误，请检查网络设置", Toast.LENGTH_LONG).show();

            if (this instanceof WelcomeActivity) {


                clearUserInfo();
                clearAlarmInfo();
                SysActivityManager.getInstance().exit();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }


    /**
     * 获取全局aplication对象
     *
     * @return
     */
    protected MyApplication getMyApplication() {
        return (MyApplication) getApplication();
    }


    /**
     * 获取Config对象
     *
     * @return
     */
    public Config getConfig() {
        return getMyApplication().getConfig();
    }

    /**
     * 通过状态码获取描述
     * 救援状态 0:指派中  1：已出发 2:已到达 3.已完成 4.拒绝
     *
     * @param state
     * @return
     */
    public String getStringByState(String state) {

        if (state.equals("-1")) {
            return "新的报警";
        }

        if (state.equals("0")) {
            return "新的报警";
        }

        if (state.equals("1")) {
            return "已出发";
        }

        if (state.equals("2")) {
            return "已到达";
        }

        if (state.equals("3")) {
            return "已完成";
        }

        if (state.equals("4")) {
            return "已拒绝";
        }
        return "位置状态";
    }

    /**
     * 显示toast提示
     *
     * @param msg
     */
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    /**
     * 显示toast提示
     *
     * @param msg
     */
    public void showAppToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击标题栏后退按钮事件
     */
    public OnClickListener backClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            onBackPressed();
            overridePendingTransition(R.anim.in_still, R.anim.out_from_left);
        }

    };

    /**
     * 根据状态设置状态字体的颜色，报警列表总使用
     *
     * @param state
     * @param textView
     */
    public void setStateColor(String state, TextView textView) {
        if (StringUtils.isEmpty(state)) {
            return;
        }

        if (state.equals(Constant.WORKER_STATE_START)) {
            textView.setTextColor(getResources().getColor(R.color.color_alarm_assigning));
        } else if (state.equals(Constant.WORKER_STATE_ARRIVED)) {
            textView.setTextColor(getResources().getColor(R.color.color_alarm_arrived));
        } else if (state.equals(Constant.WORKER_STATE_COMPLETE)) {
            textView.setTextColor(getResources().getColor(R.color.color_alarm_assigning));
        }
    }

    /**
     * 报警列表中s随机设置设置序列号的背景色
     *
     * @param textView
     */
    public void setIndexColor(TextView textView) {
        //int random = ((int) (Math.random() * 1000)) % 5;
        int random = ((Integer) textView.getTag()) % 5;
        switch (random) {
            case 0:
                textView.setBackgroundResource(R.drawable.shape_radius_1);
                break;
            case 1:
                textView.setBackgroundResource(R.drawable.shape_radius_2);
                break;
            case 2:
                textView.setBackgroundResource(R.drawable.shape_radius_3);
                break;
            case 3:
                textView.setBackgroundResource(R.drawable.shape_radius_4);
                break;
            case 4:
                textView.setBackgroundResource(R.drawable.shape_radius_5);
                break;
        }
    }

    /**
     * 获取类名
     *
     * @return
     */
    protected String getMyName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 清除登陆信息
     */
    public void clearUserInfo() {
        // 清除token,role和user信息
        getConfig().setToken("");
        getConfig().setUserId("");
        getConfig().setRole("");
        getConfig().setName("");
        getConfig().setSign("");
        JPushInterface.setAlias(getApplicationContext(), "", null);
    }

    /**
     * 设置报警信息的状态
     *
     * @param alarmId
     * @param state
     */
    public void setAlarmInfo(String alarmId, String state) {
        getConfig().setAlarmId(alarmId);
        getConfig().setAlarmState(state);
    }

    /**
     * 清除报警信息状态
     */
    public void clearAlarmInfo() {
        setAlarmInfo("", "");
    }

    /**
     * 保存登陆信息
     *
     * @param body
     */
    protected void setUserInfo(String token, LoginResponse.LoginRspBody body) {

        //将用户id和角色以及token保存在本地
        getConfig().setUserId(body.getUserId());
        getConfig().setRole(body.getType());
        getConfig().setToken(token);
        getConfig().setName(body.getName());

        getConfig().setAge(body.getAge());
        getConfig().setOperationCard(body.getOperationCard());
        getConfig().setSex(body.getSex());
        getConfig().setTel(body.getTel());
        getConfig().setBranchName(body.getBranchName());
        getConfig().setBranchId(body.getBranchId());
        getConfig().setIconUrl(body.getPic());

        getConfig().setHAddress(body.getUserAttach().getFamilyAddress());
        getConfig().setHProvince(body.getUserAttach().getFamilyProvince());
        getConfig().setHDistrict(body.getUserAttach().getFamilyCounty());
        getConfig().setHCity(body.getUserAttach().getFamilyCity());

        getConfig().setWAddress(body.getUserAttach().getResidentAddress());
        getConfig().setWDistrict(body.getUserAttach().getResidentCounty());
        getConfig().setWCity(body.getUserAttach().getResidentCity());
        getConfig().setWProvince(body.getUserAttach().getResidentProvince());

        getConfig().setSign(body.getAutograph());

        //设置设备的推送别名
        String alias = token.replaceAll("-", "_");
        new JPushAliasThread(alias).start();
        //JPushInterface.setAlias(getApplicationContext(), alias, null);
        //mJpushAliasThread.start();
    }

    protected void startLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }

    @Override
    protected void onPause() {

        //如果是前台灭屏，则
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn) {
            isForeground = false;

            if (mForeMsgReceiver != null) {
                unregisterReceiver(mForeMsgReceiver);
                mForeMsgReceiver = null;
            }
        }


        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 更新apk
     * 描述
     *
     * @param url
     * @param status
     */
    public void updateApk(final String url, int status, String description,
                          final IUpdateCancelCallback callback) {
        final ProgressDialog pBar = new ProgressDialog(this);
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setTitle("版本更新");
        pBar.setMessage("正在下载中，请稍后...");
        pBar.setCancelable(false);
        pBar.setMax(100);
        pBar.setProgress(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Dialog dialog = null;
        builder.setTitle("有新版本更新");
        builder.setCancelable(false);
        switch (status) {
            case 1:
                builder.setMessage(description)
                        .setPositiveButton("更新",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        pBar.show();
                                        new DownloadFilesTask(BaseFragmentActivity.this, pBar)
                                                .execute(url);
                                    }
                                }).setNegativeButton("退出",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });
                dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                //dialog.setCancelable(false);
                dialog.show();
                break;
            case 0:// 非强制更新
                builder.setMessage(description)
                        .setPositiveButton("更新",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        pBar.show();
                                        new DownloadFilesTask(BaseFragmentActivity.this, pBar)
                                                .execute(url);
                                    }
                                }).setNegativeButton("下次再说",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                if (callback != null) {
                                    callback.onCancel();
                                }
                            }
                        });
                dialog = builder.create();
                dialog.show();
                break;
        }
    }


    /**
     * 启动维修工页面
     */
    public void startWorker(String alarmId) {

        //启动定位
        Intent intent = new Intent(this, MainPage1Activity.class);
        startActivity(intent);
    }

    /**
     * 启动物业界面
     */
    public void startProperty(Boolean isSyncTask) {

        //防止两个账号登陆同一个设备造成定时定位功能不关闭的情况
        Intent intent = new Intent(this, LocationService.class);
        stopService(intent);

        startActivity(new Intent(BaseFragmentActivity.this, PropertyMainPageActivity.class));

//        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_ALARM_LIST_ONE,
//                getAlarmListRequest()) {
//
//            @Override
//            protected void onResponse(NetTask task, String result) {
//                // TODO Auto-generated method stub
//                AlarmInfoResponse response = AlarmInfoResponse.getAlarmInfoRsp(result);
//
//                if (null == response.getBody().getId()) {
//
//                    //清空之前保存的报警信息
//                    clearAlarmInfo();
//
//                    Intent intent = new Intent(BaseFragmentActivity.this, PropertyActivity.class);
//                    startActivity(intent);
//                } else {
//
//                    String userState = response.getBody().getUserState();
//
//                    if (userState.equals(Constant.ALARM_STATE_CONFIRM)) {    //已完成的报警任务不再展示
//                        clearAlarmInfo();
//                        Intent intent = new Intent(BaseFragmentActivity.this, PropertyActivity.class);
//                        startActivity(intent);
//                        return;
//                    }
//
//                    Intent intent = new Intent(BaseFragmentActivity.this, AlarmTraceActivity.class);
//                    String alarmId = response.getBody().getId();
//                    String state = response.getBody().getState();
//
//                    //保存当前报警信息
//                    setAlarmInfo(alarmId, state);
//
//                    intent.putExtra("alarm_id", response.getBody().getId());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                    if (state.equals(Constant.ALARM_STATE_START)) {    //已经报警，指派任务中
//                        intent.setAction(Constant.ACTION_ALARM_PROPERTY);
//
//                    } else if (state.equals(Constant.ALARM_STATE_ASSIGNED)) {  //维修工已经出发
//                        intent.setAction(Constant.ACTION_WORKER_ASSIGNED);
//
//                    } else if (state.equals(Constant.ALARM_STATE_ARRIVED)) {    //维修工已经到达
//                        intent.setAction(Constant.ACTION_WORKER_ARRIVED);
//
//                    } else if (state.equals(Constant.ALARM_STATE_COMPLETE)) {   //报警任务处理完成，物业未确认
//                        intent.setAction(Constant.ACTION_ALARM_COMPLETE);
//
//                    }
//
//                    startActivity(intent);
//                }
//            }
//
//        };
//
//        addTask(netTask);
    }

    /**
     * 请求报警列表
     *
     * @return
     */
    private RequestBean getAlarmListRequest() {
        AlarmListRequest request = new AlarmListRequest();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        request.setHead(head);
        return request;
    }

    /**
     * 弹出一个消息提示框
     *
     * @param callback
     * @param msg
     */
    public void popMsgAlertWithoutCancel(String msg, final IConfirmCallback callback) {
        popMessageAlert(msg, callback, false, null, "确定", null, null);
    }


    /**
     * 在同一个key下只能有一个弹出框实例
     *
     * @param msg
     * @param callback
     * @param key
     */
    public void popMsgAlertWithoutCancel(String msg, final IConfirmCallback callback, String key) {
        popMessageAlert(msg, callback, false, null, "确定", null, key);
    }

    /**
     * 弹出一个消息提示框，只有确认按钮，可以定义按钮的文本
     *
     * @param callback
     * @param msg
     */
    public void popMsgAlertWithoutCancel(String msg, final IConfirmCallback callback, String rightText,
                                         String title) {
        popMessageAlert(msg, callback, false, null, rightText, title, null);
    }

    /**
     * 带有取消按钮的提示框
     *
     * @param msg
     * @param callback
     */
    public void popMsgAlertWithCancel(String msg, final IConfirmCallback callback) {
        popMessageAlert(msg, callback, true, null, null, null, null);
    }

    public void popMsgAlertWithCancel(String msg, final IConfirmCallback callback, String key) {
        popMessageAlert(msg, callback, true, null, null, null, key);
    }

    /**
     * 带有取消按钮的提示框，自定义文字
     *
     * @param msg
     * @param callback
     */
    public void popMsgAlertWithCancel(String msg, final IConfirmCallback callback,
                                      String leftText, String rightText, String title) {
        popMessageAlert(msg, callback, true, leftText, rightText, title, null);
    }

    /**
     * 消息提示，点击确定时的callback
     */
    public interface IConfirmCallback {
        public void onConfirm();
    }

    /**
     * 非强制更新时点击取消时的callback
     */
    public interface IUpdateCancelCallback {
        public void onCancel();
    }

    /**
     * 产生内部消息的action
     *
     * @param action
     * @return
     */
    protected String addForeAction(String action) {
        if (action.contains("_FORE")) {
            return action;
        }
        return action + "_FORE";
    }


    /**
     * 弹出消息弹出框
     */
    private void popMessageAlert(String message, final IConfirmCallback callback,
                                 boolean needCancel, String leftText, String rightText,
                                 String title, String key) {

        View view = View.inflate(this, R.layout.layout_alert_msg, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view);

        AlertDialog dialog = null;

        if (StringUtils.isEmpty(key)) {
            dialog = builder.create();
        } else {
            dialog = mAlertDialogMap.get(key);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = builder.create();
            mAlertDialogMap.put(key, dialog);
        }


        dialog.setCanceledOnTouchOutside(false);

        initPopAlert(view, dialog, message, callback, needCancel, leftText, rightText, title);
        dialog.show();
    }


    /**
     * 初始化任务接受弹出框
     *
     * @param view
     * @param dialog
     * @param message
     * @param callback
     * @param needCancel
     */
    private void initPopAlert(View view, final AlertDialog dialog, String message,
                              final IConfirmCallback callback, boolean needCancel,
                              String leftText, String rightText, String title) {

        //设置左侧文本
        if (!StringUtils.isEmpty(leftText)) {
            ((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
        }

        //设置右侧文本
        if (!StringUtils.isEmpty(rightText)) {
            ((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
        }

        //设置标题
        if (!StringUtils.isEmpty(title)) {
            ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        }

        ((TextView) view.findViewById(R.id.tv_message)).setText(message);

        if (!needCancel) {
            view.findViewById(R.id.ll_cancel).setVisibility(View.GONE);
        }

        view.findViewById(R.id.ll_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.ll_query).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                if (callback != null) {
                    callback.onConfirm();
                }
            }

        });
    }


    /**
     * 检测服务器版本
     */
    protected void checkRemoteVersion(final ICheckVersionCallback callback) {

        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_VERSION_CHECK,
                getVersionCheckRequest()) {
            @Override
            protected void onResponse(NetTask task, String result) {
                VersionCheckResponse response = VersionCheckResponse.getVersionCheckResponse(result);
                int remoteVersion = response.getBody().getAppVersion();
                String url = response.getBody().getUrl();
                int isForce = response.getBody().getIsForce().equals("0") ? 0 : 1;
                String description = response.getBody().getDescription();
                callback.onCheckVersion(remoteVersion, url, isForce, description);
            }
        };

        addBackGroundTask(netTask);
    }

    /**
     * 检测完版本更新后处理方法
     */
    public interface ICheckVersionCallback {
        public void onCheckVersion(int remoteVersion, String url, int isForce, String description);
    }

    /**
     * 版本检测请求
     *
     * @return
     */
    private RequestBean getVersionCheckRequest() {

        VersionCheckRequest request = new VersionCheckRequest();
        RequestHead head = new RequestHead();

        request.setHead(head);

        return request;
    }

    /**
     * 页面是否在前台
     *
     * @return
     */
    public static boolean isForeground() {
        return isForeground;
    }

    /**
     * 注册应用内消息接收器
     */
    private void registerForeMsg() {
        //注册应用内部推送消息接收器
        mForeMsgReceiver = new ForeMsgReceiver(this);
        IntentFilter foreFilter = new IntentFilter();

        //维修工消息
        foreFilter.addAction(addForeAction(Constant.ACTION_ALARM_RECEIVED));
        foreFilter.addAction(addForeAction(Constant.ACTION_ALARM_ASSIGNED));
        foreFilter.addAction(addForeAction(Constant.ACTION_ALARM_UNASSIGNED));

        //物业消息
        foreFilter.addAction(addForeAction(Constant.ACTION_ALARM_PROPERTY));
        foreFilter.addAction(addForeAction(Constant.ACTION_WORKER_ASSIGNED));
        foreFilter.addAction(addForeAction(Constant.ACTION_WORKER_ARRIVED));
        foreFilter.addAction(addForeAction(Constant.ACTION_ALARM_COMPLETE));

        foreFilter.addAction(addForeAction(Constant.ACTION_ALARM_CANCEL));
        registerReceiver(mForeMsgReceiver, foreFilter);
    }

    /**
     * 接收到应用内部传送的信息进行处理
     *
     * @param intent
     */
    @Override
    public void onReceiveForeMsg(final Intent intent) {

        String msg = intent.getStringExtra("msg");
        String alarmId = intent.getStringExtra("alarm_id");

        //当前为维修工
        if (getConfig().getRole().equals(Constant.WORKER)) {


            if (intent.getAction().equals(Constant.ACTION_ALARM_RECEIVED)) {
                //播放报警声音
                playAlarmSound(true);
            } else {
                //播放收到消息声音
                playAlarmSound(false);
            }
            //在主页面收到消息，不需要跳转
            if (this instanceof WorkerActivity) {
                popMsgAlertWithoutCancel(msg, new IConfirmCallback() {
                    @Override
                    public void onConfirm() {
                        dispatchReceivedMsg(intent);
                    }
                }, alarmId);

            } else {    //在其他页面收到消息，需要跳转到主页面

                popMsgAlertWithCancel(msg, new IConfirmCallback() {
                    @Override
                    public void onConfirm() {
                        intent.setClass(BaseFragmentActivity.this, WorkerActivity.class);
                        startActivity(intent);
                    }
                }, alarmId);
            }

        } else if (getConfig().getRole().equals(Constant.PROPERTY)) {   //当前为物业


            if (intent.getAction().equals(Constant.ACTION_ALARM_PROPERTY)) {
                //播放报警声音
                playAlarmSound(true);
            } else {
                //播放收到消息声音
                playAlarmSound(false);
            }

            //在物业的主页面，不需要跳转
            if (this instanceof AlarmTraceActivity) {
                popMsgAlertWithoutCancel(msg, new IConfirmCallback() {
                    @Override
                    public void onConfirm() {
                        dispatchReceivedMsg(intent);
                    }
                }, alarmId);
            } else {    //在其他页面，需要跳转到主页面
                popMsgAlertWithCancel(msg, new IConfirmCallback() {
                    @Override
                    public void onConfirm() {
                        intent.setClass(BaseFragmentActivity.this, AlarmTraceActivity.class);
                        startActivity(intent);
                    }
                }, alarmId);
            }
        }


    }

    protected void dispatchReceivedMsg(Intent intent) {
    }


    /**
     * 播放报警声音
     */
    private void playAlarmSound(boolean isAlarm) {
        SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        try {
            //AssetFileDescriptor descriptor = getAssets().openFd("Alarm.mp3");
            if (isAlarm) {
                soundPool.load(this, R.raw.alarm, 1);
            } else {
                soundPool.load(this, R.raw.message, 1);
            }

            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    soundPool.play(sampleId, 0.5f, 0.5f, 0, 0, 1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载头像
     */
    public void loadIcon(ImageView iconView) {

        String dirPath = Utils.getTempPath();

        if (StringUtils.isEmpty(dirPath)) {
            return;
        }
        String url = getConfig().getIconUrl();
        Log.i("zhenhao", "pictrue url:" + url);

        if (StringUtils.isEmpty(url)) {
            iconView.setImageResource(R.drawable.icon_person);
            return;
        }
        String fileName = Utils.getFileNameByUrl(url);


        String filePath = dirPath + fileName;
        File file = new File(filePath);


        if (file.exists()) {
            Bitmap bitmap = Utils.getBitmapBySize(filePath, 80, 80);
            iconView.setImageBitmap(bitmap);
        } else {
            new GetPicture(url, iconView).execute();
        }
    }

    /**
     * 加载头像
     */
    public void loadIcon(String url, ImageView iconView) {

        String dirPath = Utils.getTempPath();

        if (StringUtils.isEmpty(dirPath)) {
            return;
        }

        if (StringUtils.isEmpty(url)) {
            iconView.setImageResource(R.drawable.icon_person);
            return;
        }

        String fileName = Utils.getFileNameByUrl(url);

        String filePath = dirPath + fileName;
        File file = new File(filePath);

        if (file.exists()) {
            Bitmap bitmap = Utils.getBitmapBySize(filePath, 80, 80);
            iconView.setImageBitmap(bitmap);
        } else {
            new GetPicture(url, iconView).execute();
        }
    }

    /**
     * 异步获取图片
     *
     * @author chang
     */
    public class GetPicture extends AsyncTask<String, Void, String> {

        private String mUrl;
        private ImageView mImageView;

        public GetPicture(String url, ImageView imageView) {
            mUrl = url;
            mImageView = imageView;
            mImageView.setImageResource(R.drawable.icon_person);
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String filePath = "";
            try {
                filePath = Utils.getImage(mUrl);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (!StringUtils.isEmpty(result)) {
                Bitmap bitmap = Utils.getBitmapBySize(result, 80, 80);
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);
                } else {
                    mImageView.setImageResource(R.drawable.icon_person);
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick(System.currentTimeMillis())) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 两次点击小于0.5秒钟，是快速点击
     *
     * @param curClickTime
     * @return
     */
    private boolean isFastDoubleClick(long curClickTime) {
        long timeInterval = curClickTime - mLastClickTime;
        mLastClickTime = curClickTime;
        return timeInterval < 500;
    }

    /**
     * 启动电梯视频观看
     *
     * @param liftId
     */
    public void startLiftVideo(String liftId) {
        liftId = "1cf456e3-9c92-4e61-bfa1-5d53a1f5a592";
        String server = getConfig().getServer() + NetConstant.URL_REQUEST_YDT;
        NetTask netTask = new NetTask(server, getRequestLiftVideoBean(liftId)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                LiftVideoResponse response = LiftVideoResponse.getLiftVideoRsp(result);

                Log.i("zhenhao", "result:" + result);
                if (null == response || null == response.getBody()
                        || StringUtils.isEmpty(response.getBody().getServerIp())) {
                    showToast("抱歉,您的电梯还没有接入视频!");
                } else {
                    LiftVideoResponse.LiftVideoRspBody body = response.getBody();

                    String user = body.getDvrUserName();

                    String password = body.getDvrPwd();

                    String ip = body.getServerIp();

                    int port = Utils.StringToInt(body.getDevicePort());

                    Intent intent = new Intent(BaseFragmentActivity.this, com.chorstar.video.LiftVideoActivity.class);

                    intent.putExtra("user", user);
                    intent.putExtra("password", password);
                    intent.putExtra("ip", ip);
                    intent.putExtra("port", port);

                    startActivity(intent);
                }
            }
        };

        addTask(netTask);
    }


    /**
     * 获取请求电梯视频参数的bean
     *
     * @param liftId
     * @return
     */
    private RequestBean getRequestLiftVideoBean(String liftId) {
        LiftVideoRequest request = new LiftVideoRequest();
        LiftVideoRequest.LiftVideoReqBody body = request.new LiftVideoReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setElevatorId(liftId);

        request.setHead(head);
        request.setBody(body);

        return request;
    }


    private static int mJpushCount = 0;


    /**
     * JPUSH 别名注册回调，如果注册失败，使用下面的策略进行处理
     * 请求15次，如果依然注册失败，则退出登录
     */
    private class MyAliasCallback implements TagAliasCallback {

        private String mAlias = "";


        public MyAliasCallback(String alias) {
            mAlias = alias;
        }


        @Override
        public void gotResult(int i, String s, Set<String> set) {
            if (0 == i) {
                Log.i("MYJPUSH", "set alias successfully!");
                Log.i("MYJPUSH", "alias:" + s);

            } else {
                Log.i("MYJPUSH", "mJpushCount:" + mJpushCount);
                Log.i("MYJPUSH", "return code:" + i);

                if (mJpushCount / 3 < 5) {
                    if (0 == mJpushCount % 3 && mJpushCount / 3 > 0 && mJpushCount / 3 < 5) {
                        try {
                            Thread.sleep(3 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    new JPushAliasThread(mAlias).start();
                } else {
                    showToast("推送服务注册失败，请检查网络再次登录!");
                    logout();
                }
                mJpushCount++;
            }
        }
    }


    /**
     * 注册JPUSH别名，新开线程处理
     */
    private class JPushAliasThread extends Thread {

        private String mAlias = "";

        public JPushAliasThread(String alias) {
            mAlias = alias;
        }

        @Override
        public void run() {
            JPushInterface.setAlias(BaseFragmentActivity.this, mAlias, new MyAliasCallback(mAlias));
        }
    }

    /**
     * 退出登录
     */
    protected void logout() {

        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_LOG_OUT,
                getLogoutRequest()) {
            @Override
            protected void onResponse(NetTask task, String result) {
                clearUserInfo();

                //退出登录时，取消之前设置的闹钟
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                for (PendingIntent pendingIntent : RemindUtils.mRemindList) {
                    manager.cancel(pendingIntent);
                }
                //关闭定位服务
                Intent sIntent = new Intent(BaseFragmentActivity.this, LocationService.class);
                BaseFragmentActivity.this.stopService(sIntent);

                //启动
                Intent intent = new Intent(BaseFragmentActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                SysActivityManager.getInstance().exit();
            }

            @Override
            protected void onFailed(NetTask task, String errorCode, String errorMsg) {
                //super.onFailed(task, errorCode, errorMsg);
                clearUserInfo();
                Intent intent = new Intent(BaseFragmentActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                SysActivityManager.getInstance().exit();
            }
        };

        addTask(netTask);
    }

    /**
     * 退出登录请求bean
     *
     * @return
     */
    private RequestBean getLogoutRequest() {
        VersionCheckRequest request = new VersionCheckRequest();
        RequestHead head = new RequestHead();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        request.setHead(head);

        return request;
    }


    private TelephonyManager telephonyManager;

    private MyPhoneStateListener phoneStateListener;

    /**
     * 注册通话监听
     */
    public void registerCallStateListener(final String tel, final OnCallStateListener listener) {

        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        }

        if (phoneStateListener == null) {
            phoneStateListener = new MyPhoneStateListener(this, tel, listener);
        }

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    /**
     * 解除通话监听
     */
    public void unRegisterCallStateListener() {

        if (telephonyManager != null && phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        Log.d("AAA", "通话监听解除");
    }
}