package com.honyum.elevatorMan.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class MapViewPager extends ViewPager {

	public MapViewPager(Context context) {
		super(context);
	}

	public MapViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		// Not satisfied with this method of checking..
		// working on a more robust solution
		if (v.getClass().getName().equals("com.baidu.mapapi.map.MapView")) {
			return true;
		}
		// if(v instanceof MapView){
		// return true;
		// }
		return super.canScroll(v, checkV, dx, x, y);
	}
}