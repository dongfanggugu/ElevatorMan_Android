package com.honyum.elevatorMan.activity.company;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
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
import com.honyum.elevatorMan.data.mydata.NHFixAndTask;
import com.honyum.elevatorMan.data.newdata.CompanyRepairInfo;
import com.honyum.elevatorMan.net.NHFixCompanyResponse;
import com.honyum.elevatorMan.net.NHMentenanceRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Star on 2017/6/15.
 */

public class NHFixActivity extends BaseActivityWraper {

    @BindView(R.id.tv_look_his)
    TextView tvLookHis;
    @BindView(R.id.tv_no_deal)
    TextView tvNoDeal;
    @BindView(R.id.tv_dealing)
    TextView tvDealing;
    @BindView(R.id.tv_dealt)
    TextView tvDealt;
    @BindView(R.id.tv_outbound)
    TextView tvOutbound;

    @BindView(R.id.baidu_map)
    MapView baiduMap;

    Map<String, List<NHFixAndTask>> mCompanyRepairInfo;

    private BaiduMap mMap;
    private List<NHFixAndTask> mpreDealCompanyRepairInfo;
    private List<NHFixAndTask> mSavingCompanyRepairInfo;
    private List<NHFixAndTask> mCompletedCompanyRepairInfo;
    private List<NHFixAndTask> mCanceledCompanyRepairInfo;
    private Marker locationMarker;
    private int currIndex = 1;

    private  View view;

    @Override
    public String getTitleString() {
        return "怡墅维修";
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
    }

    @Override
    protected void preView() {
        super.preView();
        SDKInitializer.initialize(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNHFixListInfo();

    }

    private RequestBean getRequestBean(String userId, String token, String brandchId) {

        NHMentenanceRequest request = new NHMentenanceRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new NHMentenanceBody().setBranchId(brandchId));
        return request;
    }

    private void requestNHFixListInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_COMPANY_FIX,
                getRequestBean(getConfig().getUserId(), getConfig().getToken(), getConfig().getBranchId())) {
            @Override
            protected void onResponse(NetTask task, String result) {

                NHFixCompanyResponse response = NHFixCompanyResponse.getCompanyRepairInfoResponse(result);
                Log.e("INFO11", result);
                mCompanyRepairInfo = response.getBody();
                //获取到了返回的信息
                if (mCompanyRepairInfo == null || mCompanyRepairInfo.size() == 0) {
                    return;
                }

                groupbyState();

                AllSet();
                currIndex =1;
                tvNoDeal.setTextColor(getResources().getColor(R.color.titleblue));
                markMap(mpreDealCompanyRepairInfo, R.drawable.undefinedtask);
            }
        };
        addTask(task);
    }

    private void groupbyState() {
        mpreDealCompanyRepairInfo = new ArrayList<NHFixAndTask>();
        mCompletedCompanyRepairInfo = new ArrayList<NHFixAndTask>();
        mSavingCompanyRepairInfo = new ArrayList<NHFixAndTask>();
        mCanceledCompanyRepairInfo = new ArrayList<NHFixAndTask>();

        mSavingCompanyRepairInfo = mCompanyRepairInfo.get("chuLiZhong");
        mpreDealCompanyRepairInfo = mCompanyRepairInfo.get("weiChuLi");
        mCanceledCompanyRepairInfo = mCompanyRepairInfo.get("yiChaoQi");
        mCompletedCompanyRepairInfo = mCompanyRepairInfo.get("yiChuLi");


        tvNoDeal.setText("未处理：" + mpreDealCompanyRepairInfo.size());
        tvDealing.setText("处理中：" + mSavingCompanyRepairInfo.size());
        tvDealt.setText("已处理：" + mCompletedCompanyRepairInfo.size());
        tvOutbound.setText("已过期：" + mCanceledCompanyRepairInfo.size());


    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_company_nicehouse_mentenance;
    }

    /**
     * 标记报警
     */
    private void markMap(final List<NHFixAndTask> alarmInfo1, int id) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        if (null == alarmInfo1) {
            return;
        }


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(id);

        for (final NHFixAndTask info : alarmInfo1) {
            double latitude;
            double longitude;
            if (currIndex == 1) {
                latitude = info.getVillaInfo().getLat();
                longitude = info.getVillaInfo().getLng();
            } else {
                latitude = info.getRepairOrderInfo().getVillaInfo().getLat();
                longitude = info.getRepairOrderInfo().getVillaInfo().getLng();
            }
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
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    view = inflater.inflate(R.layout.mapdetail_item, null);
                    TextView textInfo = (TextView) view.findViewById(R.id.tv_detail_info);
                    Button forward = (Button) view.findViewById(R.id.bt_forward);



                    final Bundle bundle = arg0.getExtraInfo();
                    final NHFixAndTask info1 = (NHFixAndTask) bundle.getSerializable("Info");
                    double latitude;
                    double longitude;
                    if (currIndex == 1) {
                        latitude = info1.getVillaInfo().getLat();
                        longitude = info1.getVillaInfo().getLng();
                        textInfo.setText(info.getVillaInfo().getCellName());
                    } else {
                        latitude = info1.getRepairOrderInfo().getVillaInfo().getLat();
                        longitude = info1.getRepairOrderInfo().getVillaInfo().getLng();
                        textInfo.setText(info.getRepairOrderInfo().getVillaInfo().getCellName());
                    }
                    LatLng point = new LatLng(latitude, longitude);
                    InfoWindow mInfoWindow = new InfoWindow(view, point, 10);


                    forward.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mMap.hideInfoWindow();
                            Intent intent = new Intent(NHFixActivity.this, NHFixDetailActivity.class);
                            Bundle bundle = new Bundle();

                                intent.putExtra("Id", currIndex);

                            bundle.putSerializable("Info", info1);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    mMap.showInfoWindow(mInfoWindow);

                    return false;
                }
            });
        }
    }

    public void AllSet() {
        mMap.clear();
        tvDealing.setTextColor(Color.BLACK);
        tvDealt.setTextColor(Color.BLACK);
        tvNoDeal.setTextColor(Color.BLACK);
        tvOutbound.setTextColor(Color.BLACK);
    }

    /**
     * 标记报警
     */
    private void markMap(List<CompanyRepairInfo> alarmInfo1) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        if (null == alarmInfo1) {
            return;
        }


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.marker_worker);

        for (CompanyRepairInfo info : alarmInfo1) {

            double latitude = info.getRepairOrderInfo().getVillaInfo().getLat();
            double longitude = info.getRepairOrderInfo().getVillaInfo().getLng();
            LatLng point = new LatLng(latitude, longitude);
            MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor).position(point);
            locationMarker = (Marker) mMap.addOverlay(markerOption);
        }
        mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                showAppToast("点击了MAKER");
                return false;
            }
        });
    }

    @OnClick({R.id.tv_look_his, R.id.tv_no_deal, R.id.tv_dealing, R.id.tv_dealt, R.id.tv_outbound})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_look_his:
                startActivity(new Intent(this, NHFixOrderListActivity.class));
                break;
            case R.id.tv_no_deal:
                currIndex = 1;
                AllSet();
                tvNoDeal.setTextColor(getResources().getColor(R.color.titleblue));
                markMap(mpreDealCompanyRepairInfo, R.drawable.undefinedtask);
                break;
            case R.id.tv_dealing:
                currIndex = 2;
                AllSet();
                tvDealing.setTextColor(getResources().getColor(R.color.titleblue));
                markMap(mSavingCompanyRepairInfo, R.drawable.dealingtask);
                break;
            case R.id.tv_dealt:
                currIndex = 3;
                AllSet();
                tvDealt.setTextColor(getResources().getColor(R.color.titleblue));
                markMap(mCompletedCompanyRepairInfo, R.drawable.deattask);
                break;
            case R.id.tv_outbound:
                currIndex = 4;
                AllSet();
                tvOutbound.setTextColor(getResources().getColor(R.color.titleblue));
                markMap(mCanceledCompanyRepairInfo, R.drawable.overtimetask);
                break;
        }
    }
}
