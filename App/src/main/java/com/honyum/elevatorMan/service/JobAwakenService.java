package com.honyum.elevatorMan.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.bumptech.glide.util.Util;
import com.honyum.elevatorMan.utils.Utils;

import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobAwakenService extends JobService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("JobAwakenService","onStartJob");
        //Utils.saveInfo2File("JobAwakenService","onStartJob");
        // boolean isGuardAlive = isServiceWork(this,GuardService.class.getName());
        boolean isMessageAlive = isServiceWork(this,LocationService.class.getName());

        if(!isMessageAlive){
            Log.e("JobAwakenService","onStartService");
      //      Utils.saveInfo2File("JobAwakenService","onStartService");
            // startService(new Intent(this,GuardService.class));
            startService(new Intent(this,LocationService.class));
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
