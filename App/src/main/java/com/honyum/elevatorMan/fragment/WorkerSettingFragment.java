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

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.PersonActivity;
import com.honyum.elevatorMan.activity.maintenance.MyLiftActivity;
import com.honyum.elevatorMan.activity.worker.AlarmListActivity;
import com.honyum.elevatorMan.activity.worker.LiftKnowledgeActivity;
import com.honyum.elevatorMan.activity.worker.WorkerActivity;
import com.honyum.elevatorMan.activity.worker.WorkerHistoryActivity;
import com.honyum.elevatorMan.base.BaseFragment;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.data.Organization;

public class WorkerSettingFragment extends BaseFragment {

    private Context context = null;

    private Map<Integer, ImageView> curPageMap;

    private static final int PAGE_ALARM = 0;

    private static final int PAGE_LIST = 1;

    private static final int PAGE_HISTORY = 2;

    private static final int PAGE_MY_LIFT = 3;

    private static final int PAGE_KNOWLEDGE = 4;

    private static int currentPage = 0;

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mView = inflater.inflate(R.layout.fragment_worker_setting, null);
        context = getActivity();

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
     *
     * @param view
     */
    private void initView(View view) {

        view.findViewById(R.id.ll_person).setOnClickListener(itemClickListener);
        view.findViewById(R.id.ll_main_page).setOnClickListener(itemClickListener);
        view.findViewById(R.id.ll_alarm_list).setOnClickListener(itemClickListener);
        view.findViewById(R.id.ll_history_list).setOnClickListener(itemClickListener);
        view.findViewById(R.id.ll_log_out).setOnClickListener(itemClickListener);
        view.findViewById(R.id.ll_my_lift).setOnClickListener(itemClickListener);
        view.findViewById(R.id.ll_knowledge).setOnClickListener(itemClickListener);

        //上海版本不显示维保
        Config config = ((BaseFragmentActivity) getActivity()).getConfig();
        String server = config.getServer();
        if (server.equals(Organization.SHANGHAI.getServer())) {
            view.findViewById(R.id.ll_my_lift).setVisibility(View.GONE);
        }

        //设置知识库的显示状态
        if (config.getKnowledgeEnable()) {
            view.findViewById(R.id.ll_knowledge).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.ll_knowledge).setVisibility(View.GONE);
        }


        curPageMap.put(PAGE_ALARM, (ImageView) view.findViewById(R.id.iv_mainpage));
        curPageMap.put(PAGE_LIST, (ImageView) view.findViewById(R.id.iv_alarm_list));
        curPageMap.put(PAGE_HISTORY, (ImageView) view.findViewById(R.id.iv_history_list));
        curPageMap.put(PAGE_MY_LIFT, (ImageView) view.findViewById(R.id.iv_my_lift));
        curPageMap.put(PAGE_KNOWLEDGE, (ImageView) view.findViewById(R.id.iv_knowledge));

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
                case R.id.ll_main_page:
                    jumpToMainPage();
                    break;

                case R.id.ll_alarm_list:
                    jumpToAlarmList();
                    break;

                case R.id.ll_history_list:
                    jumpToHistoryList();
                    break;

                case R.id.ll_my_lift:
                    jumpToMyLift();
                    break;

                case R.id.ll_log_out:
                    logout();
                    break;

                case R.id.ll_person:
                    jumpToPerson();
                    break;

                case R.id.ll_knowledge:
                    jumpToKnowledge();
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
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.in_still);
    }

    /**
     * 跳到主页
     */
    private void jumpToMainPage() {
        if (currentPage == PAGE_ALARM) {
            return;
        }
        Intent intent = new Intent(context, WorkerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right_part);
        getActivity().finish();
    }

    /**
     * 跳到报警列表
     */
    private void jumpToAlarmList() {
        if (currentPage == PAGE_LIST) {
            return;
        }
        Intent intent = new Intent(context, AlarmListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right_part);
        getActivity().finish();
    }

    /**
     * 跳转到历史列表
     */
    private void jumpToHistoryList() {
        if (currentPage == PAGE_HISTORY) {
            return;
        }
        Intent intent = new Intent(context, WorkerHistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right_part);
        getActivity().finish();
    }

    /**
     * 跳转到我的电梯页面
     */
    private void jumpToMyLift() {
        if (currentPage == PAGE_MY_LIFT) {
            return;
        }
        Intent intent = new Intent(context, MyLiftActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right_part);
        getActivity().finish();
    }

    /**
     * 跳转到电梯百科页面
     */
    private void jumpToKnowledge() {
        if (currentPage == PAGE_KNOWLEDGE) {
            return;
        }

        Intent intent = new Intent(context, LiftKnowledgeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right_part);
        getActivity().finish();
    }


    /**
     * 获取当前页面的index值
     */
    private void getCurrentPage() {
        if (getActivity() instanceof WorkerActivity) {
            currentPage = PAGE_ALARM;

        } else if (getActivity() instanceof AlarmListActivity) {
            currentPage = PAGE_LIST;

        } else if (getActivity() instanceof WorkerHistoryActivity) {
            currentPage = PAGE_HISTORY;

        } else if (getActivity() instanceof MyLiftActivity) {
            currentPage = PAGE_MY_LIFT;

        } else if (getActivity() instanceof LiftKnowledgeActivity) {
            currentPage = PAGE_KNOWLEDGE;
        }
    }
}