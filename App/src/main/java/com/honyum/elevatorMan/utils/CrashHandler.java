package com.honyum.elevatorMan.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.honyum.elevatorMan.base.SysActivityManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chang on 2015/6/23.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private static CrashHandler INSTANCE;

    private Context mContext;

    //存储设备信息和异常信息
    private Map<String, String> infoMap;

    //格式化日期，文件名使用
    private DateFormat formatter;

    /**
     * 单例模式
     */
    private CrashHandler() {
        infoMap = new HashMap<String, String>();
        formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    }

    /**
     * 获取实例
     * @return
     */
    public static CrashHandler getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context) {
        mContext = context;

        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        //设置定义的crashhandler为默认的处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        boolean result = handlerException(ex);
        if (!result) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 处理错误信息
     * @param ex
     * @return
     */
    private boolean handlerException(Throwable ex) {
        if (null == ex) {
            return false;
        }
        collectDeviceInfo(mContext);
        saveCrashInfo2File(ex);
        SysActivityManager.getInstance().exit();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        return true;
    }

    /**
     * 收集设备信息
     * @param context
     */
    private void collectDeviceInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                String versionName = packageInfo.versionName == null ? null : packageInfo.versionName;
                String versionCode = "" + packageInfo.versionCode;
                infoMap.put("version_name", versionName);
                infoMap.put("version_code", versionCode);
            }

        } catch (Exception e) {
            Log.e(TAG, "an error occured when collect package info");
            e.printStackTrace();
        }
    }

    /**
     * 保存错误信息到文件
     * @param ex
     * @return
     */
    private void saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);

        try {
            long curTime = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = time + ".log";

            String sdPath = Utils.getSdPath();
            if (null == sdPath) {
                Log.i(TAG, "the device has no sd card");
                return;
            }
            String path = sdPath + "/Elevator";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file to the file");
            e.printStackTrace();
        }
    }

}
