package com.honyum.elevatorMan.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.honyum.elevatorMan.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chang on 2015/6/23.
 */
public class DownloadFilesTask extends AsyncTask<String, Integer, Long> {

    private Activity activity;
    private URL url; // 资源位置
    private ProgressDialog pbar;
    private long beginPosition; // 开始位置
    private File file;

    public DownloadFilesTask(Activity activity, ProgressDialog pbar) {
        super();
        this.pbar = pbar;
        this.activity = activity;
    }

    @Override
    protected Long doInBackground(String... params) {
        int fileTotalSize;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        FileOutputStream output = null;
        // RandomAccessFile output = null;
        file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), activity.getString(R.string.file_name)
                + ".apk");
        try {
            output = new FileOutputStream(file);
            // 设置断点续传的开始位置
            url = new URL(params[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // httpURLConnection.setAllowUserInteraction(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(4000);
            fileTotalSize = httpURLConnection.getContentLength();
            inputStream = httpURLConnection.getInputStream();
            byte[] buf = new byte[512];
            int readsize = 0;
            // 进行循环输出
            while ((readsize = inputStream.read(buf)) != -1) {
                output.write(buf, 0, readsize);
                beginPosition += readsize;
                publishProgress((int) (beginPosition * 100.0f / fileTotalSize));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();

                    if (output != null) {
                        output.close();
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return beginPosition;
    }

    /**
     * 更新下载进度，当publishProgress方法被调用的时候就会自动来调用这个方法
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pbar.setProgress(values[0]);
    }

    // 下载完的回调
    @Override
    protected void onPostExecute(Long size) {
        pbar.cancel();
        ReplaceLaunchApk(file.getAbsolutePath());
    }

    private void ReplaceLaunchApk(String apkpath) {
        File file = new File(apkpath);
        String tag = "apk";
        if (file.exists()) {
            Log.e(tag, file.getName());
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            activity.startActivity(intent);
            activity.finish();
        } else {
            Log.e(tag, "File not exsit:" + apkpath);
        }
    }
}
