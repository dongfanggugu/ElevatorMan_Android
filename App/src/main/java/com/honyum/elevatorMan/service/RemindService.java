package com.honyum.elevatorMan.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.base.MyApplication;
import com.honyum.elevatorMan.utils.RemindUtils;
import com.honyum.elevatorMan.utils.Utils;

import java.util.Date;

public class RemindService extends Service {
    public RemindService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取全局的application对象
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
}
