package com.honyum.elevatorMan.activity.common;

import com.honyum.elevatorMan.base.BaseFragmentActivity;

/**
 * Created by Star on 2017/6/9.
 */

public class MainPageFrgActivity extends BaseFragmentActivity {

//    private FragmentManager fragmentManager;
//
//    private FragmentTransaction fragmentTransaction;
//
//    private MainFragment businessFragment;
//
//    //private MyCenterFragment myCenterFragment;
//
//    private Fragment preFragment;
//    private boolean hasAlarm = false;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mainpage1);
//        initTitle();
//        initFragment();
//
//        startService(new Intent(this, LocationService.class));
//    }
//
//    private void initMyCenterFragment() {
//        fragmentTransaction = fragmentManager.beginTransaction();
//
//        if (myCenterFragment == null) {
//            myCenterFragment = new MyCenterFragment();
//            fragmentTransaction.add(R.id.frameLayout, myCenterFragment);
//        }
//
//        if (preFragment != null) {
//            fragmentTransaction.hide(preFragment);
//        }
//
//        fragmentTransaction.show(myCenterFragment);
//        fragmentTransaction.commit();
//
//        if (preIv != null && preTv != null) {
//            preIv.setSelected(false);
//            preTv.setSelected(false);
//        }
//
//        preIv = (ImageView) findViewById(R.id.iv_my);
//        preIv.setSelected(true);
//
//        preTv = (TextView) findViewById(R.id.tv_my);
//        preTv.setSelected(true);
//
//        preFragment = myCenterFragment;
//    }
//
//    private void initBusinessFragment() {
//        fragmentTransaction = fragmentManager.beginTransaction();
//
//        if (businessFragment == null) {
//            businessFragment = new BusinessFragment();
//            fragmentTransaction.add(R.id.frameLayout, businessFragment);
//        }
//
//        if (preFragment != null) {
//            fragmentTransaction.hide(preFragment);
//        }
//
//        fragmentTransaction.show(businessFragment);
//        fragmentTransaction.commit();
//
//        if (preIv != null && preTv != null) {
//            preIv.setSelected(false);
//            preTv.setSelected(false);
//        }
//
//        preIv = (ImageView) findViewById(R.id.iv_business);
//        preIv.setSelected(true);
//
//        preTv = (TextView) findViewById(R.id.tv_business);
//        preTv.setSelected(true);
//
//        preFragment = businessFragment;
//    }
//
//    private void initFragment() {
//        fragmentManager = getSupportFragmentManager();
//
//        initBusinessFragment();
//
//        findViewById(R.id.ll_business).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initBusinessFragment();
//            }
//        });
//
//        findViewById(R.id.ll_my).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initMyCenterFragment();
//            }
//        });
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        checkAlarm();
//    }
//    /**
//     * 检测是否有未完成的任务
//     */
//    private void checkAlarm() {
//        new Thread() {
//            @Override
//            public void run() {
//
//                Config config = getConfig();
//                System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+config.getBranchId());
//                boolean hasUnassigned = ChorstarJNI.hasAlarmUnassigned(config.getServer() + "/",
//                        config.getToken(), config.getUserId());
//                boolean hasUnfinished = ChorstarJNI.hasAlarmUnfinished(config.getServer() + "/",
//                        config.getToken(), config.getUserId());
//
//                hasAlarm = (hasUnassigned || hasUnfinished);
//
//                Message msg = Message.obtain();
//                msg.arg1 = 0;
//                mHandler.sendMessage(msg);
//            }
//        }.start();
//    }
//    @Override
//    protected void handlerCallback() {
//        super.handlerCallback();
//
//        View view = findViewById(R.id.tip_msg);
//        if (hasAlarm) {
//            view.setVisibility(View.VISIBLE);
//        } else {
//            view.setVisibility(View.GONE);
//        }
//    }
//    /**
//     * 初始化标题
//     */
//    private void initTitle() {
//      initTitleBar(R.id.title_main_page,"首页");
//    }
//    /**
//     * 初始化视图
//     */
//
//
//
//    @Override
//    public void onBackPressed() {
//        // TODO Auto-generated method stub
//
//        popMsgAlertWithCancel(getString(R.string.exit_confirm), new IConfirmCallback() {
//            @Override
//            public void onConfirm() {
//                MainPageFrgActivity.super.onBackPressed();
//                SysActivityManager.getInstance().exit();
//            }
//        }, "否", "是", getString(R.string.exit_confirm));
////            new AlertDialog.Builder(this).setTitle(R.string.exit_confirm)
////                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
////
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            // TODO Auto-generated method stub
////                            dialog.dismiss();
////                            onBackPressed();
////                            SysActivityManager.getInstance().exit();
////                        }
////
////                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {
////
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    // TODO Auto-generated method stub
////                    dialog.dismiss();
////                }
////            }).show();
//    }
}
