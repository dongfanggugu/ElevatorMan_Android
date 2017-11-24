package com.honyum.elevatorMan.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.worker.AlarmDetailActivity;
import com.honyum.elevatorMan.activity.worker.RescuProcessActivity;
import com.honyum.elevatorMan.activity.worker.WorkerActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.net.AlarmListRequest;
import com.honyum.elevatorMan.net.AlarmListResponse;
import com.honyum.elevatorMan.net.ChatListResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import org.litepal.crud.DataSupport;

import java.util.List;


public class AlarmListFragment extends Fragment {

    private BaseFragmentActivity mContext;

    private Config mConfig;

    private View mView;

    public static int TYPE_ASSIGNED = 0;

    public static int TYPE_HISTORY = 1;

    private int mType;

    private List<AlarmInfo> mAlarmList;

    private ListView mListView;

    // TODO: Rename and change types and number of parameters
    public static AlarmListFragment newInstance(BaseFragmentActivity context, Config config, int type) {
        AlarmListFragment fragment = new AlarmListFragment();
        fragment.mContext = context;
        fragment.mConfig = config;
        fragment.mType = type;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_alarm_assigned, container, false);
        mListView = (ListView) mView.findViewById(R.id.list_alarm);
        if (mType == TYPE_ASSIGNED) {
            requestUnfinishedAlarmList();
        } else if (mType == TYPE_HISTORY) {
            requestFinishedAlarmList();
        }
        return mView;
    }


    /**
     * ListView 适配器
     *
     * @author chang
     */
    public class MyAdapter extends BaseAdapter {

        private Context context;
        private List<AlarmInfo> alarmInfoList;

        public MyAdapter(Context context, List<AlarmInfo> alarmInfoList) {
            this.context = context;
            this.alarmInfoList = alarmInfoList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return alarmInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return alarmInfoList.get(position);
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
                convertView = View.inflate(context, R.layout.layout_alarm_item, null);
            }

            TextView tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
            TextView tvProject = (TextView) convertView.findViewById(R.id.tv_project);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            TextView tvState = (TextView) convertView.findViewById(R.id.tv_state);

            AlarmInfo alarmInfo = alarmInfoList.get(position);
            tvIndex.setText("" + (position + 1));
            tvIndex.setTag(position);

            tvProject.setText(alarmInfoList.get(position).getCommunityInfo().getName());
            tvDate.setText(alarmInfoList.get(position).getAlarmTime());
            if (!StringUtils.isEmpty(alarmInfo.getIsMisinformation()) && alarmInfo.getIsMisinformation().equals("1")) {
                tvState.setText("已撤消");
            } else {
                tvState.setText(mContext.getStringByState(alarmInfoList.get(position).getUserState()));
            }

            mContext.setIndexColor(tvIndex);
            mContext.setStateColor(alarmInfoList.get(position).getUserState(), tvState);

            convertView.setTag(alarmInfoList.get(position));
            return convertView;
        }

    }


    /**
     * 请求报警列表
     *
     * @return
     */
    private RequestBean getAlarmListRequest(String scope) {
        AlarmListRequest request = new AlarmListRequest();
        AlarmListRequest.AlarmListReqBody body = request.new AlarmListReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(mConfig.getUserId());
        head.setAccessToken(mConfig.getToken());

        body.setScope(scope);

        request.setHead(head);
        request.setBody(body);

        return request;
    }


    /**
     * 请求未完成报警列表
     */
    private void requestUnfinishedAlarmList() {
        NetTask netTask = new NetTask(mConfig.getServer() + NetConstant.URL_WORKER_ALARM_LIST,
                getAlarmListRequest("unfinished")) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub

                AlarmListResponse response = AlarmListResponse.getAlarmListResponse(result);
                mAlarmList = response.getBody();
                showAlarmList();
            }

        };

        mContext.addTask(netTask);
    }


    private void requestFinishedAlarmList() {
        NetTask netTask = new NetTask(mConfig.getServer() + NetConstant.URL_WORKER_ALARM_LIST,
                getAlarmListRequest("finished")) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub

                AlarmListResponse response = AlarmListResponse.getAlarmListResponse(result);
//                if (response.getBody() != null) {
//                    filterList(response.getBody());
//                }
                mAlarmList = response.getBody();
                showAlarmList();
            }

        };

        mContext.addTask(netTask);
    }


    /**
     * 展示报警信息列表
     *
     */
    private void showAlarmList() {
        if (null == mAlarmList || 0 == mAlarmList.size()) {
            String tip = "";
            if (mType == TYPE_ASSIGNED) {
                tip = "当前没有进行中的报警信息!!";
            } else {
                tip = "您还没有处理完成的报警信息!!";
            }
            TextView tvTip = (TextView) mView.findViewById(R.id.tip);
            tvTip.setText(tip);
            tvTip.setVisibility(View.VISIBLE);
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (AlarmInfo alarmInfo : mAlarmList) {
            sb.append(",'"+alarmInfo.getId()+"'");
        }
        String ids = sb.toString().trim().replaceFirst(",","");

        DataSupport.deleteAll(ChatListResponse.ChatListBody.class,"alarmId not in( "+ids+")");
        MyAdapter adapter = new MyAdapter(mContext, mAlarmList);
        mListView.setAdapter(adapter);
        setListener(mListView);
    }


    /**
     * 设置列表点击事件
     * @param listView
     */
    private void setListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                final AlarmInfo alarmInfo = (AlarmInfo) view.getTag();

                if (!StringUtils.isEmpty(alarmInfo.getIsMisinformation())
                        && alarmInfo.getIsMisinformation().equals("1")) {
                    //报警撤销
                    Log.i("zhenhao", "alarm canceled!");
                    mContext.showToast("该报警已经撤销!");

                } else {

                    if (alarmInfo.getUserState().equals(Constant.WORKER_STATE_RECEIVED)) { //报警通知到维修工


                        Intent intent = new Intent(mContext, WorkerActivity.class);
                        intent.putExtra("alarm_id", alarmInfo.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Constant.ACTION_ALARM_RECEIVED);

                        //定义跳转来源，用于title显示后退按钮
                        startActivity(intent);


                    } else if (alarmInfo.getUserState().equals(Constant.WORKER_STATE_START)) {   //已出发
                        Intent intent = new Intent(mContext, WorkerActivity.class);
                        intent.putExtra("alarm_id", alarmInfo.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Constant.ACTION_ALARM_ASSIGNED);

                        //定义跳转来源，用于title的显示
                        startActivity(intent);
                    } else if (alarmInfo.getUserState().equals(Constant.WORKER_STATE_ARRIVED)) {  //已到达
                        Intent intent = new Intent(mContext, RescuProcessActivity.class);
                        intent.putExtra("alarm_id", alarmInfo.getId());
                        intent.putExtra("alarm_info", alarmInfo);

                        //定义跳转来源，用于title的显示
                        startActivity(intent);
                    } else if (alarmInfo.getUserState().equals(Constant.WORKER_STATE_COMPLETE)) {
                        Intent intent = new Intent(mContext, AlarmDetailActivity.class);
                        intent.putExtra("Id", alarmInfo.getId());
                        intent.putExtra("project", alarmInfo.getCommunityInfo().getName());
                        intent.putExtra("add", alarmInfo.getCommunityInfo().getAddress());
                        intent.putExtra("code", alarmInfo.getElevatorInfo().getLiftNum());
                        intent.putExtra("date", alarmInfo.getAlarmTime());
                        intent.putExtra("saved", alarmInfo.getSavedCount() + "");
                        intent.putExtra("injured", alarmInfo.getInjureCount() + "");
                        startActivity(intent);
                    }
                }

            }

        });
    }
}
