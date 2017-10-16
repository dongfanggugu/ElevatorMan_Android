package com.honyum.elevatorMan.activity.company;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.honyum.elevatorMan.data.BranchInfo;
import com.honyum.elevatorMan.data.MaintRecInfo;
import com.honyum.elevatorMan.data.WorkerInfo2;
import com.honyum.elevatorMan.net.GetBranchRequest;
import com.honyum.elevatorMan.net.GetBranchResponse;
import com.honyum.elevatorMan.net.GetMaintListRequest;
import com.honyum.elevatorMan.net.GetMiantListResponse;
import com.honyum.elevatorMan.net.GetWorkerRequest;
import com.honyum.elevatorMan.net.GetWorkerResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.utils.DataTravelUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Star on 2017/6/15.
 */

public class EMantenanceLookActivity extends BaseActivityWraper {

    @BindView(R.id.tv_look_his)
    TextView tvLookHis;
    @BindView(R.id.ll_subcompany)
    LinearLayout llSubcompany;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.tv_undefine)
    TextView tvUndefine;
    @BindView(R.id.tv_no_continue)
    TextView tvNoContinue;
    @BindView(R.id.tv_outbound)
    TextView tvOutbound;
    @BindView(R.id.tv_finished)
    TextView tvFinished;
    @BindView(R.id.tv_branch_name)
    TextView tvBranch_name;

    @BindView(R.id.baidu_map)
    MapView baiduMap;

    @BindView(R.id.tv_worker_name)
    TextView tvWorker_name;

    List<BranchInfo> mBranchInfo;
    BranchInfo currBranch;
    List<WorkerInfo2> mWorkerInfo2;

    Map<String, List<MaintRecInfo>> mMiantMap;

    private List<MaintRecInfo> mUndefinedList;
    private List<MaintRecInfo> mPrepareMentenanceList;
    private List<MaintRecInfo> mOverTimeList;
    private List<MaintRecInfo> mCompleteList;


    private List<MaintRecInfo> mCUndefinedList;
    private List<MaintRecInfo> mCPrepareMentenanceList;
    private List<MaintRecInfo> mCOverTimeList;
    private List<MaintRecInfo> mCCompleteList;

    private List<MaintRecInfo> mWorkerUndefinedList;
    private List<MaintRecInfo> mWorkerCPrepareMentenanceList;
    private List<MaintRecInfo> mWorkerCOverTimeList;
    private List<MaintRecInfo> mWorkerCCompleteList;

    private WorkerInfo2 currWoker;

    private List<MaintRecInfo> currCompanyInfos;

    private List<MaintRecInfo> currWorkerInfo;

    Map<String, List<MaintRecInfo>> mCUndefinedListMap;
    Map<String, List<MaintRecInfo>> mCPrepareMentenanceListMap;
    Map<String, List<MaintRecInfo>> mCOverTimeListMap;
    Map<String, List<MaintRecInfo>> mCCompleteListMap;


    List<MaintRecInfo> mUndefinedList1;
    List<MaintRecInfo> mPrepareMentenanceList1;
    List<MaintRecInfo> mOverTimeList1;
    List<MaintRecInfo> mCompleteList1;


    private BaiduMap mMap;

    private boolean isLoaded;

    private boolean infosLoaded;
    private boolean banchsLoaded;
    private boolean workersLoaded;


    @Override
    public String getTitleString() {
        return getString(R.string.EMentenance);
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
        llName.setClickable(false);
    }

    @Override
    protected void preView() {
        super.preView();
        SDKInitializer.initialize(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();

        requestGetBranchInfo();
    }

    private RequestBean getMaintListRequestBean(String userId, String token, String brandchId) {

        GetMaintListRequest request = new GetMaintListRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new GetMaintListBody().setBranchId(brandchId));
        return request;
    }

    private void requestMaintListInfo(String id) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_LIST,
                getMaintListRequestBean(getConfig().getUserId(), getConfig().getToken(), id)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                GetMiantListResponse response = GetMiantListResponse.getMiantListResponse(result);
                mMiantMap = response.getBody();
                //获取到了返回的信息
                if (mMiantMap == null || mMiantMap.size() == 0) {
                    return;
                }

                requestGetWorkerInfo(currBranch.getId());


                mCUndefinedList = new ArrayList<MaintRecInfo>();
                mCPrepareMentenanceList = new ArrayList<MaintRecInfo>();
                mCOverTimeList = new ArrayList<MaintRecInfo>();
                mCCompleteList = new ArrayList<MaintRecInfo>();


                mCOverTimeList = mMiantMap.get("overtime");
                //mPrepareMentenanceList= mMiantMap.get("today");
                fillTodayTime(mMiantMap.get("today"));
                mCUndefinedList = mMiantMap.get("unPlan");


                mUndefinedList = mCUndefinedList;
                mPrepareMentenanceList = mCPrepareMentenanceList;
                mOverTimeList = mCOverTimeList;
                mCompleteList = mCCompleteList;


//                tvUndefine.setText("未制定：" + mUndefinedList.size());
//                tvNoContinue.setText("待维保：" + mPrepareMentenanceList.size());
//                tvOutbound.setText("超期：" + mOverTimeList.size());
//                tvFinished.setText("已完成：" + mCompleteList.size());

                AllSet();
                //tvUndefine.setTextColor(getResources().getColor(R.color.titleblue));

                infosLoaded = true;
                selectDatas("");


                //markAlarm(mUndefinedList, R.drawable.undefinedtask);


            }
        };
        addTask(task);
    }


    private void selectDatas(String id) {

        AllSet();
        if (id.equals("")) {
            tvWorker_name.setText("请选择员工：");
            currWoker = new WorkerInfo2();
        }

        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        int num4 = 0;

//        mUndefinedList = new ArrayList<MaintRecInfo>();
//        mPrepareMentenanceList = new ArrayList<MaintRecInfo>();
//        mOverTimeList = new ArrayList<MaintRecInfo>();
//        mCompleteList = new ArrayList<MaintRecInfo>();

        mCUndefinedListMap = new HashMap<String, List<MaintRecInfo>>();
        mCPrepareMentenanceListMap = new HashMap<String, List<MaintRecInfo>>();
        mCOverTimeListMap = new HashMap<String, List<MaintRecInfo>>();
        mCCompleteListMap = new HashMap<String, List<MaintRecInfo>>();

//        mUndefinedList1 = new ArrayList<MaintRecInfo>();
//        mPrepareMentenanceList1 = new ArrayList<MaintRecInfo>();
//        mOverTimeList1 = new ArrayList<MaintRecInfo>();
//        mCompleteList1 = new ArrayList<MaintRecInfo>();


        for (MaintRecInfo info : mCUndefinedList) {

//            if (info.getUserId().equals(id)) {
//                mUndefinedList.add(info);
//            }
            if (id.equals("")) {

                if (!mCUndefinedListMap.containsKey(info.getCoummunityId())) {
                    List<MaintRecInfo> mUndefinedList1 = new ArrayList<MaintRecInfo>();
                    mUndefinedList1.add(info);
                    num1++;
                    mCUndefinedListMap.put(info.getCoummunityId(), mUndefinedList1);
                } else {
                    num1++;
                    mCUndefinedListMap.get(info.getCoummunityId()).add(info);
                }
            } else {

                if (!mCUndefinedListMap.containsKey(info.getCoummunityId()) && info.getUserId().equals(id)) {
                    List<MaintRecInfo> mUndefinedList1 = new ArrayList<MaintRecInfo>();

                    mUndefinedList1.add(info);
                    num1++;
                    mCUndefinedListMap.put(info.getCoummunityId(), mUndefinedList1);
                } else if (info.getUserId().equals(id)) {
                    mCUndefinedListMap.get(info.getCoummunityId()).add(info);
                    num1++;
                }
            }
        }


        for (MaintRecInfo info : mCOverTimeList) {

//            if (info.getUserId().equals(id)) {
//                mUndefinedList.add(info);
//            }
            if (id.equals("")) {

                if (!mCOverTimeListMap.containsKey(info.getCoummunityId())) {
                    List<MaintRecInfo> mOverTimeList1 = new ArrayList<MaintRecInfo>();
                    mOverTimeList1.add(info);
                    num2++;
                    mCOverTimeListMap.put(info.getCoummunityId(), mOverTimeList1);
                } else {

                    mCOverTimeListMap.get(info.getCoummunityId()).add(info);
                    num2++;

                }
            } else {

                if (!mCOverTimeListMap.containsKey(info.getCoummunityId()) && info.getUserId().equals(id)) {
                    List<MaintRecInfo> mOverTimeList1 = new ArrayList<MaintRecInfo>();
                    mOverTimeList1.add(info);
                    num2++;
                    mCOverTimeListMap.put(info.getCoummunityId(), mOverTimeList1);
                } else if (info.getUserId().equals(id)) {
                    mCOverTimeListMap.get(info.getCoummunityId()).add(info);
                    num2++;
                }
            }
        }

        for (MaintRecInfo info : mCCompleteList) {

//            if (info.getUserId().equals(id)) {
//                mUndefinedList.add(info);
//            }
            if (id.equals("")) {

                if (!mCCompleteListMap.containsKey(info.getCoummunityId())) {
                    List<MaintRecInfo> mCompleteList1 = new ArrayList<MaintRecInfo>();
                    mCompleteList1.add(info);
                    num3++;
                    mCCompleteListMap.put(info.getCoummunityId(), mCompleteList1);
                } else {
                    mCCompleteListMap.get(info.getCoummunityId()).add(info);
                }
            } else {

                if (!mCCompleteListMap.containsKey(info.getCoummunityId()) && info.getUserId().equals(id)) {
                    List<MaintRecInfo> mCompleteList1 = new ArrayList<MaintRecInfo>();
                    mCompleteList1.add(info);
                    num3++;
                    mCCompleteListMap.put(info.getCoummunityId(), mCompleteList1);

                } else if (info.getUserId().equals(id)) {
                    mCCompleteListMap.get(info.getCoummunityId()).add(info);
                    num3++;
                }
            }
        }

        for (MaintRecInfo info : mCPrepareMentenanceList) {

//            if (info.getUserId().equals(id)) {
//                mUndefinedList.add(info);
//            }
            if (id.equals("")) {

                if (!mCPrepareMentenanceListMap.containsKey(info.getCoummunityId())) {
                    List<MaintRecInfo> mPrepareMentenanceList1 = new ArrayList<MaintRecInfo>();
                    mPrepareMentenanceList1.add(info);
                    num4++;
                    mCPrepareMentenanceListMap.put(info.getCoummunityId(), mPrepareMentenanceList1);
                } else {
                    mCPrepareMentenanceListMap.get(info.getCoummunityId()).add(info);
                    num4++;
                }
            } else {

                if (!mCPrepareMentenanceListMap.containsKey(info.getCoummunityId()) && info.getUserId().equals(id)) {
                    List<MaintRecInfo> mPrepareMentenanceList1 = new ArrayList<MaintRecInfo>();
                    mPrepareMentenanceList1.add(info);
                    num4++;
                    mCPrepareMentenanceListMap.put(info.getCoummunityId(), mPrepareMentenanceList1);
                } else if (info.getUserId().equals(id)) {
                    mCPrepareMentenanceListMap.get(info.getCoummunityId()).add(info);
                    num4++;
                }
            }
        }
        isLoaded = true;
        tvUndefine.setText("未制定：" + num1+"");
        tvNoContinue.setText("待维保：" + num4+"");
        tvOutbound.setText("超期：" + num2+"");
        tvFinished.setText("已完成：" + num3+"");


    }


    public void AllSet() {
        mMap.clear();
        tvUndefine.setTextColor(Color.BLACK);
        tvNoContinue.setTextColor(Color.BLACK);
        tvOutbound.setTextColor(Color.BLACK);
        tvFinished.setTextColor(Color.BLACK);
    }

    void fillTodayTime(List<MaintRecInfo> todays) {
        for (MaintRecInfo info : todays) {
            if (info.getMaintTime().equals("")) {
                mCPrepareMentenanceList.add(info);
            } else {
                mCCompleteList.add(info);
            }
        }

    }


    private RequestBean getRequestBean(String userId, String token, String brandchId) {

        GetBranchRequest request = new GetBranchRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new GetBranchBody().setBranchId(brandchId));
        return request;
    }

    private void requestGetBranchInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_DEP_LIST,
                getRequestBean(getConfig().getUserId(), getConfig().getToken(), getConfig().getBranchId())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                GetBranchResponse response = GetBranchResponse.getGetBranchResponse(result);
                mBranchInfo = response.getBody();
                //获取到了返回的信息
                if (mBranchInfo == null || mBranchInfo.size() == 0) {
                    return;
                }
                currBranch = mBranchInfo.get(0);
                tvBranch_name.setText(currBranch.getName());
                banchsLoaded = true;
                requestMaintListInfo(currBranch.getId());

                //selectDatas(currBranch.getId());
            }
        };
        addTask(task);
    }

    /**
     * 标记报警
     */
    private void markAlarm1(final List<MaintRecInfo> info1, int id) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        if (null == info1) {
            return;
        }


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(id);


        final MaintRecInfo info = info1.get(0);
        double latitude = info.getLat();
        double longitude = info.getLng();
        LatLng point = new LatLng(latitude, longitude);
        MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor).position(point);
        Marker locationMarker = (Marker) mMap.addOverlay(markerOption);

        DataTravelUtils.setMiantRecInfoData(info1);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Info", (Serializable) info1);
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
                View view = inflater.inflate(R.layout.mapdetail_item, null);
                TextView textInfo = (TextView) view.findViewById(R.id.tv_detail_info);

                Button forward = (Button) view.findViewById(R.id.bt_forward);
                final Bundle bundle = arg0.getExtraInfo();
                final List<MaintRecInfo> info1 = (List<MaintRecInfo>) bundle.getSerializable("Info");

//                textInfo.setText(DataTravelUtils.getMiantRecInfoData().get(0).getCommunityName() + "(" + DataTravelUtils.getMiantRecInfoData().size() + ")");
//                double latitude = DataTravelUtils.getMiantRecInfoData().get(0).getLat();
//                double longitude = DataTravelUtils.getMiantRecInfoData().get(0).getLng();


                textInfo.setText(info1.get(0).getCommunityName() + "(" + info1.size() + ")");
                double latitude = info1.get(0).getLat();
                double longitude = info1.get(0).getLng();

                LatLng point = new LatLng(latitude, longitude);
                InfoWindow mInfoWindow = new InfoWindow(view, point, 10);
                forward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mMap.hideInfoWindow();
                        Intent intent = new Intent(EMantenanceLookActivity.this, EMentenanceListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Info", (Serializable) info1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                mMap.showInfoWindow(mInfoWindow);
                return false;
            }
        });

    }

    private RequestBean getWorkerRequestBean(String userId, String token, String brandchId) {

        GetWorkerRequest request = new GetWorkerRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new GetWorkerBody().setBranchId(brandchId));
        return request;
    }

    private void requestGetWorkerInfo(String brachId) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_WORKER_LIST,
                getWorkerRequestBean(getConfig().getUserId(), getConfig().getToken(), brachId)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                GetWorkerResponse response = GetWorkerResponse.getWorkerResponse(result);
                mWorkerInfo2 = response.getBody();
                //获取到了返回的信息
                if (mWorkerInfo2 == null || mWorkerInfo2.size() == 0) {
                    return;
                }
                currWoker = mWorkerInfo2.get(0);
                llName.setClickable(true);
                workersLoaded = true;
                //tvWorker_name.setText(currWoker.getName());
                AllSet();
                tvUndefine.setTextColor(getResources().getColor(R.color.titleblue));
                for (Map.Entry<String, List<MaintRecInfo>> entry : mCUndefinedListMap.entrySet()) {
                    markAlarm1(entry.getValue(), R.drawable.undefinedtask);
                }
            }
        };
        addTask(task);
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_company_mantence;
    }


    @OnClick({R.id.tv_look_his, R.id.ll_subcompany, R.id.ll_name, R.id.tv_undefine, R.id.tv_no_continue, R.id.tv_outbound, R.id.tv_finished})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_look_his:
                break;
            case R.id.ll_subcompany:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String[] items = new String[mBranchInfo.size()];
                for (int i = 0; i < mBranchInfo.size(); i++) {
                    items[i] = mBranchInfo.get(i).getName();
                }
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currBranch = mBranchInfo.get(which);
                        tvBranch_name.setText(currBranch.getName());
                        requestGetWorkerInfo(currBranch.getId());
                        requestMaintListInfo(currBranch.getId());
                    }
                });
                builder.show();
                break;
            case R.id.ll_name:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                String[] items1 = new String[mWorkerInfo2.size()];
                for (int i = 0; i < mWorkerInfo2.size(); i++) {
                    items1[i] = mWorkerInfo2.get(i).getName();
                }
                builder1.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currWoker = mWorkerInfo2.get(which);
                        tvWorker_name.setText(currWoker.getName());
                        selectDatas(currWoker.getId());
                        //requestGetWorkerInfo(currBranch.getId());
                    }
                });
                builder1.show();
                break;
            case R.id.tv_undefine:
                AllSet();
                tvUndefine.setTextColor(getResources().getColor(R.color.titleblue));
                for (Map.Entry<String, List<MaintRecInfo>> entry : mCUndefinedListMap.entrySet()) {
                    markAlarm1(entry.getValue(), R.drawable.undefinedtask);
                }
                //markAlarm(mUndefinedList, R.drawable.undefinedtask);
                break;
            case R.id.tv_no_continue:
                AllSet();
                tvNoContinue.setTextColor(getResources().getColor(R.color.titleblue));
                for (Map.Entry<String, List<MaintRecInfo>> entry : mCPrepareMentenanceListMap.entrySet()) {
                    markAlarm1(entry.getValue(), R.drawable.uhandletask);
                }
                //   markAlarm(mPrepareMentenanceList, R.drawable.uhandletask);
                break;
            case R.id.tv_outbound:
                AllSet();
                tvOutbound.setTextColor(getResources().getColor(R.color.titleblue));
                for (Map.Entry<String, List<MaintRecInfo>> entry : mCOverTimeListMap.entrySet()) {
                    markAlarm1(entry.getValue(), R.drawable.overtimetask);
                }
                //  markAlarm(mOverTimeList, R.drawable.overtimetask);
                break;
            case R.id.tv_finished:
                AllSet();
                tvFinished.setTextColor(getResources().getColor(R.color.titleblue));
                for (Map.Entry<String, List<MaintRecInfo>> entry : mCCompleteListMap.entrySet()) {
                    markAlarm1(entry.getValue(), R.drawable.deattask);
                }
                //  markAlarm(mCompleteList, R.drawable.deattask);
                break;
        }
    }
}
