package com.honyum.elevatorMan.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.LoginActivity;
import com.honyum.elevatorMan.activity.WelcomeActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
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
import com.honyum.elevatorMan.receiver.ScreenBroadcastListener;
import com.honyum.elevatorMan.utils.ScreenManager;
import com.honyum.elevatorMan.utils.ToastUtils;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class LocationService extends Service {
    private MyServiceConnection conn;
    private String TAG = "LocationService";

    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;
    private LocationStartListener mLocationStartListener;
    private LocationClientOption option = new LocationClientOption();
    private long currDelay = 10;
    public static int count = 0;

    private MyBinder binder;

    private AlarmManager alarmManager;
    private boolean allowGps = true;

    private static final String LOCATION_START = "com.chorstar.location_start";

    private static final int CODE_GPS = 61;
    private static final int CODE_CACHE = 65;
    private static final int CODE_ONLINE = 161;

    public static long time = 0;


    private PendingIntent mPendingIntent;
    //位置管理
    private LocationManager lm;

    private PowerManager.WakeLock wakeLock;

    /**
     * 判断Network是否开启(包括移动网络和wifi)
     *
     * @return
     */
    public boolean isNetworkEnabled() {
        return (isWIFIEnabled() || isTelephonyEnabled());
    }

    /**
     * 判断移动网络是否开启
     *
     * @return
     */
    public boolean isTelephonyEnabled() {
        boolean enable = false;
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (telephonyManager.getNetworkType() != TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                enable = true;
                Log.i(Thread.currentThread().getName(), "isTelephonyEnabled");
            }
        }

        return enable;
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(settingsIntent);
    }

    /**
     * 判断wifi是否开启
     */
    public boolean isWIFIEnabled() {
        boolean enable = false;
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            enable = true;
            Log.i(Thread.currentThread().getName(), "isWIFIEnabled");
        }
        return enable;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
//        PackageManager p1m = this.getPackageManager();
//        PackageInfo pi;
//        try {
//            // 参数2必须是PackageManager.GET_PERMISSIONS
//            pi = p1m.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
//            String[] permissions = pi.requestedPermissions;
//            if(permissions != null){
//                for(String str : permissions){
//                    boolean permission = (PackageManager.PERMISSION_GRANTED ==
//                            p1m.checkPermission(str, getPackageName()));
//
//                    Log.i(TAG, str + permission);
//                }
//            }
//        }catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        startProSave();
        if (!isNetworkEnabled()) {
            ToastUtils.showToast(this, "网络未开启");
        }
        //hasLocationPermission(this);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            ToastUtils.showToast(this, "网络定位未开启");
            enableLocationSettings();
        }

//        if(hasLocationPermission(this))
//        {
//            ToastUtils.showToast(this,"未获取定位权限");
//
//        }

//        List<String> allprovides = lm.getProviders(true);
//        for (String allprovide : allprovides) {
//            Log.i("Test", allprovide);
//        }



        ///如果用户没有打开GPS或网络GPS则帮它强制打开

//        if (!BaseFragmentActivity.isOPen(getBaseContext())) {
//            BaseFragmentActivity.openGPS(getBaseContext());
//        }
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");

        //init baidu location sdk
        SDKInitializer.initialize(getApplicationContext());

        mLocationClient = new LocationClient(getApplicationContext());

        //register location start broadcast receiver
        mLocationStartListener = new LocationStartListener();
        IntentFilter filter = new IntentFilter();
        filter.addAction(LOCATION_START);
        this.registerReceiver(mLocationStartListener, filter);

        //register location complete broadcast receiver
        mBDLocationListener = new LocationListener();
        mLocationClient.registerLocationListener(mBDLocationListener);



        setLocationTimer(this);



        conn = new MyServiceConnection();
        if(binder ==null){
            binder = new MyBinder();
        }


    }


    private void initOption() {
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setIsNeedAddress(true);
        option.setTimeOut(120 * 1000);
        option.setCoorType("bd09ll");
        option.setScanSpan(getConfig().getCurrDelay() * 1000);
        //getMyApplication().getBaseActivity().showAppToast(getConfig().getCurrDelay()+"");
        option.setOpenGps(false);
        mLocationClient.setLocOption(option);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        Log.i(TAG, "onStartCommand: ");
        // 清空位置信息
        getConfig().setLatitude(0);
        getConfig().setLongitude(0);


        initOption();

        //设置显示地址

        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
            mLocationClient.requestLocation();
        }
        else
        {
            mLocationClient.requestLocation();
        }
        LocationService.this.bindService(new Intent(LocationService.this, DaemonService.class), conn, Context.BIND_IMPORTANT);
        Intent it = new Intent(this, WelcomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, it, 0);//当点击消息时就会向系统发送openintent意图
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentText("定位中").setSmallIcon(R.drawable.logo).setContentTitle("梯美正在持续定位中！");
        startForeground(0, builder.build());



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
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),currDelay*1000, mPendingIntent);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + currDelay * 1000, mPendingIntent);
        }else
        {
            alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + currDelay * 1000,mPendingIntent);
        }

       // alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + currDelay * 1000, mPendingIntent);


    }

    /**
     * timer broadcast listener
     */
    private class LocationStartListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            wakeLock.acquire();
            String action = intent.getAction();
            //saveInfo2File("开始定位");
            //启动定位
            if (action.equals(LOCATION_START)) {
                Log.i(TAG, "start location");
                if (mLocationClient != null && !mLocationClient.isStarted()) {
                    mLocationClient.start();
                    mLocationClient.requestLocation();
                } else if (mLocationClient != null && mLocationClient.isStarted()) {
                    mLocationClient.requestLocation();
                    //wakeLock.acquire();
                    //alterDelay();
                }
                if (currDelay != getConfig().getCurrDelay()) {
                    currDelay = getConfig().getCurrDelay();

                }
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + currDelay * 1000, mPendingIntent);
                }else
                {
                    alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + currDelay * 1000,mPendingIntent);
                }


            }
            wakeLock.release();
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

//        if (wakeLock != null) {
//            wakeLock.release();
//            wakeLock = null;
//        }
        stopForeground(true);
        endProSave();
        super.onDestroy();
        LocationService.this.unbindService(conn);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /*
     * 进行定位，当定位成功后更新地图的位置
     */
    private class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub

            try {
                //saveInfo2File();
                if (mLocationClient != null && mLocationClient.isStarted()) {
                    mLocationClient.stop();
                }


                //String locationType = location.getNetworkLocationType();

                //Log.i(TAG, "locationType:" + locationType);
                int returnCode = location.getLocType();
                Log.i(TAG, "getLocType:" + returnCode);
                if (returnCode != CODE_GPS
                        && returnCode != CODE_CACHE
                        && returnCode != CODE_ONLINE) {
                    Log.i(TAG, "location failed");
                    Intent intent = new Intent();
                    intent.setAction(Constant.ACTION_LOCATION_COMPLETE);
                    intent.putExtra("lat", 0.0);
                    intent.putExtra("long", 0.0);
                    intent.putExtra("add", returnCode+"");
                    sendBroadcast(intent);
                    //return;
                }

                //return;
//                if (locationType.equals("cl") || location.getRadius() > 80 || (locationType.equals("ll") && !allowGps))
//                    return;


                String userId = getConfig().getUserId();
                String role = getConfig().getRole();
                String token = getConfig().getToken();

                //用户id为空，返回不处理
                if (StringUtils.isEmpty(userId)) {
                    //ToastUtils.showToast(LocationService.this,"应用内部异常1，请重新登录");

                    Intent intent = new Intent();
                    intent.setAction(Constant.ACTION_LOCATION_COMPLETE);
                    intent.putExtra("lat", 0.0);
                    intent.putExtra("long", 0.0);
                    intent.putExtra("add", "应用内部信息异常，请重新登录");
                    sendBroadcast(intent);
                    return;
                }

                //如果token唯恐，返回不处理
                if (StringUtils.isEmpty(token)) {
                    //ToastUtils.showToast(LocationService.this,"应用内部异常2，请重新登录");
                    return;
                }

                //用户角色不为修理工，返回，不处理
                if (!role.equals(Constant.WORKER)) {
                    throw new Exception("角色不是维修工，不处理" + getConfig().getUserName());
                }

                final double latitude = location.getLatitude();
                final double longitude = location.getLongitude();

                //发送定位成功的标志
                Intent intent = new Intent();
                intent.setAction(Constant.ACTION_LOCATION_COMPLETE);
                intent.putExtra("lat", latitude);
                intent.putExtra("long", longitude);
                intent.putExtra("add", location.getAddrStr());
                sendBroadcast(intent);


                //saveInfo2File(location.getRadius()+location.getNetworkLocationType() + "");
                Log.e(TAG, "onReceiveLocation: 获取到的误差半径是:" + location.getRadius());

                //saveInfo2File(latitude + longitude + "");
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
//                if (location.getRadius()>80) {
//                    throw new Exception("偏差太大，舍弃");
//                    //return;
//                }



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

                        } else if (code.equals("-9") && !(response.getHead().getRspMsg().contains("登录超时"))) {        //token验证失败

                            //保存错误编码
                            clearAndLogout(code);


                        } else if ("-9".equals(code) && response.getHead().getRspMsg().contains("登录超时")) {

                            //尝试次数大于3 说明下面广播失效了。让用户手动去
                            if (count > 3) {
                                clearAndLogout(code);
                                count = 0;

                            }
                            //防止广播没有注册上，导致一直请求超时， 不能重新登录（小概率事件）,设置重试次数3次，防止多次上传重试，导致多次调用登录，使用time变量控制，5秒内只能发一次登录广播。

                            long currTime = System.currentTimeMillis();
                            if (currTime - time < 5000) {
                                time = currTime;
                                return;
                            }
                            //更新当前时间 作为下次时间比较的依据。
                            time = currTime;
                            count++;


                            Intent intent1 = new Intent();
//                            Bundle bundle = new Bundle();
//                            bundle.putParcelable("message", message);
                            intent1.setAction(Constant.BASE_HANDLER);
                            intent1.putExtra("num", -9);
                            intent1.putExtra("message", response.getHead().getRspMsg());
                            sendBroadcast(intent1);
//                            else {
//                                //保存错误编码
//                                getConfig().setErrorCode(code);
//
//                                //清除token,role和user信息
//                                clearUserInfo();
//
//                                //如果在前台，杀掉服务，同时到登陆页面
//                                if (!Utils.isApplicationBroughtToBackground(LocationService.this)) {
//
//                                    SysActivityManager.getInstance().exit();
//                                    Intent intent = new Intent(LocationService.this,
//                                            LoginActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                }
//
//                                stopSelf();
//                            }
                        }
//                        if(wakeLock.isHeld())
//                        wakeLock.release();


                    }

                };
                task.start();
            } catch (Exception e) {
                //saveInfo2File("Exception", e);
            }
        }

        private void clearAndLogout(String code) {
            if (!TextUtils.isEmpty(code)) {
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
    }
    ScreenBroadcastListener listener = null;
    private void startProSave()
    {
        final ScreenManager screenManager = ScreenManager.getInstance(this.getApplicationContext());
        ScreenBroadcastListener listener = new ScreenBroadcastListener(this);
        listener.registerListener(new ScreenBroadcastListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                screenManager.finishActivity();
            }

            @Override
            public void onScreenOff() {
                screenManager.startActivity();
            }
        });
    }

    private void endProSave()
    {
        if(listener!=null)
            listener.unRegisterListener();
    }

//    /**
//     * 用来获取locationService对象
//     *
//     * @author zhenhao
//     */
//    public class MyBinder extends Binder {
//
//        public IBinder  getService() {
//            //return LocationService.this;
//            return binder;
//        }
//    }

    class MyBinder extends RemoteConnection.Stub{

        @Override
        public String getProcessName() throws RemoteException {
            // TODO Auto-generated method stub
            return "LocationService";
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

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "建立连接成功！");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "LocationService断开");
            //Toast.makeText(LocationService.this, "断开连接", Toast.LENGTH_SHORT).show();
            //启动被干掉的
            LocationService.this.startService(new Intent(LocationService.this, DaemonService.class));
            LocationService.this.bindService(new Intent(LocationService.this, DaemonService.class), conn, Context.BIND_IMPORTANT);
        }

    }
}