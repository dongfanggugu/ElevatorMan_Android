package com.honyum.elevatorMan.activity.worker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baidu.mapapi.map.MapView;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.net.FixNextTimeRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.honyum.elevatorMan.net.base.NetConstant.RSP_CODE_SUC_0;

/**
 * Created by Star on 2017/6/12.
 */

public class FixNextTimeActivity extends BaseActivityWraper {

    private View dialogLayout;
    private DatePicker datePicker;
    private TimePicker timePicker;
    Date date;
    String s1;
    String s2;
    private AlertDialog alertDialog;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_modify_date)
    TextView tvModifyDate;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private FixInfo mFixInfo;

    @Override
    public String getTitleString() {
        return getString(R.string.app_time);
    }

    @Override
    protected void initView() {
        mFixInfo = getIntent("Info");

        alertDialog = new AlertDialog.Builder(this).setTitle("选择时间").setView(dialogLayout).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        s1 = (datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                        String dateString = s1 + s2;
                        tvTime.setText(dateString);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        }).create();
        //end 组合控件
    }

    private void requestAddRepairOrderProcess() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_TASK_FINISH,
                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null && response.getHead().getRspCode().equals(RSP_CODE_SUC_0)) {
                    showAppToast(getString(R.string.sucess));
                    finish();
                }
            }
        };
        addTask(task);
    }

    private RequestBean getRequestBean(String userId, String token) {

        FixNextTimeRequest request = new FixNextTimeRequest();
        request.setHead(new NewRequestHead().setaccessToken(token).setuserId(userId));
        request.setBody(request.new FixNextTimeBody().setRepairOrderId(mFixInfo.getId()).setPlanTime(tvTime.getText().toString()));
        return request;
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_fix_app_time;
    }




    @OnClick({R.id.tv_modify_date, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_modify_date:
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
                break;
            case R.id.tv_submit:
                requestAddRepairOrderProcess();
                break;
        }
    }
}
