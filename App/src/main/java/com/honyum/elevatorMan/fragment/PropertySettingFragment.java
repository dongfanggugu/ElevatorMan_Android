package com.honyum.elevatorMan.fragment;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.PersonActivity;
import com.honyum.elevatorMan.activity.property.AlarmTraceActivity;
import com.honyum.elevatorMan.activity.property.ProHistoryActivity;
import com.honyum.elevatorMan.activity.property.PropertyActivity;
import com.honyum.elevatorMan.activity.property.PropertyAlarmListActivity;
import com.honyum.elevatorMan.activity.property.PropertyMaintenanceActivity;
import com.honyum.elevatorMan.base.BaseFragment;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.Organization;

public class PropertySettingFragment extends BaseFragment {
	
	private Context context = null;
	
	private Map<Integer, ImageView> curPageMap;
	
	private static final int PAGE_ALARM = 0;
	
	private static final int PAGE_DIAL = 1;
	
	private static final int PAGE_LIST = 2;

    private static final int PAGE_HISTORY = 3;

    private static final int PAGE_MAIN = 4;
	
	private static int  currentPage = 0;

    private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        mView = inflater.inflate(R.layout.fragment_property_setting, null);
		context = getActivity();
		//getCurrentPage();
		curPageMap = new HashMap<Integer, ImageView>();
		return mView;
	}

    @Override
    public void onResume() {
        super.onResume();

        getCurrentPage();
        initView(mView);
        //当信息修改时重新加载个人信息
        if (mView != null) {
            ((TextView) mView.findViewById(R.id.tv_name))
                    .setText(((BaseFragmentActivity) getActivity()).getConfig().getName());

            ((TextView) mView.findViewById(R.id.tv_company))
                    .setText(((BaseFragmentActivity) getActivity()).getConfig().getBranchName());

            BaseFragmentActivity content = (BaseFragmentActivity) getActivity();
            content.loadIcon((ImageView) mView.findViewById(R.id.img_user_icon));
        }
    }
	
	/**
	 * 初始化view
	 * @param view
	 */
	private void initView(View view) {
		
		((TextView) view.findViewById(R.id.tv_name))
		.setText(((BaseFragmentActivity) getActivity()).getConfig().getName());

		((TextView) view.findViewById(R.id.tv_company))
		.setText("");

        view.findViewById(R.id.ll_person).setOnClickListener(itemClickListener);
		view.findViewById(R.id.ll_current_alarm).setOnClickListener(itemClickListener);
		view.findViewById(R.id.ll_main_page).setOnClickListener(itemClickListener);
		view.findViewById(R.id.ll_alarm_list).setOnClickListener(itemClickListener);
		view.findViewById(R.id.ll_log_out).setOnClickListener(itemClickListener);
        view.findViewById(R.id.ll_history_list).setOnClickListener(itemClickListener);
        view.findViewById(R.id.ll_maintenance).setOnClickListener(itemClickListener);

        //上海地区隐藏维保功能
        Config config = ((BaseFragmentActivity) getActivity()).getConfig();
        if (config.getServer().equals(Organization.SHANGHAI.getServer())) {
            view.findViewById(R.id.ll_maintenance).setVisibility(View.GONE);
        }

		curPageMap.put(PAGE_ALARM, (ImageView) view.findViewById(R.id.iv_cur_alarm));
		curPageMap.put(PAGE_DIAL, (ImageView) view.findViewById(R.id.iv_mainpage));
		curPageMap.put(PAGE_LIST, (ImageView) view.findViewById(R.id.iv_alarm_list));
        curPageMap.put(PAGE_HISTORY, (ImageView) view.findViewById(R.id.iv_history_list));
        curPageMap.put(PAGE_MAIN, (ImageView) view.findViewById(R.id.iv__maintenance));

		curPageMap.get(currentPage).setVisibility(View.VISIBLE);
	}
	
	/**
	 * 处理菜单的点击事件
	 */
    private OnClickListener itemClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.ll_current_alarm:
                    jumpToCurAlarm();
                    break;
                case R.id.ll_main_page:
                    jumpToMainPage();
                    break;
                case R.id.ll_alarm_list:
                    jumpToAlarmList();
                    break;
                case R.id.ll_history_list:
                    jumpToHistoryList();
                    break;

                case R.id.ll_maintenance:
                    jumpToProjectList();
                    break;

                case R.id.ll_log_out:
                    logout();
                    break;

                case R.id.ll_person:
                    jumpToPerson();
                    break;
            }
        }

    };

    /**
     * 跳到个人信息
     */
    private void jumpToPerson() {
        Intent intent = new Intent(context, PersonActivity.class);
        startActivity(intent);
    }

    /**
	 * 跳转到当前处理的报警
	 */
	private void jumpToCurAlarm() {
        if (currentPage == PAGE_ALARM) {
            return;
        }
		String alarmId = getContext().getConfig().getAlarmId();
		String alarmState = getContext().getConfig().getAlarmState();
		
		if (StringUtils.isEmpty(alarmId) || StringUtils.isEmpty(alarmState)) {
			getContext().showToast(getString(R.string.no_alarm));
			return;
		}
		
		Intent intent = new Intent(context, AlarmTraceActivity.class);
		intent.putExtra("alarm_id", alarmId);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		
		if (alarmState.equals("1")) {  //已经出发
			intent.setAction(Constant.ACTION_WORKER_ASSIGNED);
		} else if (alarmState.equals("2")) {    //已经到达
            intent.setAction(Constant.ACTION_WORKER_ARRIVED);
        }
        else if (alarmState.equals("3")) {	//已经完成
			intent.setAction(Constant.ACTION_ALARM_COMPLETE);
            intent.putExtra("msg", getString(R.string.property_complete));
		} else if (alarmState.equals(Constant.ALARM_STATE_START)) {	//发生报警
			intent.setAction(Constant.ACTION_ALARM_PROPERTY);
		}
		startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right_part);
	}
	
	/**
	 * 跳到主页
	 */
	private void jumpToMainPage() {
        if (currentPage == PAGE_DIAL) {
            return;
        }
		Intent intent = new Intent(context, PropertyActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right_part);
	}
	
	/**
	 * 跳到报警列表
	 */
	private void jumpToAlarmList() {
        if (currentPage == PAGE_LIST) {
            return;
        }
		Intent intent = new Intent(context, PropertyAlarmListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right_part);
	}

    /**
     * 跳转到历史列表
     */
    private void jumpToHistoryList() {
        if (currentPage == PAGE_HISTORY) {
            return;
        }
        Intent intent = new Intent(context, ProHistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right_part);
    }

    private void jumpToProjectList() {
        if (currentPage == PAGE_MAIN) {
            return;
        }
        Intent intent = new Intent(context, PropertyMaintenanceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right_part);
    }
	
	/**
	 * 获取 BaseFragmentActivity 对象
	 * @return
	 */
	private BaseFragmentActivity getContext() {
		return (BaseFragmentActivity) getActivity();
	}
	
	/**
	 * 获取当前页面的index值
	 */
	private void getCurrentPage() {
		if (getActivity() instanceof AlarmTraceActivity) {
			currentPage = PAGE_ALARM;
		} else if (getActivity() instanceof PropertyActivity) {
			currentPage = PAGE_DIAL;
		} else if (getActivity() instanceof PropertyAlarmListActivity) {
			currentPage = PAGE_LIST;
		} else if (getActivity() instanceof ProHistoryActivity) {
            currentPage = PAGE_HISTORY;
        } else if (getActivity() instanceof PropertyMaintenanceActivity) {
            currentPage = PAGE_MAIN;
        }
	}
}