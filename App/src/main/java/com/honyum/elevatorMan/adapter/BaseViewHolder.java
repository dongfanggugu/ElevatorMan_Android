package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;

public class BaseViewHolder {
    //复用的View
    private final View mConvertView;
    //所有控件集合
    private SparseArray<View> mViews;
    //记录位置信息
    private int mPosition;

    /**
     * BaseViewHolder 构造函数
     *
     * @param context  上下文对象
     * @param parent   父类容器
     * @param layoutId 布局 Id
     * @param position item位置信息
     */
    public BaseViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        //设置 tag
        mConvertView.setTag(this);
    }

    public BaseViewHolder setBackGround(int viewId, int color) {
        View tv = getView(viewId);
        tv.setBackgroundColor(ContextCompat.getColor(tv.getContext(), color));
        return this;
    }

    public BaseViewHolder setBoundsCompoundDrawables(int viewId, Drawable dwLeft) {
        TextView tv = getView(viewId);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30.00f, tv.getContext().getResources().getDisplayMetrics());
        dwLeft.setBounds(0, 0, size, size);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        tv.setCompoundDrawables(null, dwLeft, null, null);//只放左边
        return this;
    }

    /**
     * 通过 viewId 获取控件
     *
     * @param viewId 控件id
     * @param <T>    View 子类
     * @return 返回 View
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    //返回 ViewHolder
    public static BaseViewHolder getViewHolder(Context context, View convertView, ViewGroup parent, int layoutId, int position) {

        //BaseViewHolder 为空，创建新的，否则返回已存在的
        if (convertView == null) {
            return new BaseViewHolder(context, parent, layoutId, position);
        } else {
            BaseViewHolder holder = (BaseViewHolder) convertView.getTag();
            //更新 item 位置信息
            holder.mPosition = position;
            return holder;
        }
    }

    //resize view Size, input dp Unit ,this method will covert to ps value auto
    public BaseViewHolder setSize(int viewId, int width, int height) {
        View tv = getView(viewId);
        ViewGroup.LayoutParams params = tv.getLayoutParams();
        int psWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, tv.getContext().getResources().getDisplayMetrics());
        int psHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, tv.getContext().getResources().getDisplayMetrics());
        params.width = psWidth;
        params.height = psHeight;
        tv.setLayoutParams(params);
        return this;
    }

    /**
     * @param viewId
     * @param width
     * @param height
     * @param direction 0 left 1 top 2 right 3 bottom
     * @return
     */
    public BaseViewHolder setTextViewDrawableSize(int viewId, int width, int height, int direction) {

        View tv = getView(viewId);
        TextView textView;
        if (tv instanceof TextView)
            textView = (TextView) tv;
        else
            throw new RuntimeException("input type mistake ,require TextView");
        Drawable[] drawables = textView.getCompoundDrawables();
        int psWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, tv.getContext().getResources().getDisplayMetrics());
        int psHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, tv.getContext().getResources().getDisplayMetrics());

        if (direction < 3)
            drawables[direction].setBounds(0, 0, psWidth, psHeight);
        else
            throw new RuntimeException("out of bound direction ");
        setSize(viewId, width, height);
        return this;

    }


    //获取 convertView
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 设置 ImageView 的值
     *
     * @param viewId
     * @return
     */
    public boolean isVisible(int viewId) {
        View tv = getView(viewId);
        return tv.getVisibility() == View.VISIBLE;
    }

    /**
     * 设置 TextView 的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public BaseViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置 TextView 的Color值
     *
     * @param viewId
     * @param
     * @return
     */
    public BaseViewHolder setTextColor(int viewId, int color) {
        TextView tv = getView(viewId);
        tv.setTextColor(color);
        return this;
    }

    /**
     * 设置TImageView的值
     *
     * @param viewId
     * @param resId
     * @return
     */
    public BaseViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**
     * 设置是否可见
     *
     * @param viewId
     * @param visible
     * @return
     */
    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }


    /**
     * 设置tag
     *
     * @param viewId
     * @param tag
     * @return
     */
    public BaseViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public BaseViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * 设置 Checkable
     *
     * @param viewId
     * @param checked
     * @return
     */
    public BaseViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 点击事件
     */
    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 触摸事件
     */
    public BaseViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * 长按事件
     */
    public BaseViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

}