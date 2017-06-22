package com.honyum.elevatorMan.activity.company;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.MaintenanceServiceInfo;
import com.honyum.elevatorMan.data.MaintenanceTaskInfo;
import com.honyum.elevatorMan.data.mydata.NHorderAndTask;

import java.util.Date;

/**
 * Created by Star on 2017/6/21.
 */

public class NFMentenanceTaskDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
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
    private MapView baiduMap;
    private BaiduMap mMap;
    private Marker locationMarker;
    private ImageView iv_expandDetail;
    private TextView tv_product_name1;
    private TextView tv_product_name2;

    private String name = "";
    private String name1 = "";
    private String name2 = "";

    private NHorderAndTask mNHorderAndTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preView();
        setContentView(R.layout.activity_order_select);
        initTitle();
        initView();
    }

    public void preView() {
        SDKInitializer.initialize(getApplicationContext());

    }

    /**
     * 标记地图
     */
    private void markMap(NHorderAndTask alarmInfo1) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        if (null == alarmInfo1) {
            return;
        }


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.marker_worker);


        double latitude = alarmInfo1.getMaintOrderInfo().getVillaInfo().getLat();
        double longitude = alarmInfo1.getMaintOrderInfo().getVillaInfo().getLng();
        LatLng point = new LatLng(latitude, longitude);
        MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor).position(point);
        locationMarker = (Marker) mMap.addOverlay(markerOption);

    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        initTitleBar("维保任务查看", R.id.title_order,
                R.drawable.back_normal, backClickListener);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        baiduMap = (MapView) findViewById(R.id.mapView);
        mMap = baiduMap.getMap();
        iv_expandDetail = (ImageView) findViewById(R.id.iv_expandDetail);
        iv_expandDetail.setOnClickListener(this);
        dialogLayout = LayoutInflater.from(this).inflate(R.layout.dia_datetime_layout, null);
        datePicker = (DatePicker) dialogLayout.findViewById(R.id.datePicker);
        timePicker = (TimePicker) dialogLayout.findViewById(R.id.timePicker);
        findViewById(R.id.tv_start).setVisibility(View.GONE);
        findViewById(R.id.tv_giveup).setVisibility(View.GONE);

        ll_moreinfo = (LinearLayout) findViewById(R.id.ll_moreinfo);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_product_name1 = (TextView) findViewById(R.id.tv_product_name1);
        tv_product_name2 = (TextView) findViewById(R.id.tv_product_name2);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_addplan = (TextView) findViewById(R.id.tv_addplan);
        iv_modify_datetime = (ImageView) findViewById(R.id.iv_modify_datetime);
        tv_giveup = (TextView) findViewById(R.id.tv_giveup);
        tv_plan_date = (TextView) findViewById(R.id.tv_plan_date);


        tv_addplan.setVisibility(View.GONE);
        //tv_addplan.setText(R.string.modify);
        tv_plan_date.setVisibility(View.GONE);
        tv_start.setVisibility(View.GONE);
        tv_giveup.setVisibility(View.GONE);
        iv_modify_datetime.setVisibility(View.GONE);


        //获取页面的参数 进行VIEW填充
        Intent it = getIntent();
        mNHorderAndTask = (NHorderAndTask) it.getExtras().get("Info");
        tv_address.setText(mNHorderAndTask.getMaintOrderInfo().getVillaInfo().getCellName());
        tv_tel.setText(mNHorderAndTask.getMaintOrderInfo().getSmallOwnerInfo().getTel());
        tv_time.setText(mNHorderAndTask.getPlanTime());
        name = mNHorderAndTask.getMaintOrderInfo().getVillaInfo().getBrand();
        name1 = mNHorderAndTask.getMaintOrderInfo().getVillaInfo().getWeight() + "KG";
        name2 = mNHorderAndTask.getMaintOrderInfo().getVillaInfo().getLayerAmount() + "层";
        markMap(mNHorderAndTask);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_expandDetail:

                if (ll_moreinfo.getVisibility() == View.VISIBLE) {
                    ll_moreinfo.setVisibility(View.GONE);
                    iv_expandDetail.setImageResource(R.drawable.openicon);
                } else {
                    ll_moreinfo.setVisibility(View.VISIBLE);
                    tv_product_name.setText(name);
                    tv_product_name1.setText(name1);
                    tv_product_name2.setText(name2);
                    iv_expandDetail.setImageResource(R.drawable.colseicon);
                }
                break;
        }
    }
}
