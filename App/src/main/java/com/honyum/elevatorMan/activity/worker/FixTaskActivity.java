package com.honyum.elevatorMan.activity.worker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.maintenance.MaintenancePlanAddActivity;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.data.FixTaskInfo;
import com.honyum.elevatorMan.data.MaintenanceServiceInfo;
import com.honyum.elevatorMan.net.FixWorkArriveRequest;
import com.honyum.elevatorMan.net.FixWorkStartRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;

import static com.honyum.elevatorMan.net.base.NetConstant.ARRIVE_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.FIX_ARRIVED;
import static com.honyum.elevatorMan.net.base.NetConstant.FIX_BEFORE_START;
import static com.honyum.elevatorMan.net.base.NetConstant.FIX_FIX_FINISH;
import static com.honyum.elevatorMan.net.base.NetConstant.FIX_LOOK_FINISHED;
import static com.honyum.elevatorMan.net.base.NetConstant.FIX_STARTED;

/**
 * Created by Star on 2017/6/12.
 */

public class FixTaskActivity extends BaseActivityWraper {
    private TextView tv_look_eva;
    private FixTaskInfo mFixTaskInfo;
    private FixInfo mFixInfo;
    private TextView tv_look_result;
    private TextView tv_index;
    private MapView baiduMap;
    private BaiduMap mMap;
    private Marker locationMarker;

    /**
     * 标记地图
     */
    private void markMap(FixTaskInfo alarmInfo1) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        if (null == alarmInfo1) {
            return;
        }


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.marker_worker);


        double latitude = alarmInfo1.getRepairOrderInfo().getVillaInfo().getLat();
        double longitude = alarmInfo1.getRepairOrderInfo().getVillaInfo().getLng();
        LatLng point = new LatLng(latitude, longitude);
        MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor).position(point);
        locationMarker = (Marker) mMap.addOverlay(markerOption);

    }
    @Override
    protected void preView() {
        super.preView();
        SDKInitializer.initialize(getApplicationContext());
    }

    @Override
    public String getTitleString() {
        return getString(R.string.curr_task);
    }

    @Override
    protected void initView() {
        mFixTaskInfo = getIntent("Info");
        mFixInfo = getIntent("Fixdata");
        tv_index = findView(R.id.tv_index);
        tv_look_result = findView(R.id.tv_look_result);

        baiduMap = findView(R.id.baidu_map);
        mMap = baiduMap.getMap();
        tv_look_eva = findView(R.id.tv_look_eva);
        changeBottomButton(mFixTaskInfo.getState());
        markMap(mFixTaskInfo);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_fix_task;
    }

    //当前状态，待确定，点击开始
            private RequestBean getRequestBean(String userId, String token) {

                FixWorkStartRequest request = new FixWorkStartRequest();
                request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
                request.setBody(request.new FixWorkStartBody().setRepairOrderProcessId(mFixTaskInfo.getId()));
                return request;
            }

            private void requestFixWorkStartInfo() {
                NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_FIX_START,
                        getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
                    @Override
                    protected void onResponse(NetTask task, String result) {
                        Response response = Response.getResponse(result);
                        if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                            showAppToast(getString(R.string.sucess));
                            changeBottomButton(FIX_STARTED);
                        }
                    }
        };
        addTask(task);
    }

    //当前状态，已出发，点击到达
    private RequestBean getArriveRequestBean(String userId, String token) {

        FixWorkArriveRequest request = new FixWorkArriveRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new FixWorkArriveBody().setRepairOrderProcessId(mFixTaskInfo.getId()));
        return request;
    }

    private void requestFixWorkArriveInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_FIX_ARRIVE,
                getArriveRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showSimpleToast(getString(R.string.sucess));
                    changeBottomButton(FIX_ARRIVED);
                }
            }
        };
        addTask(task);
    }




    //切换维修状态
    public void changeBottomButton(String state)
    {

        switch (state) {
            case FIX_BEFORE_START:
                tv_index.setVisibility(View.VISIBLE);
                tv_look_eva.setVisibility(View.GONE);
                tv_look_result.setVisibility(View.GONE);
                tv_index.setText(R.string.before_start);
                tv_index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(FixTaskActivity.this).setTitle("确认出发吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestFixWorkStartInfo();
                                    }
                                })
                                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();


                    }
                });
                break;
            case FIX_STARTED:
                tv_index.setVisibility(View.VISIBLE);
                tv_index.setText("到达");
                tv_look_eva.setVisibility(View.GONE);
                tv_look_result.setVisibility(View.GONE);
                tv_index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(FixTaskActivity.this).setTitle("确认到达吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestFixWorkArriveInfo();
                                    }
                                })
                                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();

                    }
                });
                break;
            case FIX_ARRIVED:
                tv_index.setVisibility(View.VISIBLE);
                tv_index.setText(R.string.fix_look_finish);
                tv_look_eva.setVisibility(View.GONE);
                tv_look_result.setVisibility(View.GONE);
                tv_index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(FixTaskActivity.this).setTitle("确认继续吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(FixTaskActivity.this, FixTaskFinishActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("Info",mFixTaskInfo);
                                        bundle.putSerializable("Fixdata",mFixInfo);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();

                    }
                });
                break;
            case FIX_LOOK_FINISHED:
                tv_look_eva.setVisibility(View.GONE);
                tv_look_result.setVisibility(View.GONE);
                tv_index.setVisibility(View.VISIBLE);
                tv_index.setText(R.string.fix_finish);
                tv_index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(FixTaskActivity.this).setTitle("确认完成吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(FixTaskActivity.this, FixTaskFinishActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("Info",mFixTaskInfo);
                                        bundle.putSerializable("Fixdata",mFixInfo);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();

                    }
                });
                break;
            case FIX_FIX_FINISH:
                tv_index.setVisibility(View.GONE);
                tv_look_result.setVisibility(View.VISIBLE);
                tv_look_result.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FixTaskActivity.this, FixResultLookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Info",mFixTaskInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                tv_look_eva.setVisibility(View.GONE);
                tv_look_eva.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FixTaskActivity.this, FixEvaLookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Info",mFixInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                });
                break;
        }
    }
}
