package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.honyum.elevatorMan.R;
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
        BannerInfo info = pics.get(position % pics.size());

        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);// XY填充
        Glide.with(context).load(info.getPic()).placeholder(R.drawable.banner).into(iv);
        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}