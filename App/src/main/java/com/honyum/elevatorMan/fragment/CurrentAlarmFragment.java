package com.honyum.elevatorMan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.ChatActivity;
import com.honyum.elevatorMan.activity.property.AlarmTraceActivity;
import com.honyum.elevatorMan.activity.property.PropertyActivity;
import com.honyum.elevatorMan.base.BaseFragment;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.data.Point;
import com.honyum.elevatorMan.data.WorkerInfo;
import com.honyum.elevatorMan.net.AlarmInfoRequest;
import com.honyum.elevatorMan.net.AlarmInfoResponse;
import com.honyum.elevatorMan.net.AlarmListRequest;
import com.honyum.elevatorMan.net.AlarmListResponse;
import com.honyum.elevatorMan.net.AlarmStateRequest;
import com.honyum.elevatorMan.net.AlarmStateResponse;
import com.honyum.elevatorMan.net.ConfirmAlarmRequest;
import com.honyum.elevatorMan.net.WorkersRequest;
import com.honyum.elevatorMan.net.WorkersResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.Utils;
import com.honyum.elevatorMan.view.CircleImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentAlarmFragment extends BaseFragment {


    private View mView;

    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private Marker centerMarker;

    private Marker startMarker;

    private TextView tv_ensure;

    private Overlay traceOverlay;

    private List<Marker> mMarkerList;

    private Map<String, Marker> mMarkerMap;

    private Map<Marker, WorkerInfo> mWorkerMap;

    private static final int alternation = 30;

    /**
     * 标记当前选择的修理工
     */
    private String mSelected = "";

    private String projectAdd = "";

    private String projectName = "";

    private Timer mTimer;

    private String mAlarmState = "";

    private static final int REQ_WORKER_SUC = 0;
    private static final int REQ_ENSURE_COMMIT = 1;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.arg1) {
                case REQ_WORKER_SUC:
                    String result = (String) msg.obj;
                    AlarmStateResponse response = AlarmStateResponse.getAlarmState(result);
                    markWorkersLocation(response.getBody());
                    break;
                case REQ_ENSURE_COMMIT:
                    String result1 = (String) msg.obj;
                    tv_ensure.setVisibility(View.GONE);
                    requestAlarmList();
            }
        }

    };


    private LatLng alarmLatLng;

    private WorkerAdapter adapter;

    private BaseFragmentActivity mActivity;

    public CurrentAlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SDKInitializer.initialize(getActivity().getApplicationContext());

        mView = inflater.inflate(R.layout.activity_alarm_trace1, container, false);

        mActivity = (BaseFragmentActivity) getActivity();

        Intent intent = getActivity().getIntent();

        tv_ensure = (TextView) mView.findViewById(R.id.tv_ensure);
        mMarkerList = new ArrayList<Marker>();

        mMarkerMap = new HashMap<String, Marker>();

        mWorkerMap = new HashMap<Marker, WorkerInfo>();

        initTitleBar(intent);

        initMapView();

        requestAlarmList();

        dispatchReceivedMsg(intent);


        return mView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mView != null && !hidden) {
            requestAlarmList();
        }
    }

    /**
     * 请求报警列表
     */
    private void requestAlarmList() {

        AlarmListRequest request = new AlarmListRequest();
        RequestHead head = new RequestHead();
        AlarmListRequest.AlarmListReqBody body = request.new AlarmListReqBody();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setScope("unfinished");

        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_ALARM_LIST,
                request) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub
                AlarmListResponse response = AlarmListResponse.getAlarmListResponse(result);
                if (response.getBody().isEmpty()) {
                    return;
                }
                filterList(response.getBody());
            }

        };

        mActivity.addTask(netTask);
    }

    /**
     * 将已经完成的报警任务过滤
     *
     * @param infoList
     */
    private void filterList(List<AlarmInfo> infoList) {
        List<AlarmInfo> alarmInfoList = new ArrayList<AlarmInfo>();
        for (AlarmInfo info : infoList) {
            if (!Constant.ALARM_STATE_CONFIRM.equals(info.getState())
                    && !"1".equals(info.getIsMisinformation())) {
                alarmInfoList.add(info);
            }
        }

        //没有报警信息
        if (0 == alarmInfoList.size()) {
            mActivity.showToast(getString(R.string.no_alarm));
        }

        initAlarmListView(alarmInfoList);
    }

    private void initAlarmListView(List<AlarmInfo> infos) {
        ListView listView = (ListView) mView.findViewById(R.id.alarm_listView);
        final AlarmAdapter adapter = new AlarmAdapter(infos);
        listView.setAdapter(adapter);

        listView.setEmptyView(mView.findViewById(R.id.tv_empty_tip1));

        if (infos != null && infos.size() > 0) {
            AlarmInfo info = infos.get(0);
            projectAdd = info.getCommunityInfo().getAddress();
            //adapter.setSelectedItem(0);
            alarmLatLng = new LatLng(Utils.getDouble(info.getCommunityInfo().getLat()), Utils.getDouble(info.getCommunityInfo().getLng()));
            markCenterLocation(info);
            requestAlarmState(info.getId());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (adapter.getSelectedItem() == position) {
                    return;
                }

                mBaiduMap.clear();

                final AlarmInfo info = (AlarmInfo) adapter.getItem(position);

                adapter.setSelectedItem(position);

                projectAdd = info.getCommunityInfo().getAddress();

                alarmLatLng = new LatLng(Utils.getDouble(info.getCommunityInfo().getLat()), Utils.getDouble(info.getCommunityInfo().getLng()));

                markCenterLocation(info);

                if(info.getState().equals("3")) {
                    mView.findViewById(R.id.worker_listView).setVisibility(View.GONE);
                    tv_ensure.setVisibility(View.VISIBLE);
                    tv_ensure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestSubmitAlarmCompleted(info.getId());
                        }
                    });

                }
                else {
                    requestAlarmState(info.getId());
                }
            }
        });
    }
    /**
     *
     *
     * @param alarmId
     */
    private void requestSubmitAlarmCompleted(String alarmId) {
        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.PROPERTY_CONFIRM,
                getAlarmStateRequest(alarmId)) {

            @Override
            protected void onResponse(NetTask task, String result) {
                Message msg = Message.obtain();
                msg.arg1 = REQ_ENSURE_COMMIT;
                msg.obj = result;
                mHandler.sendMessage(msg);

//                AlarmStateResponse response = AlarmStateResponse.getAlarmState(result);
//                initWorkerListView(response.getBody());
            }
        };

        mActivity.addBackGroundTask(netTask);
    }

    /**
     * 初始化地图
     */
    private void initMapView() {
        mMapView = (MapView) mView.findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //如果是报警时候标记的所有维修工，不进行操作
                if (mMarkerList.contains(marker)) {
                    return true;
                }

                //点击报警标记，显示地址
                if (marker == centerMarker) {
                    if (StringUtils.isEmpty(projectAdd)) {
                        projectAdd = getString(R.string.add_null);
                    }

                    TextView tvInfo = new TextView(mActivity);
                    tvInfo.setBackgroundResource(R.drawable.info_bac);
                    tvInfo.setPadding(10, 10, 10, 30);
                    tvInfo.setGravity(Gravity.CENTER_HORIZONTAL);
                    tvInfo.setText(projectAdd);

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

                //点击已经选中的维修工不进行操作
                if (marker == mMarkerMap.get(mSelected)) {
                    return true;
                }

                //开始标记，不进行操作
                if (marker == startMarker) {
                    return true;
                }


                if (!mSelected.equals("")) {
                    WorkerInfo preSelected = mWorkerMap.get(mMarkerMap.get(mSelected));
                    if (preSelected.getState().equals(Constant.WORKER_STATE_ARRIVED)) {
                        BitmapDescriptor unSelected = BitmapDescriptorFactory
                                .fromResource(R.drawable.marker_worker_arrived);
                        mMarkerMap.get(mSelected).setIcon(unSelected);
                    } else {
                        BitmapDescriptor unSelected = BitmapDescriptorFactory
                                .fromResource(R.drawable.marker_worker);
                        mMarkerMap.get(mSelected).setIcon(unSelected);
                    }
                }

                BitmapDescriptor selected = BitmapDescriptorFactory
                        .fromResource(R.drawable.marker_selected);
                marker.setIcon(selected);


                WorkerInfo worker = mWorkerMap.get(marker);

                mSelected = worker.getUserId();

                adapter.setSelectedId(mSelected);

                //显示轨迹
                addTrace(worker);

//                updateAlarmState(marker, worker);
                return true;
            }
        });


        // 去掉百度地图的下方的标签信息
        int count = mMapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls || child instanceof ImageView
                    || child instanceof RelativeLayout) {
                child.setVisibility(View.INVISIBLE);
            }
        }

//        findViewById(R.id.ll_worker_info).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    float x = event.getX();
//                    float y = event.getY();
//
//                    int[] position = new int[2];
//                    v.getLocationInWindow(position);
//
//
//                }
//                return true;
//            }
//        });

    }

    /**
     * 初始化拨号键
     */
    private void initDial() {
//        mView.findViewById(R.id.img_phone).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView tvPhone = (TextView) mView.findViewById(R.id.tv_telephone);
//                String phone = tvPhone.getText().toString();
//                if (StringUtils.isEmpty(phone) || phone.equals(getString(R.string.default_text))) {
//                    mActivity.showToast("电话号码无效");
//                    return;
//                }
//
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:" + phone));
//                startActivity(intent);
//            }
//        });
    }

    /**
     * 标记维修工当前位置
     *
     * @param workerInfo
     */
    private void markWorkerLocation(WorkerInfo workerInfo) {
        if (StringUtils.isEmpty(workerInfo.getLat())) {
            return;
        }
        markLocation(workerInfo);
    }

    /**
     * 标记报警地点
     *
     * @param alarmId
     */
    private void markCenterLocation(final String alarmId) {
        if (StringUtils.isEmpty(alarmId)) {
            return;
        }
        requestAlarmInfo(alarmId, new AlarmTraceActivity.IRequestAlarmInfoCallback() {
            @Override
            public void onRequestAlarmInfo(AlarmInfo alarmInfo) {
                projectName = alarmInfo.getCommunityInfo().getName();
                projectAdd = alarmInfo.getCommunityInfo().getAddress();

                //当报警时，弹出框提示范围内维修工人数
//                if (mAlarmState.equals(Constant.ACTION_ALARM_PROPERTY)) {
//                    String count = alarmInfo.getAppointCount();
//                    if (!StringUtils.isEmpty(count)) {
//                        popMsgAlertWithoutCancel("附近有" + count + "位维修工接收到报警消息，请等待维修" +
//                                "工接单", null);
//                        mAlarmState = "";
//                    }
//                } else {
//                }

                markCenterLocation(alarmInfo);

//                WorkerInfo info = new WorkerInfo();
//                updateAlarmState(null, info);
            }
        });
    }

    /**
     * 标记报警地点
     *
     * @param alarmInfo
     */
    private void markCenterLocation(AlarmInfo alarmInfo) {
        double latitude = Utils.getDouble(alarmInfo.getCommunityInfo().getLat());
        double longitude = Utils.getDouble(alarmInfo.getCommunityInfo().getLng());

        markCenterLocation(latitude, longitude);
    }

    /**
     * 标记报警位置
     *
     * @param latitude
     * @param longitude
     */
    private void markCenterLocation(double latitude, double longitude) {

        mBaiduMap.hideInfoWindow();

        LatLng point = new LatLng(latitude, longitude);

        // 中心平移到新的位置
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
        mBaiduMap.animateMapStatus(update);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.marker_alarm_old);

        MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor)
                .zIndex(9).draggable(false);
        markerOption.position(point);

        if (centerMarker != null) {
            centerMarker.remove();
        }
        centerMarker = (Marker) mBaiduMap.addOverlay(markerOption);
    }

    /**
     * 根据经纬度标记地图的位置
     *
     * @param worker
     */
    private void markLocation(WorkerInfo worker) {

        mBaiduMap.hideInfoWindow();

        LatLng point = new LatLng(Utils.getDouble(worker.getLat()), Utils.getDouble(worker.getLng()));

        MarkerOptions markerOption = (MarkerOptions) initOptions(worker.getUserId().equals(mSelected),
                worker.getState());
        markerOption.position(point);

        if (mMarkerMap.get(worker.getUserId()) != null) {
            mMarkerMap.get(worker.getUserId()).remove();
        }
        Marker marker = (Marker) mBaiduMap.addOverlay(markerOption);
        mMarkerMap.put(worker.getUserId(), marker);
        mWorkerMap.put(marker, worker);
        //workerMarker = marker;

        //如果选中，显示信息和轨迹
//        if (worker.getUserId().equals(mSelected)) {
//            updateAlarmState(marker, worker);
//            addTrace(worker);
//        }
    }

    /**
     * 将员工标记ch
     */
    private void initMarkerMap() {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.mark);
        Set<String> keySet = mMarkerMap.keySet();
        for (String key : keySet) {
            mMarkerMap.get(key).setIcon(bitmapDescriptor);
        }
    }

    /**
     * 返回marker的基本设置
     */
    private OverlayOptions initOptions(boolean selected, String state) {
        View view = View.inflate(mActivity, R.layout.layout_location_marker, null);
        ImageView imgMarker = (ImageView) view.findViewById(R.id.img_marker);
        imgMarker.setImageResource(R.drawable.marker_worker);
        if (selected) {
            imgMarker.setImageResource(R.drawable.marker_selected);
        } else {
            if (state.equals(Constant.WORKER_STATE_ARRIVED)) {
                imgMarker.setImageResource(R.drawable.marker_worker_arrived);
            } else {
                imgMarker.setImageResource(R.drawable.marker_worker);
            }
        }
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromView(view);
        return new MarkerOptions().icon(bitmapDescriptor).zIndex(9)
                .draggable(false);
    }

//	/**
//	 * 注册定时器信息接收器
//	 */
//	private void registerReceiver() {
//
//		//注册前台消息推送接收器
//        mForeMsgReceiver = new ForeMsgReceiver(this);
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(addForeAction(Constant.ACTION_ALARM_PROPERTY));
//        filter.addAction(addForeAction(Constant.ACTION_WORKER_ASSIGNED));
//        filter.addAction(addForeAction(Constant.ACTION_WORKER_ARRIVED));
//        filter.addAction(addForeAction(Constant.ACTION_ALARM_COMPLETE));
//		registerReceiver(mForeMsgReceiver, filter);
//	}

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d("BaiDuMap", "mMapView===>>>" + mMapView);
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar(Intent intent) {

        View titleView = mView.findViewById(R.id.title_alarm_trace);

        initTitleBar(titleView, "应急救援", R.drawable.back_normal, mActivity.backClickListener,
                R.drawable.icon_bbs, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, ChatActivity.class);

                        intent.putExtra("enter_mode", ChatActivity.MODE_PROPERTY);
                        startActivity(intent);
                    }
                });
    }


    /**
     * 请求报警状态
     *
     * @param alarmId
     */
    private void requestAlarmState(String alarmId) {
        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_ALARM_STATE,
                getAlarmStateRequest(alarmId)) {

            @Override
            protected void onResponse(NetTask task, String result) {
                Message msg = Message.obtain();
                msg.arg1 = REQ_WORKER_SUC;
                msg.obj = result;
                mHandler.sendMessage(msg);

                AlarmStateResponse response = AlarmStateResponse.getAlarmState(result);
                initWorkerListView(response.getBody());
            }
        };

        mActivity.addBackGroundTask(netTask);
    }


    private void initWorkerListView(List<WorkerInfo> infos) {

        mView.findViewById(R.id.worker_listView).setVisibility(View.VISIBLE);
        tv_ensure.setVisibility(View.GONE);

        ListView listView = (ListView) mView.findViewById(R.id.worker_listView);
        adapter = new WorkerAdapter(infos);
        listView.setAdapter(adapter);

        listView.setEmptyView(mView.findViewById(R.id.tv_empty_tip2));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (adapter.getSelectedItem() == position) {
                    return;
                }

                WorkerInfo info = (WorkerInfo) adapter.getItem(position);

                if (TextUtils.isEmpty(info.getLat()) || TextUtils.isEmpty(info.getLng())) {
                    mActivity.showToast("无法确定维修工位置!");
                    return;
                }

                adapter.setSelectedItem(position);

                adapter.setSelectedId(info.getUserId());

                if (!mSelected.equals("")) {
                    WorkerInfo preSelected = mWorkerMap.get(mMarkerMap.get(mSelected));
                    if (preSelected.getState().equals(Constant.WORKER_STATE_ARRIVED)) {
                        BitmapDescriptor unSelected = BitmapDescriptorFactory
                                .fromResource(R.drawable.marker_worker_arrived);
                        mMarkerMap.get(mSelected).setIcon(unSelected);
                    } else {
                        BitmapDescriptor unSelected = BitmapDescriptorFactory
                                .fromResource(R.drawable.marker_worker);
                        mMarkerMap.get(mSelected).setIcon(unSelected);
                    }
                }

                Marker marker = mMarkerMap.get(info.getUserId());
                if (marker != null) {
                    BitmapDescriptor selected = BitmapDescriptorFactory
                            .fromResource(R.drawable.marker_selected);
                    marker.setIcon(selected);
                }


                // 中心平移到新的位置
                LatLng point = new LatLng(Utils.getDouble(info.getLat()), Utils.getDouble(info.getLng()));
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
                mBaiduMap.animateMapStatus(update);

                addTrace(info);

                mSelected = info.getUserId();
            }
        });
    }

    /**
     * 报警状态请求bean
     *
     * @param alarmId
     * @return
     */
    private RequestBean getAlarmStateRequest(String alarmId) {
        AlarmStateRequest request = new AlarmStateRequest();
        AlarmStateRequest.AlarmStateReqBody body = request.new AlarmStateReqBody();
        RequestHead head = new RequestHead();
        head.setAccessToken(getConfig().getToken());

        head.setUserId(getConfig().getUserId());
        body.setAlarmId(alarmId);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    /**
     * 更新报警信息
     *
     * @param marker
     * @param workerInfo
     */
    private void updateAlarmState(Marker marker, WorkerInfo workerInfo) {
        /*String state = workerInfo.getState();
        String name = workerInfo.getName();
        String phone = workerInfo.getTel();
        String defaultText = getString(R.string.default_text);


        //项目名称
        ((TextView) findViewById(R.id.tv_project))
                .setText(StringUtils.isEmpty(projectName) ? defaultText : projectName);

        //报警状态
        ((TextView) findViewById(R.id.tv_state))
                .setText(StringUtils.isEmpty(state) ? defaultText : getStringByState(state));

        Log.i("zhenhao", "valie worker:" + workerInfo.isValidWorker());
        //刚报警，不需要展示维修工信息
        if (!workerInfo.isValidWorker()) {
            findViewById(R.id.ll_worker).setVisibility(View.GONE);
            findViewById(R.id.ll_tel).setVisibility(View.GONE);

        } else {
            findViewById(R.id.ll_worker).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_tel).setVisibility(View.VISIBLE);

            ((TextView) findViewById(R.id.tv_worker))
                    .setText(StringUtils.isEmpty(name) ? defaultText : name);

            ((TextView) findViewById(R.id.tv_telephone))
                    .setText(StringUtils.isEmpty(phone) ? defaultText : phone);
        }

        //如果没有获取报警点和维修工位置，不显示距离
        if (null == centerMarker || null == marker) {
            findViewById(R.id.ll_distance).setVisibility(View.GONE);

        } else {
            findViewById(R.id.ll_distance).setVisibility(View.VISIBLE);
            LatLng center = centerMarker.getPosition();
            LatLng worker = marker.getPosition();
            int distance = (int) DistanceUtil.getDistance(center, worker);
            ((TextView) findViewById(R.id.tv_distance)).setText("" + ((double) distance / (double) 1000) + "公里");
        }*/
    }

    /**
     * 标记维修工位置
     *
     * @param workerInfoList
     */
    private void markWorkersLocation(List<WorkerInfo> workerInfoList) {
        if (null == workerInfoList || 0 == workerInfoList.size()) {
            return;
        }

//        if (mSelected.equals("")) {
//            mSelected = workerInfoList.get(0).getUserId();
//        }

        for (WorkerInfo workerInfo : workerInfoList) {
            markWorkerLocation(workerInfo);
        }
    }

    /**
     * 获取轨迹数据
     *
     * @param workerInfo
     */
    private void addTrace(WorkerInfo workerInfo) {

        List<LatLng> pointList = getPointList(workerInfo.getPoints());
        showTrace(pointList);
    }

    /**
     * 显示轨迹
     *
     * @param pointList
     */
    private void showTrace(List<LatLng> pointList) {
        cancelTrace();

        if (null == pointList) {
            return;
        }
        if (pointList.size() < 2 || pointList.size() > 10000) {
            return;
        }

        View view = View.inflate(mActivity, R.layout.layout_location_marker, null);
        ImageView imgMarker = (ImageView) view.findViewById(R.id.img_marker);
        imgMarker.setImageResource(R.drawable.marker_start);

        LatLng startPoint = pointList.get(0);

        //设置起点
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromView(view);
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(bitmapDescriptor)
                .zIndex(9)
                .draggable(false)
                .position(startPoint);
        startMarker = (Marker) mBaiduMap.addOverlay(markerOptions);


        OverlayOptions options = new PolylineOptions()
                .color(getResources().getColor(R.color.title_bg_color))
                .width(3)
                .dottedLine(true)
                .points(pointList);

        //traceList.add(mBaiduMap.addOverlay(options));
        traceOverlay = mBaiduMap.addOverlay(options);
    }

    /**
     * 取消轨迹显示
     *
     * @param
     */
    private void cancelTrace() {
        if (traceOverlay != null) {
            traceOverlay.remove();
            traceOverlay = null;
        }

        if (startMarker != null) {
            startMarker.remove();
            startMarker = null;
        }
    }


    /**
     * 物业接收到报警信息
     *
     * @param alarmId
     */
    private void receivedAlarm(String alarmId) {

        //将当前处理的报警信息缓存到本地
        mActivity.setAlarmInfo(alarmId, Constant.ALARM_STATE_START);
        markCenterLocation(alarmId);
    }

    /**
     * 报警已经指派
     *
     * @param alarmId
     */
    private void receivedAssigned(String alarmId) {
        mActivity.setAlarmInfo(alarmId, Constant.ALARM_STATE_ASSIGNED);
        startTimer(alarmId);
    }

    /**
     * 维修工到达
     *
     * @param alarmId
     */
    private void receivedArrived(String alarmId) {
        mActivity.setAlarmInfo(alarmId, Constant.ALARM_STATE_ARRIVED);
        startTimer(alarmId);
    }

    /**
     * 报警任务完成
     *
     * @param alarmId
     * @param projectName
     */
    private void receiveComplete(final String alarmId, String projectName) {
        String msg = projectName + getString(R.string.property_complete);
        mActivity.popMsgAlertWithCancel(msg, new BaseFragmentActivity.IConfirmCallback() {
            @Override
            public void onConfirm() {
                confirmAlarm(alarmId);
            }
        }, null, "确定", "救援任务完成");
    }

    /**
     * 处理物业收到通知
     *
     * @param intent
     */
    public void dispatchReceivedMsg(final Intent intent) {

        final String alarmId = intent.getStringExtra("alarm_id");

        markCenterLocation(alarmId);

        mAlarmState = intent.getAction();

        if (Constant.ACTION_ALARM_PROPERTY.equals(intent.getAction())) {        //接收到报警任务
            receivedAlarm(alarmId);
            //标记报警地点周围维修工位置
            requestWorkers(alarmId);

        } else if (Constant.ACTION_WORKER_ASSIGNED.equals(intent.getAction())) {        //报警已经指派
            //当不是报警时清除地图上维修工标记信息
            clearAllWorkers();

            receivedAssigned(alarmId);

        } else if (Constant.ACTION_WORKER_ARRIVED.equals(intent.getAction())) {     //维修工已经到达
            receivedArrived(alarmId);

        } else if (Constant.ACTION_ALARM_COMPLETE.equals(intent.getAction())) {    //报警任务处理完成

            //停止定时任务
            stopTimer();

            cancelTrace();
            //隐藏维修工信息显示界面
            mView.findViewById(R.id.ll_worker_info).setVisibility(View.GONE);

            requestAlarmInfo(alarmId, new AlarmTraceActivity.IRequestAlarmInfoCallback() {
                @Override
                public void onRequestAlarmInfo(AlarmInfo alarmInfo) {
                    receiveComplete(alarmId, alarmInfo.getCommunityInfo().getName());
                }
            });

        } else if (Constant.ACTION_ALARM_CANCEL.equals(intent.getAction())) {   //报警任务撤销
            receivedAlarmCancel();
        }
    }

    /**
     * 报警被撤销
     */
    private void receivedAlarmCancel() {
        //停止定时任务
        stopTimer();

        mActivity.clearAlarmInfo();

        Intent intent = new Intent(mActivity,
                PropertyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 请求报警信息
     *
     * @param alarmId
     */
    private void requestAlarmInfo(String alarmId, final AlarmTraceActivity.IRequestAlarmInfoCallback callback) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_ALARM_INFO,
                getAlarmInfoRequest(alarmId)) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub
                AlarmInfoResponse response = AlarmInfoResponse
                        .getAlarmInfoRsp(result);
                if (callback != null) {
                    callback.onRequestAlarmInfo(response.getBody());
                }
            }

        };

        mActivity.addTask(task);
    }

    /**
     * 请求完报警信息的回调接口
     */
    public interface IRequestAlarmInfoCallback {
        public void onRequestAlarmInfo(AlarmInfo alarmInfo);
    }

    /**
     * 获取请求bean
     *
     * @param alarmId
     * @return
     */
    private RequestBean getAlarmInfoRequest(String alarmId) {
        AlarmInfoRequest request = new AlarmInfoRequest();
        AlarmInfoRequest.AlarmInfoReqBody body = request.new AlarmInfoReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setId(alarmId);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 启动定时器
     */
    private void startTimer(String alarmId) {
        if (mTimer != null) {
            stopTimer();
        }
        mTimer = new Timer();
        mTimer.schedule(new MyTimerTask(alarmId), 0, alternation * 1000);
    }

    /**
     * 停止定时器
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /* 定时任务
     *
     * @author changhaozhang
     *
     */
    private class MyTimerTask extends TimerTask {

        private String mAlarmId;

        public MyTimerTask(String alarmId) {
            mAlarmId = alarmId;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            requestAlarmState(mAlarmId);
        }

    }


    /**
     * 确认报警事件完成
     *
     * @param alarmId
     */
    private void confirmAlarm(String alarmId) {
        NetTask netTask = new NetTask(getConfig().getServer()
                + NetConstant.URL_CONFIRM_ALARM,
                getConfirmAlarmRequest(alarmId)) {

            @Override
            protected void onResponse(NetTask task, String result) {

                // TODO Auto-generated method stub
                // 已经完成，清理当前报警任务的缓存信息
                mActivity.clearAlarmInfo();

                Intent intent = new Intent(mActivity,
                        PropertyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        };
        mActivity.addTask(netTask);
    }

    /**
     * 获取确认报警事件完成bean
     *
     * @param alarmId
     * @return
     */
    private RequestBean getConfirmAlarmRequest(String alarmId) {
        ConfirmAlarmRequest request = new ConfirmAlarmRequest();
        ConfirmAlarmRequest.ConfirmAlarmReqBody body = request.new ConfirmAlarmReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setAlarmId(alarmId);

        request.setHead(head);
        request.setBody(body);

        return request;
    }


    /**
     * 根据下发的轨迹转换为百度地图识别的点
     *
     * @param pointList
     * @return
     */
    private List<LatLng> getPointList(List<Point> pointList) {
        List<LatLng> traceList = new ArrayList<LatLng>();
        if (null == pointList || 0 == pointList.size()) {
            return traceList;
        }

        for (Point point : pointList) {
            traceList.add(new LatLng(point.getLat(), point.getLng()));
        }

        return traceList;
    }

    /**
     * 标记维修工位置。当报警后，在地图上显示周围所有的维修工的位置
     *
     * @param pointList
     */
    private void markAllWorkers(List<Point> pointList) {

        if (null == pointList || 0 == pointList.size()) {
            return;
        }

        List<LatLng> latLngList = getPointList(pointList);

        for (LatLng point : latLngList) {
            MarkerOptions options = (MarkerOptions) getWorkerMarkerOptions();
            options.position(point);
            Marker marker = (Marker) mBaiduMap.addOverlay(options);
            mMarkerList.add(marker);
        }
    }

    /**
     * 清除维修工的标记，报警指派完成后，不再需要显示没有被指派的维修工的位置
     */
    private void clearAllWorkers() {
        if (null == mMarkerList || 0 == mMarkerList.size()) {
            return;
        }

        for (Marker marker : mMarkerList) {
            marker.remove();
        }
    }


    /**
     * 初始化维修工标记的图标
     *
     * @return
     */
    private OverlayOptions getWorkerMarkerOptions() {
        View view = View.inflate(mActivity, R.layout.layout_location_marker, null);
        ImageView imgMarker = (ImageView) view.findViewById(R.id.img_marker);
        imgMarker.setImageResource(R.drawable.marker_start_bac);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromView(view);

        return new MarkerOptions().icon(bitmapDescriptor).zIndex(9)
                .draggable(false).anchor(0.5f, 0.5f);
    }

    /**
     * 获取请求bean
     *
     * @param alarmId
     * @return
     */
    private RequestBean getWorkersRequest(String alarmId) {
        WorkersRequest request = new WorkersRequest();
        WorkersRequest.WorkersReqBody body = request.new WorkersReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setAlarmId(alarmId);

        request.setBody(body);
        request.setHead(head);

        return request;
    }

    /**
     * 请求工人信息并标记
     *
     * @param alarmId
     */
    private void requestWorkers(String alarmId) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_WORKERS,
                getWorkersRequest(alarmId)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                WorkersResponse response = WorkersResponse.getWorkersResponse(result);

                markAllWorkers(response.getBody());
            }
        };
        mActivity.addTask(task);
    }


    /**
     * 获取地图标记的view
     *
     * @param imgId
     * @return
     */
    private View getLocationView(int imgId) {
        View view = View.inflate(mActivity, R.layout.layout_location_marker, null);
        ImageView marker = (ImageView) view.findViewById(R.id.img_marker);
        marker.setImageResource(imgId);
        return view;
    }


    private class AlarmAdapter extends BaseAdapter {

        private int selectedItem = -1;

        private List<AlarmInfo> alarmInfos;

        private AlarmAdapter(List<AlarmInfo> list) {
            alarmInfos = list;
        }

        @Override
        public int getCount() {
            return alarmInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return alarmInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            final AlarmInfo info = alarmInfos.get(position);

            if (view == null) {
                view = View.inflate(mActivity, R.layout.layout_list_text1_item, null);
            }

            if (position == selectedItem) {
                view.setBackgroundColor(getResources().getColor(R.color.grey));
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            TextView alarmTime = (TextView) view.findViewById(R.id.tv_text);
            alarmTime.setText(info.getAlarmTime());

            TextView alarmName = (TextView) view.findViewById(R.id.tv_text1);
            alarmName.setText(info.getCommunityInfo().getName());

            TextView alarmState = (TextView) view.findViewById(R.id.tv_text2);
            int state = Integer.parseInt(info.getState());
            switch (state) {
                case 1:
                    alarmState.setText("已出发");
                    break;
                case 2:
                    alarmState.setText("已到达");
                    break;
                case 3:
                    alarmState.setText("已完成");
                    break;
                case 4:
                    alarmState.setText("拒绝");
                    break;
                default:
                    alarmState.setText("——");
                    break;
            }
            return view;
        }

        public int getSelectedItem() {
            return selectedItem;
        }

        public void setSelectedItem(int selectedItem) {
            this.selectedItem = selectedItem;
            notifyDataSetChanged();
        }
    }


    private class WorkerAdapter extends BaseAdapter {

        private int selectedItem = -1;

        private String selectedId = "";

        private List<WorkerInfo> workerInfos;

        public WorkerAdapter(List<WorkerInfo> workerInfos) {
            this.workerInfos = workerInfos;
        }

        @Override
        public int getCount() {
            return workerInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return workerInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            final WorkerInfo info = workerInfos.get(position);

            if (view == null) {
                view = View.inflate(mActivity, R.layout.layout_list_text3_item, null);
            }

            if (info.getUserId().equals(selectedId)) {
                view.setBackgroundColor(getResources().getColor(R.color.grey));
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            CircleImageView ivAvatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
            mActivity.loadIcon(info.getHeadPic(), ivAvatar);

            TextView tvName = (TextView) view.findViewById(R.id.worker_name);
            tvName.setText(info.getName());

            TextView tvTel = (TextView) view.findViewById(R.id.worker_tel);
            tvTel.setText(info.getTel());

            TextView tvDistance = (TextView) view.findViewById(R.id.worker_distance);
            LatLng wkLatLng = new LatLng(Utils.getDouble(info.getLat()), Utils.getDouble(info.getLng()));
            double distance = DistanceUtil.getDistance(wkLatLng, alarmLatLng) / 1000;
            DecimalFormat format = new DecimalFormat("0.00");
            String dis = format.format(distance);
            tvDistance.setText("距离目的地:" + dis + "米");

            TextView wkState = (TextView) view.findViewById(R.id.tv_state);
            int state = Integer.parseInt(info.getState());
            switch (state) {
                case 1:
                    wkState.setText("已出发");
                    break;
                case 2:
                    wkState.setText("已到达");
                    break;
                case 3:
                    wkState.setText("已完成");
                    break;
                default:
                    wkState.setText("——");
                    break;
            }
            return view;
        }

        int getSelectedItem() {
            return selectedItem;
        }

        void setSelectedItem(int selectedItem) {
            this.selectedItem = selectedItem;
            notifyDataSetChanged();
        }

        String getSelectedId() {
            return selectedId;
        }

        void setSelectedId(String selectedId) {
            this.selectedId = selectedId;
            notifyDataSetChanged();
        }
    }
}
