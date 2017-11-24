package com.honyum.elevatorMan.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.maintenance.MaintenancePlanAddActivity;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.utils.ToastUtils;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

import static com.honyum.elevatorMan.net.base.NetConstant.ADD_STATE;

/**
 * Created by Star on 2017/6/12.   包装类，对BASE做一个模板。减少initVIEW 和title的工作量。PS ： 这里需要把title的ID 统一为title; 默认带返回箭头
 */

public abstract class BaseActivityWraper extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preView();
        setContentView(getLayoutID());
        //这里注册了BK,只适用Activity
        ButterKnife.bind(this) ;
        initTitleBar(getTitleString());
        initView();

    }

    /**
     * 显示toast提示
     *
     * @param msg
     */
    public void showSimpleToast(String msg) {
        ToastUtils.showToast(getApplicationContext(), msg);
    }


    public abstract String getTitleString();

    protected void initTitleBar(String s)
    {
        initTitleBar(s, R.id.title,
                R.drawable.back_normal, backClickListener);
    }

    protected abstract void initView();

    protected abstract int getLayoutID();


    protected void preView()
    {

    }
    //减少 new Intent
    protected void startActivity1(Context context, Class clz){
        startActivity(new Intent(context,clz));
    }
    //减少类型转换
    public <T extends View> T findView(int resId){
        return (T)findViewById(resId);
    }


    //获取意图


    protected <T extends Serializable> T getIntent(String key) {
        Intent it = getIntent();
        return (T) it.getSerializableExtra(key);

    }


    /**
     * 异步获取图片
     *
     * @author chang   获取图片提出公共
     */
    public static class  GetPicture extends AsyncTask<String, Void, String> {

        private String mUrl;
        private WeakReference<ImageView> mImageView;

        public GetPicture(String url, ImageView imageView) {
            mUrl = url;
            mImageView = new WeakReference<ImageView>(imageView);
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
        //TODO 没有处理图片加载失败显示什么
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!StringUtils.isEmpty(result)) {
                Bitmap bitmap = Utils.getBitmapBySize(result, 80, 80);

               // Bitmap bitmap = Utils.getImageFromFile(new File(result));
                if (bitmap != null) {
                    mImageView.get().setImageBitmap(bitmap);
                    mImageView.get().setTag(R.id.file_path,result);
                } else {
                    //mImageView.setImageResource(R.drawable.icon_person);
                }
            }
        }
    }
}
