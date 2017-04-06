package com.honyum.elevatorMan.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.property.PropertyMaintenanceActivity;
import com.honyum.elevatorMan.base.BaseFragment;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.data.Building;
import com.honyum.elevatorMan.data.Elevator;
import com.honyum.elevatorMan.data.LiftInfo;
import com.honyum.elevatorMan.data.Project;
import com.honyum.elevatorMan.net.LiftInfoResponse;
import com.honyum.elevatorMan.net.ProMainRequest;
import com.honyum.elevatorMan.net.ReportPlanStateRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProMainPlanFragment extends BaseFragment implements PropertyMaintenanceActivity.IFilterLiftInfo {


    private List<LiftInfo> mPlanList;

    private Config mConfig;

    private BaseFragmentActivity mBaseFragmentActivity;

    private ListView mListView;

    private MyAdapter mAdapter;

    private View mView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBaseFragmentActivity = (BaseFragmentActivity) getActivity();
        mConfig = mBaseFragmentActivity.getConfig();
        mView = inflater.inflate(R.layout.fragment_pro_main_plan, container, false);

        Log.i("zhenhao", "0 view from:" + mFrom);

        if (0 == mFrom || 2 == mFrom) {
            lazyLoad();
        }
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("zhenhao", "0 visible from:" + mFrom);
            if (1 == mFrom) {
                lazyLoad();
            }
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
                convertView = View.inflate(mContext, R.layout.layout_pro_plan_item, null);
            }

            TextView tvProject = (TextView) convertView.findViewById(R.id.tv_project);
            TextView tvCode = (TextView) convertView.findViewById(R.id.tv_code);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            TextView tvType = (TextView) convertView.findViewById(R.id.tv_type);
            TextView tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            TextView tvWorker = (TextView) convertView.findViewById(R.id.tv_worker);

            final LiftInfo liftInfo = mLiftInfoList.get(position);
            convertView.setTag(liftInfo);

            setItemStyle(liftInfo.getPropertyFlg(), convertView);

            tvProject.setText(liftInfo.getCommunityName() + liftInfo.getBuildingCode() + "号楼"
            + liftInfo.getUnitCode() + "单元");
            tvProject.getPaint().setFakeBoldText(true);

            tvCode.setText(liftInfo.getNum());
            tvDate.setText(liftInfo.getPlanMainTime());
            tvType.setText(liftInfo.getPlanType());

            tvCompany.setText(liftInfo.getWorkerCompany());
            tvWorker.setText(liftInfo.getWorkerName());

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
                + NetConstant.URL_PRO_PLAN_LIST;

        RequestBean requestBean = getMainPlanRequestBean(userId, token);

        NetTask netTask = new NetTask(server, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {
                LiftInfoResponse response = LiftInfoResponse.getLiftInfoResponse(result);
                mPlanList = response.getBody();
                initView(view, mPlanList);
            }
        };

        mBaseFragmentActivity.addTask(netTask);
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
     * 上报计划处理结果
     * @param userId
     * @param token
     * @param mainId
     * @param state
     * @return
     */
    private RequestBean getReportPlanStateRequestBean(String userId, String token, String mainId,
                                                      int state) {

        ReportPlanStateRequest request = new ReportPlanStateRequest();
        ReportPlanStateRequest.ReportPlanStateReqBody body = request.new ReportPlanStateReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(userId);
        head.setAccessToken(token);

        body.setMainId(mainId);
        body.setVerify(state);

        request.setHead(head);
        request.setBody(body);

        return  request;
    }

    /**
     * 上报
     * @param userId
     * @param token
     * @param mainId
     * @param state
     */
    private void reportPlanState(final View view, String userId, String token, final String mainId,
                                 final int state) {

        String server = mConfig.getServer() + NetConstant.URL_PRO_REPORT_PLAN_RESULT;
        RequestBean requestBean = getReportPlanStateRequestBean(userId, token, mainId, state);

        NetTask netTask = new NetTask(server, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {
                if (0 == state) {
                    setItemStyle("3", view);
                } else if (1 == state) {
                    setItemStyle("2", view);
                }
            }
        };

        ((BaseFragmentActivity) getActivity()).addTask(netTask);
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
                View dialogView = View.inflate(getActivity(), R.layout.layout_plan_deal, null);
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(dialogView).create();
                initItemPopDialog(view, dialogView, dialog, (LiftInfo) view.getTag());
                dialog.show();
            }
        });
    }

    /**
     * 处理弹出框事件
     * @param itemView
     * @param view
     * @param dialog
     * @param liftInfo
     */
    private void initItemPopDialog(final View itemView, View view, final AlertDialog dialog, final LiftInfo liftInfo) {
        view.findViewById(R.id.ll_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPlanState(itemView, mConfig.getUserId(), mConfig.getToken(), liftInfo.getMainId(), 1);
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.ll_refuse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPlanState(itemView, mConfig.getUserId(), mConfig.getToken(), liftInfo.getMainId(), 0);
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.ll_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 设置维保计划显示的主题
     * @param flag
     * @param itemView
     */
    private void setItemStyle(String flag, View itemView) {

        LinearLayout background = (LinearLayout) itemView.findViewById(R.id.ll_plan_flag);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_flag);
        TextView textView = (TextView) itemView.findViewById(R.id.tv_flag);

        if (flag.equals("1")) {
            background.setBackgroundColor(getResources().getColor(R.color.color_plan_waiting));
            imageView.setImageResource(R.drawable.icon_waiting);
            textView.setText(R.string.plan_waiting);

        } else if (flag.equals("2")) {
            background.setBackgroundColor(getResources().getColor(R.color.color_plan_confirm));
            imageView.setImageResource(R.drawable.icon_confirm);
            textView.setText(R.string.plan_confirm);

        } else if (flag.equals("3")) {
            background.setBackgroundColor(getResources().getColor(R.color.color_plan_reject));
            imageView.setImageResource(R.drawable.icon_reject);
            textView.setText(R.string.plan_reject);

        }
    }


    /**
     * 请求网络
     */
    protected void lazyLoad() {
        requestMainPlan(mView, mConfig.getUserId(), mConfig.getToken());
    }
}
