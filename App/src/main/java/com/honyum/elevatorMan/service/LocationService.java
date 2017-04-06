package com.honyum.elevatorMan.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.activity.LoginActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.base.MyApplication;
import com.honyum.elevatorMan.base.SysActivityManager;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.net.ReportLocationRequest;
import com.honyum.elevatorMan.net.ReportLocationRequest.ReportLocationReqBody;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTaskNew;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;

public class LocationService extends Service {

    private String TAG = "LocationService";

    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;
    private LocationStartListener mLocationStartListener;

    private static final String LOCATION_START = "com.chorstar.location_start";

    private static final int CODE_GPS = 61;
    private static final int CODE_CACHE = 65;
    private static final int CODE_ONLINE = 161;


    private PendingIntent mPendingIntent;

    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();

        //init baidu location sdk
        SDKInitializer.initialize(getApplicationContext());
        mLocationClient = new LocationClient(this);

        //register location start broadcast receiver
        mLocationStartListener = new LocationStartListener();
        IntentFilter filter = new IntentFilter();
        filter.addAction(LOCATION_START);
        this.registerReceiver(mLocationStartListener, filter);

        //register location complete broadcast receiver
        mBDLocationListener = new LocationListener();
        mLocationClient.registerLocationListener(mBDLocationListener);


        LocationClientOption option = new LocationClientOption();
        //设置显示地址
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setIsNeedAddress(true);
        option.setTimeOut(120 * 1000);
        option.setCoorType("bd09ll");
        //option.setScanSpan(alternation * 1000);
        option.setOpenGps(false);
        mLocationClient.setLocOption(option);

        setLocationTimer(this);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        // 清空位置信息
        getConfig().setLatitude(0);
        getConfig().setLongitude(0);

        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
            mLocationClient.requestLocation();
        }

        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    /**
     * use alarmManager to set timer, so we can avoid the effect of sleep of cpu
     */
    private void setLocationTimer(Context context) {

        Intent startIntent = new Intent();
        startIntent.setAction(LOCATION_START);
        mPendingIntent = PendingIntent.getBroadcast(context, 0, startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30 * 1000,
                mPendingIntent);
    }

    /**
     * timer broadcast listener
     */
    private class LocationStartListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //启动定位
            if (action.equals(LOCATION_START)) {
                //Log.i(TAG, "start location");

                if (mLocationClient != null && !mLocationClient.isStarted()) {
                    mLocationClient.start();
                    mLocationClient.requestLocation();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onDestroy");

        //cancel the timer
        if (mPendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(mPendingIntent);
        }

        //unregister location start listener
        if (mLocationStartListener != null) {
            this.unregisterReceiver(mLocationStartListener);
        }

        //unregister location complete listener
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
        }

        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    /*
     * 进行定位，当定位成功后更新地图的位置
     */
    private class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub

            //saveInfo2File();
            if (mLocationClient != null && mLocationClient.isStarted()) {
                mLocationClient.stop();
            }

            if (null == location) {
                return;
            }

            int returnCode = location.getLocType();
            Log.i(TAG, "getLocType:" + returnCode);
            if (returnCode != CODE_GPS
                    && returnCode != CODE_CACHE
                    && returnCode != CODE_ONLINE) {
                Log.i(TAG, "location failed");
                return;
            }

            String userId = getConfig().getUserId();
            String role = getConfig().getRole();
            String token = getConfig().getToken();

            //用户id为空，返回不处理
            if (StringUtils.isEmpty(userId)) {
                return;
            }

            //如果token唯恐，返回不处理
            if (StringUtils.isEmpty(token)) {
                return;
            }

            //用户角色不为修理工，返回，不处理
            if (!role.equals(Constant.WORKER)) {
                return;
            }

            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();

//			//模拟位置的移动
//			Employee employee = new Employee();
//
//			final double latitude= employee.getLatitude();
//			final double longitude = employee.getLongitude();

            Log.i(TAG, "lat:" + latitude);
            Log.i(TAG, "lng:" + longitude);
            Log.i(TAG, "add:" + location.getAddrStr());

            //当两次定位距离小于100米时，不上传位置
            double preLat = getConfig().getLatitude();
            double preLng = getConfig().getLongitude();

            //发送定位成功的标志
            Intent intent = new Intent();
            intent.setAction(Constant.ACTION_LOCATION_COMPLETE);

            intent.putExtra("lat", latitude);
            intent.putExtra("long", longitude);
            intent.putExtra("add", location.getAddrStr());
            sendBroadcast(intent);


            LatLng prePoint = new LatLng(preLat, preLng);
            LatLng curPoint = new LatLng(latitude, longitude);

            int distance = (int) DistanceUtil.getDistance(prePoint, curPoint);
            Log.i(TAG, "distance:" + distance);
//			if (distance < 100) {
//				Log.i(TAG, "no need to upload location");
//				return;
//			}

            //上传位置信息
            NetTaskNew task = new NetTaskNew(LocationService.this,
                    getConfig().getServer()
                            + NetConstant.URL_REPORT_LOCATION, getRequestBean(
                    userId, token, latitude, longitude)) {

                @Override
                protected void onReturn(NetTaskNew task, String result) {
                    // TODO Auto-generated method stub
                    super.onReturn(task, result);
                    Response response = Response.getResponse(result);
                    String code = response.getHead().getRspCode();
                    if (code.equals("0")) {        //上传成功

                        //上传成功后，将位置缓存到本地
                        getConfig().setLatitude(latitude);
                        getConfig().setLongitude(longitude);

                    } else if (code.equals("-3") || code.equals("-2")) {        //token验证失败

                        //保存错误编码
                        getConfig().setErrorCode(code);

                        //清除token,role和user信息
                        clearUserInfo();

                        //如果在前台，杀掉服务，同时到登陆页面
                        if (!Utils.isApplicationBroughtToBackground(LocationService.this)) {

                            SysActivityManager.getInstance().exit();
                            Intent intent = new Intent(LocationService.this,
                                    LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        stopSelf();
                    }
                }

            };
            task.start();
        }

    }

    /**
     * 用来获取locationService对象
     *
     * @author zhenhao
     */
    public class MyBinder extends Binder {

        public LocationService getService() {
            return LocationService.this;
        }
    }

    /**
     * 获取全局的application对象
     *
     * @return
     */
    private MyApplication getMyApplication() {
        return (MyApplication) getApplication();
    }

    /**
     * 获取Config对象
     *
     * @return
     */
    private Config getConfig() {
        return getMyApplication().getConfig();
    }

    /**
     * 清除用户信息
     */
    private void clearUserInfo() {

        // 将用户id和角色以及token保存在本地
        getConfig().setUserId("");
        getConfig().setRole("");
        getConfig().setToken("");

        // 设置设备的推送别名
        JPushInterface.setAlias(getApplicationContext(), "", null);
    }

    /**
     * 请求bean
     *
     * @param userId
     * @param latitude
     * @param longitude
     * @return
     */
    private RequestBean getRequestBean(String userId, String token, double latitude,
                                       double longitude) {
        ReportLocationRequest request = new ReportLocationRequest();
        RequestHead head = new RequestHead();
        ReportLocationReqBody body = request.new ReportLocationReqBody();

        head.setUserId(userId);
        head.setAccessToken(token);

        body.setLat(latitude);
        body.setLng(longitude);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 保存时间信息到文件
     *
     * @return
     */
    private void saveInfo2File() {

        try {

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String time = formatter.format(new Date());
            String fileName = "time.log";

            String sdPath = Utils.getSdPath();
            if (null == sdPath) {
                Log.i(TAG, "the device has no sd card");
                return;
            }
            String path = sdPath + "/chorstar";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path + "/" + fileName, true);
            fos.write((time + "\n").toString().getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file to the file");
            e.printStackTrace();
        }
    }

}