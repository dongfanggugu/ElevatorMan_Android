package com.honyum.elevatorMan.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.UploadIconRequest;
import com.honyum.elevatorMan.net.UploadIconResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;

public class PersonIconPopActivity extends BaseFragmentActivity {

    private static final String IMAGE_ICON_NAME = "person_take.jpg";

    private static final int CAMERA_REQ_CODE = 1;

    private static final int PICKER_REQ_CODE = 2;

    private static final int CROP_REQ_CODE = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_icon_pop);
        initView();
    }


    /**
     * 初始化视图
     */
    private void initView() {


        //拍照
        findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dirPath = Utils.getTempPath();
                if (StringUtils.isEmpty(dirPath)) {
                    showToast("没有找到SD卡，请检查之后再试!");
                    return;
                }

                //目录不存在，创建目录
                File dirFile = new File(dirPath);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }

                //文件存在，删除文件
                String filePath = dirPath + "/" + IMAGE_ICON_NAME;
                File imgFile = new File(filePath);
                if (imgFile.exists()) {
                    imgFile.delete();
                }

                Uri uri = Uri.fromFile(imgFile);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAMERA_REQ_CODE);
            }
        });

        //选取图片
        findViewById(R.id.tv_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PICKER_REQ_CODE);
            }
        });

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //如果不进行拍照，取消结束此页面
        if (resultCode != Activity.RESULT_OK) {
            finish();
            return;
        }

        //拍照
        if (requestCode == CAMERA_REQ_CODE) {
            String filePath = Utils.getTempPath() + "/" + IMAGE_ICON_NAME;

            Bitmap bitmap = Utils.getBitmapBySize(filePath, 600, 800);

            int degree = Utils.readPictureDegree(filePath);

            Bitmap picture = Utils.rotatingPicture(degree, bitmap);

            //删除之前照片
            File file = new File(filePath);
            file.delete();

            //重新写入调整过分辨率的照片
            Utils.saveBitmap(picture, Utils.getTempPath(), IMAGE_ICON_NAME);

            cropImage(Uri.fromFile(file));
        }

        //裁剪完成，提交到服务器
        if (requestCode == CROP_REQ_CODE) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = extras.getParcelable("data");

            uploadIcon(bitmap);
        }

        //选取照片
        if (requestCode == PICKER_REQ_CODE && data != null) {

            Uri uri = data.getData();
            String sourcePath = Utils.getRealPathFromUri(this, uri);
            Bitmap bitmap = Utils.getBitmapBySize(sourcePath, 600, 800);
            String pickFileName = "person_pick.jpg";
            Utils.saveBitmap(bitmap, Utils.getTempPath(), pickFileName);
            cropImage(Uri.fromFile(new File(Utils.getTempPath(), pickFileName)));
        }
    }

    /**
     * 对图片进行裁剪
     *
     * @param uri
     */
    private void cropImage(Uri uri) {
        if (null == uri) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", true);

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 160);
        intent.putExtra("outputY", 160);

        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQ_CODE);
    }

    /**
     * 获取上传头像的bean
     *
     * @param userId
     * @param token
     * @param imgStr
     * @return
     */
    private RequestBean getUploadRequestBean(String userId, String token, String imgStr) {
        UploadIconRequest request = new UploadIconRequest();
        UploadIconRequest.UploadIconReqBody body = request.new UploadIconReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(userId);
        head.setAccessToken(token);

        body.setPic(imgStr);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 上传头像
     *
     * @param bitmap
     */
    private void uploadIcon(final Bitmap bitmap) {
        String server = getConfig().getServer() + NetConstant.URL_UPLOAD_ICON;
        RequestBean requestBean = getUploadRequestBean(getConfig().getUserId(), getConfig().getToken(),
                Utils.imgToStrByBase64(bitmap));

        NetTask netTask = new NetTask(server, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {
                UploadIconResponse response = UploadIconResponse.getUploadIconResponse(result);

                String url = response.getBody().getPic();

                getConfig().setIconUrl(url);

                String fileName = Utils.getFileNameByUrl(url);

                String filePath = Utils.getTempPath();

                Utils.saveBitmap(bitmap, filePath, fileName);

                //删除拍照后的原文件
                //文件存在，删除文件
                String sourcePath = fileName + "/" + IMAGE_ICON_NAME;
                File sourceFile = new File(sourcePath);
                if (sourceFile.exists()) {
                    sourceFile.delete();
                }

                finish();
            }

            @Override
            protected void onFailed(NetTask task, String errorCode, String errorMsg) {
                super.onFailed(task, errorCode, errorMsg);

                //删除拍照后的原文件
                //文件存在，删除文件
                String sourcePath = Utils.getTempPath() + "/" + IMAGE_ICON_NAME;
                File sourceFile = new File(sourcePath);
                if (sourceFile.exists()) {
                    sourceFile.delete();
                }

                finish();
            }
        };

        addTask(netTask);
    }

}
