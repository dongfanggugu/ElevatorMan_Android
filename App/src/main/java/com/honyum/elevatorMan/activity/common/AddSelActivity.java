package com.honyum.elevatorMan.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;

import java.util.List;

public class AddSelActivity extends BaseFragmentActivity {

    private BaiduMap mMap;

    private MapView mMapView;

    private PoiSearch poiSearch;

    private ListView mListView;

    private Marker mMarker;

    private String selAddress;

    //private MyAdapter myAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_add_sel);
        initTitleBar();
        initView();
    }

    private void initTitleBar() {
        initTitleBar("地址选择", R.id.title_add_sel,
                R.drawable.back_normal, backClickListener);

        View titleView = findViewById(R.id.title_add_sel);
        ImageButton btn = (ImageButton) titleView.findViewById(R.id.btn_query);
        btn.setVisibility(View.VISIBLE);
        btn.setImageResource(R.drawable.icon_add_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mMarker) {
                    showToast("请选择您的位置!");
                    return;
                }
                Intent intent = new Intent();
                LatLng point = mMarker.getPosition();
                intent.putExtra("latlng", new double[]{point.latitude, point.longitude});
                intent.putExtra("address", selAddress);
                setResult(0, intent);
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        // citySearch(1, getIntent().getStringExtra("add"));
    }

    OnGetPoiSearchResultListener poiSearchResultListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (null == poiResult
                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                showToast("没有找到结果");
                return;
            }

            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                mMap.clear();
                mListView.setVisibility(View.VISIBLE);
                Log.i("zhenhao", "size:" + poiResult.getAllPoi().size());
                MyAdapter myAdapter = new MyAdapter(AddSelActivity.this, poiResult.getAllPoi());
                mListView.setAdapter(myAdapter);
                setListener(mListView);

            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
                showToast("无法获取详细信息");
            } else {// 正常返回结果的时候，此处可以获得很多相关信息
                showToast(poiDetailResult.getName() + ": " + poiDetailResult.getAddress());
            }
        }
    };

    private void initView() {
        mMapView = (MapView) findViewById(R.id.map);
        mMap = mMapView.getMap();
        mListView = (ListView) findViewById(R.id.lv_result);


//        mMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDrag(Marker marker) {
//
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                mLat = marker.getPosition().latitude;
//                mLng = marker.getPosition().longitude;
//            }
//
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//
//            }
//        });
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(poiSearchResultListener);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkRemoteVersion(new ICheckVersionCallback() {
                    @Override
                    public void onCheckVersion(int remoteVersion, String url, int isForce, String description) {
                        citySearch(1, getIntent().getStringExtra("add"));
                    }
                });
            }
        }, 1500);

    }


    private void citySearch(int page, String address) {
        if (StringUtils.isEmpty(address)) {
            return;
        }
        PoiCitySearchOption options = new PoiCitySearchOption();
        options.city("北京市");
        options.keyword(address);
        options.pageCapacity(10);
        options.pageNum(page);
        poiSearch.searchInCity(options);
    }


    class MyAdapter extends BaseAdapter {

        private Context mContext;

        private List<PoiInfo> mList;

        private int selectedItem = -1;

        public MyAdapter(Context context, List<PoiInfo> list) {
            mContext = context;
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {

                TextView textView = new TextView(mContext);
                textView.setPadding(20, 40, 20, 40);
                textView.setTextSize(15);
                convertView = textView;
            }

            ((TextView) convertView).setText(mList.get(position).address);
            convertView.setTag(mList.get(position));
            if (position == selectedItem) {
                convertView.setBackgroundColor(getResources().getColor(R.color.grey));
            } else {
                convertView.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
            return convertView;
        }

        public void setSelectedItem(int selected) {
            selectedItem = selected;
        }
    }


    private void setListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MyAdapter adapter = (MyAdapter) parent.getAdapter();
                adapter.setSelectedItem(position);
                adapter.notifyDataSetChanged();

                PoiInfo info = (PoiInfo) view.getTag();

                selAddress = info.address;

                mMap.clear();
                markAddress(info.location);
                findViewById(R.id.tv_tip).setVisibility(View.VISIBLE);

                Log.i("zhenhao", "lat:" + info.location.latitude);
                Log.i("zhenhao", "lng:" + info.location.longitude);
            }
        });
    }


    /**
     * 标记报警
     */
    private void markAddress(LatLng point) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        mMarker = null;

        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
        mMap.animateMapStatus(update);

        MarkerOptions markerOption = (MarkerOptions) initAlarmOptions();
        markerOption.position(point);
        mMarker = (Marker) mMap.addOverlay(markerOption);
    }


    /**
     * 初始化报警标记选项
     *
     * @return
     */
    private OverlayOptions initAlarmOptions() {
        View view = View.inflate(this, R.layout.layout_location_marker, null);
        ImageView imgMarker = (ImageView) view.findViewById(R.id.img_marker);

        imgMarker.setImageResource(R.drawable.marker_worker);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromView(view);
        return new MarkerOptions().icon(bitmapDescriptor).zIndex(9)
                .draggable(true);
    }


//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent();
//        if (mMarker != null) {
//            LatLng point = mMarker.getPosition();
//            intent.putExtra("latlng", new double[] {point.latitude, point.longitude});
//        } else {
//            intent.putExtra("latlng", new double[]{});
//        }
//        setResult(0, intent);
//        super.onBackPressed();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
