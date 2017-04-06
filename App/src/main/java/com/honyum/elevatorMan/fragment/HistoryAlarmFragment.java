package com.honyum.elevatorMan.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragment;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.net.AlarmListRequest;
import com.honyum.elevatorMan.net.AlarmListResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryAlarmFragment extends BaseFragment {


    private View mView;

    private BaseFragmentActivity mActivity;

    public HistoryAlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.activity_property_alarm_list, container, false);

        mActivity = (BaseFragmentActivity) getActivity();

        initTitleBar();

        requestAlarmList();

        return mView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mView != null && !hidden) {
            requestAlarmList();
        }
    }

    private void initView(List<AlarmInfo> alarmInfoList) {

        ListView listView = (ListView) mView.findViewById(R.id.list_alarm);
        listView.setAdapter(new MyAdapter(getActivity(), alarmInfoList));
    }

    /**
     * listview 适配器
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

            //历史列表中不需要查看详情
            convertView.findViewById(R.id.img_detail).setVisibility(View.GONE);

            TextView tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
            TextView tvProject = (TextView) convertView.findViewById(R.id.tv_project);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            TextView tvState = (TextView) convertView.findViewById(R.id.tv_state);

            tvIndex.setText("" + (position + 1));
            tvIndex.setTag(position);
            tvProject.setText(mAlarmInfoList.get(position).getCommunityInfo().getName());
            tvDate.setText(mAlarmInfoList.get(position).getAlarmTime());

            if (mAlarmInfoList.get(position).getIsMisinformation().equals("1")) {
                tvState.setText("已撤消");
            } else {
                tvState.setText(mActivity.getStringByState(mAlarmInfoList.get(position).getState()));
            }

            mActivity.setIndexColor(tvIndex);
            mActivity.setStateColor(mAlarmInfoList.get(position).getState(), tvState);

            convertView.setTag(mAlarmInfoList.get(position));

            return convertView;
        }

    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        View titleView = mView.findViewById(R.id.title_alarm_list);

        initTitleBar(titleView, "应急救援", R.drawable.back_normal, mActivity.backClickListener,
                0, null);
    }

    /**
     * 请求报警列表BEAN
     *
     * @return
     */
    private RequestBean getAlarmListRequest() {
        AlarmListRequest request = new AlarmListRequest();
        RequestHead head = new RequestHead();
        AlarmListRequest.AlarmListReqBody body = request.new AlarmListReqBody();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setScope("finished");

        request.setHead(head);
        request.setBody(body);

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

        mActivity.addTask(netTask);
    }

    /**
     * 将已经完成的报警任务过滤
     *
     * @param infoList
     */
    private void filterList(List<AlarmInfo> infoList) {
        List<AlarmInfo> alarmInfoList = new ArrayList<AlarmInfo>();
        for (AlarmInfo info : infoList) {
            if (info.getUserState().equals(Constant.ALARM_STATE_CONFIRM)
                    || info.getIsMisinformation().equals("1")) {
                alarmInfoList.add(info);
            }
        }

        //没有报警信息
        if (0 == alarmInfoList.size()) {
            mActivity.showToast(getString(R.string.history_null));
        }
        initView(alarmInfoList);
    }
}
