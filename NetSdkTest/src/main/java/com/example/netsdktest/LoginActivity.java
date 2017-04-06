package com.example.netsdktest;

import com.example.manager.DeviceManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.chorstar.video.LiftVideoActivity;

import java.text.DecimalFormat;

public class LoginActivity extends Activity
{
	EditText editAddress;	
	private ProgressDialog mProgressDlg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_hb);
		
		editAddress = (EditText)findViewById(R.id.edit_ip);
		
		((Button)findViewById(R.id.btn_login)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				login();
			}
		});


	}

	private void login()
	{
		//获取Ip
		final String strIp = editAddress.getText().toString().trim();
		if (strIp.length() == 0)
		{
			return;
		}
		
		//获取端口
		String strPort = ((EditText)findViewById(R.id.edit_port)).getText().toString().trim();
		int nValue = 0;
		try
		{
			nValue = Integer.parseInt(strPort);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		
		if (nValue <= 0 || nValue > 65535)
		{
			return;
		}
		
		//获取用户名
		final String strName = ((EditText)findViewById(R.id.edit_name)).getText().toString().trim();
		if (strName.length() == 0)
		{
			return;
		}
		
		//获取密码
		final String strPassword = ((EditText)findViewById(R.id.edit_password)).getText().toString().trim();
		if (strPassword.length() == 0)
		{
			return;
		}
		
		//弹出进度框
		mProgressDlg = ProgressDialog.show(this, null, "正在登录...", true, false);
		
		//登录
		final int nPort = nValue;
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				//DeviceManager device = ((NetSdkTestApplication)getApplication()).device;
                DeviceManager device = DeviceManager.getInstance();

				final int nResult = device.login(strName, strPassword, strIp, nPort);
                //final int nResult = device.login("admin", "888888", "223.223.199.57", 18101);
				mProgressDlg.dismiss();
				
				editAddress.post(new Runnable()
				{
					
					@Override
					public void run()
					{
						// TODO Auto-generated method stub
						if (0 != nResult)
						{
							Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
						}
						else
						{
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							startActivity(intent);

							finish();
						}
					}
				});
			}
		}).start();	
	}
}
