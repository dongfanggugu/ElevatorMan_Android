package com.honyum.elevatorMan.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.SendChatRequest;
import com.honyum.elevatorMan.net.UploadVideoRequest;
import com.honyum.elevatorMan.net.VideoUrlResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.view.MovieRecorderView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 视频拍摄页面
 * Created by Wood on 2016/4/6.
 */
public class RecordVideoActivity extends BaseFragmentActivity implements View.OnClickListener {
    private static final String LOG_TAG = "RecordVideoActivity";
    private static final int REQ_CODE = 110;
    private static final int RES_CODE = 111;
    /**
     * 录制进度
     */
    private static final int RECORD_PROGRESS = 100;
    /**
     * 录制结束
     */
    private static final int RECORD_FINISH = 101;

    private MovieRecorderView movieRecorderView;
    private Button buttonShoot;
    private RelativeLayout rlBottomRoot;
    private ProgressBar progressVideo;
    private TextView textViewCountDown;
    private TextView textViewUpToCancel;//上移取消
    private TextView textViewReleaseToCancel;//释放取消
    /**
     * 是否结束录制
     */
    private boolean isFinish = true;
    /**
     * 是否触摸在松开取消的状态
     */
    private boolean isTouchOnUpToCancel = false;
    /**
     * 当前进度
     */
    private int currentTime = 0;
    private String mAlarmId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECORD_PROGRESS:
                    progressVideo.setProgress(currentTime);
                    if (currentTime < 10) {
                        textViewCountDown.setText("00:0" + currentTime);
                    } else {
                        textViewCountDown.setText("00:" + currentTime);
                    }
                    break;
                case RECORD_FINISH:
                    if (isTouchOnUpToCancel) {//录制结束，还在上移删除状态没有松手，就复位录制
                        resetData();
                    } else {//录制结束，在正常位置，录制完成跳转页面
                        isFinish = true;
                        buttonShoot.setEnabled(false);
                        movieRecorderView.stop();
                        // finishActivity();
                        uploadVideo(movieRecorderView.getRecordFile().getAbsolutePath());
                    }
                    break;
            }
        }
    };
    /**
     * 按下的位置
     */
    private float startY;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        initView();
    }

    public void initView() {
        mAlarmId = getIntent().getStringExtra("AlarmId");
        // ((TextView) findViewById(R.id.title)).setText("录制视频");
        // findViewById(R.id.title_left).setOnClickListener(this);

        movieRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
        buttonShoot = (Button) findViewById(R.id.button_shoot);
        rlBottomRoot = (RelativeLayout) findViewById(R.id.rl_bottom_root);
        //progressVideo = (DonutProgress) findViewById(R.id.progress_video);
        progressVideo = (ProgressBar) findViewById(R.id.progressBar_loading);
        textViewCountDown = (TextView) findViewById(R.id.textView_count_down);
        textViewCountDown.setText("00:00");
        textViewUpToCancel = (TextView) findViewById(R.id.textView_up_to_cancel);
        textViewReleaseToCancel = (TextView) findViewById(R.id.textView_release_to_cancel);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) movieRecorderView.getLayoutParams();
        layoutParams.height = width * 4 / 3;//根据屏幕宽度设置预览控件的尺寸，为了解决预览拉伸问题
        //LogUtil.e(LOG_TAG, "mSurfaceViewWidth:" + width + "...mSurfaceViewHeight:" + layoutParams.height);
        movieRecorderView.setLayoutParams(layoutParams);

        FrameLayout.LayoutParams rlBottomRootLayoutParams = (FrameLayout.LayoutParams) rlBottomRoot.getLayoutParams();
        rlBottomRootLayoutParams.height = width / 3 * 2;
        rlBottomRoot.setLayoutParams(rlBottomRootLayoutParams);

        //处理触摸事件
        buttonShoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textViewUpToCancel.setVisibility(View.VISIBLE);//提示上移取消

                    isFinish = false;//开始录制
                    startY = event.getY();//记录按下的坐标
                    movieRecorderView.record(new MovieRecorderView.OnRecordFinishListener() {
                        @Override
                        public void onRecordFinish() {
                            handler.sendEmptyMessage(RECORD_FINISH);
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textViewUpToCancel.setVisibility(View.GONE);
                    textViewReleaseToCancel.setVisibility(View.GONE);

                    if (startY - event.getY() > 100) {//上移超过一定距离取消录制，删除文件
                        if (!isFinish) {
                            resetData();
                        }
                    } else {
                        if (movieRecorderView.getTimeCount() > 3) {//录制时间超过三秒，录制完成
                            handler.sendEmptyMessage(RECORD_FINISH);
                        } else {//时间不足取消录制，删除文件
                            Toast.makeText(RecordVideoActivity.this, "视频录制时间太短", Toast.LENGTH_SHORT).show();
                            resetData();
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //根据触摸上移状态切换提示
                    if (startY - event.getY() > 100) {
                        isTouchOnUpToCancel = true;//触摸在松开就取消的位置
                        if (textViewUpToCancel.getVisibility() == View.VISIBLE) {
                            textViewUpToCancel.setVisibility(View.GONE);
                            textViewReleaseToCancel.setVisibility(View.VISIBLE);
                        }
                    } else {
                        isTouchOnUpToCancel = false;//触摸在正常录制的位置
                        if (textViewUpToCancel.getVisibility() == View.GONE) {
                            textViewUpToCancel.setVisibility(View.VISIBLE);
                            textViewReleaseToCancel.setVisibility(View.GONE);
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    resetData();
                }
                return true;
            }
        });

        progressVideo.setMax(10);
        movieRecorderView.setOnRecordProgressListener(new MovieRecorderView.OnRecordProgressListener() {
            @Override
            public void onProgressChanged(int maxTime, int currentTime) {
                RecordVideoActivity.this.currentTime = currentTime;
                handler.sendEmptyMessage(RECORD_PROGRESS);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //  checkCameraPermission();
    }

    /**
     * 上传视频文件
     *
     * @param filePath 文件路径
     */
    private void uploadVideo(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        try {
            //audioDuration = audioRecorder.getAudioDuration(filePath);

            FileInputStream inputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(buffer)) != -1) {
                String decode = Base64.encodeToString(buffer, 0, len, Base64.DEFAULT);
                sb.append(decode);
            }
            inputStream.close();

            String server = getConfig().getServer() + NetConstant.UPLOAD_VIDEO;

            UploadVideoRequest request = new UploadVideoRequest();
            RequestHead head = new RequestHead();
            UploadVideoRequest.RequestBody body = request.new RequestBody();

            head.setAccessToken(getConfig().getToken());
            head.setUserId(getConfig().getUserId());

            body.setVideo(sb.toString());

            request.setHead(head);
            request.setBody(body);

            NetTask netTask = new NetTask(server, request) {
                @Override
                protected void onResponse(NetTask task, String result) {
                    VideoUrlResponse response = VideoUrlResponse.getVideoUrl(result);
                    String url = response.getBody().getUrl();

                    finishActivity(url);

                    //sendChat(CHAT_CONTENT_VOICE, audioUrl);
                }
            };

            addBackGroundTask(netTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 检测摄像头和录音权限
//     */
//    private void checkCameraPermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            // Camera permission has not been granted.
//            Toast.makeText(this, "视频录制和录音没有授权", Toast.LENGTH_LONG);
//            this.finish();
//        } else {
//            resetData();
//        }
//    }

    /**
     * 重置状态
     */
    private void resetData() {
        if (movieRecorderView.getRecordFile() != null)
            movieRecorderView.getRecordFile().delete();
        movieRecorderView.stop();
        isFinish = true;
        currentTime = 0;
        progressVideo.setProgress(0);
        textViewCountDown.setText("00:00");
        buttonShoot.setEnabled(true);
        textViewUpToCancel.setVisibility(View.GONE);
        textViewReleaseToCancel.setVisibility(View.GONE);
        try {
            movieRecorderView.initCamera();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送聊天信息
     *
     * @param chatType 聊天类型
     * @param content  聊天内容
     */
    private void sendChat(int chatType, String content) {


        String server = getConfig().getServer() + NetConstant.SEND_CHAT;

        SendChatRequest request = new SendChatRequest();
        RequestHead head = new RequestHead();
        SendChatRequest.RequestBody body = request.new RequestBody();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setAlarmId(mAlarmId);
        body.setType(chatType + "");
        body.setUserName(getConfig().getName());


        body.setContent(content);
        body.setTimeLength(movieRecorderView.getTimeCount());


        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {

            }
        };

        addBackGroundTask(netTask);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = true;
        movieRecorderView.stop();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                if (!deleteDir(new File(dir, children[i]))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Override
    public void onDestroy() {
        //TODO 退出界面删除文件，如果要删除文件夹，需要提供文件夹路径
        if (movieRecorderView.getRecordFile() != null) {
            File file = new File(movieRecorderView.getRecordFile().getAbsolutePath());
            if (file != null && file.exists()) {
                Log.e(LOG_TAG, "file.exists():" + file.exists());
                file.delete();
            }
        }
        super.onDestroy();
    }

    /**
     * TODO 录制完成需要做的事情
     */
    private void finishActivity(String url) {
        if (isFinish) {
            //movieRecorderView.stop();
//            Intent intent = new Intent(this, VideoPreviewActivity.class);
//            intent.putExtra("path", movieRecorderView.getRecordFile().getAbsolutePath());
//            startActivityForResult(intent, REQ_CODE);
            sendChat(4,url);
            Intent intent = new Intent();
            //  intent.putExtra("tag", tag);
            intent.putExtra("result", url);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.title_left:
//                finish();
//                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RES_CODE) {
            setResult(RES_CODE);
            finish();
        }
    }
}