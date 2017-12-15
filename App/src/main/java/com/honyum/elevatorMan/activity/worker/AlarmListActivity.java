package com.honyum.elevatorMan.activity.worker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.fragment.AlarmListFragment;
import com.honyum.elevatorMan.fragment.AlarmMapFragment;
import com.honyum.elevatorMan.net.AlarmListRequest;
import com.honyum.elevatorMan.net.AlarmListResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.util.List;
//import com.honyum.elevatorMan.utils.AlarmSqliteUtils;

public class AlarmListActivity extends WorkerBaseActivity {

    public static final String TAG = "AlarmListActivity";

    private List<AlarmInfo> mAlarmListInProcess;

    private List<AlarmInfo> mAlarmListFinished;

    private List<AlarmInfo> mAlarmListReceived;

    private ListView mListView;

    private int mCurrentPage = 0;

    private ImageView mIvAlarm, mIvAssigned, mIvHistory;

    private TextView mTvAlarm, mTvAssigned, mTvHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_alarm_list);
        setContentView(R.layout.activity_alarm_list_new);

        initTitleBar();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar(getString(R.string.title_alarm_list), R.id.title_alarm_list,
                R.drawable.back_normal, backClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();

//        if (0 == mCurrentPage) {
//            requestUnassignedAlarmList();
//        } else if (1 == mCurrentPage) {
//            requestUnfinishedAlarmList();
//        } else if (2 == mCurrentPage) {
//            requestFinishedAlarmList();
//        }

    }

    public void loadAlarmUnAssigned() {
        AlarmMapFragment fragment = AlarmMapFragment.newInstance(this, getConfig());
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.ll_content, fragment);
        transaction.commit();
    }

    public void loadAlarmAssigned() {
        AlarmListFragment fragment = AlarmListFragment.newInstance(this, getConfig(), AlarmListFragment.TYPE_ASSIGNED);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.ll_content, fragment);
        transaction.commit();
    }

    public void loadAlarmHistory() {
        AlarmListFragment fragment = AlarmListFragment.newInstance(this, getConfig(), AlarmListFragment.TYPE_HISTORY);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.ll_content, fragment);
        transaction.commit();
    }

    /**
     * 初始化视图
     */
    private void initView() {

        //约定0和1
        if(getIntent().hasExtra("newCode"))
        {
            mCurrentPage = getIntent().getIntExtra("newCode",0);
            findViewById(R.id.ll_bottom).setVisibility(View.GONE);
            if(mCurrentPage == 0)
            initTitleBar("报警处置", R.id.title_alarm_list,
                    R.drawable.back_normal, backClickListener);
            else
                initTitleBar("接警汇总", R.id.title_alarm_list,
                        R.drawable.back_normal, backClickListener);

        }

        //接收到的报警
        findViewById(R.id.ll_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == mCurrentPage) {
                    return;
                }
                mCurrentPage = 0;
                loadAlarmUnAssigned();
                updateBottomStyle();
            }
        });

        //已经指派的任务
        findViewById(R.id.ll_assigned).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 == mCurrentPage) {
                    return;
                }
                mCurrentPage = 1;
                loadAlarmAssigned();
                updateBottomStyle();
            }
        });

        //已经完成的任务
        findViewById(R.id.ll_history).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (2 == mCurrentPage) {
                    return;
                }
                mCurrentPage = 2;
                loadAlarmHistory();
                updateBottomStyle();
            }
        });

        if (0 == mCurrentPage) {

            loadAlarmUnAssigned();
        } else if (1 == mCurrentPage) {
            loadAlarmAssigned();
        } else if (2 == mCurrentPage) {
            loadAlarmHistory();
        }

        mIvAlarm = (ImageView) findViewById(R.id.iv_alarm);
        mIvAssigned = (ImageView) findViewById(R.id.iv_assigned);
        mIvHistory = (ImageView) findViewById(R.id.iv_history);

        mTvAlarm = (TextView) findViewById(R.id.tv_alarm);
        mTvAssigned = (TextView) findViewById(R.id.tv_assigned);
        mTvHistory = (TextView) findViewById(R.id.tv_history);

        updateBottomStyle();
    }

    /**
     * 展示报警信息列表
     */
    private void showAlarmList() {

        if (0 == mCurrentPage) {
            mListView.setAdapter(new MyAdapter(this, mAlarmListReceived));

        } else if (1 == mCurrentPage) {
            mListView.setAdapter(new MyAdapter(this, mAlarmListInProcess));

        } else if (2 == mCurrentPage) {
            mListView.setAdapter(new MyAdapter(this, mAlarmListFinished));

        }
        setListener(mListView);
    }


    /**
     * 设置列表点击事件
     *
     * @param listView
     */
    private void setListener(ListView listView) {
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                final AlarmInfo alarmInfo = (AlarmInfo) view.getTag();

                if (!StringUtils.isEmpty(alarmInfo.getIsMisinformation())
                        && alarmInfo.getIsMisinformation().equals("1")) {
                    //报警撤销
                    Log.i("zhenhao", "alarm canceled!");

                } else {

                    if (alarmInfo.getUserState().equals(Constant.WORKER_STATE_RECEIVED)) { //报警通知到维修工


                        Intent intent = new Intent(AlarmListActivity.this, WorkerActivity.class);
                        intent.putExtra("alarm_id", alarmInfo.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Constant.ACTION_ALARM_RECEIVED);

                        //定义跳转来源，用于title显示后退按钮
                        intent.putExtra("from", TAG);
                        startActivity(intent);


                    } else if (alarmInfo.getUserState().equals(Constant.WORKER_STATE_START)) {   //已出发
                        Intent intent = new Intent(AlarmListActivity.this, WorkerActivity.class);
                        intent.putExtra("alarm_id", alarmInfo.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Constant.ACTION_ALARM_ASSIGNED);

                        //定义跳转来源，用于title的显示
                        intent.putExtra("from", TAG);
                        startActivity(intent);
                    } else if (alarmInfo.getUserState().equals(Constant.WORKER_STATE_ARRIVED)) {  //已到达
                        Intent intent = new Intent(AlarmListActivity.this, RescuProcessActivity.class);
                        intent.putExtra("alarm_id", alarmInfo.getId());

                        //定义跳转来源，用于title的显示
                        intent.putExtra("from", TAG);
                        startActivity(intent);
                    }
                }

            }

        });
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

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setScope(scope);

        request.setHead(head);
        request.setBody(body);

        return request;
    }


    /**
     * 请求未完成报警列表
     */
    private void requestUnfinishedAlarmList() {
        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_WORKER_ALARM_LIST,
                getAlarmListRequest("unfinished")) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub

                AlarmListResponse response = AlarmListResponse.getAlarmListResponse(result);
                mAlarmListInProcess = response.getBody();
                showAlarmList();
            }

        };

        addTask(netTask);
    }


    private void requestFinishedAlarmList() {
        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_WORKER_ALARM_LIST,
                getAlarmListRequest("finished")) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub

                AlarmListResponse response = AlarmListResponse.getAlarmListResponse(result);
//                if (response.getBody() != null) {
//                    filterList(response.getBody());
//                }
                mAlarmListFinished = response.getBody();
                showAlarmList();
            }

        };

        addTask(netTask);
    }


    /**
     * ListView 适配器
     *
     * @author chang
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

            AlarmInfo alarmInfo = mAlarmInfoList.get(position);
            tvIndex.setText("" + (position + 1));
            tvIndex.setTag(position);
            if (0 == mCurrentPage) {
                tvProject.setText(mAlarmInfoList.get(position).getCommunityInfo().getName());
                tvDate.setText(mAlarmInfoList.get(position).getAlarmTime());
                mAlarmInfoList.get(position).setUserState("-1");
            } else {

                tvProject.setText(mAlarmInfoList.get(position).getCommunityInfo().getName());
                tvDate.setText(mAlarmInfoList.get(position).getAlarmTime());
            }

            if (!StringUtils.isEmpty(alarmInfo.getIsMisinformation()) && alarmInfo.getIsMisinformation().equals("1")) {
                tvState.setText("已撤消");
            } else {
                tvState.setText(getStringByState(mAlarmInfoList.get(position).getUserState()));
            }


            setIndexColor(tvIndex);
            setStateColor(mAlarmInfoList.get(position).getState(), tvState);

            convertView.setTag(mAlarmInfoList.get(position));
            return convertView;
        }

    }

    /**
     * 请求没有指派的任务
     */
    private void requestUnassignedAlarmList() {

        Config config = getConfig();
        String server = config.getServer();

        NetTask task = new NetTask(server + NetConstant.URL_ALARM_UNASSIGNED, getAlarmListRequest("")) {
            @Override
            protected void onResponse(NetTask task, String result) {
                AlarmListResponse response = AlarmListResponse.getAlarmListResponse(result);
                mAlarmListReceived = response.getBody();

                //过滤掉已经超过设置的超时时间的报警
//                int waitSecond = getConfig().getAlarmWaitTime();
//                Iterator<AlarmInfo> iterator = mAlarmListReceived.iterator();
//                long currentMillions = new Date().getTime();
//
//
//                while (iterator.hasNext()) {
//                    AlarmInfo info = iterator.next();
//                    long alarmMillions = com.honyum.elevatorMan.utils.Utils.stringToMillions(info.getAlarmTime());
//                    long interval = currentMillions - alarmMillions;
//
//                    if (interval > waitSecond * 1000 * 1.5) {
//                        iterator.remove();
//                    }
//                }

                showAlarmList();
            }
        };

        addTask(task);
    }

    /**
     * 更新底部显示风格
     */
    private void updateBottomStyle() {
        if (0 == mCurrentPage) {
            mIvAlarm.setImageResource(R.drawable.alarm_pressed);
            mTvAlarm.setTextColor(getResources().getColor(R.color.color_alarm_bottom_text));

            mIvAssigned.setImageResource(R.drawable.assigned_normal);
            mTvAssigned.setTextColor(getResources().getColor(R.color.grey));

            mIvHistory.setImageResource(R.drawable.history_normal);
            mTvHistory.setTextColor(getResources().getColor(R.color.grey));

        } else if (1 == mCurrentPage) {
            mIvAlarm.setImageResource(R.drawable.alarm_normal);
            mTvAlarm.setTextColor(getResources().getColor(R.color.grey));

            mIvAssigned.setImageResource(R.drawable.assigned_pressed);
            mTvAssigned.setTextColor(getResources().getColor(R.color.color_alarm_bottom_text));

            mIvHistory.setImageResource(R.drawable.history_normal);
            mTvHistory.setTextColor(getResources().getColor(R.color.grey));

        } else if (2 == mCurrentPage) {
            mIvAlarm.setImageResource(R.drawable.alarm_normal);
            mTvAlarm.setTextColor(getResources().getColor(R.color.grey));

            mIvAssigned.setImageResource(R.drawable.assigned_normal);
            mTvAssigned.setTextColor(getResources().getColor(R.color.grey));

            mIvHistory.setImageResource(R.drawable.history_pressed);
            mTvHistory.setTextColor(getResources().getColor(R.color.color_alarm_bottom_text));
        }
    }
}