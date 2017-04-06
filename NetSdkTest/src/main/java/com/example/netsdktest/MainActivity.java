package com.example.netsdktest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity 
{	
	FragmentDevice fragmentDevice;
	FragmentVideo fragmentVideo;
	FragmentManager fragmentManager;
	
	RadioGroup mRadioGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_hb);
		
		//添加切换页签的响应
		mRadioGroup = (RadioGroup)findViewById(R.id.tab_menu);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				handleLoadFragment(checkedId);
			}
		});	
		
		//初始化界面
		fragmentManager = getSupportFragmentManager();
		handleLoadFragment(R.id.radio_video);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void handleLoadFragment(int nRadioBtnId)
	{
		FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (nRadioBtnId == R.id.radio_video) {
            if (null != fragmentDevice)
            {
                transaction.hide(fragmentDevice);
            }

            if (null == fragmentVideo)
            {
                fragmentVideo = new FragmentVideo();
                transaction.add(R.id.viewContent, fragmentVideo);
            }
            else
            {
                transaction.show(fragmentVideo);
            }
        } else if (nRadioBtnId == R.id.radio_device) {
            if (null != fragmentVideo)
            {
                transaction.hide(fragmentVideo);
            }

            if (null == fragmentDevice)
            {
                fragmentDevice = new FragmentDevice();
                transaction.add(R.id.viewContent, fragmentDevice);
            }
            else
            {
                transaction.show(fragmentDevice);
            }
        }



//		switch (nRadioBtnId) {
//		case R.id.radio_video:
//			if (null != fragmentDevice)
//			{
//				transaction.hide(fragmentDevice);
//			}
//
//			if (null == fragmentVideo)
//			{
//				fragmentVideo = new FragmentVideo();
//				transaction.add(R.id.viewContent, fragmentVideo);
//			}
//			else
//			{
//				transaction.show(fragmentVideo);
//			}
//			break;
//		case R.id.radio_device:
//			if (null != fragmentVideo)
//			{
//				transaction.hide(fragmentVideo);
//			}
//
//			if (null == fragmentDevice)
//			{
//				fragmentDevice = new FragmentDevice();
//				transaction.add(R.id.viewContent, fragmentDevice);
//			}
//			else
//			{
//				transaction.show(fragmentDevice);
//			}
//			break;
//		default:
//			break;
//		}
		transaction.commit();
	}
}
