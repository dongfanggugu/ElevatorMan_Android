package com.honyum.elevatorMan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.honyum.elevatorMan.R;

public class MainActivity extends Activity {

	private MapView mMapView = null;
	private BaiduMap baiduMap;
	
	//定位
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap= mMapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        markMyLocation(baiduMap, 39.963175, 116.400244);
        
        //定位
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        
        
        findViewById(R.id.location).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mLocationClient.start();
				mLocationClient.requestLocation();
			}
        	
        });
//        mLocationClient.requestLocation();
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
	}
    
	
	public class MyListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null == location) {
				Log.i("baidu", "location null");
				return;
			}
			markMyLocation(baiduMap, location.getLatitude(), location.getLongitude());

			StringBuffer sb = new StringBuffer(256);
			sb.append("timee:");
			sb.append(location.getTime());
			
			sb.append("\nerror code:");
			sb.append(location.getLocType());
			
			sb.append("\nlatitude:");
			sb.append(location.getLatitude());
			
			sb.append("\nlongtitude:");
			sb.append(location.getLongitude());
			
			sb.append("\nradius:");
			sb.append(location.getRadius());
			
			sb.append("\ndirection");
			sb.append(location.getDirection());
			
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			} 
		}

	}
	
	/**
	 * 根据给定坐标标记位置
	 * @param latitude
	 * @param longtitude
	 */
	public void markMyLocation(BaiduMap bMap, double latitude,
			double longtitude) {

		//标记图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.mark);
		
		
		LatLng point = new LatLng(latitude, longtitude);

		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap).zIndex(9).draggable(true);

		bMap.addOverlay(option);

		//点击标记时显示信息
		bMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				LatLng point = marker.getPosition();

				TextView textView = new TextView(MainActivity.this);
				textView.setText("lat:" + point.latitude + "\n"
						+ "long:" + point.longitude);
				InfoWindow infoWindow = new InfoWindow(textView, point, -100);
				baiduMap.showInfoWindow(infoWindow);
				return false;
			}

		});
	}  
}
