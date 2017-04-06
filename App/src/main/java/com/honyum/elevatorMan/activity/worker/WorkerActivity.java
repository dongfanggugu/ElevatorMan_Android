package com.honyum.elevatorMan.activity.worker;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.ChatActivity;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.data.ContactList;
import com.honyum.elevatorMan.net.AcceptAlarmReqBody;
import com.honyum.elevatorMan.net.AcceptAlarmRequest;
import com.honyum.elevatorMan.net.AlarmInfoRequest;
import com.honyum.elevatorMan.net.AlarmInfoRequest.AlarmInfoReqBody;
import com.honyum.elevatorMan.net.AlarmInfoResponse;
import com.honyum.elevatorMan.net.ReportExceptRequest;
import com.honyum.elevatorMan.net.ReportExceptRequest.ReportExceptReqBody;
import com.honyum.elevatorMan.net.ReportStateRequest;
import com.honyum.elevatorMan.net.ReportStateRequest.ReportStateReqBody;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.receiver.LocationReceiver;
import com.honyum.elevatorMan.receiver.LocationReceiver.ILocationComplete;
import com.honyum.elevatorMan.service.LocationService;
import com.honyum.elevatorMan.utils.Utils;
import com.honyum.elevatorMan.view.CircleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorkerActivity extends WorkerBaseActivity implements
        ILocationComplete {

    private LocationReceiver mLocationReceiver;

    private static boolean isForeground = false;

    private boolean hasAlarm = false;


    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private Marker centerMarker = null;
    private Marker workerMarker = null;

    private boolean isCenter = true;

    //当前处理的报警事件
    private AlarmInfo mAlarmInfo = null;

    private String mCurrentAdd = "";


    /**
     * 页面浮动信息标记
     */
    //没有报警任务
    private static final int TYPE_NO_TASK = 0;


    //接收到报警任务
    private static final int TYPE_RECEIVED = 1;

    //被指派处理报警信息，已出发
    private static final int TYPE_ASSIGNED = 2;


    private ListView listView;


    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_worker);

        //  copyKnowledge();
        Intent intent = this.getIntent();
        initTitleBar();

        initMapInfo();

        initListView();

        Intent sIntent = new Intent(this, LocationService.class);
        startService(sIntent);

        //初始化定位按钮
        initLocationBtn();

        if (intent != null) {
            dispatchReceivedMsg(intent);
        }
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.listView);

        TextView textView = new TextView(this);
        textView.setText("暂无合作伙伴!");
        textView.setGravity(Gravity.CENTER);
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, this.getResources().getDisplayMetrics());
        textView.setPadding(dp, dp, dp, dp);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ((ViewGroup) listView.getParent()).addView(textView);
        listView.setEmptyView(textView);
    }

    /**
     * 当有没有处理完成的报警时，提示用户
     */
    private void showUnfinshedMessage() {
        if (hasAlarm) {
            popMsgAlertWithoutCancel("你有未处理的报警，请到报警列表进行处理!", null);
        }
    }

    /**
     * 初始化地图信息
     */
    private void initMapInfo() {
        mMapView = (MapView) findViewById(R.id.baidu_map);
        mBaiduMap = mMapView.getMap();

//        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                setFloatView(TYPE_NO_TASK, null);
//                //mBaiduMap.hideInfoWindow();
//            }
//
//            @Override
//            public boolean onMapPoiClick(MapPoi mapPoi) {
//                return false;
//            }
//        });

        // 点击标记时显示信息
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                // TODO Auto-generated method stub

                if (marker == workerMarker) {
                    TextView tvInfo = new TextView(WorkerActivity.this);
                    tvInfo.setBackgroundResource(R.drawable.info_bac);
                    tvInfo.setPadding(10, 10, 10, 30);
                    tvInfo.setGravity(Gravity.CENTER_HORIZONTAL);

                    if (StringUtils.isEmpty(mCurrentAdd)) {
                        mCurrentAdd = getString(R.string.add_null);
                    }
                    tvInfo.setText(mCurrentAdd);

                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(tvInfo);

                    InfoWindow infoWindow = new InfoWindow(bitmapDescriptor, marker.getPosition(), 0,
                            new InfoWindow.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick() {
                                    mBaiduMap.hideInfoWindow();
                                }
                            });
                    mBaiduMap.showInfoWindow(infoWindow);
                    return true;
                }

//                if (null == mAlarmInfo) {
//                    return true;
//                }
//
//                if (marker ==  centerMarker) {
//                    if (TYPE_RECEIVED == mWorkerState) {
//                        setFloatView(TYPE_RECEIVED, mAlarmInfo);
//                    } else if (TYPE_ASSIGNED == mWorkerState) {
//                        setFloatView(TYPE_ASSIGNED, mAlarmInfo);
//                    }
//                }
                return true;
            }

        });


        //去掉百度地图的下方的标签信息
        int count = mMapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mMapView.getChildAt(i);

            //去掉比例尺，百度logo，
            if (child instanceof ZoomControls || child instanceof ImageView
                    || child instanceof RelativeLayout) {
                child.setVisibility(View.INVISIBLE);
            }
        }

    }

    /**
     * 定位完成，更新地图位置
     */
    @Override
    public void onLocationComplete(double latitude, double longitude,
                                   String address) {
        // TODO Auto-generated method stub

        //清除地图的弹出框信息
        mBaiduMap.hideInfoWindow();

        mCurrentAdd = address;
        // 标记位置
        markWorkerLocation(latitude, longitude);
        updateDistance();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            mMapView.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mMapView.onPause();

        isForeground = false;

        unRegisterReceiver();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mMapView.onResume();
        isForeground = true;

        // 注册定位接收器
        this.registerReceiver();
    }

    /**
     * 返回marker的基本设置
     */
    private OverlayOptions initOptions(boolean focus) {
        View view = View.inflate(this, R.layout.layout_location_marker, null);

//        //获取marker高度
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker_worker);
//        markerHeight = bitmap.getHeight();

        ImageView imgMarker = (ImageView) view.findViewById(R.id.img_marker);
        imgMarker.setImageResource(R.drawable.marker_worker);

        if (focus) {
            imgMarker.setImageResource(R.drawable.marker_alarm);
        }

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromView(view);
        return new MarkerOptions().icon(bitmapDescriptor).zIndex(9)
                .draggable(false);
    }

    /**
     * 根据经纬度标记地图的位置
     *
     * @param latitude
     * @param longitude
     */
    private void markLocation(double latitude, double longitude, AlarmInfo alarmInfo, int type) {

        //mBaiduMap.hideInfoWindow();
        if (null == mBaiduMap) {
            return;
        }

        LatLng point = new LatLng(latitude, longitude);

        // 中心平移到新的位置
        if (isCenter) {
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
            mBaiduMap.animateMapStatus(update);
            isCenter = false;
        }

        if (alarmInfo != null) {
            if (centerMarker != null) {
                //centerMarker.remove();
                centerMarker.setPosition(point);
            } else {
                MarkerOptions markerOption = (MarkerOptions) initOptions(true);
                markerOption.position(point);
                centerMarker = (Marker) mBaiduMap.addOverlay(markerOption);
            }

            //标记报警信息
            setFloatView(type, alarmInfo);

        } else {
            if (workerMarker != null) {
                workerMarker.setPosition(point);
            } else {
                MarkerOptions markerOption = (MarkerOptions) initOptions(false);
                markerOption.position(point);
                workerMarker = (Marker) mBaiduMap.addOverlay(markerOption);
            }
        }
    }

    /**
     * 标记维修工位置
     *
     * @param latitude
     * @param longitude
     */
    private void markWorkerLocation(double latitude, double longitude) {
        markLocation(latitude, longitude, null, 0);
    }

    /**
     * 标记报警位置
     *
     * @param alarmInfo
     * @param state
     */
    private void markAlarmLocation(AlarmInfo alarmInfo, int state) {
        double latitude = Utils.getDouble(alarmInfo.getCommunityInfo().getLat());
        double longitude = Utils.getDouble(alarmInfo.getCommunityInfo().getLng());
        markLocation(latitude, longitude, alarmInfo, state);
    }

    /**
     * 注册定位信息接收器
     */
    private void registerReceiver() {

        // 注册定位信息接收器
        mLocationReceiver = new LocationReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_LOCATION_COMPLETE);
        registerReceiver(mLocationReceiver, filter);

//        //注册应用内部推送消息接收器
//        mForeMsgReceiver = new ForeMsgReceiver(this);
//        IntentFilter foreFilter = new IntentFilter();
//        foreFilter.addAction(addForeAction(Constant.ACTION_ALARM_ASSIGNED));
//        foreFilter.addAction(addForeAction(Constant.ACTION_ALARM_UNASSIGNED));
//        registerReceiver(mForeMsgReceiver, foreFilter);
    }

    /**
     * 解除消息接收器
     */
    private void unRegisterReceiver() {
        if (mLocationReceiver != null) {
            unregisterReceiver(mLocationReceiver);
            mLocationReceiver = null;
        }

//        if (mForeMsgReceiver != null) {
//            unregisterReceiver(mForeMsgReceiver);
//            mForeMsgReceiver = null;
//        }
    }

    /**
     * 接收到报警信息
     *
     * @param alarmId
     */
    private void receivedAlarm(final String alarmId) {
        requestAlarmInfo(alarmId, new IRequestAlarmInfoCallback() {
            @Override
            public void onGetAlarmInfo(AlarmInfo alarmInfo) {
                String state = alarmInfo.getState();

                //当报警接单过期后，不允许再接受报警
                if (null == state || state.equals("0")) {
                    markLocation(alarmInfo, TYPE_RECEIVED);
                } else {
                    popMsgAlertWithoutCancel("报警已经过期，不能再接单，感谢您的参与!", new IConfirmCallback() {
                        @Override
                        public void onConfirm() {
                            finish();
                        }
                    });
                }
            }
        });
    }

    /**
     * 标记报警位置和维修工当前位置
     *
     * @param alarmInfo
     */
    private void markLocation(AlarmInfo alarmInfo, int state) {
        isCenter = true;
        markAlarmLocation(alarmInfo, state);
    }

    /**
     * 接收到指派信息
     *
     * @param alarmId
     */
    private void receivedAssigned(final String alarmId) {
        requestAlarmInfo(alarmId, new IRequestAlarmInfoCallback() {
            @Override
            public void onGetAlarmInfo(final AlarmInfo alarmInfo) {
                //根据配置确认显示视频接口
                if (getConfig().getVideoEnable()) {

                    View videoView = findViewById(R.id.iv_video);
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startLiftVideo(alarmInfo.getElevatorInfo().getId());
                        }
                    });
                }
                if (alarmInfo.getUserState().equals("1")) {

                    //标记维修工位置
                    markLocation(alarmInfo, TYPE_ASSIGNED);
                } else {
                    popMsgAlertWithoutCancel("该报警状态已经过期，请到报警列表查看此报警的新状态!", new IConfirmCallback() {
                        @Override
                        public void onConfirm() {
                            finish();
                        }
                    });
                }
            }
        });
    }

    /**
     * 未指派任务
     *
     * @param
     */
    private void receivedUnAssigned() {
        popMsgAlertWithoutCancel("您未被指派处理此次救援，感谢你的积极参与!", new IConfirmCallback() {
            @Override
            public void onConfirm() {
                finish();
            }
        });
//		setFloatView(TYPE_NO_TASK, null);
//        //未被指派时，地图上的报警地点不再显示
//        if (centerMarker != null) {
//            centerMarker.remove();
//        }
    }

    /**
     * 报警任务被取消
     */
    private void receivedAlarmCancel() {

        popMsgAlertWithoutCancel("此次报警任务已经取消，感谢你的积极参与!", new IConfirmCallback() {
            @Override
            public void onConfirm() {
                finish();
            }
        });
//        setFloatView(TYPE_NO_TASK, null);
//
//        //撤销报警后，地图上报警地点不再显示
//        if (centerMarker != null) {
//            centerMarker.remove();
//        }
    }

    /**
     * 设置底部信息的显示情况， type = 0， 不展示 type = 1， 展示报警相关信息 type = 2， 展示去往现场的相关信息
     *
     * @param type
     */
//	private void setBottom(int type, AlarmInfo alarmInfo) {
//
//		LinearLayout llAlarm = (LinearLayout) findViewById(R.id.ll_alarm);
//
//		LinearLayout llProcess = (LinearLayout) findViewById(R.id.ll_process);
//
//		TextView btnAccept = (TextView) findViewById(R.id.btn_accept);
//		TextView btnCancel = (TextView) findViewById(R.id.btn_cancel);
//
//		TextView btnArrive = (TextView) findViewById(R.id.btn_arrive);
//		TextView btnExcept = (TextView) findViewById(R.id.btn_except);
//
//		// 将报警事件id绑定到按钮
//		if (alarmInfo != null) {
//			btnAccept.setTag(alarmInfo);
//			btnCancel.setTag(alarmInfo);
//
//            btnArrive.setTag(alarmInfo);
//			btnExcept.setTag(alarmInfo);
//		}
//
//		btnAccept.setOnClickListener(bottomClickListener);
//		btnCancel.setOnClickListener(bottomClickListener);
//		btnArrive.setOnClickListener(bottomClickListener);
//        btnExcept.setOnClickListener(bottomClickListener);
//
//		switch (type) {
//		case 0:
//			llAlarm.setVisibility(View.GONE);
//			llProcess.setVisibility(View.GONE);
//			break;
//		case 1:
//			llAlarm.setVisibility(View.VISIBLE);
//            llProcess.setVisibility(View.GONE);
//			break;
//		case 2:
//			llAlarm.setVisibility(View.GONE);
//            llProcess.setVisibility(View.VISIBLE);
//			break;
//		}
//	}

    /**
     * 设置顶部信息的展示情况 type = 0， 不展示 type = 1， 展示报警相关信息 type = 2， 展示分配过程提示
     *
     * @param type
     */
    private void setTopInfo(int type, final AlarmInfo info) {
        //LinearLayout llTip = (LinearLayout) findViewById(R.id.ll_tip);
        LinearLayout llInfo = (LinearLayout) findViewById(R.id.ll_info);

        if (info != null) {
            ((TextView) findViewById(R.id.alarm_time)).setText(info.getAlarmTime());
            ((TextView) findViewById(R.id.tv_project)).setText(info.getCommunityInfo().getName());
            ((TextView) findViewById(R.id.tv_telephone)).setText(info.getCommunityInfo().getPropertyUtel());

            if ("1".equals(info.getType())) {
                ((TextView) findViewById(R.id.tv_address)).setText(info.getCommunityInfo().getAddress()
                        + info.getElevatorInfo().getBuildingCode()
                        + "楼" + info.getElevatorInfo().getUnitCode() + "单元"
                        + info.getElevatorInfo().getLiftNum());
            } else {
                ((TextView) findViewById(R.id.tv_address)).setText(info.getCommunityInfo().getAddress());
            }

            if ("1".equals(info.getType())) {
                ((TextView) findViewById(R.id.tv_elevatorInfo)).setText("电梯信息:品牌-" + info.getElevatorInfo().getBrand()
                        + " 梯型-" + info.getElevatorInfo().getElevatorType()
                        + " 救援码-" + info.getElevatorInfo().getNumber());
            } else {
                ((TextView) findViewById(R.id.tv_elevatorInfo)).setText("电梯信息:暂无电梯信息");
            }

            findViewById(R.id.img_phone).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = info.getCommunityInfo().getPropertyUtel();
                    if (StringUtils.isEmpty(phone) || phone.equals(getString(R.string.default_text))) {
                        showToast("电话号码无效");
                        return;
                    }

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
            });
            TextView tvAccept = (TextView) findViewById(R.id.tv_accept);
            TextView tvRefuse = (TextView) findViewById(R.id.tv_refuse);
            if (null == info.getState() || info.getUserState().equals("0")) {
                tvAccept.setText("确定");
                tvRefuse.setText("取消");
            } else if (info.getUserState().equals("1")) {
                tvAccept.setText("顺利到达");
                tvRefuse.setText("无法到达");
            }
            tvAccept.setTag(info);
            tvRefuse.setTag(info);
            tvAccept.setOnClickListener(bottomClickListener);
            tvRefuse.setOnClickListener(bottomClickListener);
            if (centerMarker != null && workerMarker != null) {
                LatLng center = centerMarker.getPosition();
                LatLng worker = workerMarker.getPosition();
                int distance = (int) DistanceUtil.getDistance(center, worker);
                ((TextView) findViewById(R.id.tv_distance)).setText("" + distance + " m");
            }

            findViewById(R.id.tv_navigation).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (centerMarker == null || workerMarker == null) {
                        showToast("请等待等位结果...");
                        return;
                    }

                    LatLng worker = workerMarker.getPosition();
                    LatLng center = centerMarker.getPosition();

                    jumpThirdApp(center.latitude, center.longitude);
                }
            });
        }

        // 设置提示的字体加粗
        //TextView tvTip = (TextView) findViewById(R.id.tv_tip);
        //tvTip.setTypeface(Typeface.DEFAULT_BOLD);

        switch (type) {
            case 0:
                //llTip.setVisibility(View.GONE);
                llInfo.setVisibility(View.GONE);
                break;
            case 1:
                //llTip.setVisibility(View.VISIBLE);
                llInfo.setVisibility(View.VISIBLE);
//			((TextView) findViewById(R.id.tv_tip))
//					.setText(getString(R.string.has_alarm_msg));
                break;
            case 2:
                //llTip.setVisibility(View.GONE);
                llInfo.setVisibility(View.VISIBLE);
//			((TextView) findViewById(R.id.tv_tip))
//					.setText(getString(R.string.wait_msg));
                break;
        }

    }


    /**
     * 跳转第三方地图应用
     */
    private void jumpThirdApp(double eLat, double eLng) {
        String destination = getIntent().getStringExtra("destination");
        Log.d("TAG", "目的地===>>>" + destination);
        Intent intent;

        if (0 == eLat || 0 == eLng) {
            return;
        }

        if (isAvilible(this, "com.baidu.BaiduMap")) {//传入指定应用包名

            intent = new Intent();
            intent.setData(Uri.parse("baidumap://map/navi?location=" + eLat + "," + eLng + "&query=" + destination));
            startActivity(intent); //启动调用

        } else if (isAvilible(this, "com.autonavi.minimap")) {

            LatLng gcj = Utils.bd2Gcj(eLat, eLng);

            intent = new Intent();
            intent.setData(Uri.parse("androidamap://navi?sourceApplication=ConcreteCloud&poiname="
                    + destination + "&lat=" + gcj.latitude + "&lon=" + gcj.longitude + "&dev=1&style=0"));
            startActivity(intent);

        } else if (isAvilible(this, "com.tencent.map")) {

            //http://apis.map.qq.com/uri/v1/routeplan?type=bus&from=我的家&fromcoord=39.980683,116.302&to=中关村&tocoord=39.9836,116.3164&policy=1&referer=myapp
            LatLng gcj = Utils.bd2Gcj(eLat, eLng);

            intent = new Intent();
            intent.setData(Uri.parse("qqmap://map/routeplan?type=drive&to="
                    + destination + "&tocoord=" + gcj.latitude + "," + gcj.longitude + "&referer=ConcreteCloud"));
            startActivity(intent);

        } else {//未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
//            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
            Toast.makeText(this, "当前设备未安装任何地图应用,请前往应用商店自行下载!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName 应用包名
     * @return true|false
     */
    private boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    /**
     * 底部按钮监听
     */
    private OnClickListener bottomClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            AlarmInfo alarmInfo = (AlarmInfo) v.getTag();
            switch (v.getId()) {
                case R.id.tv_accept:
                    if (null == alarmInfo.getState() || alarmInfo.getState().equals("0")) {
                        acceptAlarm(alarmInfo);
                    } else if (alarmInfo.getUserState().equals("1")) {
                        reportState(Constant.ALARM_STATE_ARRIVED, alarmInfo);
                    }
                    break;
                case R.id.tv_refuse:
                    if (null == alarmInfo.getState() || alarmInfo.getState().equals("0")) {
                        popMsgAlertWithoutCancel("您已经拒绝了处理此次应急救援报警，感谢您的参与!", new IConfirmCallback() {
                            @Override
                            public void onConfirm() {
                                finish();
                            }
                        });
                    } else if (alarmInfo.getUserState().equals("1")) {
                        popExceptWindow(alarmInfo);
                    }
                    break;
//			case R.id.btn_arrive:
//				reportState(Constant.ALARM_STATE_ARRIVED, alarmInfo);
//				break;
//			case R.id.btn_except:
//				popExceptWindow(alarmInfo);
//				break;
            }
        }

    };

    /**
     * 接受报警信息
     */
    private void acceptAlarm(final AlarmInfo alarmInfo) {

        //通过地图的标记获取报警电梯的地址
        if (null == centerMarker || null == workerMarker) {
            return;
        }
        LatLng center = centerMarker.getPosition();
        LatLng worker = workerMarker.getPosition();
        int distance = (int) DistanceUtil.getDistance(center, worker);

        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_ACCEPT_ALARM,
                getAcceptAlarmRequest(alarmInfo.getId(), distance)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub
                //modify the state of the alarm in the sqlite
                //AlarmSqliteUtils.acceptAlarm(WorkerActivity.this, alarmInfo.getId());
                //setFloatView(TYPE_NO_TASK, null);
                popWaitingMsg();
            }

            @Override
            protected void onFailed(NetTask task, String errorCode, String errorMsg) {
                //super.onFailed(task, errorCode, errorMsg);
                setFloatView(TYPE_NO_TASK, null);
                showToast("报警任务已经取消，感谢您的参与");
            }
        };
        addTask(netTask);

    }

    /**
     * 不接受报警信息
     */
    private void cancelAlarm() {
        setFloatView(TYPE_NO_TASK, null);
    }

    /**
     * 维修工到达现场
     *
     * @param state
     */
    private void reportState(final String state, final AlarmInfo alarmInfo) {
        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_REPORT_STATE,
                getReportStateRequest(state, alarmInfo.getId())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub
                if (state.equals(Constant.ALARM_STATE_ARRIVED)) {        //已到达
                    Intent intent = new Intent(WorkerActivity.this,
                            RescuProcessActivity.class);
                    intent.putExtra("alarm_id", alarmInfo.getId());

                    intent.putExtra("alarm_info", mAlarmInfo);

                    startActivity(intent);
                }
            }

            @Override
            protected void onFailed(NetTask task, String errorCode, String errorMsg) {
                //super.onFailed(task, errorCode, errorMsg);
                //setFloatView(TYPE_NO_TASK, null);
                showToast(errorMsg);
            }
        };
        addTask(netTask);
    }

    /**
     * 意外情况的监听
     */
    private void popExceptWindow(AlarmInfo alarmInfo) {
        View view = View.inflate(this, R.layout.layout_except, null);
        Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        initExceptView(view, dialog, alarmInfo.getId());
        dialog.show();
    }

    /**
     * 初始化意外情况弹出框
     *
     * @param view
     * @param dialog
     */
    private void initExceptView(final View view, final AlertDialog dialog, final String alarmId) {

        OnClickListener exceptClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                switch (v.getId()) {
                    case R.id.btn_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.btn_confirm:
                        EditText etException = (EditText) view.findViewById(R.id.et_exception);
                        String remark = etException.getText().toString();
                        if (StringUtils.isEmpty(remark)) {
                            showToast("请填写无法到达的理由!");
                            return;
                        }
                        dialog.dismiss();
                        reportExcept(alarmId, remark);
                }
            }

        };

        // 添加按钮的监听
        view.findViewById(R.id.btn_cancel).setOnClickListener(
                exceptClickListener);
        view.findViewById(R.id.btn_confirm).setOnClickListener(
                exceptClickListener);
    }


    /**
     * 显示接收报警成功弹框
     */
    private void popWaitingMsg() {
        popMsgAlertWithoutCancel(getString(R.string.wait_msg), new IConfirmCallback() {
            @Override
            public void onConfirm() {
                finish();
            }
        }, "确定", getString(R.string.accept_suc));
    }

    /**
     * 处理接收到的消息
     *
     * @param intent
     */
    protected void dispatchReceivedMsg(Intent intent) {

        View view = findViewById(R.id.title_worker);

        if (Constant.ACTION_ALARM_RECEIVED.equals(intent.getAction())) { // 接收到报警信息
            String alarmId = intent.getStringExtra("alarm_id");
            receivedAlarm(alarmId);

            //隐藏导航按钮
            findViewById(R.id.tv_navigation).setVisibility(View.GONE);
            view.findViewById(R.id.btn_query).setVisibility(View.GONE);

        } else if (Constant.ACTION_ALARM_ASSIGNED.equals(intent.getAction())) { // 接收到指派任务
            final String alarmId = intent.getStringExtra("alarm_id");

            String msg = intent.getStringExtra("msg");

            //当从推送消息进入时，需要弹出确认对话框，当从列表进入时，不需要弹框
            String from = intent.getStringExtra("from");

            receivedAssigned(alarmId);


        } else if (Constant.ACTION_ALARM_UNASSIGNED.equals(intent.getAction())) { // 未指派任务
            receivedUnAssigned();

        } else if (Constant.ACTION_ALARM_CANCEL.equals(intent.getAction())) {   //报警任务撤销
            receivedAlarmCancel();

            //隐藏导航按钮
            findViewById(R.id.tv_navigation).setVisibility(View.GONE);
            view.findViewById(R.id.btn_query).setVisibility(View.GONE);
        }
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar(getString(R.string.main_page), R.id.title_worker,
                R.drawable.back_normal, backClickListener);

        initTitleBar(R.id.title_worker, getString(R.string.main_page), R.drawable.icon_bbs,
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (null == mAlarmInfo) {
                            return;
                        }

                        if (!mAlarmInfo.getState().equals(Constant.ALARM_STATE_ASSIGNED)
                                || !mAlarmInfo.getState().equals(Constant.ALARM_STATE_ARRIVED)) {
                            showToast("该报警已经完成,无法进入电梯交流群组");
                            return;
                        }

                        Intent intent = new Intent(WorkerActivity.this, ChatActivity.class);
                        intent.putExtra("alarm_id", mAlarmInfo.getId());

                        startActivity(intent);
                    }
                });


//		String from = intent.getStringExtra("from");
//
//		if (StringUtils.isEmpty(from) || from.equals(JPushMsgReceiver.TAG)) {
//			initTitleBar(getString(R.string.main_page), R.id.title_worker,
//					R.drawable.navi_setting_normal, menuClickListener);
//
//            //检测是否有未完成的报警
//		} else if (from.equals(AlarmListActivity.TAG)) {
//			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//			setExitFlag(false);
//			initTitleBar(getString(R.string.main_page), R.id.title_worker,
//					R.drawable.back_normal, backClickListener);
//		}

    }

    /**
     * 根据报警id报警信息
     *
     * @param alarmId
     * @param callback
     */
    private void requestAlarmInfo(String alarmId, final IRequestAlarmInfoCallback callback) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_ALARM_INFO,
                getAlarmInfoRequest(alarmId)) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub

                AlarmInfoResponse response = AlarmInfoResponse
                        .getAlarmInfoRsp(result);
                mAlarmInfo = response.getBody();
                callback.onGetAlarmInfo(response.getBody());

                MyAdapter myAdapter = new MyAdapter(mAlarmInfo.getContactList());
                listView.setAdapter(myAdapter);
            }
        };

        addTask(task);
    }

    /**
     * 请求完报警信息的回掉
     */
    public interface IRequestAlarmInfoCallback {
        public void onGetAlarmInfo(AlarmInfo alarmInfo);
    }

    /**
     * 获取请求bean
     *
     * @param alarmId
     * @return
     */
    private RequestBean getAlarmInfoRequest(String alarmId) {
        AlarmInfoRequest request = new AlarmInfoRequest();
        AlarmInfoReqBody body = request.new AlarmInfoReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setId(alarmId);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 接受任务提交请求
     *
     * @param alarmId
     * @param distance
     * @return
     */
    private RequestBean getAcceptAlarmRequest(String alarmId, int distance) {
        AcceptAlarmRequest request = new AcceptAlarmRequest();
        AcceptAlarmReqBody body = new AcceptAlarmReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setAlarmId(alarmId);
        body.setDistance("" + distance);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 更新界面显示的距离
     */
    private void updateDistance() {
        if (null == centerMarker || null == workerMarker) {
            return;
        }
        LatLng center = centerMarker.getPosition();
        LatLng worker = workerMarker.getPosition();
        int distance = (int) DistanceUtil.getDistance(center, worker);
        ((TextView) findViewById(R.id.tv_distance)).setText("" + distance + " m");
    }


    /**
     * 上报状态的请求
     *
     * @param state
     * @return
     */
    private RequestBean getReportStateRequest(String state, String alarmId) {
        ReportStateRequest request = new ReportStateRequest();
        ReportStateReqBody body = request.new ReportStateReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setState(state);
        body.setAlarmId(alarmId);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 上报意外情况
     *
     * @param alarmId
     * @param remark
     * @return
     */
    private RequestBean getReportExceptRequest(String alarmId, String remark) {
        ReportExceptRequest request = new ReportExceptRequest();
        ReportExceptReqBody body = request.new ReportExceptReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setAlarmId(alarmId);
        body.setRemark(remark);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 上报意外情况
     *
     * @param alarmId
     * @param remark
     */
    private void reportExcept(String alarmId, String remark) {
        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_WORKER_EXCEPT,
                getReportExceptRequest(alarmId, remark)) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub
                showToast(getString(R.string.except_submit));
//						setFloatView(TYPE_NO_TASK, null);
                finish();
            }

        };
        addTask(netTask);
    }

    /**
     * 设置浮动view的显示信息
     *
     * @param type
     * @param alarmInfo
     */
    private void setFloatView(int type, AlarmInfo alarmInfo) {
        switch (type) {
            case TYPE_NO_TASK:
                setTopInfo(0, null);
                //setBottom(0, null);
                break;
            case TYPE_RECEIVED:
                setTopInfo(1, alarmInfo);
                //setBottom(1, alarmInfo);
                break;
            case TYPE_ASSIGNED:
                setTopInfo(2, alarmInfo);
                //setBottom(2, alarmInfo);
                break;
        }
    }

    /**
     * 初始化定位按钮
     */
    private void initLocationBtn() {
        findViewById(R.id.btn_location).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent sIntent = new Intent(WorkerActivity.this, LocationService.class);
                startService(sIntent);
                isCenter = true;
            }

        });
    }

    /**
     * 判断当前页面是否在前台
     *
     * @return
     */
    public static boolean isForeground() {
        return isForeground;
    }

    /**
     * 拷贝知识库数据库到应用安装路径下
     */
    private void copyKnowledge() {
        String knowledge = "knowledge.db";
        String dbPath = "/data/data/com.honyum.elevatorMan/databases";
        File knFile = new File(dbPath + File.separator + knowledge);
        //if (!knFile.exists()) {
        Utils.copyAssetFile(this, knowledge, dbPath, knowledge);
        //}
    }

    private class MyAdapter extends BaseAdapter {

        private List<ContactList> contactLists;

        public MyAdapter(List<ContactList> list) {
            contactLists = list;
        }

        @Override
        public int getCount() {
            return contactLists.size();
        }

        @Override
        public Object getItem(int i) {
            return contactLists.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            ViewHolder vh;

            final ContactList contact = contactLists.get(position);

            if (view == null) {
                vh = new ViewHolder();
                view = View.inflate(WorkerActivity.this, R.layout.layout_list_text2_item, null);
                vh.ivAvatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
                vh.tvName = (TextView) view.findViewById(R.id.tv_text);
                vh.tvTel = (TextView) view.findViewById(R.id.tv_text1);
                view.setTag(vh);
            } else {
                vh = (ViewHolder) view.getTag();
            }

            loadIcon(contact.getHeadPic(), vh.ivAvatar);
            vh.ivAvatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.fl_bg).setVisibility(View.VISIBLE);
                    findViewById(R.id.fl_bg).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            findViewById(R.id.fl_bg).setVisibility(View.GONE);
                        }
                    });

                    CircleImageView iv = (CircleImageView) findViewById(R.id.iv_ava);
                    loadIcon(contact.getHeadPic(), iv);
                }
            });

            vh.tvName.setText(contact.getName());

            vh.tvTel.setText(contact.getTel());

            return view;
        }
    }

    class ViewHolder {
        private TextView tvName, tvTel;
        private CircleImageView ivAvatar;
    }
}