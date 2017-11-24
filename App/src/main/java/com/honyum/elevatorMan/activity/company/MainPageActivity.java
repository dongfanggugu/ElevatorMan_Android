package com.honyum.elevatorMan.activity.company;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.chorstar.jni.ChorstarJNI;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.ChatActivity;
import com.honyum.elevatorMan.activity.common.HelpCenterActivity;
import com.honyum.elevatorMan.activity.common.MallActivity;
import com.honyum.elevatorMan.activity.common.NousActivity;
import com.honyum.elevatorMan.activity.common.NousDetailActivity;
import com.honyum.elevatorMan.activity.common.PersonActivity;
import com.honyum.elevatorMan.activity.common.SignActivity;
import com.honyum.elevatorMan.activity.worker.LiftKnowledgeActivity;
import com.honyum.elevatorMan.adapter.BannerAdapter;
import com.honyum.elevatorMan.adapter.PageIndicatorAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.base.SysActivityManager;
import com.honyum.elevatorMan.data.BannerInfo;
import com.honyum.elevatorMan.net.AdvDetailRequest;
import com.honyum.elevatorMan.net.AdvDetailResponse;
import com.honyum.elevatorMan.net.BannerResponse;
import com.honyum.elevatorMan.net.EmptyRequest;
import com.honyum.elevatorMan.net.GetApplyResponse;
import com.honyum.elevatorMan.net.GetApplyResponseBody;
import com.honyum.elevatorMan.net.PersonResponse;
import com.honyum.elevatorMan.net.PersonsRequest;
import com.honyum.elevatorMan.net.UploadFileRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.CrashHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static com.honyum.elevatorMan.activity.common.ChatActivity.MODE_PROPERTY;

/**
 * Created by Star on 2017/6/14.
 */

public class MainPageActivity extends BaseFragmentActivity implements View.OnClickListener,ListItemCallback<ImageView> {
    private boolean hasAlarm = false;

    private TextView personNum;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage1);
        initTitle();

//        initPageIndicator();

        initView();
        //开启位置上传
        startLocationService();
        // startService(new Intent(this, LocationService.class));
    }
    private List<Integer> pics;

    private void requestBanner() {
        String server = getConfig().getServer() + NetConstant.GET_BANNER;

        //String request = Constant.EMPTY_REQUEST;

        NetTask netTask = new NetTask(server, new EmptyRequest()) {
            @Override
            protected void onResponse(NetTask task, String result) {
                BannerResponse response = BannerResponse.getResult(result);
                initPageIndicator(response.getBody());
            }
        };

        addBackGroundTask(netTask);
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
                if(iv!=null)
                {
                            Intent intent = new Intent(MainPageActivity.this, NousDetailActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("kntype", "详情");
                            bundle.putString("content",i);
                            intent.putExtras(bundle);
                            startActivity(intent);
                }

            }
        };

        addBackGroundTask(netTask);
    }

    private RequestBean requestFileLog(String fileArray, String filename) {
        RequestHead head = new NewRequestHead().setaccessToken(getConfig().getToken()).setuserId(getConfig().getUserId());

        UploadFileRequest request = new UploadFileRequest();
        request.setHead(head);

        request.setBody(request.new UploadFileRequestBody().setImg(fileArray).setName(filename));
        return request;
    }

    private void uploadLogFile(String fileArray, String filename, final String path) {
        String server = getConfig().getServer() + NetConstant.UPLOAD_FILE;

        NetTask netTask = new NetTask(server, requestFileLog(fileArray, filename)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                File deleteFile = new File(path);
                if (deleteFile != null) {
                    deleteFile.delete();
                }
            }
        };
        addBackGroundTask(netTask);
    }


    private RequestBean getPesonsBean() {

        PersonsRequest rt = new PersonsRequest();
        rt.setHead(new NewRequestHead().setuserId(getConfig().getUserId()).setaccessToken(getConfig().getToken()));
        rt.setBody(rt.new PersonsBody().setBranchId(getConfig().getBranchId()));
        return rt;

    }
    private void getPersons() {
        String address = getConfig().getServer() + NetConstant.GET_PERSONS;
        NetTask netTask = new NetTask(address, getPesonsBean()) {
            @Override
            protected void onResponse(NetTask task, String result) {

                PersonResponse pr = PersonResponse.getPersonResponse(result);
                if (StringUtils.isNotEmpty(pr.getBody().getCount())) {
                    personNum.setText(pr.getBody().getCount());
                }
                personNum.setOnClickListener(v -> goToLookActivity());


            }
        };
        addTask(netTask);
    }
    public void goToLookActivity() {
        Intent it = new Intent(MainPageActivity.this, LookPersonsActivity.class);
        startActivity(it);
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
//            if(pic.getPicUrl()!="") {
//                iv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        Uri content_url = Uri.parse(pic.getPicUrl());
//                        intent.setData(content_url);
//                        startActivity(intent);
//                    }
//                });
//            }
//            else
//            {
//                requestBannerAdv(pic.getId(),iv);
//            }
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
    public void performItemCallback(final ImageView iv) {
        if (StringUtils.isNotEmpty((String) iv.getTag()))
        {
            requestBannerAdv((String) iv.getTag(),iv);
        }
    }
    private void initPageIndicator1() {
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
        getPersons();
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

        findViewById(R.id.tv_attendance).setOnClickListener(v -> jumpToAttendance());

        findViewById(R.id.ll_company_extra_content).setVisibility(View.VISIBLE);

        findViewById(R.id.ll_business).setOnClickListener(v -> jumpToMall());
        findViewById(R.id.ll_insurance).setOnClickListener(v -> jumpToInsurance());
        findViewById(R.id.ll_chat).setOnClickListener(v -> jumpToChat());

        findViewById(R.id.ll_content).setVisibility(View.VISIBLE);
        personNum = (TextView) findViewById(R.id.tv_personsnum);
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
        final TextView tel = (TextView) findViewById(R.id.alarmtel);
        final String telNum = tel.getText().toString().replace("-","");

        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainPageActivity.this).setTitle("呼出:"+telNum)
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

        checkError();
        requestBanner();

    }

    private void checkError()
    {
        File[] files = CrashHandler.getFiles();
        if (CrashHandler.getFiles() != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file != null) {
                    if (file.isDirectory()) {
                        file.delete();
                        Log.i("MainActivity", "删除文件夹！");
                        return;
                    }

                }
                try {
                    FileInputStream fileInputStream = new FileInputStream(files[i]);
                    byte[] buffer = new byte[(int) file.length()];
                    fileInputStream.read(buffer);
                    fileInputStream.close();
                    uploadLogFile(Base64.encodeToString(buffer, Base64.DEFAULT), file.getName(), file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }

    private void jumpToChat() {
        Intent it = new Intent(this, ChatActivity.class);
        it.putExtra("enter_mode", MODE_PROPERTY);
        startActivity(it);
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
            case R.id.ll_person1:
                jumpToMall();
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
            case R.id.tv_attendance:
                jumpToAttendance();
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

    private void requestApply() {

        String server = getConfig().getServer() + NetConstant.GET_APPLIY;
        RequestBean request = new RequestBean();
        RequestHead head = new RequestHead();
        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());
        request.setHead(head);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                GetApplyResponse response = GetApplyResponse.getResponse(result);
                dealResult(response);
            }
        };

        addTask(netTask);

    }

    private void dealResult(GetApplyResponse response) {

        if (response.getBody() != null) {
            dealState(response.getBody());
        }
    }

    private void dealState(GetApplyResponseBody body) {

        switch (body.getState()) {

            case "0": {
                Intent it = new Intent(this, CompanyApplyActivity.class);
                it.putExtra("data", body);
                startActivity(it);
                break;
            }
            case "1": {
                Intent it = new Intent(this, InsuranceLookActivity.class);
                startActivity(it);
                break;
            }
            case "2": {
                Intent it = new Intent(this, CompanyApplyActivity.class);
                it.putExtra("data", body);
                startActivity(it);
                break;
            }
            default: {
                GetApplyResponseBody gb = new GetApplyResponseBody();
                gb.setState("99");
                Intent it = new Intent(this, CompanyApplyActivity.class);
                it.putExtra("data", gb);
                startActivity(it);
                break;
            }
        }

    }
    private void jumpToInsurance() {

        if (getConfig().getBranchId().equals("0000000000")) {
//        Intent intent = new Intent(this, CompanyApplyActivity.class);
//        startActivity(intent);
            requestApply();
        } else {
            GetApplyResponseBody b = new GetApplyResponseBody();
            b.setState("1");
            dealState(b);
        }
    }
    /**
     * 跳转到维保服务页面
     */
    private void jumpToMainService() {
        Intent intent = new Intent(this, NHMentenanceActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到维修单页面
     */
    private void jumpToRepair() {
        Intent intent = new Intent(this, NHFixActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到紧急救援
     */
    private void jumpToAlarmList() {
        Intent intent = new Intent(this, RescuLookActivity.class);
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
        Intent intent = new Intent(this, EMantenanceLookActivity.class);
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
    private void jumpToAttendance() {
        Intent intent = new Intent(this, SignActivity.class);
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        popMsgAlertWithCancel(getString(R.string.exit_confirm), new BaseFragmentActivity.IConfirmCallback() {
            @Override
            public void onConfirm() {
                MainPageActivity.super.onBackPressed();
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
