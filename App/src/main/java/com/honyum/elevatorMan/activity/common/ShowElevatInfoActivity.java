package com.honyum.elevatorMan.activity.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.util.common.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.EleInfo;
import com.honyum.elevatorMan.data.ElevatorInfo;
import com.honyum.elevatorMan.data.Point;
import com.honyum.elevatorMan.data.TopErrorTypeInfo;
import com.honyum.elevatorMan.net.EleErrorUpdateRequest;
import com.honyum.elevatorMan.net.EleRecordRequest;
import com.honyum.elevatorMan.net.EleRecordResponse;
import com.honyum.elevatorMan.net.EleRecordUpdateRequest;
import com.honyum.elevatorMan.net.EleTopErrorTypeResponse;
import com.honyum.elevatorMan.net.NewRequestHead;
import com.honyum.elevatorMan.net.UploadImageRequest;
import com.honyum.elevatorMan.net.UploadImageResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.honyum.elevatorMan.activity.common.SelectPhotoActivity.KEY_PHOTO_PATH;

/**
 * Created by Star on 2017/8/22.
 */

public class ShowElevatInfoActivity extends BaseFragmentActivity {
    private static final int FIRST = 110;
    private static final int SECOND = 111;
    private static final int THIRD = 112;


    private ImageView signImage;
    private ImageView useImage;
    private ImageView portImage;

    private ImageView full_image;

    private BaiduMap mMap;
    InnerNode str1 = new InnerNode();
    InnerNode str2 = new InnerNode();
    InnerNode str3 = new InnerNode();

    private String img1 = "";
    private String img2 = "";
    private String img3 = "";

    private String url1 = "";
    private String url2 = "";
    private String url3 = "";


    private Button btn_del_1;
    private Button btn_del_2;
    private Button btn_del_3;


    private TextView tv_x;
    private TextView tv_y;


    private ElevatorInfo mElevatorInfo;


    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;
    private TextView tv_elenum;
    private TextView tv_address;
    private String mPublicPath;

    private List<TopErrorTypeInfo> errorType;
    private MapView baiduMap;
    private Marker marker;
    private ScrollView mScrollView;

    public void preView() {
        SDKInitializer.initialize(getApplicationContext());

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preView();
        setContentView(R.layout.activity_update_eve_info);
        initTitleBar();
        initView();
    }


    private class MyBDLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // 非空判断
            if (bdLocation != null) {
                // 根据BDLocation 对象获得经纬度以及详细地址信息
                tv_y.setText(bdLocation.getLatitude() + "");
                tv_x.setText(bdLocation.getLongitude() + "");
                String address = bdLocation.getAddrStr();
                if (mLocationClient.isStarted()) {
                    // 获得位置之后停止定位
                    mLocationClient.stop();
                }
//                mMap.setMyLocationEnabled(false);//关键点，必须设置
//
//                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
//                        .fromResource(R.drawable.marker_worker);
//
//
//                LatLng point = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//                MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor).position(point).zIndex(9).draggable(true);
//                marker = (Marker) mMap.addOverlay(markerOption);
//                mMap.setOnMapStatusChangeListener(new mapChangeListener());

                mMap.setMyLocationEnabled(false);//关键点，必须设置
//                BitmapDescriptor mark = BitmapDescriptorFactory
//                        .fromResource(R.drawable.marker_worker);
//                OverlayOptions option = new MarkerOptions().position(
//                        new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude()))
//                        .zIndex(9)  //设置marker所在层级
//                        .draggable(true);  //设置手势拖拽
//                marker = (Marker)(mMap.addOverlay(option));
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude()));
                mMap.animateMapStatus(update);
                mMap.setOnMapStatusChangeListener(new mapChangeListener());
                baiduMap.showZoomControls(false);
                baiduMap.showScaleControl(false);
                View child = baiduMap.getChildAt(1);
                if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
                    child.setVisibility(View.INVISIBLE);
                }
                baiduMap.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP){
                            //允许ScrollView截断点击事件，ScrollView可滑动
                            mScrollView.requestDisallowInterceptTouchEvent(false);
                        }else{
                            //不允许ScrollView截断点击事件，点击事件由子View处理
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                        return false;
                    }
                });

            }
        }
    }
    private class  mapChangeListener implements BaiduMap.OnMapStatusChangeListener
    {

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            tv_y.setText(mapStatus.target.latitude + "");
            tv_x.setText(mapStatus.target.longitude + "");


        }
    }

    private void requestUpdateElevatorError(String reason, final DialogInterface dialog) {


        EleErrorUpdateRequest rt = new EleErrorUpdateRequest();
        rt.setHead(new NewRequestHead().setuserId(getConfig().getUserId()).setaccessToken(getConfig().getToken()));
        rt.setBody(rt.new EleErrorUpdateRequestBody().setLiftNum(tv_elenum.getText() + "").setErrorReason(reason));

        NetTask task = new NetTask(getConfig().getServer() + NetConstant.EDIT_ERROR_REASON,
                rt) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("提交成功！");
                dialog.dismiss();
                finish();

            }
        };
        addTask(task);

    }

    private void requestErrorType() {


        RequestBean rt = new RequestBean();
        rt.setHead(new NewRequestHead().setuserId(getConfig().getUserId()).setaccessToken(getConfig().getToken()));
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.ERROR_REASON,
                rt) {
            @Override
            protected void onResponse(NetTask task, String result) {
                EleTopErrorTypeResponse response = EleTopErrorTypeResponse.getEleTopErrorTypeResponse(result);
                errorType = response.getBody();

                if (errorType != null) {
                    String[] items = new String[errorType.size()];
                    for (int i = 0; i < errorType.size(); i++) {
                        items[i] = errorType.get(i).getName();
                    }
                    showErrorDialog(items);
                }


            }
        };
        addTask(task);

    }

    /**
     * 获得所在位置经纬度及详细地址
     */
    public void getLocation() {
        // 声明定位参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精度
        option.setCoorType("bd09ll");// 设置返回定位结果是百度经纬度 默认gcj02
        option.setScanSpan(5000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        // 设置定位参数
        mLocationClient.setLocOption(option);
        // 启动定位
        mLocationClient.start();

    }

    public void showErrorDialog(final String[] itms) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(itms, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSubmitDialog("提交" + itms[which] + "位置的错误吗？", itms[which]);
            }
        });
        builder.create().show();
    }


    private void initView() {

        if (getIntent().getStringExtra("info") == null) {
            showToast("操作失败");
            return;
        }
        String s = getIntent().getStringExtra("info");
        if (StringUtils.isNotEmpty(s)) {
            requestEleInfo(s);
        }

        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValues())
                    saveInfoToDatabase();
                else
                    showToast("请输入完整信息！");
            }
        });
        findViewById(R.id.tv_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestErrorType();
            }
        });

        mScrollView = (ScrollView)findViewById(R.id.scrollview);
        tv_x = (TextView) findViewById(R.id.tv_x);
        tv_y = (TextView) findViewById(R.id.tv_y);

        tv_elenum = (TextView) findViewById(R.id.tv_elenum);
        tv_address = (TextView) findViewById(R.id.tv_address);


        btn_del_1 = (Button) findViewById(R.id.btn_del_1);
        btn_del_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signImage.setImageResource(R.drawable.icon_img_original);
                btn_del_1.setVisibility(View.GONE);
                img1 = "";
                signImage.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {

                        Intent it = new Intent(ShowElevatInfoActivity.this, SelectPhotoActivity.class);
                        it.putExtra("order", FIRST);
                        startActivityForResult(it, FIRST);

                    }
                });
            }
        });
        btn_del_2 = (Button) findViewById(R.id.btn_del_2);
        btn_del_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useImage.setImageResource(R.drawable.icon_img_original);
                btn_del_2.setVisibility(View.GONE);
                img2 = "";
                useImage.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {

                        Intent it = new Intent(ShowElevatInfoActivity.this, SelectPhotoActivity.class);
                        it.putExtra("order", SECOND);
                        startActivityForResult(it, SECOND);
                    }
                });
            }
        });
        btn_del_3 = (Button) findViewById(R.id.btn_del_3);
        btn_del_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portImage.setImageResource(R.drawable.icon_img_original);
                btn_del_3.setVisibility(View.GONE);
                img3 = "";
                portImage.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {

                        Intent it = new Intent(ShowElevatInfoActivity.this, SelectPhotoActivity.class);
                        it.putExtra("order", THIRD);
                        startActivityForResult(it, THIRD);
                    }
                });
            }
        });
        mLocationClient = new LocationClient(getApplicationContext());
        mBDLocationListener = new MyBDLocationListener();
        // 注册监听
        mLocationClient.registerLocationListener(mBDLocationListener);


//        findViewById(R.id.tv_getpostion).
//
//                setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        getLocation(v);
//                    }
//                });
        signImage = (ImageView)

                findViewById(R.id.iv_sign);

        useImage = (ImageView)

                findViewById(R.id.iv_use);

        portImage = (ImageView)

                findViewById(R.id.iv_port);

        signImage.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(ShowElevatInfoActivity.this, SelectPhotoActivity.class);
                it.putExtra("order", FIRST);
                startActivityForResult(it, FIRST);

            }
        });
        useImage.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(ShowElevatInfoActivity.this, SelectPhotoActivity.class);
                it.putExtra("order", SECOND);
                startActivityForResult(it, SECOND);
            }
        });
        portImage.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(ShowElevatInfoActivity.this, SelectPhotoActivity.class);
                it.putExtra("order", THIRD);
                startActivityForResult(it, THIRD);
            }
        });
        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValues()) {
                    //TODO 发送网络请求，先提交图片。 不存数据库。
                    submitEleInfo();
                } else {
                    showToast("请输入完整信息！");
                }
            }
        });
        getLocation();
        baiduMap = (MapView) findViewById(R.id.mapView);
        mMap = baiduMap.getMap();

    }

    public void submitEleInfo() {

        str1.url = url1;
        str2.url = url2;
        str3.url = url3;

        str1.img = img1;
        str2.img = img2;
        str3.img = img3;

        str2.next = str3;
        str1.next = str2;
        str3.next = null;


        requestUploadImage(str1);


    }

    private RequestBean getImageRequestBean(String userId, String token, String path) {
        UploadImageRequest request = new UploadImageRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new UploadImageBody().setImg(path));
        return request;
    }

    private void requestUploadImage(final InnerNode node) {

        NetTask task = new NetTask(getConfig().getServer() + NetConstant.UP_LOAD_IMG,
                getImageRequestBean(getConfig().getUserId(), getConfig().getToken(), node.img)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                UploadImageResponse response = UploadImageResponse.getUploadImageResponse(result);
                if (response.getHead() != null && response.getHead().getRspCode().equals("0")) {
                    String url = response.getBody().getUrl();
                    node.url = url;
                    if (node.next != null) {
                        requestUploadImage(node.next);
                    } else if (checkUrl()) {
                        requestUpdateEleInfo();
                    }
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);

    }

    public boolean checkUrl() {
        if (str1 == null || str2 == null || str3 == null)
            return false;
        else
            return true;
    }

    class InnerNode {
        String url;
        String img;
        InnerNode next;

    }


    public void requestUpdateEleInfo() {
        String server = getConfig().getServer() + NetConstant.ELE_RECORD_UPDATE;

        EleRecordUpdateRequest er = new EleRecordUpdateRequest();
        List<EleRecordUpdateRequest.EleRecordUpdateBody> data = new ArrayList<EleRecordUpdateRequest.EleRecordUpdateBody>();
        data.add(er.new EleRecordUpdateBody()
                .setLiftNum(tv_elenum.getText() + "").setLat(Double.valueOf(tv_y.getText() + ""))
                .setLng(Double.valueOf(tv_x.getText() + "")).setSignPicture(str1.url).setLogoPicture(str2.url).setDoorwayPicture(str3.url));
        er.setHead(new NewRequestHead().setaccessToken(getConfig().getToken()).setuserId(getConfig().getUserId()));
        er.setBody(data);
        NetTask netTask = new NetTask(server, er) {
            @Override
            protected void onResponse(NetTask task, String result) {
                finish();
            }
        };
        addTask(netTask);
    }

    private boolean checkValues() {
        if (img1.equals("") || img2.equals("") || img3.equals("") || (tv_x.getText() + "").equals("0.0") || (tv_y.getText() + "").equals("0.0") || StringUtils.isEmpty(tv_elenum.getText() + "") || StringUtils.isEmpty(tv_address.getText() + ""))

        {
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            selectPic(requestCode, data);
        }
    }


    public void displayBigImage(String img) {

        full_image = (ImageView) findViewById(R.id.full_image);
        full_image.setVisibility(View.VISIBLE);
        full_image.setImageBitmap(Utils.stringtoBitmap(img));
        full_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                full_image.setVisibility(View.GONE);
            }
        });

    }

    private void selectPic(int requestCode, Intent data) {

        Intent it = data;
        if (it != null && StringUtils.isNotEmpty(it.getStringExtra(KEY_PHOTO_PATH)))
            switch (data.getIntExtra("order", 0)) {
                case FIRST:

                    Log.e("TAG", "selectPic: " + it.getStringExtra(KEY_PHOTO_PATH));
                    img1 = Utils.imgToStrByBase64BySize(it.getStringExtra(KEY_PHOTO_PATH));
                    btn_del_1.setVisibility(View.VISIBLE);
                    Glide.with(ShowElevatInfoActivity.this).load(it.getStringExtra(KEY_PHOTO_PATH)).override(480, 640).into(signImage);
                    signImage.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick(View v) {

                            displayBigImage(img1);
                        }
                    });

                    break;
                case SECOND:
                    Glide.with(ShowElevatInfoActivity.this).load(it.getStringExtra(KEY_PHOTO_PATH)).override(480, 640).into(useImage);
                    //img2 = showImage(useImage, it);
                    img2 = Utils.imgToStrByBase64BySize(it.getStringExtra(KEY_PHOTO_PATH));
                    btn_del_2.setVisibility(View.VISIBLE);
                    useImage.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick(View v) {

                            displayBigImage(img2);
                        }
                    });
                    break;
                case THIRD:
                    Glide.with(ShowElevatInfoActivity.this).load(it.getStringExtra(KEY_PHOTO_PATH)).override(480, 640).into(portImage);
                    img3 = Utils.imgToStrByBase64BySize(it.getStringExtra(KEY_PHOTO_PATH));
                    btn_del_3.setVisibility(View.VISIBLE);
                    portImage.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick(View v) {

                            displayBigImage(img3);
                        }
                    });
                    break;
                case 0:
                    break;
            }
    }

    public void showSubmitDialog(String message, final String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //TODO  提交错误请求接口
                requestUpdateElevatorError(errorMessage, dialog);

            }
        });
        builder.create().show();
    }

    public void showExitDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (checkValues()) {
                    dialog.dismiss();
                    saveInfoToDatabase();
                } else {
                    dialog.dismiss();
                    showToast("请填写完整的信息！");
                }

            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if (checkValues())
            showExitDialog("当前未保存，需要保存后再退出吗？");
        else
            super.onBackPressed();
    }

    private void saveInfoToDatabase() {

        mElevatorInfo = new ElevatorInfo();
        mElevatorInfo.setEleId(tv_elenum.getText() + "");
        mElevatorInfo.setProjectName(tv_address.getText() + "");
        mElevatorInfo.setUserId(getConfig().getUserId());

        mElevatorInfo.setX(Double.valueOf(tv_x.getText() + ""));
        mElevatorInfo.setY(Double.valueOf(tv_y.getText() + ""));
        mElevatorInfo.setSignImage(img1);
        mElevatorInfo.setUseImage(img2);
        mElevatorInfo.setPortImage(img3);


        mElevatorInfo.saveOrUpdate("eleId=?", tv_elenum.getText() + "");

        finish();
    }


    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar("电梯信息", R.id.title,
                R.drawable.back_normal, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkValues())
                            showExitDialog("当前未保存，需要保存后再退出吗？");
                        else
                            finish();

                    }
                });
    }

    public String getPathFromUrl(String url) {
        String s = "";

        if (url.equals(""))
            return "";
        FutureTarget<File> future = Glide.with(this)
                .load(url)
                .downloadOnly(480, 640);
        try {
            File cacheFile = future.get();
            s = cacheFile.getAbsolutePath();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return s;
    }

    private class GetImageCacheTask extends AsyncTask<String, Void, File> {
        private final Intent mIntent;
        private WeakReference<Context> mContext;
        private final String mImageUrl;
        private final int mOder;

        public GetImageCacheTask(Context context, Intent it, String imageUrl, int order) {
            mContext = new WeakReference<Context>(context);
            mIntent = it;
            mImageUrl = imageUrl;
            mOder = order;
        }

        @Override
        protected File doInBackground(String... params) {
            //String imgUrl =  params[0];
            try {
                return Glide.with(mContext.get())
                        .load(mImageUrl)
                        .downloadOnly(480, 640)
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            String path = result.getPath();
            mIntent.putExtra(KEY_PHOTO_PATH, path);
            //Log.e("TAG", "onResponse: "+ it.getStringExtra(KEY_PHOTO_PATH));
            mIntent.putExtra("order", mOder);
            selectPic(0, mIntent);
            //此path就是对应文件的缓存路径

            //将缓存文件copy, 命名为图片格式文件
        }
    }

    public void requestEleInfo(String eleNum) {
        String server = getConfig().getServer() + NetConstant.ELE_RECORD_BYID;

        EleRecordRequest er = new EleRecordRequest();

        er.setHead(new NewRequestHead().setaccessToken(getConfig().getToken()).setuserId(getConfig().getUserId()));
        er.setBody(er.new EleRecordRequestBody().setLiftNum(eleNum.trim()));

        NetTask netTask = new NetTask(server, er) {
            @Override
            protected void onResponse(NetTask task, String result) {
                if (!result.equals("{}")) {
                    EleRecordResponse response = EleRecordResponse.getEleRecordResponse(result);
                    EleInfo ei = response.getBody();
                    if (ei != null) {
                        tv_elenum.setText(ei.getLiftNum());
                        tv_address.setText(ei.getCommunityName() + ei.getBuildingCode() + ei.getUnitCode());
                        tv_x.setText(ei.getLng() + "");
                        tv_y.setText(ei.getLat() + "");
                        new GetImageCacheTask(getApplicationContext(), new Intent(), StringUtils.isEmpty(ei.getSignPicture()) ? "" : ei.getSignPicture(), FIRST).execute();
                        new GetImageCacheTask(getApplicationContext(), new Intent(), StringUtils.isEmpty(ei.getLogoPicture()) ? "" : ei.getLogoPicture(), SECOND).execute();
                        new GetImageCacheTask(getApplicationContext(), new Intent(), StringUtils.isEmpty(ei.getDoorwayPicture()) ? "" : ei.getDoorwayPicture(), THIRD).execute();

                    }
                    else
                    {
                        showToast("未获取到当前编号的电梯信息");
                        finish();
                    }
                }
            }
        };
        addTask(netTask);
    }


}
