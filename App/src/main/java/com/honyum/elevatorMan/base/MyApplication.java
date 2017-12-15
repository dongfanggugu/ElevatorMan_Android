package com.honyum.elevatorMan.base;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.honyum.elevatorMan.utils.CrashHandler;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.litepal.LitePalApplication;

import cn.jpush.android.api.JPushInterface;

import static com.honyum.elevatorMan.constant.Constant.crashHandlerEnable;

public class MyApplication extends LitePalApplication {

    private Config config;

    private BaseFragmentActivity baseActivity;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        super.onCreate();
        // LitePal.initialize(this);
        //CrashHandler.getInstance().init(this);
        ZXingLibrary.initDisplayOpinion(this);
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        config = new Config(this);
        MultiDex.install(this);
        config.setVideoEnable(false);
        config.setKnowledge(true);
        // LeakCanary.install(this);
        //处理AsyncTask onPostExecute不执行的问题
        try {
            Class.forName("android.os.AsyncTask");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //开启crashHandler


        if (crashHandlerEnable) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }
        //AlarmSqliteUtils.initSQL(this);

        //load the chorstar library
        System.loadLibrary("chorstar");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {

        super.onLowMemory();
        Glide.get(this).clearMemory();
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