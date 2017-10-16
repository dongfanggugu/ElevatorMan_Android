package com.honyum.elevatorMan.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.WelcomeActivity;

/**
 * Created by Star on 2017/9/8.
 */

public class DaemonService  extends Service{


    private MyBinder binder;
    private static final String TAG = "DaemonService";
    private MyServiceConnection conn;


    @Override
    public void onCreate() {
        super.onCreate();
        if(binder ==null){
            binder = new MyBinder();
        }
        conn = new MyServiceConnection();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DaemonService.this.bindService(new Intent(DaemonService.this, LocationService.class), conn, Context.BIND_IMPORTANT);

        Intent it = new Intent(this, WelcomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, it, 0);//当点击消息时就会向系统发送openintent意图
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentText("定位中").setSmallIcon(R.drawable.logo).setContentTitle("梯美正在持续定位中！").setContentIntent(contentIntent);
        startForeground(0,builder.build());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    class MyBinder extends RemoteConnection.Stub{

        @Override
        public String getProcessName() throws RemoteException {
            // TODO Auto-generated method stub
            return "DaemonService";
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DaemonService.this.unbindService(conn);
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "建立连接成功！");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Location stop!");
            //Toast.makeText(LocalService.this, "断开连接", 0).show();
            //启动被干掉的
            DaemonService.this.startService(new Intent(DaemonService.this, LocationService.class));
            DaemonService.this.bindService(new Intent(DaemonService.this, LocationService.class), conn, Context.BIND_IMPORTANT);
        }

    }
}
