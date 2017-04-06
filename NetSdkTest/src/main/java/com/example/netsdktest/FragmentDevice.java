package com.example.netsdktest;

import com.example.manager.DeviceManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FragmentDevice extends Fragment
{
	DeviceManager device;
	EditText editName;
	
	ProgressDialog progressDlg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_device_hb, container, false);
		
		//获取登录的设备
		//device = ((NetSdkTestApplication)getActivity().getApplication()).device;
        device = DeviceManager.getInstance();
		
		//设备名
		editName = (EditText)view.findViewById(R.id.edit_deviceName);
		editName.setText(device.getDeviceName());
		
		//设备型号
		((EditText)view.findViewById(R.id.edit_deviceType)).setText(device.getDeviceMode());
		
		//版本
		((EditText)view.findViewById(R.id.edit_version)).setText(device.getDeviceVersion());		
		
		//序列号
		((EditText)view.findViewById(R.id.edit_deviceSn)).setText(device.getSerialNo());
		
		//通道数
		String channelCount = Integer.toString(device.getChannelCount());
		((EditText)view.findViewById(R.id.edit_channelCount)).setText(channelCount);
		
		//保存
		((Button)view.findViewById(R.id.btn_save)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				onSave();
			}
		});
		
		//注销
		((Button)view.findViewById(R.id.btn_logout)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				new Thread(new Runnable()
				{
					
					@Override
					public void run()
					{
						// TODO Auto-generated method stub
						//注销设备之前，要关闭预览、回放、语音对讲等功能，避免内存泄露
						device.logout();
						
						//跳转到登录页面
						Intent intent = new Intent(getActivity(), LoginActivity.class);
						getActivity().startActivity(intent);
						
						getActivity().finish();
					}
				}).start();
			}
		});
		
		return view;
	}
	
	void onSave()
	{
		//修改设备名
		final String name = editName.getText().toString();
		if (0 == name.length())
		{
			Toast.makeText(getActivity(), "设备名不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		//弹出提示框
		progressDlg = ProgressDialog.show(getActivity(), null, "正在保存...", true, false);
		
		//起线程处理网络请求
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				final boolean bSuccess = device.setDeviceName(name);
				
				progressDlg.dismiss();
				progressDlg = null;
				
				editName.post(new Runnable()
				{
					
					@Override
					public void run()
					{
						// TODO Auto-generated method stub
						String strMessage = bSuccess ? "成功" : "失败";
						Toast.makeText(getActivity(), strMessage, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}).start();
	}

}
