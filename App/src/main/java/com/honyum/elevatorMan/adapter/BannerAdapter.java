package com.honyum.elevatorMan.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

    public BannerAdapter(Context context, List<BannerInfo> pics) {
        this.context = context;
        this.pics = pics;
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
        Glide.with(context).load(info.getPic()).placeholder(R.drawable.banner).into(iv);
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