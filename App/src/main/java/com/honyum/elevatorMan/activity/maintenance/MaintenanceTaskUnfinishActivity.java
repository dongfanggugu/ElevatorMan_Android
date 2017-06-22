package com.honyum.elevatorMan.activity.maintenance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.MaintenanceServiceUnFinishRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Star on 2017/6/10.
 */

public class MaintenanceTaskUnfinishActivity extends BaseFragmentActivity {

    private String currId;
    private DatePicker datePicker;
    private TimePicker timePicker;
    Date date;
    String s1;
    String s2;
    private AlertDialog alertDialog;
    private View dialogLayout;
    private TextView tv_time;
    private TextView tv_submit;
    private EditText et_remark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentenance_app_time);
        initTitle();
        initView();
    }
    /**
     * 初始化标题
     */
    private void initTitle() {


        initTitleBar("另行约定", R.id.title,
                R.drawable.back_normal, backClickListener);
    }

    /**
     * 初始化view
     */
    private void initView() {
        dialogLayout = LayoutInflater.from(this).inflate(R.layout.dia_datetime_layout, null);
        datePicker = (DatePicker) dialogLayout.findViewById(R.id.datePicker);
        timePicker = (TimePicker) dialogLayout.findViewById(R.id.timePicker);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_submit =  (TextView)findViewById(R.id.tv_submit);

        et_remark = (EditText) findViewById(R.id.et_remark);
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tv_time.setText(sdf.format(new Date(time)));
        Intent it = getIntent();
        currId = it.getStringExtra("Id");
        //使用dialog组合日期和时间控件。
        date = new Date();
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
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date d = new Date();
                        try {
                            d = sdf.parse(dateString);
                            long t = d.getTime();
                            long cl = System.currentTimeMillis();

                            if (cl > t) {
                                showToast("选择日期应大于当前日期！");
                                return;
                            }
                            tv_time.setText(dateString);
                            dialog.dismiss();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        }).create();
        //end 组合控件
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMaintOrderProcessWorkerUnableFinish();
            }
        });
    }

    private void requestMaintOrderProcessWorkerUnableFinish() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_TASK_UNFINISH,
                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showAppToast(getString(R.string.sucess));
                    finish();
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());
            }
            //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());
        };
        addTask(task);
    }

    private RequestBean getRequestBean(String userId, String token) {

        MaintenanceServiceUnFinishRequest request = new MaintenanceServiceUnFinishRequest();
        request.setHead(new NewRequestHead().setaccessToken(token).setuserId(userId));
        request.setBody(request.new MaintenanceServiceUnFinishBody().setMaintOrderProcessId(currId).setMaintUserFeedback(et_remark.getText().toString().trim()).setPlanTime(tv_time.getText().toString()));
        return request;
    }

}
