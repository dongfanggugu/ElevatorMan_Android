package com.honyum.elevatorMan.activity.maintenance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.worker.WorkerBaseActivity;
import com.honyum.elevatorMan.data.LiftInfo;
import com.honyum.elevatorMan.data.RemindDate;
import com.honyum.elevatorMan.net.ReportMainRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.RemindUtils;
import com.honyum.elevatorMan.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class MaintenanceActivity extends WorkerBaseActivity {

    //电梯的id
    private String mLiftId = "";

    private ImageView mImageView1;

    private ImageView mImageView2;

    private ImageView mImageView3;

    private ImageView imageView;


    private String mPublicPath = "";

    //临时文件，处理照片的压缩
    private String mTempFile = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //和制定计划使用同一个xml文件
        setContentView(R.layout.activity_plan);
        initTitleBar();
        initPublicPath(getIntent());

        initView(getIntent());
    }

    /**
     * 初始化照片存储路径
     * @param intent
     */
    private void initPublicPath(Intent intent) {
        String sdPath = Utils.getSdPath();

        if (null == sdPath) {
            return;
        }

        LiftInfo liftInfo = (LiftInfo) intent.getSerializableExtra("lift");
        mPublicPath = sdPath + "/chorstar/maintenance/" + liftInfo.getNum() + "/";
    }


    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        setExitFlag(false);
        initTitleBar(getString(R.string.maintenance), R.id.title_plan, R.drawable.back_normal,
                backClickListener);
    }

    /**
     * 初始化界面
     * @param intent
     */
    private void initView(final Intent intent) {
        if (null == intent) {
            return;
        }

        final LiftInfo liftInfo = (LiftInfo) intent.getSerializableExtra("lift");

        ((TextView) findViewById(R.id.tv_lift_code)).setText(liftInfo.getNum());
        ((TextView) findViewById(R.id.tv_lift_add)).setText(liftInfo.getAddress());


        imageView = (ImageView)findViewById(R.id.iv_sign);

        //维保日期处理
        final TextView tvPlanDate = (TextView) findViewById(R.id.tv_plan_date);
        final TextView tvPlanType = (TextView) findViewById(R.id.tv_plan_type);

        tvPlanDate.setText(Utils.dateToString(new Date()));
        tvPlanType.setText(liftInfo.getPlanType());

        //日期设置后回调接口
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                tvPlanDate.setText(Utils.dateToString(date));
            }
        };

        //点击日期时修改
        findViewById(R.id.ll_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(Utils.stringToDate(tvPlanDate.getText().toString()));
                DatePickerDialog datePickerDialog = new DatePickerDialog(MaintenanceActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE)) {
                    @Override
                    protected void onStop() {
                    }
                };
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setSpinnersShown(false);

                datePicker.setCalendarViewShown(true);
                datePickerDialog.show();
            }
        });

        //维保类型维保内容帮助
        findViewById(R.id.tv_type_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = LiftInfo.stringToType(tvPlanType.getText().toString());
                Intent intent = new Intent(MaintenanceActivity.this, MainHelpActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });


        //显示拍照
        findViewById(R.id.ll_camera).setVisibility(View.VISIBLE);
        findViewById(R.id.view_temp).setVisibility(View.VISIBLE);

        mImageView1 = (ImageView) findViewById(R.id.iv_image1);
        mImageView2 = (ImageView) findViewById(R.id.iv_image2);
        mImageView3 = (ImageView) findViewById(R.id.iv_image3);

        mImageView1.setTag(R.id.index, 1);
        mImageView2.setTag(R.id.index, 2);
        mImageView3.setTag(R.id.index, 3);

        mImageView1.setOnClickListener(imageViewClickListener);
        mImageView2.setOnClickListener(imageViewClickListener);
        mImageView3.setOnClickListener(imageViewClickListener);

        if (StringUtils.isEmpty(mPublicPath)) {
            return;
        }

        //加载之前拍摄的照片，如果有的话
        loadPicture(mPublicPath + "/1/", mImageView1, (Button) findViewById(R.id.btn_del_1));
        loadPicture(mPublicPath + "/2/", mImageView2, (Button) findViewById(R.id.btn_del_2));
        loadPicture(mPublicPath + "/3/", mImageView3, (Button) findViewById(R.id.btn_del_3));


        //确定按钮
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getConfig().getSign();

                if (StringUtils.isEmpty(url)) {
                    showToast("您还没有设置您的手写签名,\n请到个人中心->设置->我的签名中设置");
                    return;
                }

                String planDate = tvPlanDate.getText().toString();

                String imgPath1 = (String) mImageView1.getTag(R.id.file_path);
                String imgPath2 = (String) mImageView2.getTag(R.id.file_path);
                String imgPath3 = (String) mImageView3.getTag(R.id.file_path);

                if (StringUtils.isEmpty(imgPath1) || StringUtils.isEmpty(imgPath2)
                        || StringUtils.isEmpty(imgPath3)) {
                    showToast("请完整拍摄三张照片");
                    return;
                }

                List<String> dirPathList = new ArrayList<String>();

                //图片路径
                dirPathList.add(imgPath1);
                dirPathList.add(imgPath2);
                dirPathList.add(imgPath3);


                String fileName1 = getFileName(imgPath1);
                String fileName2 = getFileName(imgPath2);
                String fileName3 = getFileName(imgPath3);

                List<String> fileNameList = new ArrayList<String>();
                fileNameList.add(fileName1);
                fileNameList.add(fileName2);
                fileNameList.add(fileName3);

                String img1 = Utils.imgToStrByBase64(imgPath1);
                String img2 = Utils.imgToStrByBase64(imgPath2);
                String img3 = Utils.imgToStrByBase64(imgPath3);

                List<String> imgStrList = new ArrayList<String>();
                imgStrList.add(img1);
                imgStrList.add(img2);
                imgStrList.add(img3);

                reportMaintenance(getConfig().getUserId(), getConfig().getToken(), liftInfo.getId(),
                        planDate, fileNameList, imgStrList, dirPathList);

            }
        });

    }

    /**
     * 加载之前拍摄的照片
     * @param dirPath
     * @param imageView
     * @param delButton
     */
    private void loadPicture(String dirPath, final ImageView imageView, final Button delButton) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return;
        }
        File[] files = file.listFiles();
        if (null == files || 0 == files.length) {
            return;
        }

        final String filePath = files[0].getAbsolutePath();

        Bitmap bitmap = Utils.getBitmapBySize(filePath, 60, 80);
        imageView.setImageBitmap(bitmap);
        imageView.setTag(R.id.file_path, filePath);
        imageView.setOnClickListener(overViewClickListener);

        delButton.setVisibility(View.VISIBLE);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filePath);
                file.delete();

                delButton.setVisibility(View.GONE);

                imageView.setImageResource(R.drawable.icon_img_original);
                imageView.setOnClickListener(imageViewClickListener);
            }
        });
    }

    /**
     * 获取上报维保的bean
     * @param userId
     * @param token
     * @param liftId
     * @param mainTime
     * @param fileNameList
     * @param imgStrList
     * @return
     */
    private RequestBean getRequestBean(String userId, String token, String liftId, String mainTime,
                                       List<String> fileNameList, List<String> imgStrList) {
        ReportMainRequest request = new ReportMainRequest();
        ReportMainRequest.ReportMainBody body = request.new ReportMainBody();
        RequestHead head = new RequestHead();

        body.setId(liftId);
        body.setMainTime(mainTime);
        body.setPhotoFileName(fileNameList);
        body.setPhotoBase64(imgStrList);

        head.setUserId(userId);
        head.setAccessToken(token);

        request.setBody(body);
        request.setHead(head);

        return request;
    }

    /**
     * 上报维保完成结果
     * @param userId
     * @param token
     * @param liftId
     * @param mainTime
     * @param fileNameList
     * @param imgStrList
     */
    private void reportMaintenance(String userId, String token, String liftId, String mainTime,
                                   List<String> fileNameList, List<String> imgStrList,
                                   final List<String> dirPathList) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_REPORT_MAIN,
                getRequestBean(userId, token, liftId, mainTime, fileNameList, imgStrList)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                deleteFiles(dirPathList);
                showToast("此次维保结果提交完成，请及时到维保计划里面制定您的电梯维保计划!");

                String url = getConfig().getSign();

                imageView.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_submit).setVisibility(View.GONE);
                new GetPicture(url, imageView).execute();

                //finish();
                //Intent intent = new Intent(MaintenanceActivity.this, MyLiftActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
            }
        };

        addTask(task);
    }

    /**
     * 异步获取图片
     *
     * @author chang
     */
    public class GetPicture extends AsyncTask<String, Void, String> {

        private String mUrl;
        private ImageView mImageView;

        public GetPicture(String url, ImageView imageView) {
            mUrl = url;
            mImageView = imageView;
            mImageView.setImageResource(R.drawable.icon_person);
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String filePath = "";
            try {
                filePath = Utils.getImage(mUrl);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (!StringUtils.isEmpty(result)) {
                //Bitmap bitmap = Utils.getBitmapBySize(result, 80, 80);

                Bitmap bitmap = Utils.getImageFromFile(new File(result));
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);
                } else {
                    mImageView.setImageResource(R.drawable.icon_person);
                }
            }
        }
    }

    /**
     * 调用系统相机之后返回
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }


        if (StringUtils.isEmpty(mPublicPath)) {
            showToast("请检查SD卡");
            return;
        }

        String dirPath = mPublicPath + requestCode + "/";


        ImageView imageView = null;
        Button delButton = null;
        if (1 == requestCode) {
            imageView = mImageView1;
            delButton = (Button) findViewById(R.id.btn_del_1);

        } else  if (2 == requestCode) {
            imageView = mImageView2;
            delButton = (Button) findViewById(R.id.btn_del_2);

        } else if (3 ==  requestCode) {
            imageView = mImageView3;
            delButton = (Button) findViewById(R.id.btn_del_3);
        }

        showImage(dirPath, imageView, delButton);
    }

    /**
     * 显示拍摄的照片并
     * @param dirPath
     * @param imageView
     * @param delButton
     */
    private void showImage(String dirPath, final ImageView imageView,
                           final Button delButton) {

        Bitmap bitmap = Utils.getBitmapBySize(mTempFile, 600, 800);

        //清理临时目录
        initTempFile();

        FileOutputStream outputStream = null;

        File dirFile = new File(dirPath);

        //如果目录存在，则删除，重新创建，保证同一个电梯在同一时间同一个照片位置只有一个照片目录
        if (dirFile.exists()) {
            Utils.deleteFiles(dirFile);
        } else {
            dirFile.mkdirs();
        }

        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";

        final String filePath = dirPath + fileName;

        try {
            outputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {

            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Bitmap showImage = Utils.getBitmapBySize(filePath, 60, 80);
        imageView.setImageBitmap(showImage);
        imageView.setTag(R.id.file_path, filePath);
        imageView.setOnClickListener(overViewClickListener);

        delButton.setVisibility(View.VISIBLE);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filePath);
                file.delete();

                delButton.setVisibility(View.GONE);


                imageView.setTag(R.id.file_path, "");
                imageView.setImageResource(R.drawable.icon_img_original);
                imageView.setOnClickListener(imageViewClickListener);
            }
        });
    }


    /**
     * 点击照片位置时进行拍照
     */
    private View.OnClickListener imageViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //初始化临时目录
            initTempFile();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File out = new File(mTempFile);
            Uri uri = Uri.fromFile(out);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, (Integer) v.getTag(R.id.index));
        }
    };

    /**
     * 拍照之后点击照片查看照片预览
     */
    private View.OnClickListener overViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof ImageView) {
                String filePath = (String) v.getTag(R.id.file_path);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                ((ImageView) findViewById(R.id.iv_overview)).setImageBitmap(bitmap);

                final LinearLayout llFullScreen = (LinearLayout) findViewById(R.id.ll_full_screen);
                llFullScreen.setVisibility(View.VISIBLE);
                llFullScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llFullScreen.setVisibility(View.GONE);
                    }
                });

            }
        }
    };


    /**
     * BASE64编码转换成图片
     * @param imgStr
     * @param imgFilePath
     */
    private void stringToImage(String imgStr, String imgFilePath) {
        if (null == imgStr) {
            return;
        }

        byte[] bytes = Base64.decode(imgStr, Base64.DEFAULT);

        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        File imgFile = new File(imgFilePath);
        if (imgFile.exists()) {
            imgFile.delete();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件的绝对路径截取文件名称
     * @param filePath
     * @return
     */
    private String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return "";
        }

        String[] paths = filePath.split("/");
        return paths[paths.length - 1];
    }

    /**
     * 删除图片
     * @param dirPathList
     */
    private void deleteFiles(List<String> dirPathList) {
        for (String dirPath : dirPathList) {
            deleteFileByPath(dirPath);
        }
    }

    /**
     * 删除指定路径的文件
     * @param filePath
     */
    private void deleteFileByPath(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 初始化临时文件
     */
    private void initTempFile() {

        String sdPath = Utils.getSdPath();

        if (null == sdPath) {
            return;
        }

        String tempPath = sdPath + "/chorstar/maintenance/temp/";
        mTempFile = tempPath + "original.jpg";
        File tempFile = new File(mTempFile);

        //文件存在，删除
        if (tempFile.exists()) {
            tempFile.delete();
            return;
        }

        //目录不存在，创建目录
        File pathFile = new File(tempPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
    }

}
