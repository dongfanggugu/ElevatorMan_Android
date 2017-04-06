package com.honyum.elevatorMan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.honyum.elevatorMan.constant.Constant;

public class LocationReceiver extends BroadcastReceiver {

	private ILocationComplete mLocationComplete;
	
	public LocationReceiver(ILocationComplete locationComplete) {
		mLocationComplete = locationComplete;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Constant.ACTION_LOCATION_COMPLETE)) {
			if (null == mLocationComplete) {
				return;
			}
			double latitude = intent.getDoubleExtra("lat", 0);
			double longitude = intent.getDoubleExtra("long", 0);
			String address = intent.getStringExtra("add");
			mLocationComplete.onLocationComplete(latitude, longitude, address);
		}
	}
	
	public interface ILocationComplete {
		public void onLocationComplete(double latitude, double longitude,
				String address);
	}
}