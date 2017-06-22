package com.honyum.elevatorMan.activity.company;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.mydata.NHorderAndTask;
import com.honyum.elevatorMan.net.NHMentenanceCompanyResponse;
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

public class NHMentenanceActivity extends BaseActivityWraper {

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
    int currIndex = 1;

    Map<String, List<NHorderAndTask>> mComapnyMentenanceInfo;


    private BaiduMap mMap;
    private List<NHorderAndTask> mpreDealComapnyMentenanceInfo;
    private List<NHorderAndTask> mSavingComapnyMentenanceInfo;
    private List<NHorderAndTask> mCompletedComapnyMentenanceInfo;
    private List<NHorderAndTask> mCanceledComapnyMentenanceInfo;

    @Override
    public String getTitleString() {
        return "怡墅维保";
    }

    @Override
    protected void initView() {
        mMap = baiduMap.getMap();
    }

    @Override
    protected void preView() {
        super.preView();
        SDKInitializer.initialize(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNHMentenanceListInfo();

    }

    private RequestBean getRequestBean(String userId, String token, String brandchId) {

        NHMentenanceRequest request = new NHMentenanceRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new NHMentenanceBody().setBranchId(brandchId));
        return request;
    }

    private void requestNHMentenanceListInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_COMPANY_NHMENTENANCE,
                getRequestBean(getConfig().getUserId(), getConfig().getToken(), getConfig().getBranchId())) {
            @Override
            protected void onResponse(NetTask task, String result) {

                NHMentenanceCompanyResponse response = NHMentenanceCompanyResponse.getNHMentenanceCompanyResponse(result);
                Log.e("INFO11", result);
                mComapnyMentenanceInfo = response.getBody();
                //获取到了返回的信息
                if (mComapnyMentenanceInfo == null || mComapnyMentenanceInfo.size() == 0) {
                    return;
                }

                groupbyState();

                AllSet();
                tvNoDeal.setTextColor(getResources().getColor(R.color.titleblue));
                markMap(mpreDealComapnyMentenanceInfo, R.drawable.undefinedtask);
            }
        };
        addTask(task);
    }

    private void groupbyState() {
        mpreDealComapnyMentenanceInfo = new ArrayList<NHorderAndTask>();
        mCompletedComapnyMentenanceInfo = new ArrayList<NHorderAndTask>();
        mSavingComapnyMentenanceInfo = new ArrayList<NHorderAndTask>();
        mCanceledComapnyMentenanceInfo = new ArrayList<NHorderAndTask>();

        mSavingComapnyMentenanceInfo = mComapnyMentenanceInfo.get("chuLiZhong");
        mpreDealComapnyMentenanceInfo = mComapnyMentenanceInfo.get("weiChuLi");
        mCanceledComapnyMentenanceInfo = mComapnyMentenanceInfo.get("yiChaoQi");
        mCompletedComapnyMentenanceInfo = mComapnyMentenanceInfo.get("yiChuLi");


        tvNoDeal.setText("未处理：" + mpreDealComapnyMentenanceInfo.size());
        tvDealing.setText("处理中：" + mSavingComapnyMentenanceInfo.size());
        tvDealt.setText("已处理：" + mCompletedComapnyMentenanceInfo.size());
        tvOutbound.setText("已过期：" + mCanceledComapnyMentenanceInfo.size());


    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_company_nicehouse_mentenance;
    }

    /**
     * 标记报警
     */
    private void markMap(final List<NHorderAndTask> alarmInfo1, int id) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        if (null == alarmInfo1) {
            return;
        }


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(id);

        for (final NHorderAndTask info : alarmInfo1) {
            double latitude;
            double longitude;

            if (currIndex == 1) {
                latitude = info.getVillaInfo().getLat();
                longitude = info.getVillaInfo().getLng();
            } else {
                latitude = info.getMaintOrderInfo().getVillaInfo().getLat();
                longitude = info.getMaintOrderInfo().getVillaInfo().getLng();
            }
            LatLng point = new LatLng(latitude, longitude);
            MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor).position(point);
            Marker locationMarker = (Marker) mMap.addOverlay(markerOption);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Info", info);
            locationMarker.setExtraInfo(bundle);

            mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View view = inflater.inflate(R.layout.mapdetail_item, null);
                    TextView textInfo = (TextView) view.findViewById(R.id.tv_detail_info);
                    final Bundle bundle = arg0.getExtraInfo();
                    final NHorderAndTask info1 = (NHorderAndTask) bundle.getSerializable("Info");
                    Button forward = (Button) view.findViewById(R.id.bt_forward);

                    if (currIndex == 1) {
                        textInfo.setText(info1.getVillaInfo().getCellName());
                    } else {
                        textInfo.setText(info1.getMaintOrderInfo().getVillaInfo().getCellName());
                    }
                    double latitude;
                    double longitude;

                    if (currIndex == 1) {
                        latitude = info1.getVillaInfo().getLat();
                        longitude = info1.getVillaInfo().getLng();
                    } else {
                        latitude = info1.getMaintOrderInfo().getVillaInfo().getLat();
                        longitude = info1.getMaintOrderInfo().getVillaInfo().getLng();
                    }
                    LatLng point = new LatLng(latitude, longitude);
                    InfoWindow mInfoWindow = new InfoWindow(view, point, 10);


                    forward.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mMap.hideInfoWindow();
                            if(currIndex==1) {
                                Intent intent = new Intent(NHMentenanceActivity.this, NHMentenanceOrderDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Info", info1);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(NHMentenanceActivity.this, NFMentenanceTaskDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Info", info1);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
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

    @OnClick({R.id.tv_look_his, R.id.tv_no_deal, R.id.tv_dealing, R.id.tv_dealt, R.id.tv_outbound})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_look_his:
                startActivity(new Intent(this, NHMentenanceOrderListActivity.class));
                break;
            case R.id.tv_no_deal:
                AllSet();
                tvNoDeal.setTextColor(getResources().getColor(R.color.titleblue));
                currIndex = 1;
                markMap(mpreDealComapnyMentenanceInfo, R.drawable.undefinedtask);
                break;
            case R.id.tv_dealing:
                AllSet();
                tvDealing.setTextColor(getResources().getColor(R.color.titleblue));
                currIndex = 2;
                markMap(mSavingComapnyMentenanceInfo, R.drawable.dealingtask);
                break;
            case R.id.tv_dealt:
                AllSet();
                tvDealt.setTextColor(getResources().getColor(R.color.titleblue));
                currIndex = 2;
                markMap(mCompletedComapnyMentenanceInfo, R.drawable.deattask);
                break;
            case R.id.tv_outbound:
                AllSet();
                tvOutbound.setTextColor(getResources().getColor(R.color.titleblue));
                currIndex = 2;
                markMap(mCanceledComapnyMentenanceInfo, R.drawable.overtimetask);
                break;
        }
    }
}
