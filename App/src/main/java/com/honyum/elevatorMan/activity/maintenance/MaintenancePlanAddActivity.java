package com.honyum.elevatorMan.activity.maintenance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.MaintenanceServiceInfo;
import com.honyum.elevatorMan.data.MaintenanceTaskInfo;
import com.honyum.elevatorMan.net.MaintenanceServiceAddPlanRequest;
import com.honyum.elevatorMan.net.MaintenanceServiceArriveRequest;
import com.honyum.elevatorMan.net.MaintenanceServiceStartRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;

import java.util.Date;

import static com.honyum.elevatorMan.net.base.NetConstant.ADD_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.ARRIVE_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.ENSURED_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.EVA_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.FINISH_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.START_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.UNENSURE_STATE;

/**
 * Created by Star on 2017/6/9.
 */

public class MaintenancePlanAddActivity extends BaseFragmentActivity implements View.OnClickListener {
    private LinearLayout ll_moreinfo;
    private LinearLayout ll_date;
    private TextView tv_plan_date;
    private View dialogLayout;
    private DatePicker datePicker;
    private TimePicker timePicker;
    Date date;
    String s1;
    String s2;
    private AlertDialog alertDialog;
    private TextView tv_tel;
    private TextView tv_address;
    private TextView tv_time;
    private TextView tv_product_name;
    private String currId;
    private TextView tv_giveup;
    private TextView tv_addplan;
    private TextView tv_start;
    private ImageView iv_modify_datetime;

    private MaintenanceTaskInfo mMaintenanceTaskInfo;
    private MaintenanceServiceInfo mMaintenanceServiceInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_select);
        initTitle();
        initView();
    }

    /**
     * 初始化标题
     */
    private void initTitle() {


        initTitleBar("维保计划制定", R.id.title_order,
                R.drawable.back_normal, backClickListener);
    }

    /**
     * 初始化视图
     */
    private void initView() {

        findViewById(R.id.iv_expandDetail).setOnClickListener(this);
        dialogLayout = LayoutInflater.from(this).inflate(R.layout.dia_datetime_layout, null);
        datePicker = (DatePicker) dialogLayout.findViewById(R.id.datePicker);
        timePicker = (TimePicker) dialogLayout.findViewById(R.id.timePicker);
        findViewById(R.id.tv_start).setVisibility(View.GONE);
        findViewById(R.id.tv_giveup).setVisibility(View.GONE);


        date = new Date();

        //使用dialog组合日期和时间控件。
        findViewById(R.id.ll_date).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(date.getHours() + 1);
                timePicker.setCurrentMinute(0);
                int minute = timePicker.getCurrentMinute();
                s2 = "  " + (timePicker.getCurrentHour()) + ":" + (minute < 10 ? "0" + minute : minute);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        s2 = ("  " + hourOfDay + ":" + (minute < 10 ? "0" + minute : minute));
                    }
                });
                alertDialog.show();
            }
        });
        alertDialog = new AlertDialog.Builder(this).setTitle("选择时间").setView(dialogLayout).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        s1 = (datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                        String dateString = s1 + s2;
                        tv_plan_date.setText(dateString);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        }).create();
        //end 组合控件

        tv_plan_date = (TextView) findViewById(R.id.tv_plan_date);
        ll_moreinfo = (LinearLayout) findViewById(R.id.ll_moreinfo);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_addplan = (TextView) findViewById(R.id.tv_addplan);
        iv_modify_datetime = (ImageView) findViewById(R.id.iv_modify_datetime);
        tv_giveup = (TextView) findViewById(R.id.tv_giveup);


        //获取页面的参数 进行VIEW填充
        Intent it = getIntent();

        if(it.getStringExtra("State").equals(ADD_STATE))
        {
              mMaintenanceServiceInfo  = (MaintenanceServiceInfo)it.getSerializableExtra("Info");
              tv_address.setText(mMaintenanceServiceInfo.getVillaInfo().getAddress());
              tv_tel.setText(mMaintenanceServiceInfo.getOwnerInfo().getTel());
              tv_time.setText(mMaintenanceServiceInfo.getExpireTime());
              tv_product_name.setText(mMaintenanceServiceInfo.getVillaInfo().getBrand());
              currId = mMaintenanceServiceInfo.getId();
        }
        else
        {
            mMaintenanceTaskInfo = (MaintenanceTaskInfo)it.getSerializableExtra("Info");
            tv_address.setText(mMaintenanceTaskInfo.getMaintOrderInfo().getVillaInfo().getAddress());
            tv_tel.setText(mMaintenanceTaskInfo.getMaintOrderInfo().getOwnerInfo().getTel());
            tv_time.setText(mMaintenanceTaskInfo.getPlanTime());
            tv_product_name.setText(mMaintenanceTaskInfo.getMaintOrderInfo().getVillaInfo().getBrand());
            currId = mMaintenanceTaskInfo.getId();
        }
        changeBottomButton(it.getStringExtra("State"));


    }

    private void requestAddMaintOrderProcess() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_SERVICE_ADD,
                getAddRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showAppToast(getString(R.string.sucess));
                    finish();
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);
    }

    private RequestBean getAddRequestBean(String userId, String token) {
        MaintenanceServiceAddPlanRequest request = new MaintenanceServiceAddPlanRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new MaintenanceServiceAddPlanBody().setMaintOrderId(currId).setPlanTime(tv_time.getText().toString()));
        return request;
    }



    private void requestMaintOrderProcessWorkerSetOut(final String state) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_TASK_START,
                getSetOutRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showAppToast(getString(R.string.sucess));
                    changeBottomButton(state);
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);
    }
    private RequestBean getSetOutRequestBean(String userId, String token) {
        MaintenanceServiceStartRequest request = new MaintenanceServiceStartRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new MaintenanceServiceStartBody().setMaintOrderProcessId(currId));
        return request;
    }




    private RequestBean getArriveRequestBean(String userId, String token) {
        MaintenanceServiceArriveRequest request = new MaintenanceServiceArriveRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new MaintenanceServiceArriveBody().setMaintOrderProcessId(currId));
        return request;
    }

    private void requestMaintOrderProcessWorkerArrive(final String state) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_TASK_ARRIVE,
                getArriveRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showAppToast(getString(R.string.sucess));
                    changeBottomButton(state);
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);
    }



    /**
     * 根据字符串返回的state返回决定按钮的显示和隐藏，使用Java 7之后 的 case:String，切换状态使用
     *
     * @param state
     */
    private void changeBottomButton(String state) {

        switch (state) {
            //TODO 归类节省代码,木有做
            case ADD_STATE:
                tv_addplan.setVisibility(View.VISIBLE);
                tv_addplan.setText(R.string.add_plan);
                tv_start.setVisibility(View.GONE);
                iv_modify_datetime.setVisibility(View.VISIBLE);
                tv_giveup.setVisibility(View.GONE);
                tv_addplan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestAddMaintOrderProcess();
                    }
                });
                break;
            case UNENSURE_STATE:
                tv_addplan.setVisibility(View.GONE);
                //tv_addplan.setText(R.string.modify);
                tv_start.setVisibility(View.GONE);
                tv_giveup.setVisibility(View.GONE);
                iv_modify_datetime.setVisibility(View.GONE);
                break;
            case ENSURED_STATE:
                iv_modify_datetime.setVisibility(View.GONE);
                tv_addplan.setVisibility(View.VISIBLE);
                tv_addplan.setText(R.string.start_now);
                //TODO 改变按钮外观。并在回调函数设置下一个状态  需要改变形式
                tv_addplan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestMaintOrderProcessWorkerSetOut(START_STATE);
                    }
                });
                tv_start.setVisibility(View.GONE);
                tv_giveup.setVisibility(View.GONE);
                break;
            case START_STATE:
                tv_addplan.setVisibility(View.VISIBLE);
                tv_addplan.setText(R.string.arrive);
                tv_addplan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestMaintOrderProcessWorkerArrive(ARRIVE_STATE);
                }
            });
                tv_start.setVisibility(View.GONE);
                tv_giveup.setVisibility(View.GONE);
                break;
            case ARRIVE_STATE:
                tv_addplan.setVisibility(View.GONE);
                //tv_addplan.setText(R.string.finish);
                tv_start.setVisibility(View.VISIBLE);
                tv_start.setText(R.string.finish);
                tv_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenancePlanAddActivity.this, MaintenanceTaskFinishActivity.class);
                        intent.putExtra("Id", currId);
                        startActivity(intent);
                    }
                });
                tv_giveup.setVisibility(View.VISIBLE);
                tv_giveup.setText(R.string.today_giveup);
                tv_giveup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenancePlanAddActivity.this, MaintenanceTaskUnfinishActivity.class);
                        intent.putExtra("Id", currId);
                        startActivity(intent);
                    }
                });
                break;
            case FINISH_STATE:
                tv_addplan.setVisibility(View.VISIBLE);
                tv_addplan.setText(R.string.look_result);
                tv_giveup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenancePlanAddActivity.this, MaintenanceTaskUnfinishActivity.class);
                        intent.putExtra("Id", currId);
                        startActivity(intent);
                    }
                });
                tv_start.setVisibility(View.GONE);
                tv_giveup.setVisibility(View.GONE);
                break;
            case EVA_STATE:
                tv_addplan.setVisibility(View.VISIBLE);
                tv_addplan.setText(R.string.look_eva);
                tv_start.setVisibility(View.GONE);
                tv_giveup.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_expandDetail:

                if (ll_moreinfo.getVisibility() == View.VISIBLE) {
                    ll_moreinfo.setVisibility(View.GONE);
                } else {
                    ll_moreinfo.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
