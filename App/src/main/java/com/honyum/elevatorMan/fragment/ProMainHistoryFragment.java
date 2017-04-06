package com.honyum.elevatorMan.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.property.ProMainDetailActivity;
import com.honyum.elevatorMan.activity.property.PropertyMaintenanceActivity;
import com.honyum.elevatorMan.base.BaseFragment;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.data.LiftInfo;
import com.honyum.elevatorMan.net.LiftInfoResponse;
import com.honyum.elevatorMan.net.MainHistoryRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.util.ArrayList;
import java.util.List;

public class ProMainHistoryFragment extends BaseFragment implements PropertyMaintenanceActivity.IFilterLiftInfo {


    private int mCurrentPage = 1;

    private List<LiftInfo> mLiftInfos;

    private Config mConfig;

    private PullToRefreshListView listView;

    private MyAdapter myAdapter;

    private View mView;

    private String mProject = "";

    private String mBuilding = "";

    private String mUnit = "";

    private String mLiftId = "";

    private BaseFragmentActivity mBaseFragmentActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBaseFragmentActivity = (BaseFragmentActivity) getActivity();
        mConfig = mBaseFragmentActivity.getConfig();
        mView = inflater.inflate(R.layout.fragment_pro_main_history, container, false);

        mCurrentPage = 1;
        mLiftInfos = new ArrayList<LiftInfo>();

        Log.i("zhenhao", "2 view from:" + mFrom);
        if (0 == mFrom) {
            lazyLoad();
        }

        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("zhenhao", "2 visible from:" + mFrom);

            if (1 == mFrom) {
                lazyLoad();
            }
        }
    }

    @Override
    public void onFilter(String project, String building, String unit, String liftId) {

        //从第一页开始
        mCurrentPage = 1;
        mLiftInfos.clear();

        mProject = project;
        mBuilding = building;
        mUnit = unit;
        mLiftId = liftId;

        requestMainHistory(mView, mConfig.getUserId(), mConfig.getToken(), mCurrentPage, mProject,
                mBuilding, mUnit, mLiftId);
    }

    /**
     * 初始化视图
     * @param view
     */
    private void initView(final View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list_history);
        myAdapter = new MyAdapter(getActivity(), mLiftInfos);
        listView.setAdapter(myAdapter);
        setListViewListener(listView.getRefreshableView());


        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                mCurrentPage++;
                refreshMainHistory(mConfig.getUserId(), mConfig.getToken(), mCurrentPage,
                        mProject, mBuilding, mBuilding, mLiftId);
            }
        });

    }

    /**
     * ListView适配器
     */
    private class MyAdapter extends BaseAdapter {

        private Context mContext;

        private List<LiftInfo> mLiftInfoList;

        public MyAdapter(Context context, List<LiftInfo> liftInfoList) {
            mContext = context;
            mLiftInfoList = liftInfoList;
        }

        @Override
        public int getCount() {
            return mLiftInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mLiftInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(mContext, R.layout.layout_alarm_item, null);
            }

            TextView tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
            TextView tvProject = (TextView) convertView.findViewById(R.id.tv_project);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);

            final LiftInfo liftInfo = mLiftInfoList.get(position);
            convertView.setTag(liftInfo);

            tvIndex.setText("" + (position + 1));
            tvProject.setText(liftInfo.getCommunityName() + liftInfo.getBuildingCode() + "号楼"
                    + liftInfo.getUnitCode() + "单元");
            tvDate.setText(liftInfo.getMainTime());
            return convertView;
        }
    }

    /**
     * 获取请求维保历史的bean
     * @param userId
     * @param token
     * @param page
     * @return
     */
    private RequestBean getMainHistoryRequestBean(String userId, String token, int page,
                                                  String project, String building, String unit,
                                                  String liftId) {
        MainHistoryRequest request = new MainHistoryRequest();
        MainHistoryRequest.MainHistoryReqBody body = request.new MainHistoryReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(userId);
        head.setAccessToken(token);

        body.setPage(page);
        body.setCommunityName(project);
        body.setBuildingCode(building);
        body.setUnitCode(unit);
        body.setId(liftId);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 请求维保历史
     * @param view
     * @param userId
     * @param token
     * @param page
     */
    private void requestMainHistory(final View view, String userId, String token, int page,
                                    String project, String building, String unit, String liftNum) {
        String server = mConfig.getServer() + NetConstant.URL_PRO_GET_MAIN_HISTORY;
        final RequestBean requestBean = getMainHistoryRequestBean(userId, token, page, project, building,
                unit, liftNum);

        NetTask netTask = new NetTask(server, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {
                LiftInfoResponse response = LiftInfoResponse.getLiftInfoResponse(result);
                mLiftInfos.addAll(response.getBody());
                initView(view);
            }
        };

        mBaseFragmentActivity.addTask(netTask);
    }

    /**
     * 下拉刷新，请求维保历史
     * @param userId
     * @param token
     * @param page
     */
    private void refreshMainHistory(String userId, String token, int page, String project, String building,
                                    String unit, String liftNum) {
        String server = mConfig.getServer() + NetConstant.URL_PRO_GET_MAIN_HISTORY;
        RequestBean requestBean = getMainHistoryRequestBean(userId, token, page, project, building,
                unit, liftNum);

        NetTask netTask = new NetTask(server, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {
                LiftInfoResponse response = LiftInfoResponse.getLiftInfoResponse(result);

                if (null == response.getBody() || 0 == response.getBody().size()) {
                    mCurrentPage--;
                } else {
                    mLiftInfos.addAll(response.getBody());
                    myAdapter.notifyDataSetChanged();
                }
                listView.onRefreshComplete();
            }
        };

        ((BaseFragmentActivity) getActivity()).addBackGroundTask(netTask);
    }


    /**
     * 设置ListView的高度
     * @param listView
     */
    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 点击item事件处理
     * @param listView
     */
    private void setListViewListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LiftInfo liftInfo = (LiftInfo) view.getTag();
                Intent intent = new Intent(getActivity(), ProMainDetailActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("lift", liftInfo);
                startActivity(intent);
            }
        });
    }

    /**
     * 请求网络
     */
    public void lazyLoad() {
        mLiftInfos.clear();
        requestMainHistory(mView, mConfig.getUserId(), mConfig.getToken(), mCurrentPage, "", "", "", "");
    }

}
