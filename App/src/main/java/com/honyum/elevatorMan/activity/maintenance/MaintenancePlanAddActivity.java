package com.honyum.elevatorMan.activity.maintenance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.MaintenanceServiceInfo;
import com.honyum.elevatorMan.data.MaintenanceTaskInfo;
import com.honyum.elevatorMan.net.MaintenanceServiceAddPlanRequest;
import com.honyum.elevatorMan.net.MaintenanceServiceArriveRequest;
import com.honyum.elevatorMan.net.MaintenanceServiceStartRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.honyum.elevatorMan.net.base.NetConstant.ADD_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.ARRIVE_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.ENSURED_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.EVA_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.FINISH_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.START_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.UNENSURE_STATE;
import static com.honyum.elevatorMan.net.base.NetConstant.UN_FINISH;

/**
 * Created by Star on 2017/6/9.
 */

public class MaintenancePlanAddActivity extends BaseFragmentActivity implements View.OnClickListener {
    private LinearLayout ll_moreinfo;
    private LinearLayout ll_date;
    private TextView tv_plan_date;
    private View dialogLayout;
    private DatePicker datePicker;
    private TimePicker timePicker;
    Date date;
    String s1;
    String s2;
    private AlertDialog alertDialog;
    private TextView tv_tel;
    private TextView tv_address;
    private TextView tv_time;
    private TextView tv_product_name;
    private String currId;
    private TextView tv_giveup;
    private TextView tv_addplan;
    private TextView tv_start;
    private ImageView iv_modify_datetime;
    private MaintenanceTaskInfo mMaintenanceTaskInfo;
    private MaintenanceServiceInfo mMaintenanceServiceInfo;
    private MapView baiduMap;
    private BaiduMap mMap;
    private Marker locationMarker;
    private ImageView iv_expandDetail;
    private TextView tv_product_name1;
    private TextView tv_product_name2;

    private String name = "";
    private String name1 = "";
    private String name2 = "";

    private boolean isTimePass;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preView();
        setContentView(R.layout.activity_order_select);
        initTitle();
        initView();
    }

    public void preView() {
        SDKInitializer.initialize(getApplicationContext());

    }

    /**
     * 标记地图
     */
    private void markMap(MaintenanceServiceInfo alarmInfo1) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        if (null == alarmInfo1) {
            return;
        }


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.marker_worker);


        double latitude = alarmInfo1.getVillaInfo().getLat();
        double longitude = alarmInfo1.getVillaInfo().getLng();
        LatLng point = new LatLng(latitude, longitude);
        MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor).position(point);
        locationMarker = (Marker) mMap.addOverlay(markerOption);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
        mMap.animateMapStatus(update);

    }

    /**
     * 标记地图
     */
    private void markMap(MaintenanceTaskInfo alarmInfo1) {


        //mBaiduMap.hideInfoWindow();
        if (null == mMap) {
            return;
        }

        if (null == alarmInfo1) {
            return;
        }


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.marker_worker);


        double latitude = alarmInfo1.getMaintOrderInfo().getVillaInfo().getLat();
        double longitude = alarmInfo1.getMaintOrderInfo().getVillaInfo().getLng();
        LatLng point = new LatLng(latitude, longitude);
        MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor).position(point);
        locationMarker = (Marker) mMap.addOverlay(markerOption);

    }
    /**
     * 初始化标题
     */
    private void initTitle() {

        Intent it = getIntent();

        if (it.getStringExtra("State").equals("-1"))
            initTitleBar("维保计划制定", R.id.title_order,
                    R.drawable.back_normal, backClickListener);
        else
            initTitleBar("维保任务单", R.id.title_order,
                    R.drawable.back_normal, backClickListener);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        baiduMap = (MapView) findViewById(R.id.mapView);
        mMap = baiduMap.getMap();

        iv_expandDetail = (ImageView) findViewById(R.id.iv_expandDetail);
        iv_expandDetail.setOnClickListener(this);
        dialogLayout = LayoutInflater.from(this).inflate(R.layout.dia_datetime_layout, null);
        datePicker = (DatePicker) dialogLayout.findViewById(R.id.datePicker);
        timePicker = (TimePicker) dialogLayout.findViewById(R.id.timePicker);
        findViewById(R.id.tv_start).setVisibility(View.GONE);
        findViewById(R.id.tv_giveup).setVisibility(View.GONE);



        date = new Date();

        //使用dialog组合日期和时间控件。
        findViewById(R.id.ll_date).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(date.getHours() + 1);
                timePicker.setCurrentMinute(0);
                int minute = timePicker.getCurrentMinute();
                s2 = "  " + (timePicker.getCurrentHour()) + ":" + (minute < 10 ? "0" + minute : minute);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        s2 = ("  " + hourOfDay + ":" + (minute < 10 ? "0" + minute : minute));
                    }
                });
                alertDialog.show();
            }
        });
        alertDialog = new AlertDialog.Builder(this).setTitle("选择时间").setView(dialogLayout).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        s1 = (datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                        String dateString = s1 + s2;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date d = new Date();
                        try {
                            isTimePass = false;
                            d = sdf.parse(dateString);
                            long t = d.getTime();
                            long cl = System.currentTimeMillis();

                            if (cl > t) {
                                isTimePass = false;
                            }
                            else {
                                isTimePass = true;
                            }
                            tv_time.setText(dateString);
                            dialog.dismiss();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        }).create();
        //end 组合控件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(System.currentTimeMillis());
        String s = sdf.format(d).toString();
        tv_plan_date = (TextView) findViewById(R.id.tv_plan_date);
//        tv_plan_date.setText(s);
        ll_moreinfo = (LinearLayout) findViewById(R.id.ll_moreinfo);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_product_name1 = (TextView) findViewById(R.id.tv_product_name1);
        tv_product_name2 = (TextView) findViewById(R.id.tv_product_name2);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_addplan = (TextView) findViewById(R.id.tv_addplan);
        iv_modify_datetime = (ImageView) findViewById(R.id.iv_modify_datetime);
        tv_giveup = (TextView) findViewById(R.id.tv_giveup);


        //获取页面的参数 进行VIEW填充
        Intent it = getIntent();
        mMaintenanceServiceInfo = (MaintenanceServiceInfo) it.getSerializableExtra("Info1");
        mMaintenanceTaskInfo = (MaintenanceTaskInfo) it.getSerializableExtra("Info");
        if(it.getStringExtra("State").equals(ADD_STATE))
        {
            //mMaintenanceServiceInfo = (MaintenanceServiceInfo) it.getSerializableExtra("Info");
            tv_address.setText(mMaintenanceServiceInfo.getVillaInfo().getCellName());
            tv_tel.setText(mMaintenanceServiceInfo.getSmallOwnerInfo().getName()+" "+mMaintenanceServiceInfo.getVillaInfo().getContactsTel());
            tv_time.setText(s);
            name = mMaintenanceServiceInfo.getVillaInfo().getBrand();
            name1 = mMaintenanceServiceInfo.getVillaInfo().getWeight() + "KG";
            name2 = mMaintenanceServiceInfo.getVillaInfo().getLayerAmount() + "层";

            currId = mMaintenanceServiceInfo.getId();

            markMap(mMaintenanceServiceInfo);
        }
        else
        {
            mMaintenanceTaskInfo = (MaintenanceTaskInfo)it.getSerializableExtra("Info");
            tv_address.setText(mMaintenanceTaskInfo.getMaintOrderInfo().getVillaInfo().getCellName());
            tv_tel.setText(mMaintenanceTaskInfo.getMaintOrderInfo().getSmallOwnerInfo().getName()+" "+mMaintenanceTaskInfo.getMaintOrderInfo().getVillaInfo().getContactsTel());
            tv_time.setText(mMaintenanceTaskInfo.getPlanTime());
            currId = mMaintenanceTaskInfo.getId();
            name = mMaintenanceTaskInfo.getMaintOrderInfo().getVillaInfo().getBrand();
            name1 = mMaintenanceTaskInfo.getMaintOrderInfo().getVillaInfo().getWeight() + "KG";
            name2 = mMaintenanceTaskInfo.getMaintOrderInfo().getVillaInfo().getLayerAmount() + "层";
            markMap(mMaintenanceTaskInfo);
        }
        changeBottomButton(it.getStringExtra("State"));



    }

    private void requestAddMaintOrderProcess() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_SERVICE_ADD,
                getAddRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showAppToast(getString(R.string.sucess));
                    finish();
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);
    }


    private RequestBean getAddRequestBean(String userId, String token) {
        MaintenanceServiceAddPlanRequest request = new MaintenanceServiceAddPlanRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new MaintenanceServiceAddPlanBody().setMaintOrderId(currId).setPlanTime(tv_time.getText().toString()));
        return request;
    }



    private void requestMaintOrderProcessWorkerSetOut(final String state) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_TASK_START,
                getSetOutRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showAppToast(getString(R.string.sucess));
                    changeBottomButton(state);
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);
    }
    private RequestBean getSetOutRequestBean(String userId, String token) {
        MaintenanceServiceStartRequest request = new MaintenanceServiceStartRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new MaintenanceServiceStartBody().setMaintOrderProcessId(currId));
        return request;
    }




    private RequestBean getArriveRequestBean(String userId, String token) {
        MaintenanceServiceArriveRequest request = new MaintenanceServiceArriveRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new MaintenanceServiceArriveBody().setMaintOrderProcessId(currId));
        return request;
    }

    private void requestMaintOrderProcessWorkerArrive(final String state) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_TASK_ARRIVE,
                getArriveRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showAppToast(getString(R.string.sucess));
                    changeBottomButton(state);
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);
    }



    /**
     * 根据字符串返回的state返回决定按钮的显示和隐藏，使用Java 7之后 的 case:String，切换状态使用
     *
     * @param state
     */
    private void changeBottomButton(String state) {

        switch (state) {
            case ADD_STATE:

                tv_addplan.setVisibility(View.VISIBLE);
                tv_addplan.setText(R.string.add_plan);
                tv_start.setVisibility(View.GONE);
                iv_modify_datetime.setVisibility(View.VISIBLE);
                tv_giveup.setVisibility(View.GONE);
                tv_addplan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(MaintenancePlanAddActivity.this).setTitle("确认添加计划吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(isTimePass)
                                        requestAddMaintOrderProcess();
                                        else showToast("选择日期应大于当前日期！");
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
                break;
            case UNENSURE_STATE:

                tv_addplan.setVisibility(View.GONE);
                //tv_addplan.setText(R.string.modify);
                tv_plan_date.setVisibility(View.GONE);
                tv_start.setVisibility(View.GONE);
                tv_giveup.setVisibility(View.GONE);
                iv_modify_datetime.setVisibility(View.GONE);
                break;
            case ENSURED_STATE:

                tv_plan_date.setVisibility(View.GONE);
                iv_modify_datetime.setVisibility(View.GONE);
                tv_addplan.setVisibility(View.VISIBLE);
                tv_addplan.setText(R.string.start_now);
                tv_addplan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(MaintenancePlanAddActivity.this).setTitle("确认现在出发吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestMaintOrderProcessWorkerSetOut(START_STATE);
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

                tv_start.setVisibility(View.GONE);
                tv_giveup.setVisibility(View.VISIBLE);
                tv_giveup.setText(R.string.today_giveup);
                tv_giveup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenancePlanAddActivity.this, MaintenanceTaskUnfinishActivity.class);
                        intent.putExtra("Id", currId);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case START_STATE:

                tv_plan_date.setVisibility(View.GONE);
                tv_addplan.setVisibility(View.VISIBLE);
                tv_addplan.setText(R.string.arrive);
                tv_addplan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(MaintenancePlanAddActivity.this).setTitle("确认到达吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestMaintOrderProcessWorkerArrive(ARRIVE_STATE);
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
                tv_start.setVisibility(View.GONE);
                tv_giveup.setVisibility(View.GONE);

                break;
            case ARRIVE_STATE:

                tv_plan_date.setVisibility(View.GONE);
                tv_addplan.setVisibility(View.GONE);
                //tv_addplan.setText(R.string.finish);
                tv_start.setVisibility(View.VISIBLE);
                tv_start.setText(R.string.finish);
                tv_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(MaintenancePlanAddActivity.this).setTitle("确认提交维保结果吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(MaintenancePlanAddActivity.this, MaintenanceTaskFinishActivity.class);
                                        intent.putExtra("Id", currId);
                                        startActivity(intent);
                                        finish();
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


                tv_giveup.setVisibility(View.GONE);
//                tv_giveup.setText(R.string.today_giveup);
//                tv_giveup.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(MaintenancePlanAddActivity.this, MaintenanceTaskUnfinishActivity.class);
//                        intent.putExtra("Id", currId);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
                break;
            case FINISH_STATE:

                tv_plan_date.setVisibility(View.GONE);
                tv_addplan.setVisibility(View.VISIBLE);
                tv_addplan.setText(R.string.look_result);
                tv_addplan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenancePlanAddActivity.this, MaintenanceTaskResult.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Info", mMaintenanceTaskInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                tv_start.setVisibility(View.GONE);
                tv_giveup.setVisibility(View.GONE);
                break;
            case EVA_STATE:

                tv_plan_date.setVisibility(View.GONE);
                tv_addplan.setVisibility(View.VISIBLE);
                tv_addplan.setText(R.string.look_eva);
                tv_addplan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenancePlanAddActivity.this, MaintenanceEvaResult.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Info", mMaintenanceTaskInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                tv_start.setVisibility(View.GONE);
                tv_giveup.setVisibility(View.GONE);
                break;
            case UN_FINISH:

                Intent intent = new Intent(this, MaintenancePlanAddActivity.class);
                intent.putExtra("State", ADD_STATE);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Info1", mMaintenanceServiceInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_expandDetail:

                if (ll_moreinfo.getVisibility() == View.VISIBLE) {
                    ll_moreinfo.setVisibility(View.GONE);
                    iv_expandDetail.setImageResource(R.drawable.openicon);
                } else {
                    ll_moreinfo.setVisibility(View.VISIBLE);
                    tv_product_name.setText(name);
                    tv_product_name1.setText(name1);
                    tv_product_name2.setText(name2);
                    iv_expandDetail.setImageResource(R.drawable.colseicon);
                }
                break;
        }
    }
}
