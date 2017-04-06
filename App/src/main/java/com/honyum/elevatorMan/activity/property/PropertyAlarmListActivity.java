package com.honyum.elevatorMan.activity.property;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.net.AlarmListRequest;
import com.honyum.elevatorMan.net.AlarmListResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

public class PropertyAlarmListActivity extends PropertyBaseActivity {

	public static final String TAG = "PropertyAlarmListActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_alarm_list);
		initTitleBar();
	}

    /**
     * 初始化视图
     * @param alarmInfoList
     */
	private void initView(List<AlarmInfo> alarmInfoList) {
		
		ListView listView = (ListView) findViewById(R.id.list_alarm);
		listView.setAdapter(new MyAdapter(this, alarmInfoList));
		setListener(listView);
	}

    @Override
    public void onResume() {
        super.onResume();
        requestAlarmList();
    }
	
	/**
	 * listview 适配器
	 * @author chang
	 *
	 */
	public class MyAdapter extends BaseAdapter {
		
		private Context mContext;
		private List<AlarmInfo> mAlarmInfoList;
		
		public MyAdapter(Context context, List<AlarmInfo> alarmInfoList) {
			mContext = context;
			mAlarmInfoList = alarmInfoList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mAlarmInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mAlarmInfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (null == convertView) {
				convertView = View.inflate(mContext, R.layout.layout_alarm_item, null);
			}
			
			
			TextView tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
			TextView tvProject = (TextView) convertView.findViewById(R.id.tv_project);
			TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			TextView tvState = (TextView) convertView.findViewById(R.id.tv_state);
			
			tvIndex.setText("" + (position + 1));
            tvIndex.setTag(position);
			tvProject.setText(mAlarmInfoList.get(position).getCommunityInfo().getName());
			tvDate.setText(mAlarmInfoList.get(position).getAlarmTime());
			tvState.setText(getStringByState(mAlarmInfoList.get(position).getState()));
			
			setIndexColor(tvIndex);
			setStateColor(mAlarmInfoList.get(position).getState(), tvState);
			
			convertView.setTag(mAlarmInfoList.get(position));

			return convertView;
		}
		
	}
	
	/**
	 * 设置点击监听
	 * @param listView
	 */
	private void setListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AlarmInfo alarmInfo = (AlarmInfo) view.getTag();

				Intent intent = new Intent(PropertyAlarmListActivity.this, AlarmTraceActivity.class);
				intent.putExtra("alarm_id", alarmInfo.getId());
				String alarmState = alarmInfo.getState();
				
				//保存当前报警信息到本地缓存，用于处理当前报警信息
				getConfig().setAlarmId(alarmInfo.getId());
				getConfig().setAlarmState(alarmState);
				
				if (alarmState.equals(Constant.ALARM_STATE_ASSIGNED)) {  //已经出发或者已经到达
					intent.setAction(Constant.ACTION_WORKER_ASSIGNED);
				
				} else if (alarmState.equals(Constant.ALARM_STATE_ARRIVED)) {   //已经到达
                    intent.setAction(Constant.ACTION_WORKER_ARRIVED);

                } else if (alarmState.equals(Constant.ALARM_STATE_START)) {	//发生报警
					intent.setAction(Constant.ACTION_ALARM_PROPERTY);
					
				} else if (alarmState.equals(Constant.ALARM_STATE_COMPLETE)) {		//已经完成
					intent.setAction(Constant.ACTION_ALARM_COMPLETE);
                    intent.putExtra("msg", getString(R.string.property_complete));
				}

                //定义跳转来源，用于title的显示
                intent.putExtra("from", TAG);
				startActivity(intent);
			}
			
		});
	}
	
	/**
	 * 初始化标题栏
	 */
	private void initTitleBar() {
		initTitleBar(getString(R.string.alarm_list), R.id.title_alarm_list, 
				R.drawable.navi_setting_normal, menuClickListener);
	}
	
	/**
	 * 请求报警列表BEAN
	 * @return
	 */
	private RequestBean getAlarmListRequest() {
		AlarmListRequest request = new AlarmListRequest();
		RequestHead head = new RequestHead();
		
		head.setUserId(getConfig().getUserId());
		head.setAccessToken(getConfig().getToken());
		
		request.setHead(head);
		
		return request;
	}
	
	/**
	 * 请求报警列表
	 */
	private void requestAlarmList() {
		NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_ALARM_LIST, 
				getAlarmListRequest()) {

			@Override
			protected void onResponse(NetTask task, String result) {
				// TODO Auto-generated method stub
				AlarmListResponse response = AlarmListResponse.getAlarmListResponse(result);
				if (null == response.getBody()) {
					return;
				}
				filterList(response.getBody());
			}
			
		};
		
		addTask(netTask);
	}
	
	/**
	 * 将已经完成的报警任务过滤
	 * @param infoList
	 */
	private void filterList(List<AlarmInfo> infoList) {
		List<AlarmInfo> alarmInfoList = new ArrayList<AlarmInfo>();
		for (AlarmInfo info : infoList) {
			if (!info.getUserState().equals(Constant.ALARM_STATE_CONFIRM)
                    && !info.getIsMisinformation().equals("1")) {
				alarmInfoList.add(info);
			}
		}
		
		//没有报警信息
		if (0 == alarmInfoList.size()) {
			showToast(getString(R.string.no_alarm));
		}
		initView(alarmInfoList);
	}
}