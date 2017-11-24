package com.honyum.elevatorMan.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.BannerInfo;

import java.util.List;

/**
 * Created by 李有鬼 on 2017/1/16 0016
 */

public class BannerAdapter extends PagerAdapter {

    private Context context;

    private List<BannerInfo> pics;

    private int imageHeight;
    private int imageWidth;

    public BannerAdapter(Context context, List<BannerInfo> pics) {
        this.context = context;
        this.pics = pics;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        imageWidth = dm.widthPixels;         // 屏幕宽度（像素）
        imageHeight = dm.heightPixels;
    }

    /**
     * 判断mainactivity是否处于栈顶
     *
     * @return true在栈顶false不在栈顶
     */
    private boolean isMainActivityTop(Context context2) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(((BaseFragmentActivity) context).getClass().getName());
    }

    @Override
    public int getCount() {
        return pics.size() * 2000;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final BannerInfo info = pics.get(position % pics.size());
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);// XY填充
        //iv.setScaleType(ImageView.ScaleType.CENTER);
        Glide.with(context).load(info.getPic()).asBitmap().placeholder(R.drawable.banner).into(iv);
        iv.setTag(info.getId());
        iv.setTag(R.id.url, info.getPicUrl());

        if (info.getPicUrl() != "") {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(info.getPicUrl());
                    intent.setData(content_url);
                    context.startActivity(intent);
                }
            });
        }
        else {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListItemCallback<ImageView>) context).performItemCallback(iv);
                }
            });

        }

        container.addView(iv);
        return iv;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}