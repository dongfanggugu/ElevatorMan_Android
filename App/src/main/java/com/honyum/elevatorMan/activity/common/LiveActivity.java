package com.honyum.elevatorMan.activity.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.utils.CheckTopTask;
import com.honyum.elevatorMan.utils.ScreenManager;

public class LiveActivity extends Activity {

    public static Handler mHandler = new Handler(Looper.getMainLooper());
    private static CheckTopTask mCheckTopTask;
    public static final String TAG = LiveActivity.class.getSimpleName();

    public static void actionToLiveActivity(Context pContext) {
        mCheckTopTask = new CheckTopTask(pContext.getApplicationContext());
        CheckTopTask.startForeground(pContext);
        mHandler.postDelayed(mCheckTopTask, 4000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("LiveActivity","触摸销毁！");
        finishSelf();
        return super.onTouchEvent(event);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isScreenOn()) {
            finishSelf();
            Log.i("LiveActivity","亮屏销毁！");

        }
    }
    public void finishSelf() {
        if (!isFinishing()) {
            finish();
        }
    }
    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return powerManager.isInteractive();
        } else {
            return powerManager.isScreenOn();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_live);

        Window window = getWindow();
        //放在左上角
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams attributes = window.getAttributes();
        //宽高设计为1个像素
        attributes.width = 1;
        attributes.height = 1;
        //起始坐标
        attributes.x = 0;
        attributes.y = 0;
        window.setAttributes(attributes);

        ScreenManager.getInstance(this).setActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveActivity.mHandler.removeCallbacks(mCheckTopTask);
        Log.d(TAG, "onDestroy");
    }
}
