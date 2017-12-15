package com.honyum.elevatorMan.activity.property;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.CommonMainPage;
import com.honyum.elevatorMan.activity.common.EditIconActivity;
import com.honyum.elevatorMan.activity.common.MainPage1Activity;
import com.honyum.elevatorMan.activity.common.NousActivity;
import com.honyum.elevatorMan.activity.common.NousDetailActivity;
import com.honyum.elevatorMan.activity.common.PersonActivity;
import com.honyum.elevatorMan.activity.common.ToDoListActivity;
import com.honyum.elevatorMan.activity.company.MainPageActivity;
import com.honyum.elevatorMan.adapter.BannerAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.base.SysActivityManager;
import com.honyum.elevatorMan.data.BannerInfo;
import com.honyum.elevatorMan.net.AdvDetailRequest;
import com.honyum.elevatorMan.net.AdvDetailResponse;
import com.honyum.elevatorMan.net.BannerResponse;
import com.honyum.elevatorMan.net.EmptyRequest;
import com.honyum.elevatorMan.net.UploadFileRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.CrashHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class PropertyMainPageActivity extends BaseFragmentActivity implements View.OnClickListener,ListItemCallback<ImageView> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage1);

        initTitle();

//        initPageIndicator();

        initView();
    }
    /**
     * 初始化标题
     */
    private void initTitle() {
        initTitleBar(R.id.title_main_page,"首页");
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

                            Intent intent = new Intent(PropertyMainPageActivity.this, NousDetailActivity.class);
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

    private void initView() {




        findViewById(R.id.tv_attendance).setOnClickListener(v -> {


            Intent it = new Intent(PropertyMainPageActivity.this, ToDoListActivity.class);
            startActivity(it);
        });
        findViewById(R.id.ll_rescue).setOnClickListener(v -> jumpToCurAlarm());
        findViewById(R.id.ll_maintenance).setOnClickListener(v -> jumpToProjectList());
        findViewById(R.id.ll_fix).setOnClickListener(v -> jumpToNearMt());
//        findViewById(R.id.ll_person).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpToPerson();
//            }
//        });

        findViewById(R.id.ll_bbs).setOnClickListener(v -> jumpToElevatorMall());

        ((TextView)findViewById(R.id.tv_evemall)).setText("电梯商城");
        ((TextView)findViewById(R.id.tv_nhfix)).setText("附近维保");
        ((ImageView)findViewById(R.id.iv_image)).setImageResource(R.drawable.mall);
        findViewById(R.id.tv_question).setOnClickListener(this);
        findViewById(R.id.tv_rule).setOnClickListener(this);
        findViewById(R.id.tv_num).setOnClickListener(this);
        findViewById(R.id.tv_handle).setOnClickListener(this);
        final TextView tel = (TextView) findViewById(R.id.alarmtel);
        final String telNum = tel.getText().toString().replace("-","");

        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PropertyMainPageActivity.this).setTitle("呼出:"+telNum)
                        .setPositiveButton("确定", (dialog, which) -> {
                            Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNum));
                            startActivity(intent1);
                        })
                        .setNegativeButton("返回", (dialog, which) -> dialog.dismiss()).show();
            }
        });
        checkError();

        requestBanner();


//        findViewById(R.id.ll_current_alarm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpToCurAlarm();
//            }
//        });
//
//        findViewById(R.id.ll_project_alarm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpToProAlarm();
//            }
//        });
//
//        findViewById(R.id.ll_elevator_mall).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpToElevatorMall();
//            }
//        });
//
//        findViewById(R.id.ll_maintenance).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpToProjectList();
//            }
//        });
//
//        findViewById(R.id.ll_personal_center).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpToPerson();
//            }
//        });
//
//        findViewById(R.id.ll_near_maintenance).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpToNearMt();
//            }
//        });
//
//        findViewById(R.id.ll_value_added_service).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpToValueAddedService();
//            }
//        });
    }


    /**
     * 跳转增值服务
     */
    private void jumpToValueAddedService() {
    }


    /**
     * 跳转附近维保
     */
    private void jumpToNearMt() {
        Intent intent = new Intent(this, NearMaintenanceActivity.class);
        startActivity(intent);
    }


    /**
     * 跳到个人信息
     */
    private void jumpToPerson() {
        Intent intent = new Intent(this, PersonActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到维保管理
     */
    private void jumpToProjectList() {
        Intent intent = new Intent(this, PropertyMaintenanceActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到电梯商城
     */
    private void jumpToElevatorMall() {
        Intent intent = new Intent(this, ElevatorMallActivity.class);
        startActivity(intent);
    }


    /**
     * 跳到项目报警
     */
    private void jumpToProAlarm() {
        Intent intent = new Intent(this, PropertyActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到当前处理的报警
     */
    private void jumpToCurAlarm() {

        Intent intent = new Intent(this, AlarmHandleActivity.class);
        startActivity(intent);

        /*String alarmId = getConfig().getAlarmId();
        String alarmState = getConfig().getAlarmState();

        if (StringUtils.isEmpty(alarmId) || StringUtils.isEmpty(alarmState)) {
            showToast(getString(R.string.no_alarm));
            return;
        }

        Intent intent = new Intent(this, AlarmTraceActivity.class);
        intent.putExtra("alarm_id", alarmId);

        if (alarmState.equals("1")) {  //已经出发
            intent.setAction(Constant.ACTION_WORKER_ASSIGNED);
        } else if (alarmState.equals("2")) {    //已经到达
            intent.setAction(Constant.ACTION_WORKER_ARRIVED);
        } else if (alarmState.equals("3")) {    //已经完成
            intent.setAction(Constant.ACTION_ALARM_COMPLETE);
            intent.putExtra("msg", getString(R.string.property_complete));
        } else if (alarmState.equals(Constant.ALARM_STATE_START)) {    //发生报警
            intent.setAction(Constant.ACTION_ALARM_PROPERTY);
        }
        startActivity(intent);*/
    }

    @Override
    public void onBackPressed() {
        popMsgAlertWithCancel(getString(R.string.exit_confirm), () -> {
            PropertyMainPageActivity.super.onBackPressed();
            SysActivityManager.getInstance().exit();
        }, "否", "是", getString(R.string.exit_confirm));
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
        }
    }
    @Override
    public void performItemCallback(final ImageView iv) {
        //final String info = (String) iv.getTag(R.id.url);
      if(StringUtils.isNotEmpty((String) iv.getTag()))
        {
            requestBannerAdv((String) iv.getTag(),iv);
        }
    }
}
