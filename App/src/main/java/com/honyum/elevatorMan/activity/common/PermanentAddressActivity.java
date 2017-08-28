package com.honyum.elevatorMan.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.property.AddPermanentAddressActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.PropertyAddressInfo;
import com.honyum.elevatorMan.net.PropertyAddressListRequest;
import com.honyum.elevatorMan.net.PropertyAddressListResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.util.List;

public class PermanentAddressActivity extends BaseFragmentActivity {

    private MapView mapView;

    private BaiduMap baiduMap;

    private Marker marker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(this.getApplicationContext());

        setContentView(R.layout.activity_permanent_address);

        initTitleBar();

        initMapView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        requestAdd();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

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

                if (pal.getBody() == null) {
                    return;
                }
                List<PropertyAddressInfo> body = pal.getBody();

                initListView(body);
            }
        };

        addTask(netTask);
    }

    private void initListView(List<PropertyAddressInfo> body) {
        ListView listView = (ListView) findViewById(R.id.listView);
        final MyAdapter adapter = new MyAdapter(body);
        listView.setAdapter(adapter);

        listView.setEmptyView(findViewById(R.id.tv_empty_tip));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (adapter.getSelectedItem() == position) {
                    return;
                }

                PropertyAddressInfo info = (PropertyAddressInfo) adapter.getItem(position);

                if (info.getLat() == 0 || info.getLng() == 0) {
                    showToast("位置信息不明确");
                    return;
                }

                adapter.setSelectedItem(position);

                markLocation(info);
            }
        });
    }


    private void markLocation(PropertyAddressInfo info) {

        LatLng point = new LatLng(info.getLat(), info.getLng());

        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(point, 15);
        baiduMap.animateMapStatus(update);

        MarkerOptions options = (MarkerOptions) initOptions();
        options.position(point);

        if (marker == null) {
            marker = (Marker) baiduMap.addOverlay(options);
        }

        marker.setPosition(point);
    }

    /**
     * 返回marker的基本设置
     */
    private OverlayOptions initOptions() {
        View view = View.inflate(this, R.layout.layout_location_marker, null);
        ImageView imgMarker = (ImageView) view.findViewById(R.id.img_marker);
        imgMarker.setImageResource(R.drawable.marker_alarm_old);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromView(view);
        return new MarkerOptions().icon(bitmapDescriptor).zIndex(9)
                .draggable(false);
    }


    private void initMapView() {
        mapView = (MapView) findViewById(R.id.mapView);
        baiduMap = mapView.getMap();
    }

    private void initTitleBar() {
        initTitleBar(R.id.title, "常驻地址",
                R.drawable.back_normal, backClickListener,
                R.drawable.icon_add, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PermanentAddressActivity.this,
                                AddressActivity.class);
                        intent.putExtra("category","wy");
                        startActivity(intent);
                    }
                });

    }


    private class MyAdapter extends BaseAdapter {

        private int selectedItem = -1;

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
                view = View.inflate(PermanentAddressActivity.this, R.layout.layout_list_text1_item, null);
            }

            PropertyAddressInfo info = propertyAddressInfos.get(position);

            if (position == selectedItem) {
                view.setBackgroundColor(getResources().getColor(R.color.grey));
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.transparent));
            }


            TextView tvTexT = (TextView) view.findViewById(R.id.tv_text);
            tvTexT.setText(info.getAddress());

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
