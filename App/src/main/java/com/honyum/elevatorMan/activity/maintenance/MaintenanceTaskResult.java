package com.honyum.elevatorMan.activity.maintenance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.MaintenanceTaskInfo;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;

/**
 * Created by Star on 2017/6/12.
 */

public class MaintenanceTaskResult extends BaseFragmentActivity {

    private TextView tv_fix_complete;
    private EditText et_remark;

    private MaintenanceTaskInfo mMaintenanceTaskInfo;
    private ImageView iv_before_image;
    private ImageView iv_after_image;

    private LinearLayout llFullScreen;
    private ImageView ivOverview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maint_result);
        initTitle();
        initView();
    }

    private void initView() {
        Intent it = getIntent();
        mMaintenanceTaskInfo = (MaintenanceTaskInfo) it.getSerializableExtra("Info");
        et_remark = (EditText)findViewById(R.id.et_remark);
        et_remark.setText(mMaintenanceTaskInfo.getMaintUserFeedback());
        et_remark.setFocusable(false);
        llFullScreen = (LinearLayout) findViewById(R.id.ll_full_screen);
        llFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llFullScreen.setVisibility(View.GONE);
            }
        });
        ivOverview = (ImageView) findViewById(R.id.iv_overview);
        tv_fix_complete = (TextView)findViewById(R.id.tv_fix_complete);
        tv_fix_complete.setVisibility(View.GONE);



        iv_before_image = (ImageView)findViewById(R.id.iv_image1);
        iv_after_image =  (ImageView)findViewById(R.id.iv_image2);
        iv_before_image.setDrawingCacheEnabled(true);
        iv_after_image.setDrawingCacheEnabled(true);
        iv_before_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviewImage(iv_before_image);
            }
        });
        iv_after_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviewImage(iv_after_image);
            }
        });


        new GetPicture(mMaintenanceTaskInfo.getBeforeImg(),iv_before_image).execute();
        new GetPicture(mMaintenanceTaskInfo.getAfterImg(),iv_after_image).execute();


    }

    /**
     * 查看预览信息
     *
     * @param image1
     */

    private void showPreviewImage(ImageView image1) {
        ivOverview.setImageBitmap(Bitmap.createBitmap(image1.getDrawingCache()));
        llFullScreen.setVisibility(View.VISIBLE);
        image1.setDrawingCacheEnabled(false);
        image1.setDrawingCacheEnabled(true);
    }
    /**
     * 初始化标题
     */
    private void initTitle() {


        initTitleBar("维保结果查看", R.id.title_service_result,
                R.drawable.back_normal, backClickListener);
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
            //mImageView.setImageResource(R.drawable.icon_img_original);
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
            super.onPostExecute(result);
            if (!StringUtils.isEmpty(result)) {
                //Bitmap bitmap = Utils.getBitmapBySize(result, 80, 80);

                Bitmap bitmap = Utils.getImageFromFile(new File(result));
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);
                } else {
                    mImageView.setImageResource(R.drawable.defaut_image);
                }
            }
        }
    }

}
