package com.honyum.elevatorMan.activity.property;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.MaintenanceProjectInfo;
import com.honyum.elevatorMan.data.PropertyAddressInfo;
import com.honyum.elevatorMan.net.AddContactMaintRequest;
import com.honyum.elevatorMan.net.AllCommunitysResponse;
import com.honyum.elevatorMan.net.AllMaintenanceProjectRequest;
import com.honyum.elevatorMan.net.PropertyAddressListRequest;
import com.honyum.elevatorMan.net.PropertyAddressListResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearMaintenanceActivity extends BaseFragmentActivity {

    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private Map<Marker, MaintenanceProjectInfo> markerMap;

    private Marker PerAddMarker;

    private List<PropertyAddressInfo> propertyAddressInfos;

    private LatLng stationPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(this.getApplicationContext());
        setContentView(R.layout.activity_near_maintenance);

        markerMap = new HashMap<Marker, MaintenanceProjectInfo>();

        initTitleBar();

        initMapView();

        requestMtProject();

        requestAdd();
    }

    /**
     * 获取常驻地址
     */
    private void requestAdd() {

        String server = getConfig().getServer() + NetConstant.GET_PROPERTY_ADDRESS_LIST;

        PropertyAddressListRequest request = new PropertyAddressListRequest();
        RequestHead head = new RequestHead();
        PropertyAddressListRequest.PalReqBody body = request.new PalReqBody();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setBranchId(getConfig().getBranchId());

        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                PropertyAddressListResponse pal = PropertyAddressListResponse.getPal(result);

                propertyAddressInfos = pal.getBody();

                showDialog();
            }
        };

        addTask(netTask);
    }


    private void showDialog() {

        if (propertyAddressInfos == null || propertyAddressInfos.size() == 0) {
            showToast("暂无驻点地址");
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        dialog.setTitle("请选择驻点地址");
        dialog.setCancelable(false);

        ListView listView = new ListView(this);
        final MyAdapter adapter = new MyAdapter(propertyAddressInfos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PropertyAddressInfo info = (PropertyAddressInfo) adapter.getItem(position);

                markPerAdd(info);

                dialog.dismiss();
            }
        });

        dialog.setView(listView);

        dialog.show();
    }

    private void markPerAdd(PropertyAddressInfo info) {

        if (info.getLng() == 0 || info.getLat() == 0) {
            return;
        }

        stationPoint = new LatLng(info.getLat(), info.getLng());

        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(stationPoint, 15);
        mBaiduMap.animateMapStatus(update);

        MarkerOptions options = (MarkerOptions) initOptions(R.drawable.marker_alarm_old);
        options.position(stationPoint);

        if (PerAddMarker == null) {
            PerAddMarker = (Marker) mBaiduMap.addOverlay(options);
        }

        PerAddMarker.setPosition(stationPoint);
    }


    private void initTitleBar() {
        initTitleBar(R.id.title, "附近维保",
                R.drawable.back_normal, backClickListener,
                R.drawable.add_switch, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                    }
                });
    }


    private void initMapView() {
        mMapView = (MapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        //隐藏百度Logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                final MaintenanceProjectInfo info = markerMap.get(marker);

                if (info == null) {
                    return false;
                }

                LatLng point = new LatLng(info.getLat(), info.getLng());

                View view = View.inflate(NearMaintenanceActivity.this, R.layout.layout_mtpro_infowindow, null);

                TextView tvProName = (TextView) view.findViewById(R.id.tv_pro_name);
                tvProName.setText(info.getName());

//                TextView tvBranchName = (TextView) view.findViewById(R.id.tv_branch_name);
//                tvBranchName.setText(info.getBranchName());

                TextView tvProTel = (TextView) view.findViewById(R.id.tv_pro_tel);
                tvProTel.setText(info.getProjectTela());
                tvProTel.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN && !TextUtils.isEmpty(info.getProjectTela())) {
                            addContactMaint(info.getId());
                        }
                        return false;
                    }
                });

                TextView tvProAddress = (TextView) view.findViewById(R.id.tv_pro_address);
                tvProAddress.setText(info.getAddress());

                TextView tvEtBrand = (TextView) view.findViewById(R.id.tv_et_brand);
                if (TextUtils.isEmpty(info.getBrand())) {
                    tvEtBrand.setText("暂无");
                } else {
                    tvEtBrand.setText(info.getBrand());
                }

                TextView tvProDistance = (TextView) view.findViewById(R.id.tv_pro_distance);
                if (stationPoint == null) {
                    tvProDistance.setText("—— 公里");
                } else {
                    double distance = DistanceUtil.getDistance(point, stationPoint) / 1000;
                    DecimalFormat df = new DecimalFormat("0.00");
                    String dis = df.format(distance);
                    tvProDistance.setText("" + dis + " 公里");
                }

                InfoWindow infoWindow = new InfoWindow(view, point, -50);

                mBaiduMap.showInfoWindow(infoWindow);

                return true;
            }
        });
    }

    /**
     * 添加物业联系维保公司记录
     *
     * @param id 项目ID
     */
    private void addContactMaint(String id) {
        String server = getConfig().getServer() + NetConstant.ADD_CONTACT_MAINT;

        AddContactMaintRequest request = new AddContactMaintRequest();
        RequestHead head = new RequestHead();
        AddContactMaintRequest.AddCmReqBody body = request.new AddCmReqBody();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setCommunityId(id);

        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
            }
        };

        addTask(netTask);
    }

    /**
     * 请求所有的维保项目
     */
    private void requestMtProject() {

        String server = getConfig().getServer() + NetConstant.GET_ALL_COMMUNITYS_BY_PROPERTY;

        AllMaintenanceProjectRequest request = new AllMaintenanceProjectRequest();
        RequestHead head = new RequestHead();
        AllMaintenanceProjectRequest.AllMaintenanceProjectReqBody body
                = request.new AllMaintenanceProjectReqBody();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                AllCommunitysResponse response = AllCommunitysResponse.getAllCommunity(result);
                List<MaintenanceProjectInfo> body = response.getBody();

                for (MaintenanceProjectInfo info : body) {
                    markMtPro(info);
                }
            }
        };

        addTask(netTask);
    }

    private void markMtPro(MaintenanceProjectInfo info) {

        if (info.getLng() == 0 || info.getLat() == 0) {
            return;
        }

        LatLng point = new LatLng(info.getLat(), info.getLng());

        MarkerOptions options = (MarkerOptions) initOptions(R.drawable.marker_project);
        options.position(point);

        Marker marker = (Marker) mBaiduMap.addOverlay(options);
        markerMap.put(marker, info);
    }


    private OverlayOptions initOptions(int res) {
        View view = View.inflate(this, R.layout.layout_location_marker, null);
        ImageView imgMarker = (ImageView) view.findViewById(R.id.img_marker);
        imgMarker.setImageResource(res);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromView(view);
        return new MarkerOptions().icon(bitmapDescriptor).zIndex(9)
                .draggable(false);
    }

    private class MyAdapter extends BaseAdapter {

        private List<PropertyAddressInfo> propertyAddressInfos;

        public MyAdapter(List<PropertyAddressInfo> propertyAddressInfos) {
            this.propertyAddressInfos = propertyAddressInfos;
        }

        @Override
        public int getCount() {
            return propertyAddressInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return propertyAddressInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = View.inflate(NearMaintenanceActivity.this, R.layout.layout_list_text1_item, null);
            }

            PropertyAddressInfo info = propertyAddressInfos.get(position);


            TextView tvTexT = (TextView) view.findViewById(R.id.tv_text);
            tvTexT.setText(info.getAddress());

            return view;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterCallStateListener();
    }
}
