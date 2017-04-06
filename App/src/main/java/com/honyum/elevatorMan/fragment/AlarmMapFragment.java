package com.honyum.elevatorMan.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
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
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.net.AcceptAlarmReqBody;
import com.honyum.elevatorMan.net.AcceptAlarmRequest;
import com.honyum.elevatorMan.net.AlarmListRequest;
import com.honyum.elevatorMan.net.AlarmListResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.receiver.LocationReceiver;
import com.honyum.elevatorMan.service.LocationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmMapFragment extends Fragment implements LocationReceiver.ILocationComplete {


    private View mView;

    private MapView mMapView;

    private BaiduMap mMap;

    private List<AlarmInfo> mAlarmInfoList;

    private BaseFragmentActivity mContext;

    private Config mConfig;

    private LocationReceiver mLocationReceiver;

    private String mAlarmSel = "";

    private Marker locationMarker;

    private Map<Marker, AlarmInfo> mMarkMap = new HashMap<Marker, AlarmInfo>();

    private ListView listView;

    public static final AlarmMapFragment newInstance(BaseFragmentActivity context, Config config) {
        AlarmMapFragment f = new AlarmMapFragment();
        f.mContext = context;
        f.mConfig = config;
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SDKInitializer.initialize(mContext.getApplicationContext());
        mView = inflater.inflate(R.layout.fragment_alarm_map, container, false);
        mMapView = (MapView) mView.findViewById(R.id.baidu_map);
        mMap = mMapView.getMap();
        initView();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLocationReceiver != null) {
            mContext.unregisterReceiver(mLocationReceiver);
        }
    }

    /**
     * 初始化视图
     */
    public void initView() {
        listView = (ListView) mView.findViewById(R.id.listView);

        initMapView();

        requestUnassignedAlarmList();
        //markOnMap();

        Intent sIntent = new Intent(mContext, LocationService.class);
        mContext.startService(sIntent);
        //初始化定位按钮
        initLocationBtn();

    }

    /**
     * 初始化listView
     */
    private void initListView(MyAdapter myAdapter) {

        final TextView alarmTime = (TextView) mView.findViewById(R.id.alarm_time);
        final TextView projectAddress = (TextView) mView.findViewById(R.id.project_address);
        final TextView projectDistance = (TextView) mView.findViewById(R.id.project_distance);
        final TextView propertyTel = (TextView) mView.findViewById(R.id.property_tel);
        final TextView elevatorInfo = (TextView) mView.findViewById(R.id.elevator_info);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyAdapter adapter = (MyAdapter) adapterView.getAdapter();
                adapter.setSelectedItem(i);

                final AlarmInfo info = (AlarmInfo) adapter.getItem(i);

                alarmTime.setText("报警时间:" + info.getAlarmTime() + "");

                if ("1".equals(info.getType())) {
                    projectAddress.setText("项目地址:" + info.getCommunityInfo().getAddress()
                            + info.getElevatorInfo().getBuildingCode()
                            + "楼" + info.getElevatorInfo().getUnitCode() + "单元"
                            + info.getElevatorInfo().getLiftNum());
                } else {
                    projectAddress.setText("项目地址:" + info.getCommunityInfo().getAddress() + "");
                }

                final double latitude = Double.parseDouble(info.getCommunityInfo().getLat());
                final double longitude = Double.parseDouble(info.getCommunityInfo().getLng());

                setLocationComplete(new LocationComplete() {
                    @Override
                    public void completeListener() {
                        LatLng location = locationMarker.getPosition();
                        LatLng alarm = new LatLng(latitude, longitude);
                        int distance = (int) DistanceUtil.getDistance(location, alarm);
                        if (distance < 1000) {
                            projectDistance.setText("项目距离:" + distance + "米");
                        } else {
                            double km = distance / 1000.0;
                            projectDistance.setText("项目距离:" + km + "公里");
                        }
                    }
                });

                propertyTel.setText("物业电话:" + info.getCommunityInfo().getPropertyUtel() + "");
                propertyTel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phone = info.getCommunityInfo().getPropertyUtel();
                        if (StringUtils.isEmpty(phone) || phone.equals(getString(R.string.default_text))) {
                            Toast.makeText(mContext, "对不起，电话号码无效!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }
                });


                if ("1".equals(info.getType())) {
                    elevatorInfo.setText("电梯信息:品牌-" + info.getElevatorInfo().getBrand()
                            + " 梯型-" + info.getElevatorInfo().getElevatorType()
                            + " 救援码-" + info.getElevatorInfo().getNumber());
                } else {
                    elevatorInfo.setText("电梯信息:暂无电梯信息");
                }


                mView.findViewById(R.id.tv_accept).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int distance = Integer.MAX_VALUE;
                        if (locationMarker != null) {
                            LatLng location = locationMarker.getPosition();
                            LatLng alarm = new LatLng(latitude, longitude);
                            distance = (int) DistanceUtil.getDistance(location, alarm);
                        }
                        acceptAlarm(info, distance);
                    }
                });
            }
        });

        myAdapter.setSelectedItem(0);
        final AlarmInfo info = (AlarmInfo) myAdapter.getItem(0);

        alarmTime.setText("报警时间:" + info.getAlarmTime() + "");

        if ("1".equals(info.getType())) {
            projectAddress.setText("项目地址:" + info.getCommunityInfo().getAddress()
                    + info.getElevatorInfo().getBuildingCode()
                    + "号楼" + info.getElevatorInfo().getUnitCode() + "单元"
                    + info.getElevatorInfo().getLiftNum());
        } else {
            projectAddress.setText("项目地址:" + info.getCommunityInfo().getAddress() + "");
        }

        final double latitude = Double.parseDouble(info.getCommunityInfo().getLat());
        final double longitude = Double.parseDouble(info.getCommunityInfo().getLng());

        setLocationComplete(new LocationComplete() {
            @Override
            public void completeListener() {
                LatLng location = locationMarker.getPosition();
                LatLng alarm = new LatLng(latitude, longitude);
                int distance = (int) DistanceUtil.getDistance(location, alarm);
                if (distance < 1000) {
                    projectDistance.setText("项目距离:" + distance + "米");
                } else {
                    double km = distance / 1000.0;
                    projectDistance.setText("项目距离:" + km + "公里");
                }
            }
        });


        propertyTel.setText("物业电话:" + info.getCommunityInfo().getPropertyUtel() + "");
        propertyTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = info.getCommunityInfo().getPropertyUtel();
                if (StringUtils.isEmpty(phone) || phone.equals(getString(R.string.default_text))) {
                    Toast.makeText(mContext, "对不起，电话号码无效!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });


        if ("1".equals(info.getType())) {
            elevatorInfo.setText("电梯信息:品牌-" + info.getElevatorInfo().getBrand()
                    + " 梯型-" + info.getElevatorInfo().getElevatorType()
                    + " 救援码-" + info.getElevatorInfo().getNumber());
        } else {
            elevatorInfo.setText("电梯信息:暂无电梯信息");
        }

        mView.findViewById(R.id.tv_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int distance = Integer.MAX_VALUE;
                if (locationMarker != null) {
                    LatLng location = locationMarker.getPosition();
                    LatLng alarm = new LatLng(latitude, longitude);
                    distance = (int) DistanceUtil.getDistance(location, alarm);
                }
                acceptAlarm(info, distance);
            }
        });
    }


    /**
     * 请求没有指派的任务
     */
    private void requestUnassignedAlarmList() {
        String server = mConfig.getServer();

        NetTask task = new NetTask(server + NetConstant.URL_ALARM_UNASSIGNED, getAlarmListRequest("")) {
            @Override
            protected void onResponse(NetTask task, String result) {
                AlarmListResponse response = AlarmListResponse.getAlarmListResponse(result);
                mAlarmInfoList = response.getBody();

                if (mAlarmInfoList.isEmpty()) {
                    return;
                }

                mView.findViewById(R.id.ll_elevator_info).setVisibility(View.VISIBLE);
                mView.findViewById(R.id.listView).setVisibility(View.VISIBLE);

                markOnMap();
                MyAdapter myAdapter = new MyAdapter();
                listView.setAdapter(myAdapter);
                initListView(myAdapter);
            }
        };

        mContext.addTask(task);
    }

    /**
     * 请求报警列表
     *
     * @return
     */
    private RequestBean getAlarmListRequest(String scope) {
        AlarmListRequest request = new AlarmListRequest();
        AlarmListRequest.AlarmListReqBody body = request.new AlarmListReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(mConfig.getUserId());
        head.setAccessToken(mConfig.getToken());
        body.setScope(scope);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 展示报警信息列表
     */
    private void markOnMap() {
//        markAlarm(null);
        for (int i = 0; i < mAlarmInfoList.size(); i++) {
            AlarmInfo info = mAlarmInfoList.get(i);
            markAlarm(info);
        }
    }

    private void initMapView() {
        /*mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final AlarmInfo info = mMarkMap.get(marker);
                if (null == info) {
                    Log.i("zhenhao", "select self");
                    return true;
                }


                View view = View.inflate(mContext, R.layout.layout_alarm_info, null);

                TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
                tvTime.setText(info.getAlarmTime());
                TextView tvProject = (TextView) view.findViewById(R.id.tv_project);
                tvProject.setText(info.getCommunityInfo().getName());

                TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
                tvAddress.setText(info.getCommunityInfo().getAddress());

                TextView tvTel = (TextView) view.findViewById(R.id.tv_telephone);
                tvTel.setText(info.getCommunityInfo().getPropertyUtel());

                TextView tvDistance = (TextView) view.findViewById(R.id.tv_distance);
                final double latitude = Double.parseDouble(info.getCommunityInfo().getLat());
                final double longitude = Double.parseDouble(info.getCommunityInfo().getLng());

                view.findViewById(R.id.img_phone).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String phone = info.getCommunityInfo().getPropertyUtel();
                        if (StringUtils.isEmpty(phone) || phone.equals(getString(R.string.default_text))) {
                            Toast.makeText(mContext, "对不起，电话号码无效!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }
                });


                view.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int distance = Integer.MAX_VALUE;
                        if (locationMarker != null) {
                            LatLng location = locationMarker.getPosition();
                            LatLng alarm = new LatLng(latitude, longitude);
                            distance = (int) DistanceUtil.getDistance(location, alarm);
                        }
                        acceptAlarm(info, distance);
                    }
                });

                if (locationMarker != null) {
                    LatLng location = locationMarker.getPosition();
                    LatLng alarm = new LatLng(latitude, longitude);
                    int distance = (int) DistanceUtil.getDistance(location, alarm);
                    if (distance < 1000) {
                        tvDistance.setText(distance + "米");
                    } else {
                        double km = distance / 1000.0;
                        tvDistance.setText(km + "公里");
                    }
                }

                int height = com.honyum.elevatorMan.utils.Utils.getViewHW(view)[1];


                InfoWindow infoWindow = new InfoWindow(view, marker.getPosition(), 0);

                mMap.showInfoWindow(infoWindow);

                return true;
            }
        });
        mMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });*/


        //去掉百度地图的下方的标签信息
        int count = mMapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mMapView.getChildAt(i);

            //去掉比例尺，百度logo，
            if (child instanceof ImageView
                    || child instanceof RelativeLayout) {
                child.setVisibility(View.INVISIBLE);
            }
        }

    }

    /**
     * 标记报警
     */
    private void markAlarm(AlarmInfo alarmInfo) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        if (null == alarmInfo) {
            return;
        }

        double latitude = Double.parseDouble(alarmInfo.getCommunityInfo().getLat());
        double longitude = Double.parseDouble(alarmInfo.getCommunityInfo().getLng());
        LatLng point = new LatLng(latitude, longitude);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
        mMap.animateMapStatus(update);

        MarkerOptions markerOption = (MarkerOptions) initAlarmOptions();
        markerOption.position(point);
        Marker marker = (Marker) mMap.addOverlay(markerOption);
        marker.setAnchor(0.5f, 0.5f);
        mMarkMap.put(marker, alarmInfo);

    }


    /**
     * 初始化报警标记选项
     *
     * @return
     */
    private OverlayOptions initAlarmOptions() {
        View view = View.inflate(mContext, R.layout.layout_location_marker, null);
        ImageView imgMarker = (ImageView) view.findViewById(R.id.img_marker);

        imgMarker.setImageResource(R.drawable.marker_alarm);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromView(view);
        return new MarkerOptions().icon(bitmapDescriptor).zIndex(9)
                .draggable(false);
    }


    /**
     * 初始化定位按钮
     */
    private void initLocationBtn() {
        mView.findViewById(R.id.btn_location).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent sIntent = new Intent(mContext, LocationService.class);
                mContext.startService(sIntent);
            }

        });
    }

    /**
     * 注册定位信息接收器
     */
    private void registerReceiver() {

        // 注册定位信息接收器
        mLocationReceiver = new LocationReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_LOCATION_COMPLETE);
        mContext.registerReceiver(mLocationReceiver, filter);

    }


    @Override
    public void onLocationComplete(double latitude, double longitude, String address) {

        LatLng point = new LatLng(latitude, longitude);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
        mMap.animateMapStatus(update);


        if (null == locationMarker) {
            View view = View.inflate(mContext, R.layout.layout_location_marker, null);

            ImageView imgMarker = (ImageView) view.findViewById(R.id.img_marker);

            imgMarker.setImageResource(R.drawable.marker_worker);

            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                    .fromView(view);
            MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor).zIndex(9)
                    .draggable(false);

            markerOption.position(point);
            locationMarker = (Marker) mMap.addOverlay(markerOption);
        } else {
            locationMarker.setPosition(point);
        }

        if (locationComplete != null) {
            locationComplete.completeListener();
        }
    }

    private LocationComplete locationComplete;

    public void setLocationComplete(LocationComplete locationComplete) {
        this.locationComplete = locationComplete;
    }

    private interface LocationComplete {
        void completeListener();
    }


    /**
     * 接受报警信息
     */
    private void acceptAlarm(final AlarmInfo alarmInfo, int distance) {

        NetTask netTask = new NetTask(mConfig.getServer() + NetConstant.URL_ACCEPT_ALARM,
                getAcceptAlarmRequest(alarmInfo.getId(), distance)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub
                //modify the state of the alarm in the sqlite
                mContext.popMsgAlertWithoutCancel("您已经参与" + alarmInfo.getCommunityInfo().getName() + "项目" +
                        "的救援任务，请等待系统指派任务", new BaseFragmentActivity.IConfirmCallback() {
                    @Override
                    public void onConfirm() {
                        mMap.hideInfoWindow();
                    }
                });
            }

            @Override
            protected void onFailed(NetTask task, String errorCode, String errorMsg) {
                //super.onFailed(task, errorCode, errorMsg);
                mContext.popMsgAlertWithoutCancel("该任务已经被取消，感谢您的参与!", new BaseFragmentActivity.IConfirmCallback() {
                    @Override
                    public void onConfirm() {
                        mMap.hideInfoWindow();
                    }
                });
            }
        };
        mContext.addTask(netTask);

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

        head.setUserId(mConfig.getUserId());
        head.setAccessToken(mConfig.getToken());

        body.setAlarmId(alarmId);
        body.setDistance("" + distance);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    private class MyAdapter extends BaseAdapter {

        private int selectedItem = -1;

        @Override
        public int getCount() {
            return mAlarmInfoList.size();
        }

        @Override
        public Object getItem(int i) {
            return mAlarmInfoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = View.inflate(getActivity(), R.layout.layout_list_text1_item, null);
            }

            AlarmInfo info = mAlarmInfoList.get(position);

            if (position == selectedItem) {
                view.setBackgroundColor(getResources().getColor(R.color.grey));
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.transparent));
            }


            TextView tvTexT = (TextView) view.findViewById(R.id.tv_text);
            tvTexT.setText(info.getCommunityInfo().getName());

            return view;
        }

        int getSelectedItem() {
            return selectedItem;
        }

        void setSelectedItem(int selectedItem) {
            this.selectedItem = selectedItem;
            notifyDataSetChanged();
        }
    }
}
