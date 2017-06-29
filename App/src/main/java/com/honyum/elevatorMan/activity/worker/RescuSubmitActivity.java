package com.honyum.elevatorMan.activity.worker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.MainGroupActivity;
import com.honyum.elevatorMan.activity.common.MainPage1Activity;
import com.honyum.elevatorMan.activity.common.MainpageActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.AudioUrlResponse;
import com.honyum.elevatorMan.net.ReportStateRequest;
import com.honyum.elevatorMan.net.ReportStateRequest.ReportStateReqBody;
import com.honyum.elevatorMan.net.UploadPicRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RescuSubmitActivity extends BaseFragmentActivity {

    private ImageView taskPic;

    /**
     * 救援照片存储目录
     */
    private File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator + "elevatorMan/rescuePic/");

    /**
     * 照片文件
     */
    private File photoFile;

    private String alarmId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescu_submit);
        alarmId = getIntent().getStringExtra("alarm_id");
        initTitleBar();
        initView(alarmId);
    }

    /**
     * 初始化
     */
    private void initView(final String alarmId) {
        final EditText etSaved = (EditText) findViewById(R.id.et_saved);
        final EditText etInjured = (EditText) findViewById(R.id.et_injured);
        final EditText etOther = (EditText) findViewById(R.id.et_other);
        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        taskPic = (ImageView) findViewById(R.id.task_picture);

        photoFile = new File(sampleDir, alarmId + ".jpg");

        if (photoFile.exists()) {
            Bitmap decodeFile = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            taskPic.setImageBitmap(decodeFile);
        }

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (StringUtils.isEmpty(etSaved.getText().toString())) {
                    showToast("请填写被救人数");
                    return;
                }

                if (StringUtils.isEmpty(etInjured.getText().toString())) {
                    showToast("请填写伤亡人数");
                    return;
                }

                if (!photoFile.exists()) {
                    showToast("未拍摄救援照片");
                    return;
                }


                int savedCount = parseFromString(etSaved.getText().toString());
                int injuredCount = parseFromString(etInjured.getText().toString());
                String other = etOther.getText().toString();
                uploadRescuePic(alarmId, savedCount, injuredCount, other);
            }

        });

        taskPic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 101);
            }
        });
    }


    /**
     * 上传图片
     *
     * @param alarmId
     * @param savedCount
     * @param injuredCount
     * @param other
     */
    private void uploadRescuePic(final String alarmId, final int savedCount,
                                 final int injuredCount, final String other) {
        try {
            FileInputStream inputStream = new FileInputStream(photoFile);
            byte[] buffer = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(buffer)) != -1) {
                String s = Base64.encodeToString(buffer, 0, len, Base64.DEFAULT);
                sb.append(s);
            }
            inputStream.close();

            String server = getConfig().getServer() + NetConstant.UPLOAD_CERT;

            UploadPicRequest request = new UploadPicRequest();
            RequestHead head = new RequestHead();
            UploadPicRequest.RequestBody body = request.new RequestBody();

            head.setAccessToken(getConfig().getToken());
            head.setUserId(getConfig().getUserId());

            body.setPic(sb.toString());

            request.setHead(head);
            request.setBody(body);

            NetTask netTask = new NetTask(server, request) {
                @Override
                protected void onResponse(NetTask task, String result) {
                    AudioUrlResponse response = AudioUrlResponse.getAudioUrl(result);
                    String picUrl = response.getBody().getPic();
                    submit(alarmId, savedCount, injuredCount, other, picUrl);
                }
            };

            addBackGroundTask(netTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交救援报表
     *
     * @param savedCount
     * @param injuredCount
     */
    private void submit(String alarmId, int savedCount, int injuredCount, String other, String picUrl) {
        //TODO  提交
        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_REPORT_STATE,
                getReportStateRequest(alarmId, savedCount, injuredCount, other, picUrl)) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub
                Toast.makeText(RescuSubmitActivity.this, getString(R.string.submit_suc), Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent(RescuSubmitActivity.this, MainGroupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            protected void onFailed(NetTask task, String errorCode, String errorMsg) {
                //super.onFailed(task, errorCode, errorMsg);
                showToast(errorMsg);

                Intent intent = new Intent(RescuSubmitActivity.this, MainGroupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        };

        addTask(netTask);
    }


    /**
     * @return 以当前时间作为文件名
     */
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }


    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar(getString(R.string.title_rescu_complete), R.id.title_rescu_complete,
                R.drawable.back_normal, backClickListener);
    }

    /**
     * 点击标题栏后退按钮事件
     */
    private OnClickListener backClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            onBackPressed();
        }

    };


    /**
     * 上报完成状态的请求
     *
     * @param alarmId
     * @param savedCount
     * @param injureCount
     * @param result
     * @return
     */
    private RequestBean getReportStateRequest(String alarmId, int savedCount, int injureCount,
                                              String result, String picUrl) {
        ReportStateRequest request = new ReportStateRequest();
        ReportStateReqBody body = request.new ReportStateReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setState("3");
        body.setAlarmId(alarmId);
        body.setInjureCount(injureCount);
        body.setSavedCount(savedCount);
        body.setResult(result);
        body.setPic(picUrl);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 根据字符串返回int型
     *
     * @param string
     * @return
     */
    private int parseFromString(String string) {
        int result = 0;
        try {
            result = Integer.parseInt(string);
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 101 && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }

            photoFile = new File(sampleDir, alarmId + ".jpg");

            try {
                FileOutputStream outputStream = new FileOutputStream(photoFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            taskPic.setImageBitmap(bitmap);
        }
    }
}