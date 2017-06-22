package com.honyum.elevatorMan.activity.worker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.data.MaintenanceServiceInfo;
import com.honyum.elevatorMan.net.FixNextTimeRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;

import java.text.SimpleDateFormat;
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
    @BindView(R.id.tv_city)
    TextView tv_city;
    private AlertDialog alertDialog;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private BaiduMap mMap;
    private FixInfo mFixInfo;

    @Override
    public String getTitleString() {
        return getString(R.string.title_apptime);
    }

    @Override
    protected void initView() {
        mFixInfo = getIntent("Info");
        dialogLayout = LayoutInflater.from(this).inflate(R.layout.dia_datetime_layout, null);
        datePicker = (DatePicker) dialogLayout.findViewById(R.id.datePicker);
        timePicker = (TimePicker) dialogLayout.findViewById(R.id.timePicker);
        //使用dialog组合日期和时间控件。
        mMap = mapView.getMap();
        date = new Date();
        findViewById(R.id.tv_modify_date).setOnClickListener(new View.OnClickListener() {

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
        tv_city.setText(mFixInfo.getVillaInfo().getCellName());
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tvTime.setText(sdf.format(new Date(time)));
        markMap(mFixInfo);
    }

    private void requestAddRepairOrderProcess() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_TASK_ADD,
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
    protected void preView() {
        super.preView();
        SDKInitializer.initialize(getApplicationContext());

    }
    /**
     * 标记地图
     */
    private void markMap(FixInfo alarmInfo1) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        if (null == alarmInfo1) {
            return;
        }


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.marker_worker);


        double latitude = alarmInfo1.getVillaInfo().getLat();
        double longitude = alarmInfo1.getVillaInfo().getLng();
        LatLng point = new LatLng(latitude, longitude);
        MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor).position(point);
        Marker locationMarker = (Marker) mMap.addOverlay(markerOption);

    }
    @Override
    protected int getLayoutID() {
        return R.layout.activity_fix_app_time;
    }




    @OnClick({R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_submit:
                requestAddRepairOrderProcess();
                break;
        }
    }
}
