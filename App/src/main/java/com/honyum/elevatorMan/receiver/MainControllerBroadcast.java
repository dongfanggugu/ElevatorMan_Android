package com.honyum.elevatorMan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.service.LocationService;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Star on 2017/11/9.
 */

public class MainControllerBroadcast extends BroadcastReceiver {
    private WeakReference<Handler> mHandler;
    private static long time = 0;
    /**
     * 保存时间信息到文件
     *
     * @return
     */
    private void saveInfo2File(String red, Exception ex) {

        try {
            if (ex == null)
                return;
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            String result = writer.toString();
            printWriter.close();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String time = formatter.format(new Date());
            String fileName = "MainControllerBroadcast" + System.currentTimeMillis() +".log";
            String sdPath = Utils.getSdPath();
            if (null == sdPath) {
                //Log.i(TAG, "the device has no sd card");
                return;
            }
            String path = sdPath + "/Elevator";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(path + "/" + fileName);
            if (file != null) {
                if (file.length() > 1 * 1024 * 1024)
                    return;
            }
            FileOutputStream fos = new FileOutputStream(path + "/" + fileName, true);
            fos.write((time + "\n").toString().getBytes());
            fos.write((red + "\n").toString().getBytes());
            fos.write(("\n").toString().getBytes());
            fos.write((result + "\n").toString().getBytes());
            fos.close();
          //  Log.e(TAG, path);
        } catch (Exception e) {
          //  Log.e(TAG, "an error occured while writing file to the file");
            e.printStackTrace();
        }
    }
    public MainControllerBroadcast(Handler handler) {
        mHandler = new WeakReference<Handler>(handler);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (intent != null)
                if (Constant.BASE_HANDLER.equals(intent.getAction())) {
                    if (intent.hasExtra("num")&&intent.hasExtra("message")) {
                        //成功发送登录广播，则重新记录请求的发送次数。
                        long currTime = System.currentTimeMillis();
                        if (currTime - time < 5000) {
                            time = currTime;
                            return;
                        }
                        //更新当前时间 作为下次时间比较的依据。
                        time = currTime;
                        LocationService.count = 0;
                        int num = intent.getIntExtra("num",-9);
                        String data = intent.getStringExtra("message");
                        Message message = Message.obtain();
                        message.arg1 = num;
                        message.obj = data;
                        mHandler.get().sendMessage(message);
                    }
                }
        }
        catch (Exception e)
        {
            saveInfo2File("MainCon",e);
        }
    }
}
