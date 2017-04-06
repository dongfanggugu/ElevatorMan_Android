package com.honyum.elevatorMan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;

public class PicturePickActivity extends BaseFragmentActivity {


    private final int RESULT_OK = -1;

    private final int RESULT_CANCEL = 0;

    private static final int CAMERA_REQ_CODE = 1;

    private static final int PICKER_REQ_CODE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_icon_pop);
        initView(getIntent());
    }

    /**
     * 初始化视图
     * @param intent
     */
    private void initView(final Intent intent) {

        //拍摄照片
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
                String fileName = intent.getStringExtra("tag") + ".jpg";
                String filePath = dirPath + "/" + fileName;
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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICKER_REQ_CODE);
            }
        });


        //取消
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCEL);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //如果不进行拍照，取消结束此页面
        if (resultCode != Activity.RESULT_OK) {
            setResult(RESULT_CANCEL);
            finish();
            return;
        }

        //拍摄照片
        if (requestCode == CAMERA_REQ_CODE) {
            String tag = getIntent().getStringExtra("tag");

            String dir = Utils.getTempPath();
            String fileName = tag + ".jpg";

            Bitmap bitmap = Utils.getBitmapBySize(dir + "/" + fileName, 600, 800);

            //删除之前的图片文件
            File file = new File(dir, fileName);
            file.delete();

            Utils.saveBitmapWithQuality(bitmap, dir, fileName, 50);

            Intent intent = new Intent();
            intent.putExtra("tag", tag);
            intent.putExtra("result", dir + "/" + fileName);
            setResult(RESULT_OK, intent);
            finish();
        }

        //选取照片
        if (requestCode == PICKER_REQ_CODE) {
            String tag = getIntent().getStringExtra("tag");


            String filePath = Utils.getRealPathFromUri(this, data.getData());
            Bitmap bitmap = Utils.getBitmapBySize(filePath, 600, 800);

            //删除之前的图片文件
            String dir = Utils.getTempPath();
            String fileName = tag + ".jpg";

            File file = new File(dir, fileName);
            file.delete();

            Utils.saveBitmapWithQuality(bitmap, dir, fileName, 50);

            Intent intent = new Intent();
            intent.putExtra("tag", tag);
            intent.putExtra("result", dir + "/" + fileName);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
