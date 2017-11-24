package com.honyum.elevatorMan.activity.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.constant.ConstantIntent;
import com.honyum.elevatorMan.net.AttendanceReponse;
import com.honyum.elevatorMan.net.SignInRequest;
import com.honyum.elevatorMan.net.SignInRequestBody;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.net.base.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Star on 2017/10/31.
 */

public class SignLeaveActivity extends BaseActivityWraper {


    private String s2;
    private String currId = "";
    private TextView tv_start_time;
    private TextView tv_end_time = null;
    private TextView tv_submit;
    private EditText et_reason;

    private String action = "";


    private String title = "";
    private View dialogLayout;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Date date;
    private AlertDialog alertDialog;
    private String s1;
    private boolean isTimePass;
    private TextView tv_start_lable;


    @Override
    protected void preView() {
        Intent it = getIntent();
        if (it != null) {
            if (it.hasExtra(ConstantIntent.INTENT_TITLE)) {
                title = it.getStringExtra(ConstantIntent.INTENT_TITLE);
            }
            if (it.hasExtra(ConstantIntent.INTENT_ACTION)) {
                action = it.getStringExtra(ConstantIntent.INTENT_ACTION);
            }
        }
    }

    @Override
    public String getTitleString() {
        return title;
    }

    @Override
    protected void initView() {

        if (TextUtils.equals(action, SignActivity.Companion.getSIGN_LEAVE())) {
            findView(R.id.ll_end_time).setVisibility(View.VISIBLE);
            findView(R.id.line).setVisibility(View.VISIBLE);
            tv_end_time = findView(R.id.tv_end_time);
        }
        else
        {
            requestAttendance();
        }
        tv_start_time = findView(R.id.tv_start_time);
        et_reason = findView(R.id.et_reason);
        tv_submit = findView(R.id.tv_submit);

        tv_start_lable = findView(R.id.tv_start_lable);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_end_time != null) {
                    if (isTimePass)
                        submit();
                    else showToast("选择日期应大于当前日期");
                } else {
                    submit();
                }
            }
        });

        dialogLayout = LayoutInflater.from(this).inflate(R.layout.dia_datetime_layout, null);
        datePicker = (DatePicker) dialogLayout.findViewById(R.id.datePicker);
        timePicker = (TimePicker) dialogLayout.findViewById(R.id.timePicker);
        date = new Date();
        tv_start_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tv_start_time.setTag(R.id.index, true);
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(date.getHours() + 1);
                timePicker.setCurrentMinute(0);
                int minute = timePicker.getCurrentMinute();
                s2 = " " + (timePicker.getCurrentHour()) + ":" + (minute < 10 ? "0" + minute : minute);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        s2 = (" " + hourOfDay + ":" + (minute < 10 ? "0" + minute : minute));
                    }
                });
                alertDialog.show();
            }
        });

        if (tv_end_time != null) {
            tv_end_time.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    tv_start_time.setTag(R.id.index, false);
                    timePicker.setIs24HourView(true);
                    timePicker.setCurrentHour(date.getHours() + 1);
                    timePicker.setCurrentMinute(0);
                    int minute = timePicker.getCurrentMinute();
                    s2 = " " + (timePicker.getCurrentHour()) + ":" + (minute < 10 ? "0" + minute : minute);
                    timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            s2 = (" " + hourOfDay + ":" + (minute < 10 ? "0" + minute : minute));
                        }
                    });
                    alertDialog.show();
                }
            });

        }
        alertDialog = new AlertDialog.Builder(this).setTitle("选择时间").setView(dialogLayout).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        s1 = (datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                        String dateString = s1 + s2;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date d = new Date();
                        try {
                            isTimePass = false;
                            d = sdf.parse(dateString);
                            long t = d.getTime();
                            long cl = System.currentTimeMillis();

                            if (cl > t) {
                                isTimePass = false;
                            } else {
                                isTimePass = true;
                            }
                            if ((boolean) tv_start_time.getTag(R.id.index) == true)
                                tv_start_time.setText(dateString);
                            else if (tv_end_time != null)
                                tv_end_time.setText(dateString);
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

    }

    private void submit() {
        String startTime = tv_start_time.getText().toString().trim();


        String reason = et_reason.getText().toString().trim();

        String endTime = "";
        if (tv_end_time != null) {
            endTime = tv_end_time.getText().toString().trim();
            if (TextUtils.isEmpty(endTime) || TextUtils.isEmpty(startTime)) {
                showToast("请输入完整的时间信息");
                return;
            }
            requestSign("", action, "", startTime, endTime, reason);
            return;
        }
        //说明是销假
        requestSign(currId, action, "", "", "", "");
    }

    private RequestBean getRequest(String id, String state, String signTime, String startTime, String endTime, String reason) {

        SignInRequestBody signInRequestbody = new SignInRequestBody(id, getConfig().getBranchId(), signTime, state, startTime, endTime, reason);
        SignInRequest signInRequest = new SignInRequest();
        RequestHead head = new NewRequestHead().setaccessToken(getConfig().getToken()).setuserId(getConfig().getUserId());
        signInRequest.setHead(head);
        signInRequest.setBody(signInRequestbody);
        return signInRequest;

    }

    private void requestSign(String id, String state, String signTime, String startTime, String endTime, String reason) {
        String server = "";
        if (tv_end_time != null)
            server = getConfig().getServer() + NetConstant.SIGN;
        else
            server = getConfig().getServer() + NetConstant.SIGN_CANCEL;

        NetTask netTask = new NetTask(server, getRequest(id, state, signTime, startTime, endTime, reason)) {

            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                showToast(response.getHead().getRspMsg());
                finish();
            }

        };
        addTask(netTask);
    }

    private RequestBean getAttendanceRequest() {

        RequestBean requestBean = new RequestBean();
        requestBean.setHead(new NewRequestHead().setuserId(getConfig().getUserId()).setaccessToken(getConfig().getToken()));
        return requestBean;

    }

    private void requestAttendance() {
        String
                server = getConfig().getServer() + NetConstant.SIGN_GET;

        NetTask netTask = new NetTask(server, getAttendanceRequest()) {

            @Override
            protected void onResponse(NetTask task, String result) {
                AttendanceReponse response = AttendanceReponse.Companion.getResponse(result);

                if (TextUtils.isEmpty(response.getBody().getStartTime()) || TextUtils.isEmpty(response.getBody().getEndTime())) {
                   // showToast("当前无请假信息，无需销假");
                    return;
                } else {
                    currId = response.getBody().getId();
                    tv_start_time.setText(response.getBody().getStartTime() + "至" + response.getBody().getEndTime());
                    tv_start_time.setClickable(false);
                    tv_start_lable.setText("上次请假时间: ");
                    et_reason.setText(response.getBody().getReason());
                    et_reason.setClickable(false);
                    et_reason.setFocusable(false);
                    et_reason.setHint("");

                }
            }

        };
        addTask(netTask);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sign_leave;
    }
}
