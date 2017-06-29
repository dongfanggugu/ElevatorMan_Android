package com.honyum.elevatorMan.activity.maintenance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.MaintenanceServiceFinishRequest;
import com.honyum.elevatorMan.net.UploadImageRequest;
import com.honyum.elevatorMan.net.UploadImageResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Star on 2017/6/10.
 */


public class MaintenanceTaskFinishActivity extends BaseFragmentActivity {

    private ImageView mImageView1;
    private ImageView mImageView2;
    private String currId = "";
    private TextView tv_fix_complete;
    private String bi = "";
    private String ai = "";
    private String mPublicPath = "";
    //临时文件，处理照片的压缩
    private String mTempFile = "";

    private EditText et_remark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maint_result);
        initPublicPath();
        initTitle();
        initView();

    }

    /**
     * 初始化照片存储路径
     *
     * @param
     */
    private void initPublicPath() {
        Intent it = getIntent();
        currId = it.getStringExtra("Id");
        String sdPath = Utils.getSdPath();

        if (null == sdPath) {
            return;
        }

        //LiftInfo liftInfo = (LiftInfo) intent.getSerializableExtra("lift");
        mPublicPath = sdPath + "/chorstar/maintenancetask/" + currId + "/";
    }
    /**
     * 初始化标题
     */
    private void initTitle() {

        initTitleBar("维保结果", R.id.title_service_result,
                R.drawable.back_normal, backClickListener);
    }

    /**
     * 调用系统相机之后返回
     *
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
            delButton.setTag(requestCode);
            showImage(dirPath, imageView, delButton);
            String imgPath1 = (String) mImageView1.getTag(R.id.file_path);
            Log.e("TAG", "onActivityResult: " + imgPath1);
            requestUploadImage(Utils.imgToStrByBase64(imgPath1), imgPath1,requestCode);

        } else if (2 == requestCode) {
            imageView = mImageView2;
            delButton = (Button) findViewById(R.id.btn_del_2);
            delButton.setTag(requestCode);
            showImage(dirPath, imageView, delButton);
            String imgPath2 = (String) mImageView2.getTag(R.id.file_path);
            Log.e("TAG", "onActivityResult: " + imgPath2);
            requestUploadImage(Utils.imgToStrByBase64(imgPath2),imgPath2, requestCode);

        }


    }
    private void requestMaintOrderProcessWorkerFinish() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_MAINT_TASK_FINISH,
                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null&&response.getHead().getRspCode().equals("0")) {
                    showAppToast(getString(R.string.sucess));
                    finish();
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());
            }
            //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());;
        };
        addTask(task);
    }

    private RequestBean getRequestBean(String userId, String token) {

        MaintenanceServiceFinishRequest request = new MaintenanceServiceFinishRequest();
        request.setHead(new NewRequestHead().setaccessToken(token).setuserId(userId));
        request.setBody(request.new MaintenanceServiceFinishBody().setMaintOrderProcessId(currId).setMaintUserFeedback(et_remark.getText().toString().trim()).setAfterImg(ai).setBeforeImg(bi));
        return request;
    }

    /**
     * 显示拍摄的照片并
     *
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


        Bitmap showImage = Utils.getBitmapBySize(filePath, 120, 160);
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

                if ((int)delButton.getTag(R.id.index) == 1) {
                    ai = "";
                    saveImageData(imageView.getTag(R.id.file_path).toString(), "");
                } else if ((int)delButton.getTag(R.id.index) == 2) {
                    bi = "";
                    saveImageData(imageView.getTag(R.id.file_path).toString(), "");
                }
                imageView.setTag(R.id.file_path, "");
                imageView.setImageResource(R.drawable.defaut_image);
                imageView.setOnClickListener(imageViewClickListener);
            }
        });
    }

    /**
     * 将图片转换成BASE64
     *
     * @param imgPath
     * @return
     */
    public static String imgToStrByBase64(String imgPath) {
        if (StringUtils.isEmpty(imgPath)) {
            return "";
        }
        File file = new File(imgPath);
        if (!file.exists()) {
            return "";
        }

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);

        return Utils.imgToStrByBase64(bitmap);
    }
    /**
     * 初始化view
     */
    private void initView() {

        mImageView1 = (ImageView) findViewById(R.id.iv_image1);
        mImageView2 = (ImageView) findViewById(R.id.iv_image2);
        mImageView1.setOnClickListener(imageViewClickListener);
        mImageView2.setOnClickListener(imageViewClickListener);
        mImageView1.setTag(R.id.index, 1);
        mImageView2.setTag(R.id.index, 2);

        et_remark = (EditText) findViewById(R.id.et_remark);

        Button button1 = (Button) findViewById(R.id.btn_del_1);
        Button button2 = (Button) findViewById(R.id.btn_del_2);
        button1.setTag(R.id.index,1);
        button2.setTag(R.id.index,2);
        loadPicture(mPublicPath + "/1/", mImageView1, button1);
        loadPicture(mPublicPath + "/2/", mImageView2, button2);
        if (StringUtils.isEmpty(mPublicPath)) {
            return;
        }
        tv_fix_complete = (TextView) findViewById(R.id.tv_fix_complete);

        tv_fix_complete.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View v) {
                if (ai.equals("") || bi.equals("")) {
                    showAppToast("请拍摄两张完整的图片！");
                    return;
                }
                requestMaintOrderProcessWorkerFinish();
            }
        });
    }

    /**
     * 加载之前拍摄的照片
     *
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
        if ((int)imageView.getTag(R.id.index) == 1) {
            ai = getImageData(filePath);
        } else if ((int)imageView.getTag(R.id.index) == 2) {
            bi = getImageData(filePath);
        }
        imageView.setOnClickListener(overViewClickListener);

        delButton.setVisibility(View.VISIBLE);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filePath);
                file.delete();

                delButton.setVisibility(View.GONE);

                if ((int)delButton.getTag(R.id.index) == 1) {
                    ai = "";
                    saveImageData(imageView.getTag(R.id.file_path).toString(), "");
                } else if ((int)delButton.getTag(R.id.index) == 2) {
                    bi = "";
                    saveImageData(imageView.getTag(R.id.file_path).toString(), "");
                }
                imageView.setImageResource(R.drawable.defaut_image);
                imageView.setOnClickListener(imageViewClickListener);
            }
        });
    }

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
     * 初始化临时文件
     */
    private void initTempFile() {

        String sdPath = Utils.getSdPath();

        if (null == sdPath) {
            return;
        }

        String tempPath = sdPath + "/chorstar/maintenancetask/temp/";
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


    private RequestBean getImageRequestBean(String userId, String token, String path) {
        UploadImageRequest request = new UploadImageRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new UploadImageBody().setImg(path));
        return request;
    }


    private void requestUploadImage(final String path, final String realPath, final int request) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.UP_LOAD_IMG,
                getImageRequestBean(getConfig().getUserId(), getConfig().getToken(), path)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                UploadImageResponse response = UploadImageResponse.getUploadImageResponse(result);
                if (response.getHead() != null && response.getHead().getRspCode().equals("0")) {
                    String url = response.getBody().getUrl();
                    showAppToast(getString(R.string.sucess));
                    if (request == 1) {
                        ai = url;
                        saveImageData(realPath, url);
                    } else if (request == 2) {
                        saveImageData(realPath, url);
                        bi = url;
                    }
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);
    }
}
