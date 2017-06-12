package com.honyum.elevatorMan.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chorstar.jni.ChorstarJNI;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.knowledge.TitleListActivity;
import com.honyum.elevatorMan.activity.maintenance.MaintenanceManagerActivity;
import com.honyum.elevatorMan.activity.maintenance.MaintenanceServiceActivity;
import com.honyum.elevatorMan.activity.worker.AlarmListActivity;
import com.honyum.elevatorMan.activity.worker.LiftKnowledgeActivity;
import com.honyum.elevatorMan.activity.worker.RepairOrderActivity;
import com.honyum.elevatorMan.adapter.PageIndicatorAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.base.SysActivityManager;
import com.honyum.elevatorMan.service.LocationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Star on 2017/6/9.
 */

public class MainPage1Activity extends BaseFragmentActivity implements View.OnClickListener {

    private boolean hasAlarm = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage1);
        initTitle();

//        initPageIndicator();

        initView();

        startService(new Intent(this, LocationService.class));
    }
    private List<Integer> pics;

    private int prePos;

    private void initPageIndicator() {
        pics = new ArrayList<Integer>();
        pics.add(R.drawable.banner);
        pics.add(R.drawable.banner);
        pics.add(R.drawable.banner);

        View pi = findViewById(R.id.main_page_indicator);

        ViewPager vp = (ViewPager) pi.findViewById(R.id.viewPager);
        PageIndicatorAdapter adapter = new PageIndicatorAdapter(this, pics);
        vp.setAdapter(adapter);
        vp.setCurrentItem(adapter.getCount() / 2);

        final LinearLayout llIndicator = (LinearLayout) pi.findViewById(R.id.ll_indicator);
        for (Integer pic : pics) {
            ImageView v = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
            v.setLayoutParams(params);
            v.setBackgroundResource(R.drawable.sel_page_indicator);
            llIndicator.addView(v);
        }
        llIndicator.getChildAt(0).setEnabled(false);

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }


            @Override
            public void onPageSelected(int position) {
                llIndicator.getChildAt(position % pics.size()).setEnabled(false);
//                llIndicator.getChildAt(position % 5).startAnimation(AnimationUtils.loadAnimation(this, R.anim.indicator));
                llIndicator.getChildAt(prePos).setEnabled(true);
                prePos = position % pics.size();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkAlarm();
    }
    /**
     * 检测是否有未完成的任务
     */
    private void checkAlarm() {
        new Thread() {
            @Override
            public void run() {

                Config config = getConfig();
                boolean hasUnassigned = ChorstarJNI.hasAlarmUnassigned(config.getServer() + "/",
                        config.getToken(), config.getUserId());
                boolean hasUnfinished = ChorstarJNI.hasAlarmUnfinished(config.getServer() + "/",
                        config.getToken(), config.getUserId());

                hasAlarm = (hasUnassigned || hasUnfinished);

                Message msg = Message.obtain();
                msg.arg1 = 0;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
    @Override
    protected void handlerCallback() {
        super.handlerCallback();

        View view = findViewById(R.id.tip_msg);
        if (hasAlarm) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
    /**
     * 初始化标题
     */
    private void initTitle() {
      initTitleBar(R.id.title_main_page,"首页");
    }
    /**
     * 初始化视图
     */
    private void initView() {
        findViewById(R.id.ll_rescue).setOnClickListener(this);
        findViewById(R.id.ll_maintenance).setOnClickListener(this);
        findViewById(R.id.ll_fix).setOnClickListener(this);
        findViewById(R.id.ll_person).setOnClickListener(this);
        findViewById(R.id.ll_bbs).setOnClickListener(this);

        findViewById(R.id.tv_question).setOnClickListener(this);
        findViewById(R.id.tv_rule).setOnClickListener(this);
        findViewById(R.id.tv_num).setOnClickListener(this);
        findViewById(R.id.tv_handle).setOnClickListener(this);


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
                String region = getConfig().getRegion();

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
        Intent intent = new Intent(this, MaintenanceServiceActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到维修单页面
     */
    private void jumpToRepair() {
        Intent intent = new Intent(this, RepairOrderActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到紧急救援
     */
    private void jumpToAlarmList() {
        Intent intent = new Intent(this, AlarmListActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到电梯百科
     */
    private void jumpToWiki() {
        Intent intent = new Intent(this, LiftKnowledgeActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到个人中心
     */
    private void jumpToPerson() {
        Intent intent = new Intent(this, PersonActivity.class);
        startActivity(intent);
    }

    private void jumpToMaintenance() {
        Intent intent = new Intent(this, MaintenanceManagerActivity.class);
        startActivity(intent);
    }

    private void jumpToHelpCenter() {
        Intent intent = new Intent(this, HelpCenterActivity.class);
        startActivity(intent);
    }
    /**
     * 跳转到常见问题
     */
    private void jumpToQuestion() {
        Intent intent = new Intent(MainPage1Activity.this, TitleListActivity.class);
        intent.putExtra("type", "常见问题");
        startActivity(intent);
    }
    /**
     * 跳转到救援识别码
     */
    private void jumpToSave_num() {
        Intent intent = new Intent(MainPage1Activity.this, TitleListActivity.class);
        intent.putExtra("type", "故障对照");
        startActivity(intent);
    }
    /**
     * 跳转到操作手册
     */
    private void jumpToHandle_rule() {
        Intent intent = new Intent(MainPage1Activity.this, TitleListActivity.class);
        intent.putExtra("type", "操作手册");
        startActivity(intent);
    }
    /**
     * 跳转到安全法规
     */
    private void jumpToSafe_rule() {
        Intent intent = new Intent(MainPage1Activity.this, TitleListActivity.class);
        intent.putExtra("type", "安全法规");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        popMsgAlertWithCancel(getString(R.string.exit_confirm), new IConfirmCallback() {
            @Override
            public void onConfirm() {
                MainPage1Activity.super.onBackPressed();
                SysActivityManager.getInstance().exit();
            }
        }, "否", "是", getString(R.string.exit_confirm));
//            new AlertDialog.Builder(this).setTitle(R.string.exit_confirm)
//                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // TODO Auto-generated method stub
//                            dialog.dismiss();
//                            onBackPressed();
//                            SysActivityManager.getInstance().exit();
//                        }
//
//                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // TODO Auto-generated method stub
//                    dialog.dismiss();
//                }
//            }).show();
    }
}