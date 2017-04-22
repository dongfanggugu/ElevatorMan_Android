package com.honyum.elevatorMan.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;

public class PersonSignActivity extends BaseFragmentActivity {

    private ImageView imageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_sign);
        initView();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.iv_sign);

    }


    @Override
    protected void onResume() {
        super.onResume();

        String url = getConfig().getSign();

        if (StringUtils.isEmpty(url)) {
            showToast("您的手写签名为空,请添加签名");
            initTitleBar("添加");
            return;
        }

        initTitleBar("修改");

        new GetPicture(url, imageView).execute();
    }

    private void initTitleBar(String right) {
        initTitleBar(R.id.title_sign, "签名", R.drawable.back_normal, backClickListener,
                R.drawable.icon_add, rightClickListener);
    }

    /**
     * 点击标题栏后退按钮事件
     */
    public View.OnClickListener rightClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            startActivity(new Intent(PersonSignActivity.this, SecondActivity.class));
        }

    };

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
}
