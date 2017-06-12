package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Star on 2017/6/10.
 */

public abstract class BaseListViewAdapter<T> extends android.widget.BaseAdapter {
    //通过泛型支持，通用数据类型
    protected List<T> mDatas;
    //加载布局使用上下文
    protected Context mContext;
    //保存item布局ID
    protected int mLayoutId;


    //子类中构造。
    public BaseListViewAdapter(List<T> datas, Context context, int layoutId) {
        mDatas = datas;
        mContext = context;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder  = BaseViewHolder.getViewHolder(mContext,convertView,parent,mLayoutId,position);
        T t = mDatas.get(position);
        bindData(holder, t,position);
        return holder.getConvertView() ;
    }

    public abstract void bindData(BaseViewHolder holder, T t,int index) ;

}
