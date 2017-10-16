package com.honyum.elevatorMan.activity.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.chorstar.jni.ChorstarJNI;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.maintenance.MaintenanceManagerActivity;
import com.honyum.elevatorMan.activity.maintenance.MaintenanceServiceActivity;
import com.honyum.elevatorMan.activity.worker.AlarmListActivity;
import com.honyum.elevatorMan.activity.worker.EbuyActivity;
import com.honyum.elevatorMan.activity.worker.FixOrderListActivity;
import com.honyum.elevatorMan.activity.worker.LiftKnowledgeActivity;
import com.honyum.elevatorMan.adapter.BannerAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.BannerInfo;
import com.honyum.elevatorMan.net.AdvDetailRequest;
import com.honyum.elevatorMan.net.AdvDetailResponse;
import com.honyum.elevatorMan.net.BannerResponse;
import com.honyum.elevatorMan.net.EmptyRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;

import java.util.List;

/**
 * Created by Star on 2017/6/9.
 */

public class MainPage1Activity extends BaseFragmentActivity implements View.OnClickListener, ListItemCallback<ImageView> {

    private boolean hasAlarm = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage1);
        initTitle();
        initView();

        startLocationService();
        //startService(new Intent(this, LocationService.class));
    }
    private List<Integer> pics;

    private void requestBanner() {
        String server = getConfig().getServer() + NetConstant.GET_BANNER;

        //String request = Constant.EMPTY_REQUEST;

        NetTask netTask = new NetTask(server, new EmptyRequest()) {
            @Override
            protected void onResponse(NetTask task, String result) {
                //Log.e("TAG", "onResponse: "+result );
                BannerResponse response = BannerResponse.getResult(result);
                initPageIndicator(response.getBody());
            }
        };

        addBackGroundTask(netTask);
    }




    private int prePos;

    private int curItemPos;

    private void initPageIndicator(final List<BannerInfo> pics) {

        View view = findViewById(R.id.main_page_indicator);

        final ViewPager vp = (ViewPager) view.findViewById(R.id.viewPager);
        final BannerAdapter adapter = new BannerAdapter(this, pics);
        vp.setAdapter(adapter);
        vp.setCurrentItem(adapter.getCount() / 2);
        curItemPos = adapter.getCount() / 2;

        final LinearLayout llIndicator = (LinearLayout) view.findViewById(R.id.ll_indicator);
        for (final BannerInfo pic : pics) {
            ImageView iv = new ImageView(this);
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
                System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+config.getBranchId());
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
            getConfig().setCurrDelay(getConfig().getLocationUploadTask());
            startLocationService();
        } else {
            view.setVisibility(View.GONE);
            getConfig().setCurrDelay(getConfig().getLocationUpload());
            startLocationService();
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
//        findViewById(R.id.ll_person).setOnClickListener(this);
//        findViewById(R.id.ll_person1).setOnClickListener(this);
        findViewById(R.id.ll_bbs).setOnClickListener(this);

        findViewById(R.id.tv_question).setOnClickListener(this);
        findViewById(R.id.tv_rule).setOnClickListener(this);
        findViewById(R.id.tv_num).setOnClickListener(this);
        findViewById(R.id.tv_handle).setOnClickListener(this);
        findViewById(R.id.ll_worker_extra).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_mall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainPage1Activity.this, MallActivity.class);
                startActivity(it);
            }
        });
        findViewById(R.id.ll_work_insurance).setOnClickListener(v -> {Intent it = new Intent(MainPage1Activity.this, InsuranceBuyActivity.class);
            startActivity(it);});
        findViewById(R.id.ll_ebuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainPage1Activity.this, EbuyActivity.class);
                startActivity(it);
            }
        });
        requestBanner();
        final TextView tel = (TextView) findViewById(R.id.alarmtel);
        final String telNum = tel.getText().toString().replace("-", "");

        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainPage1Activity.this).setTitle("呼出:" + telNum)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNum));
                                startActivity(intent1);
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });


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
            case R.id.ll_person1:
                jumpToMall();
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

    private void jumpToMall() {
        Intent intent = new Intent(this, MallActivity.class);
        startActivity(intent);


    }

    /**
     * 跳转到维保服务页面
     */
    private void jumpToMainService() {
        //  NHMentenanceActivity  MaintenanceServiceActivity  MaintenanceServiceActivity
        Intent intent = new Intent(this, MaintenanceServiceActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到维修单页面           NHFixActivity  FixOrderListActivity
     */
    private void jumpToRepair() {
        Intent intent = new Intent(this, FixOrderListActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到紧急救援    RescuLookActivity  AlarmListActivity
     */
    private void jumpToAlarmList() {
        Intent intent = new Intent(this, AlarmListActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到电梯百科
     */
    private void jumpToWiki() {
        //RescuLookActivity  LiftKnowledgeActivity
        Intent intent = new Intent(this,LiftKnowledgeActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到个人中心
     */
    private void jumpToPerson() {  //PersonActivity
        Intent intent = new Intent(this, PersonActivity.class);
        startActivity(intent);
    }

    //   MaintenanceManagerActivity EMantenanceLookActivity
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
//        Intent intent = new Intent(MainPageActivity.this, TitleListActivity.class);
//        intent.putExtra("type", "常见问题");
//        startActivity(intent);
        Intent intent =new Intent(this,NousActivity.class);
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
        Intent intent =new Intent(this,NousActivity.class);
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
        Intent intent =new Intent(this,NousActivity.class);
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

        Intent intent =new Intent(this,NousActivity.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("kntype", "安全法规");
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void requestBannerAdv(String Id, final ImageView iv) {
        String server = getConfig().getServer() + NetConstant.GET_ADVERTISEMENT_DETAIL;


        AdvDetailRequest request = new AdvDetailRequest();
        request.setHead(new NewRequestHead().setuserId(getConfig().getUserId()).setaccessToken(getConfig().getToken()));
        request.setBody(request.new AdvDetailBody().setId(Id));


        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                AdvDetailResponse response = AdvDetailResponse.getAdvDetail(result);
                final String i = response.getBody().getContent();
                if (iv != null) {
                            Intent intent = new Intent(MainPage1Activity.this, NousDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("kntype", "详情");
                            bundle.putString("content", i);
                            intent.putExtras(bundle);
                            startActivity(intent);
                }

            }
        };

        addBackGroundTask(netTask);
    }

    @Override
    public void performItemCallback(final ImageView iv) {
        //final String info = (String) iv.getTag(R.id.url);
        if (StringUtils.isNotEmpty((String) iv.getTag())) {
            requestBannerAdv((String) iv.getTag(), iv);
        }
    }
}
