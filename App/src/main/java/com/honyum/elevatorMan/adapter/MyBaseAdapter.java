package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


public abstract class MyBaseAdapter<T> extends BaseAdapter {

    public Context context;
    public LayoutInflater inflater;
    private List<T> dataSource;

    public MyBaseAdapter(Context context, List<T> dataSource) {
        super();
        this.context = context;
        this.dataSource = dataSource;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public T getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(position, convertView, parent);
    }

    public abstract View getItemView(int position, View convertView, ViewGroup parent);

    public void addAll(List<T> list, boolean isClear) {
        if (isClear) {
            dataSource.clear();
        }
        dataSource.addAll(list);
        notifyDataSetChanged();
    }


    public void clearAll(boolean isClear) {
        if (isClear) {
            dataSource.clear();
        }
        notifyDataSetChanged();
    }

    public void add(T t) {
        dataSource.add(t);
        notifyDataSetChanged();
    }

    public void add(int pos, T t) {
        dataSource.add(pos, t);
        notifyDataSetChanged();
    }

    public void remove(T t) {
        dataSource.remove(t);
        notifyDataSetChanged();
    }
}
