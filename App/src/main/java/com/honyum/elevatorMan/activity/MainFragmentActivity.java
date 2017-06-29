package com.honyum.elevatorMan.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.HelpCenterActivity;
import com.honyum.elevatorMan.activity.common.MainPageFrgActivity;
import com.honyum.elevatorMan.activity.common.NousActivity;
import com.honyum.elevatorMan.activity.common.PersonActivity;
import com.honyum.elevatorMan.activity.maintenance.MaintenanceManagerActivity;
import com.honyum.elevatorMan.activity.maintenance.MaintenanceServiceActivity;
import com.honyum.elevatorMan.activity.worker.AlarmListActivity;
import com.honyum.elevatorMan.activity.worker.FixOrderListActivity;
import com.honyum.elevatorMan.activity.worker.LiftKnowledgeActivity;
import com.honyum.elevatorMan.adapter.BannerAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.SysActivityManager;
import com.honyum.elevatorMan.data.BannerInfo;
import com.honyum.elevatorMan.net.BannerResponse;
import com.honyum.elevatorMan.net.EmptyRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;

import java.util.List;

/**
 * Created by Star on 2017/6/26.
 */

public class MainFragmentActivity extends Fragment implements View.OnClickListener{
    private View mView;

    private BaseFragmentActivity mActivity;

    public MainFragmentActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       //  Inflate the layout for this fragment
        mActivity = (BaseFragmentActivity) getActivity();
        mView = inflater.inflate(R.layout.fragment_mainpage, container, false);

        requestBanner();

        initView();

        return mView;
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mActivity.findViewById(R.id.ll_rescue).setOnClickListener(this);
        mActivity.findViewById(R.id.ll_maintenance).setOnClickListener(this);
        mActivity.findViewById(R.id.ll_fix).setOnClickListener(this);
        mActivity.findViewById(R.id.ll_person).setOnClickListener(this);
        mActivity.findViewById(R.id.ll_bbs).setOnClickListener(this);

        mActivity.findViewById(R.id.tv_question).setOnClickListener(this);
        mActivity.findViewById(R.id.tv_rule).setOnClickListener(this);
        mActivity.findViewById(R.id.tv_num).setOnClickListener(this);
        mActivity.findViewById(R.id.tv_handle).setOnClickListener(this);
        requestBanner();


    }
    private int prePos;

    private int curItemPos;

    private void initPageIndicator(final List<BannerInfo> pics) {

        View view = mActivity.findViewById(R.id.main_page_indicator);

        final ViewPager vp = (ViewPager) view.findViewById(R.id.viewPager);
        final BannerAdapter adapter = new BannerAdapter(mActivity, pics);
        vp.setAdapter(adapter);
        vp.setCurrentItem(adapter.getCount() / 2);
        curItemPos = adapter.getCount() / 2;

        final LinearLayout llIndicator = (LinearLayout) view.findViewById(R.id.ll_indicator);
        for (BannerInfo pic : pics) {
            ImageView iv = new ImageView(mActivity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
            iv.setLayoutParams(params);
            iv.setBackgroundResource(R.drawable.sel_page_indicator);
            llIndicator.addView(iv);
        }
        llIndicator.getChildAt(0).setEnabled(false);

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                llIndicator.getChildAt(position % pics.size()).setEnabled(false);
                llIndicator.getChildAt(prePos).setEnabled(true);
                prePos = position % pics.size();
                curItemPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vp.setCurrentItem(curItemPos++);
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    private void requestBanner() {
        String server = mActivity.getConfig().getServer() + NetConstant.GET_BANNER;

        //String request = Constant.EMPTY_REQUEST;

        NetTask netTask = new NetTask(server, new EmptyRequest()) {
            @Override
            protected void onResponse(NetTask task, String result) {
                BannerResponse response = BannerResponse.getResult(result);
                initPageIndicator(response.getBody());
            }
        };

        mActivity.addBackGroundTask(netTask);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_question:
                jumpToQuestion();
                break;
            case R.id.tv_rule:
                jumpToSafe_rule();
                break;
            case R.id.tv_num:
                jumpToSave_num();
                break;
            case R.id.tv_handle:
                jumpToHandle_rule();
                break;

            case R.id.ll_rescue:
                jumpToAlarmList();
                break;
            case R.id.ll_maintenance:
                jumpToMaintenance();
                break;
            case R.id.ll_fix:
                jumpToRepair();
                break;
            case R.id.ll_person:
                jumpToPerson();
                break;
            case R.id.ll_bbs:
                String region = mActivity.getConfig().getRegion();

//                if (region.equals(Constant.SHANGHAI)
//                        || region.equals(Constant.BEIJING)) {
//                    showToast("该功能暂未开放");
//
//                    return;
//                }
                jumpToMainService();
                break;
            case R.id.ll_wiki:
                jumpToWiki();

                break;
        }
    }

    /**
     * 跳转到维保服务页面
     */
    private void jumpToMainService() {
        //  NHMentenanceActivity  MaintenanceServiceActivity
        Intent intent = new Intent(mActivity, MaintenanceServiceActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到维修单页面           NHFixActivity  FixOrderListActivity
     */
    private void jumpToRepair() {
        Intent intent = new Intent(mActivity, FixOrderListActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到紧急救援    RescuLookActivity  AlarmListActivity
     */
    private void jumpToAlarmList() {
        Intent intent = new Intent(mActivity, AlarmListActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到电梯百科
     */
    private void jumpToWiki() {
        //RescuLookActivity  LiftKnowledgeActivity
        Intent intent = new Intent(mActivity,LiftKnowledgeActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到个人中心
     */
    private void jumpToPerson() {
        Intent intent = new Intent(mActivity, PersonActivity.class);
        startActivity(intent);
    }

    //   MaintenanceManagerActivity EMantenanceLookActivity
    private void jumpToMaintenance() {
        Intent intent = new Intent(mActivity, MaintenanceManagerActivity.class);
        startActivity(intent);
    }

    private void jumpToHelpCenter() {
        Intent intent = new Intent(mActivity, HelpCenterActivity.class);
        startActivity(intent);
    }
    /**
     * 跳转到常见问题
     */
    private void jumpToQuestion() {
//        Intent intent = new Intent(MainPageActivity.this, TitleListActivity.class);
//        intent.putExtra("type", "常见问题");
//        startActivity(intent);
        Intent intent =new Intent(mActivity,NousActivity.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("kntype", "常见问题");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    /**
     * 跳转到救援识别码
     */
    private void jumpToSave_num() {
//        Intent intent = new Intent(MainPageActivity.this, TitleListActivity.class);
//        intent.putExtra("type", "故障对照");
//        startActivity(intent);
        Intent intent =new Intent(mActivity,NousActivity.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("kntype", "故障码");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    /**
     * 跳转到操作手册
     */
    private void jumpToHandle_rule() {
//        Intent intent = new Intent(MainPageActivity.this, TitleListActivity.class);
//        intent.putExtra("type", "操作手册");
//        startActivity(intent);
        Intent intent =new Intent(mActivity,NousActivity.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("kntype", "操作手册");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    /**
     * 跳转到安全法规
     */
    private void jumpToSafe_rule() {
//        Intent intent = new Intent(MainPageActivity.this, TitleListActivity.class);
//        intent.putExtra("type", "安全法规");
//        startActivity(intent);

        Intent intent =new Intent(mActivity,NousActivity.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("kntype", "安全法规");
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
