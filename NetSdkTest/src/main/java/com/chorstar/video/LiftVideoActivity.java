package com.chorstar.video;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.manager.DeviceManager;
import com.example.netsdktest.MainActivity;
import com.example.netsdktest.R;
import com.hanbang.netsdk.BaseNetControl;
import com.hanbang.playsdk.PlaySDK;
import com.hanbang.playsdk.PlaySurfaceView;

public class LiftVideoActivity extends Activity {

    private String TAG = "LiftVideoActivity";

    private ProgressDialog mProgressDlg;

    private boolean isPlaying = false;

    private PlaySurfaceView mSurfaceView;

    private DeviceManager mDevice;

    private PlaySDK mPlaySDK;

    private GestureDetector mGesture;

    private static final int FLING_MIN_DISTANCE = 50;

    private static final int FLING_MIN_VELOCITY = 0;

    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lift_video);
        init();
        prepareVideo(getIntent());
    }

    /**
     * 初始化数据
     */
    private void init() {
        mDevice = DeviceManager.getInstance();
        mPlaySDK = new PlaySDK();
        mGesture = new GestureDetector(this, new GestureListener());
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mSurfaceView = (PlaySurfaceView) findViewById(R.id.surface_video);

       mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               //if (isPlaying)
               return mGesture.onTouchEvent(event);
           }
       });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 启动视频播放操作
     * @param intent
     */
    private void prepareVideo(Intent intent) {
        String user = intent.getStringExtra("user");
        String password = intent.getStringExtra("password");
        String server = intent.getStringExtra("ip");
        int port = intent.getIntExtra("port", 18101);
        loginVideoSever(user, password, server, port);
    }

    /**
     * 到视频服务器上注册用户
     * @param user
     * @param password
     * @param server
     * @param port
     */
    private void loginVideoSever(final String user, final String password, final String server, final int port) {
        //弹出进度框
        mProgressDlg = ProgressDialog.show(this, null, "正在连接...", true, false);

        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                //DeviceManager device = ((NetSdkTestApplication)getApplication()).device;

                final int nResult = mDevice.login(user, password, server, port);
                mProgressDlg.dismiss();

                mHandler.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        // TODO Auto-generated method stub
                        if (0 != nResult)
                        {
                            Toast.makeText(LiftVideoActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startPlay();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 启动视频播发
     */
    private void startPlay() {
        if (isPlaying) {
            stopPlay();
        } else {
            mProgressDlg = ProgressDialog.show(this, null, "开启视频...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mSurfaceView.setPlayer(mPlaySDK);
                    final int result = mDevice.startPreview(0, 0, videoCallback);
                    mProgressDlg.dismiss();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (0 == result) {
                                isPlaying = true;
                            } else {
                                Toast.makeText(LiftVideoActivity.this, "开启电梯视频失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    /**
     * 处理传递过来的视频流，进行播放
     */
    BaseNetControl.NetDataCallback videoCallback = new BaseNetControl.NetDataCallback() {
        @Override
        public void onNetData(DataType dataType, byte[] data, int offset, int length, long timeStamp) {

            if (!mPlaySDK.isOpened()) {
                if (mPlaySDK.openStream(data, offset, length)) {
                    mPlaySDK.play();
                }
            }

            mPlaySDK.inputData(data, offset, length);
        }

        @Override
        public void onDisconnected() {

        }
    };

    /**
     * 停止视频播放
     */
    private void stopPlay() {
        if (!isPlaying) {
            return;
        }

        isPlaying = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                mSurfaceView.setPlayer(null);

                mDevice.stopPreview(0);

                mPlaySDK.closeStream();
            }
        }).start();
    }

    /**
     * 处理视频的滑动事件
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                Log.i(TAG, "向左滑动");
            } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                Log.i(TAG, "向右滑动");
            } else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                    && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                Log.i(TAG, "向上滑动");
            } else if (e2.getY() - e1.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                Log.i(TAG, "向下滑动");
            }

            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isPlaying) {
                stopPlay();
            } else {
                startPlay();
            }
            return false;
        }
    }
}
