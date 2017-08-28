package com.honyum.elevatorMan.base;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    private Config config;

    private BaseFragmentActivity baseActivity;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        // LitePal.initialize(this);
        //CrashHandler.getInstance().init(this);
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        config = new Config(this);

        config.setVideoEnable(false);
        config.setKnowledge(true);

        //处理AsyncTask onPostExecute不执行的问题
        try {
            Class.forName("android.os.AsyncTask");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //AlarmSqliteUtils.initSQL(this);

        //load the chorstar library
        System.loadLibrary("chorstar");
    }

    /**
     * 获取配置文件对象
     *
     * @return
     */
    public Config getConfig() {
        return config;
    }

    public void setBaseActivity(BaseFragmentActivity activity) {
        baseActivity = activity;
    }

    public BaseFragmentActivity getBaseActivity() {
        return baseActivity;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }
}