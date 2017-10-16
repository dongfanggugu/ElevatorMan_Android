package com.honyum.elevatorMan.activity.company;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.AlarmInfo1;
import com.honyum.elevatorMan.net.GetAlarmListRequest;
import com.honyum.elevatorMan.net.GetAlarmListResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Star on 2017/6/14.
 */

public class RescuLookActivity extends BaseActivityWraper {


    @BindView(R.id.tv_look_his)
    TextView tvLookHis;
    @BindView(R.id.tv_itemname)
    TextView tvItemname;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_companyname)
    TextView tvCompanyname;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_rectime)
    TextView tvRectime;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_predetail)
    TextView tvPredetail;
    @BindView(R.id.tv_saving)
    TextView tvSaving;
    @BindView(R.id.tv_completed)
    TextView tvCompleted;
    @BindView(R.id.tv_canceled)
    TextView tvCanceled;
    @BindView(R.id.baidu_map)
    MapView baiduMap;
    @BindView(R.id.tv_index)
    LinearLayout tvIndex;
    @BindView(R.id.ll_data)
    LinearLayout ll_data;


    private List<AlarmInfo1> mAlarmInfo1;
    private List<AlarmInfo1> mpreDealAlarmInfo1;
    private List<AlarmInfo1> mSavingAlarmInfo1;
    private List<AlarmInfo1> mCompletedAlarmInfo1;
    private List<AlarmInfo1> mCanceledAlarmInfo1;
    private BaiduMap mMap;


    @Override
    public String getTitleString() {
        return getString(R.string.company_save);
    }

    @Override
    protected void initView() {
        mMap = baiduMap.getMap();
        if (getConfig().getLat().equals("") || getConfig().getLng().equals("")) {

        } else {
            LatLng cenpt = new LatLng(Double.valueOf(getConfig().getLat()), Double.valueOf(getConfig().getLng()));
            //Log.e("TAG", "jingdu lng"+getConfig().getLng()+"weidu"+ getConfig().getLat());
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mMap.setMapStatus(mMapStatusUpdate);
        }
        findView(R.id.ll_data);
    }

    void fillDetail(AlarmInfo1 alarmInfo1) {
        ll_data.setVisibility(View.VISIBLE);
        tvItemname.setText(alarmInfo1.getCommunityName());
        tvAddress.setText("地址：" + alarmInfo1.getCommunityAddress());
        tvCompanyname.setText("公司名称：" + alarmInfo1.getCommunityName());
        tvRectime.setText("报警时间：" + alarmInfo1.getAlarmTime());
        if(alarmInfo1.getIsMisinformation().equals("1"))
        {
            tvState.setText("当前状态：已撤销");
        }
        else if(alarmInfo1.getState().equals("1") || alarmInfo1.getState().equals("2"))
        {
            tvState.setText("当前状态：救援中");
        }
        else if(alarmInfo1.getState().equals("3"))
        {
            tvState.setText("当前状态：已完成");
        }
        else if(alarmInfo1.getState().equals("0"))
        {
            tvState.setText("当前状态：待处理");
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        requestAlarmListInfo("2");
    }

    @Override
    protected void preView() {
        super.preView();
        SDKInitializer.initialize(getApplicationContext());

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_company_save;
    }


    private RequestBean getAlarmListRequesttBean(String userId, String token, String brandchId, String history) {

        GetAlarmListRequest request = new GetAlarmListRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new GetAlarmListBody().setBranchId(brandchId).setHistory(history));
        return request;
    }

    private void requestAlarmListInfo(String his) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_ALARM_LIST_BRANCH,
                getAlarmListRequesttBean(getConfig().getUserId(), getConfig().getToken(), getConfig().getBranchId(), his)) {
            @Override
            protected void onResponse(NetTask task, String result) {

                GetAlarmListResponse response = GetAlarmListResponse.getAlarmListResponse(result);
                mAlarmInfo1 = response.getBody();
                //获取到了返回的信息
                if (mAlarmInfo1 == null) {
                    return;
                }
                groupbyState();
                AllSet();
                tvPredetail.setTextColor(getResources().getColor(R.color.titleblue));
                markAlarm(mpreDealAlarmInfo1, R.drawable.undefinedtask);

            }
        };
        addTask(task);
    }

    private void groupbyState() {
        mpreDealAlarmInfo1 = new ArrayList<AlarmInfo1>();
        mCompletedAlarmInfo1 = new ArrayList<AlarmInfo1>();
        mSavingAlarmInfo1 = new ArrayList<AlarmInfo1>();
        mCanceledAlarmInfo1 = new ArrayList<AlarmInfo1>();

        for (AlarmInfo1 info : mAlarmInfo1
                ) {
            if (info.getIsMisinformation().equals("1")) {
                mCanceledAlarmInfo1.add(info);
            } else if (info.getState().equals("1") || info.getState().equals("2")) {
                mSavingAlarmInfo1.add(info);
            } else if (info.getState().equals("3")) {
                mCompletedAlarmInfo1.add(info);
            } else if (info.getState().equals("0")) {
                mpreDealAlarmInfo1.add(info);
            }

        }
        tvPredetail.setText("待处理：" + mpreDealAlarmInfo1.size());
        tvSaving.setText("处理中：" + mSavingAlarmInfo1.size());
        tvCompleted.setText("已完成：" + mCompletedAlarmInfo1.size());
        tvCanceled.setText("已撤销：" + mCanceledAlarmInfo1.size());


    }


    @OnClick({R.id.tv_predetail, R.id.tv_saving, R.id.tv_completed, R.id.tv_canceled, R.id.tv_look_his})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_predetail:

                AllSet();
                tvPredetail.setTextColor(getResources().getColor(R.color.titleblue));
                markAlarm(mpreDealAlarmInfo1, R.drawable.undefinedtask);
                break;
            case R.id.tv_saving:
                AllSet();
                tvSaving.setTextColor(getResources().getColor(R.color.titleblue));
                markAlarm(mSavingAlarmInfo1, R.drawable.dealingtask);
                break;
            case R.id.tv_completed:
                AllSet();
                tvCompleted.setTextColor(getResources().getColor(R.color.titleblue));
                markAlarm(mCompletedAlarmInfo1, R.drawable.deattask);
                break;
            case R.id.tv_canceled:
                AllSet();
                tvCanceled.setTextColor(getResources().getColor(R.color.titleblue));
                markAlarm(mCanceledAlarmInfo1, R.drawable.overtimetask);
                break;
            case R.id.tv_look_his:
                startActivity(new Intent(RescuLookActivity.this, RescuHisListActivity.class));
                break;
        }
    }

    /**
     * 标记报警
     */
    private void markAlarm(final List<AlarmInfo1> alarmInfo1, int id) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        if (null == alarmInfo1) {
            return;
        }


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(id);

        for (final AlarmInfo1 info : alarmInfo1) {

            double latitude = info.getLat();
            double longitude = info.getLng();
            LatLng point = new LatLng(latitude, longitude);
            MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor).position(point);
            Marker locationMarker = (Marker) mMap.addOverlay(markerOption);

            Bundle bundle = new Bundle();
            bundle.putSerializable("Info", info);
            locationMarker.setExtraInfo(bundle);
            mMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.hideInfoWindow();
                }

                @Override
                public boolean onMapPoiClick(MapPoi mapPoi) {
                    return false;
                }
            });
            mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                Bundle bundle = arg0.getExtraInfo();
                AlarmInfo1 info1 = (AlarmInfo1) bundle.getSerializable("Info");
                fillDetail(info1);
                return false;
            }
        });
        }
    }

    public void AllSet() {
        mMap.clear();
        ll_data.setVisibility(View.GONE);
        tvPredetail.setTextColor(Color.BLACK);
        tvSaving.setTextColor(Color.BLACK);
        tvCanceled.setTextColor(Color.BLACK);
        tvCompleted.setTextColor(Color.BLACK);
    }
}
