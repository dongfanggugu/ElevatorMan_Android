package com.honyum.elevatorMan.activity.worker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.data.FixTaskInfo;
import com.honyum.elevatorMan.net.FixWorkFinishRequest;
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

import butterknife.BindView;
import butterknife.OnClick;

import static com.honyum.elevatorMan.net.base.NetConstant.RSP_CODE_SUC_0;

/**
 * Created by Star on 2017/6/12.
 */

public class FixTaskFinishActivity extends BaseActivityWraper {

    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.iv_image1)
    ImageView ivImage1;
    @BindView(R.id.iv_image2)
    ImageView ivImage2;
    @BindView(R.id.iv_image3)
    ImageView ivImage3;
    @BindView(R.id.iv_image4)
    ImageView ivImage4;
    @BindView(R.id.tv_fix_complete)
    TextView tvFixComplete;
    @BindView(R.id.tv_fix_complete1)
    TextView tvFixComplete1;
    @BindView(R.id.iv_overview)
    ImageView ivOverview;
    @BindView(R.id.ll_full_screen)
    LinearLayout llFullScreen;
    private FixTaskInfo mFixTaskInfo;
    private String currId;
    private String currImage;
    private FixInfo mFixInfo;
    private String bi = "";
    private String ai = "";
    private String ci = "";
    private String di = "";
    private String mPublicPath = "";
    //临时文件，处理照片的压缩
    private String mTempFile = "";


    @Override
    public String getTitleString() {
        return getString(R.string.fix_look_finish);
    }

    @Override
    protected void initView() {
        initPublicPath();

        mFixInfo = getIntent("Fixdata");

       // Log.e("TAG", "initView: "+mFixTaskInfo.getState()+"!!!"+mFixInfo.getState() );
        if(mFixInfo.getState().equals("7"))
        {
            tvFixComplete1.setVisibility(View.VISIBLE);
        }
        else
        {
            tvFixComplete1.setVisibility(View.GONE);
        }
        ivImage1.setTag(R.id.index, 1);
        ivImage2.setTag(R.id.index, 2);
        ivImage3.setTag(R.id.index, 3);
        ivImage4.setTag(R.id.index, 4);

        ivImage1.setOnClickListener(imageViewClickListener);
        ivImage2.setOnClickListener(imageViewClickListener);

        ivImage3.setOnClickListener(imageViewClickListener);
        ivImage4.setOnClickListener(imageViewClickListener);


        Button button1 = (Button) findViewById(R.id.btn_del_1);
        Button button2 = (Button) findViewById(R.id.btn_del_2);
        button1.setTag(R.id.index,1);
        button2.setTag(R.id.index,2);
        Button button3 = (Button) findViewById(R.id.btn_del_3);
        Button button4 = (Button) findViewById(R.id.btn_del_4);
        button3.setTag(R.id.index,3);
        button4.setTag(R.id.index,4);
        loadPicture(mPublicPath + "/1/", ivImage1, button1);
        loadPicture(mPublicPath + "/2/", ivImage2, button2);
        loadPicture(mPublicPath + "/3/", ivImage3, button3);
        loadPicture(mPublicPath + "/4/", ivImage4, button4);
//        if (mFixInfo.getState().equals(FIX_PAYMENT_END)) {
//            tvFixComplete1.setVisibility(View.VISIBLE);
//            tvFixComplete.setVisibility(View.GONE);
//        } else {
//            tvFixComplete.setVisibility(View.VISIBLE);
//            tvFixComplete1.setVisibility(View.GONE);
//        }
//        String
//
//        new GetPicture(mMaintenanceTaskInfo.getBeforeImg(),ivImage1).execute();
//        new GetPicture(mMaintenanceTaskInfo.getAfterImg(),ivImage2).execute();
//        new GetPicture(mMaintenanceTaskInfo.getBeforeImg(),ivImage3).execute();
//        new GetPicture(mMaintenanceTaskInfo.getAfterImg(),ivImage4).execute();


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
        if ((int)imageView.getTag(R.id.index) == 1) {
            ai = getImageData(filePath);
        } else if ((int)imageView.getTag(R.id.index) == 2) {
            bi = getImageData(filePath);
        }
        else if ((int)imageView.getTag(R.id.index) == 3) {
            ci = getImageData(filePath);
        } else if ((int)imageView.getTag(R.id.index) == 4) {
            di = getImageData(filePath);
        }
        delButton.setVisibility(View.VISIBLE);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filePath);
                file.delete();

                delButton.setVisibility(View.GONE);
                if( (int)delButton.getTag(R.id.index) == 1)
                {
                    ai = "";
                    saveImageData(filePath,"");
                }
                else if((int)delButton.getTag(R.id.index)  == 2)
                {
                    bi = "";
                    saveImageData(filePath,"");
                }
                else if((int)delButton.getTag(R.id.index)  == 3)
                {
                    ci = "";
                    saveImageData(filePath,"");
                }
                else if((int)delButton.getTag(R.id.index)  == 4)
                {
                    di = "";
                    saveImageData(filePath,"");
                }
                imageView.setImageResource(R.drawable.defaut_image);
                imageView.setOnClickListener(imageViewClickListener);
            }
        });
    }

    /**
     * 初始化照片存储路径
     *
     * @param
     */
    private void initPublicPath() {
        mFixTaskInfo = getIntent("Info");
        currId = mFixTaskInfo.getId();
        String sdPath = Utils.getSdPath();

        if (null == sdPath) {
            return;
        }

        //LiftInfo liftInfo = (LiftInfo) intent.getSerializableExtra("lift");
        mPublicPath = sdPath + "/chorstar/maintenancetask/" + currId + "/";
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
            imageView = ivImage1;
            delButton = (Button) findViewById(R.id.btn_del_1);
            delButton.setTag(requestCode);
            showImage(dirPath, imageView, delButton);
            String imgPath1 = (String) ivImage1.getTag(R.id.file_path);
            Log.e("TAG", "onActivityResult: " + imgPath1);
            requestUploadImage(Utils.imgToStrByBase64(imgPath1),imgPath1,requestCode);

        } else if (2 == requestCode) {
            imageView = ivImage2;
            delButton = (Button) findViewById(R.id.btn_del_2);
            delButton.setTag(requestCode);
            showImage(dirPath, imageView, delButton);
            String imgPath2 = (String) ivImage2.getTag(R.id.file_path);
            Log.e("TAG", "onActivityResult: " + imgPath2);
            requestUploadImage(Utils.imgToStrByBase64(imgPath2),imgPath2,requestCode);

        } else if (3 == requestCode) {
            imageView = ivImage3;
            delButton = (Button) findViewById(R.id.btn_del_3);
            delButton.setTag(requestCode);
            showImage(dirPath, imageView, delButton);
            String imgPath2 = (String) ivImage3.getTag(R.id.file_path);
            Log.e("TAG", "onActivityResult: " + imgPath2);
            requestUploadImage(Utils.imgToStrByBase64(imgPath2),imgPath2,requestCode);

        } else if (4 == requestCode) {
            imageView = ivImage4;
            delButton = (Button) findViewById(R.id.btn_del_4);
            delButton.setTag(requestCode);
            showImage(dirPath, imageView, delButton);
            String imgPath2 = (String) ivImage4.getTag(R.id.file_path);
            Log.e("TAG", "onActivityResult: " + imgPath2);
            requestUploadImage(Utils.imgToStrByBase64(imgPath2),imgPath2,requestCode);

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


                if( (int)delButton.getTag() == 1)
                {
                    ai = "";
                    saveImageData(filePath,"");
                }
                else if((int)delButton.getTag()  == 2)
                {
                    bi = "";
                    saveImageData(filePath,"");
                }
                else if((int)delButton.getTag()  == 3)
                {
                    ci = "";
                    saveImageData(filePath,"");
                }
                else if((int)delButton.getTag()  == 4)
                {
                    di = "";
                    saveImageData(filePath,"");
                }


                imageView.setTag(R.id.file_path, "");
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
    private void requestFixLookFinish(String state) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_FIX_FINISH,
                getRequestBean(getConfig().getUserId(), getConfig().getToken(), state)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null && response.getHead().getRspCode().equals(RSP_CODE_SUC_0)) {
                    showAppToast(getString(R.string.sucess));
                    finish();
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());
            }
            //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());
        };
        addTask(task);
    }

    private RequestBean getRequestBean(String userId, String token, String state) {

        FixWorkFinishRequest request = new FixWorkFinishRequest();
        request.setHead(new NewRequestHead().setaccessToken(token).setuserId(userId));
        request.setBody(request.new FixWorkFinishBody().setRepairOrderProcessId(currId).setState(state).setFinishResult(etRemark.getText().toString()).setPictures(currImage).setPictures(ai+","+bi+","+ci+","+di));
        return request;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_fix_result;
    }


    @OnClick({ R.id.tv_fix_complete1,R.id.ll_full_screen,R.id.tv_fix_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fix_complete:
                if (StringUtils.isEmpty(mPublicPath)) {
                    return;
                }
                if (ai.equals("") || bi.equals("") || ci.equals("") || di.equals("")) {
                    showAppToast("请拍摄四张张完整的图片！");
                    return;
                }
                requestFixLookFinish(NetConstant.FIX_LOOK_FINISHED);
                break;
            case R.id.tv_fix_complete1:
                if (StringUtils.isEmpty(mPublicPath)) {
                    return;
                }
                if (ai.equals("") || bi.equals("") || ci.equals("") || di.equals("")) {
                    showAppToast("请拍摄四张张完整的图片！");
                    return;
                }
                requestFixLookFinish(NetConstant.FIX_FIX_FINISH);
                break;
        }
    }

    private RequestBean getImageRequestBean(String userId, String token, String path) {
        UploadImageRequest request = new UploadImageRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new UploadImageBody().setImg(path));
        return request;
    }


    private void requestUploadImage(final String path,final String realPath ,final int request) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.UP_LOAD_IMG,
                getImageRequestBean(getConfig().getUserId(), getConfig().getToken(), path)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                UploadImageResponse response = UploadImageResponse.getUploadImageResponse(result);
                if (response.getHead() != null && response.getHead().getRspCode().equals("0")) {
                    String url = response.getBody().getUrl();
                    showAppToast(getString(R.string.sucess));
                    if(request == 1)
                    {
                        ai = url;
                        saveImageData(realPath,url);
                    }
                    else if(request == 2)
                    {
                        bi = url;
                        saveImageData(realPath,url);
                    }
                    else if(request == 3)
                    {
                        ci = url;
                        saveImageData(realPath,url);
                    }
                    else if(request == 4)
                    {
                        di = url;
                        saveImageData(realPath,url);
                    }
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);
    }

}
