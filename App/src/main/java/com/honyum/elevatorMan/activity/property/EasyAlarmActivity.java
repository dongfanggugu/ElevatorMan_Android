package com.honyum.elevatorMan.activity.property;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.Elevator;
import com.honyum.elevatorMan.net.ReportAlarmRequest;
import com.honyum.elevatorMan.net.ReportAlarmRequest.ReportAlarmReqBody;
import com.honyum.elevatorMan.net.ReportAlarmResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

public class EasyAlarmActivity extends BaseFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_easy_alarm);
		initTitleBar();
		
		Intent intent = getIntent();		
		initView(intent);
	}
	
	/**
	 * 初始化view
	 * @param intent
	 */
	private void initView(final Intent intent) {
		((TextView) findViewById(R.id.tv_project)).setText(intent.getStringExtra("project"));
		((TextView) findViewById(R.id.tv_lift)).setText(intent.getStringExtra("code"));
		findViewById(R.id.btn_easy_alarm).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String id = intent.getStringExtra("id");
				String injured = ((CheckBox) findViewById(R.id.cb_injured)).isChecked() ? "1" : "0";
				String remark = ((EditText) findViewById(R.id.et_remark)).getText().toString();
				reportAlarm(id, injured, remark);
			}
			
		});
	}
	
	
	/**
	 * 物业报警
	 * 
	 * @param elevatorId
	 * @param injured
	 * @param description
	 */
	private void reportAlarm(String elevatorId, String injured,
			String description) {
		NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_REPORT_ALARM,
				getReportAlarmRequest(elevatorId, injured, description)) {

			@Override
			protected void onResponse(NetTask task, String result) {
				// TODO Auto-generated method stub
				ReportAlarmResponse response = ReportAlarmResponse
						.getReportAlarmResponse(result);
				String alarmId = response.getBody().getId();
				Intent intent = new Intent(EasyAlarmActivity.this,
						AlarmTraceActivity.class);
				intent.putExtra("alarm_id", alarmId);
				intent.setAction(Constant.ACTION_ALARM_PROPERTY);
				startActivity(intent);
			}

		};

		addTask(netTask);
	}

	/**
	 * 提交报警信息请求
	 * 
	 * @param elevatorId
	 * @param injured
	 * @param description
	 * @return
	 */
	private RequestBean getReportAlarmRequest(String elevatorId,
			String injured, String description) {
		ReportAlarmRequest request = new ReportAlarmRequest();
		ReportAlarmReqBody body = request.new ReportAlarmReqBody();
		RequestHead head = new RequestHead();
		

		head.setUserId(getConfig().getUserId());
		head.setAccessToken(getConfig().getToken());

		body.setLiftId(elevatorId);
		body.setIsInjure(injured);
		body.setRemark(description);

		request.setHead(head);
		request.setBody(body);

		return request;

	}
	
	/**
	 * 初始化标题栏
	 */
	private void initTitleBar() {
		initTitleBar(getString(R.string.easy_alarm), R.id.title,
				R.drawable.back_normal, backClickListener);
	}

	
}
