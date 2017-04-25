package com.honyum.elevatorMan.fragment;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.property.ProMainDetailActivity;
import com.honyum.elevatorMan.activity.property.PropertyMaintenanceActivity;
import com.honyum.elevatorMan.base.BaseFragment;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.data.Building;
import com.honyum.elevatorMan.data.Elevator;
import com.honyum.elevatorMan.data.LiftInfo;
import com.honyum.elevatorMan.net.LiftInfoResponse;
import com.honyum.elevatorMan.net.ProMainRequest;
import com.honyum.elevatorMan.net.ReportPlanStateRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.util.ArrayList;
import java.util.List;

public class ProMainCompleteFragment extends BaseFragment implements PropertyMaintenanceActivity.IFilterLiftInfo {

    private List<LiftInfo> mPlanList;

    private Config mConfig;

    private ListView mListView;

    private MyAdapter mAdapter;

    private View mView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mConfig = ((BaseFragmentActivity) getActivity()).getConfig();
        mView = inflater.inflate(R.layout.fragment_pro_main_complete, container, false);
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("zhenhao", "1 visible from:" + mFrom);
        if (isVisibleToUser) {
            lazyLoad();
        }
    }

    /**
     * 初始化视图
     * @param view
     */
    private void initView(View view, List<LiftInfo> liftInfoList) {

        mListView = (ListView) view.findViewById(R.id.list_plan);
        mAdapter = new MyAdapter(this.getActivity(), liftInfoList);
        mListView.setAdapter(mAdapter);
        setListViewListener(mListView);
    }

    @Override
    public void onFilter(String project, String building, String unit, String liftId) {

        //项目为空，显示所有计划
        if (StringUtils.isEmpty(project)) {
            mAdapter = new MyAdapter(getActivity(), mPlanList);
            mListView.setAdapter(mAdapter);

        } else if (!StringUtils.isEmpty(project) && StringUtils.isEmpty(building)) {
            List<LiftInfo> filterList = getPlanListByProject(mPlanList, project);
            mAdapter = new MyAdapter(getActivity(), filterList);
            mListView.setAdapter(mAdapter);

        } else if (!StringUtils.isEmpty(project)
                && !StringUtils.isEmpty(building)
                && StringUtils.isEmpty(unit)) {
            mAdapter = new MyAdapter(getActivity(), getPlanListByBuilding(mPlanList, project, building));
            mListView.setAdapter(mAdapter);

        } else if (!StringUtils.isEmpty(project)
                && !StringUtils.isEmpty(building)
                && !StringUtils.isEmpty(unit)
                && StringUtils.isEmpty(liftId)) {
            mAdapter = new MyAdapter(getActivity(), getPlanListByUnit(mPlanList, project, building, unit));
            mListView.setAdapter(mAdapter);

        } else if (!StringUtils.isEmpty(liftId)) {
            mAdapter = new MyAdapter(getActivity(), getPlanListByLift(mPlanList, liftId));
            mListView.setAdapter(mAdapter);
        }
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
                convertView = View.inflate(mContext, R.layout.layout_maint_info_item, null);
            }

            TextView tvProject = (TextView) convertView.findViewById(R.id.tv_project);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);

            TextView tvInfo = (TextView) convertView.findViewById(R.id.tv_info);

            final LiftInfo liftInfo = mLiftInfoList.get(position);
            convertView.setTag(liftInfo);

            tvProject.setText(liftInfo.getCommunityName() + liftInfo.getBuildingCode() + "号楼"
                    + liftInfo.getUnitCode() + "单元");

            tvDate.setText(liftInfo.getPostTime());

            tvInfo.setText("维保时间:" + liftInfo.getMainTime() + " 维保人:" + liftInfo.getWorkerName());
            return convertView;
        }
    }

    /**
     * 获取请求电梯维保信息的bean
     * @param userId
     * @param token
     * @return
     */
    private RequestBean getMainPlanRequestBean(String userId, String token) {

        ProMainRequest request = new ProMainRequest();
        RequestHead head = new RequestHead();

        head.setUserId(userId);
        head.setAccessToken(token);

        request.setHead(head);

        return request;
    }

    /**
     * 请求维保信息
     * @param view
     * @param userId
     * @param token
     */
    private void requestMainPlan(final View view, String userId, String token) {
        String server = mConfig.getServer()
                + NetConstant.URL_PRO_GET_FINISH_RESULT;

        RequestBean requestBean = getMainPlanRequestBean(userId, token);

        NetTask netTask = new NetTask(server, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {
                LiftInfoResponse response = LiftInfoResponse.getLiftInfoResponse(result);
                mPlanList = response.getBody();
                initView(view, mPlanList);
            }
        };

        ((BaseFragmentActivity) getActivity()).addTask(netTask);
    }

    /**
     * 根据楼栋信息获取电梯id数组
     * @param building
     * @return
     */
    private List<String> getLiftIdList(Building building) {
        List<String> liftIdList = new ArrayList<String>();
        if (null == building) {
            return liftIdList;
        }

        if (null == building.getElevatorList()) {
            return liftIdList;
        }

        for (Elevator elevator : building.getElevatorList()) {
            liftIdList.add(elevator.getId());
        }

        return liftIdList;
    }

    /**
     * 根据项目名称获取电梯数据列表
     * @param liftInfoList
     * @param project
     * @return
     */
    private List<LiftInfo> getPlanListByProject(List<LiftInfo> liftInfoList, String project) {
        List<LiftInfo> resultList = new ArrayList<LiftInfo>();
        for (LiftInfo liftInfo : liftInfoList) {
            if (liftInfo.getCommunityName().equals(project)) {
                resultList.add(liftInfo);
            }
        }
        return resultList;
    }

    /**
     * 根据项目名称和楼栋名称获取电梯数据列表
     * @param liftInfoList
     * @param project
     * @param building
     * @return
     */
    private List<LiftInfo> getPlanListByBuilding(List<LiftInfo> liftInfoList, String project,
                                                 String building) {
        List<LiftInfo> resultList = new ArrayList<LiftInfo>();
        for (LiftInfo liftInfo : liftInfoList) {
            if (liftInfo.getCommunityName().equals(project) && liftInfo.getBuildingCode().equals(building)) {
                resultList.add(liftInfo);
            }
        }
        return resultList;
    }

    /**
     * 根据项目名称和楼栋名称和单元号获取电梯数据列表
     * @param liftInfoList
     * @param project
     * @param building
     * @param unit
     * @return
     */
    private List<LiftInfo> getPlanListByUnit(List<LiftInfo> liftInfoList, String project,
                                             String building, String unit) {

        List<LiftInfo> resultList = new ArrayList<LiftInfo>();
        for (LiftInfo liftInfo : liftInfoList) {
            if (liftInfo.getCommunityName().equals(project)
                    && liftInfo.getBuildingCode().equals(building)
                    && liftInfo.getUnitCode().equals(unit)) {
                resultList.add(liftInfo);
            }
        }
        return resultList;
    }


    /**
     * 根据电梯ID获取电梯数据列表
     * @param liftId
     * @return
     */
    private List<LiftInfo> getPlanListByLift(List<LiftInfo> liftInfoList, String liftId) {

        List<LiftInfo> resultList = new ArrayList<LiftInfo>();
        for (LiftInfo liftInfo : liftInfoList) {
            if (liftInfo.getId().equals(liftId)) {
                resultList.add(liftInfo);
            }
        }
        return resultList;
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
                intent.putExtra("type", "1");
                intent.putExtra("lift", liftInfo);
                startActivity(intent);
            }
        });
    }

    /**
     * 请求网络
     */
    public void lazyLoad() {
        requestMainPlan(mView, mConfig.getUserId(), mConfig.getToken());
    }
}
