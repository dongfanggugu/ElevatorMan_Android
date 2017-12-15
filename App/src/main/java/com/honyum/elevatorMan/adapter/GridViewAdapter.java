package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.data.IconInfo;

import java.util.List;

/**
 * Created by xiaolin on 2015/1/24.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<IconInfo> strList;
    private int hidePosition = AdapterView.INVALID_POSITION;

    public GridViewAdapter(Context context, List<IconInfo> strList) {
        this.context = context;
        this.strList = strList;
    }

    @Override
    public int getCount() {
        return strList.size();
    }

    @Override
    public IconInfo getItem(int position) {
        return strList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_iamge_tag, parent);
            holder.fl_back = (FrameLayout) convertView.findViewById(R.id.fl_back);
            holder.tv_icon = (TextView) convertView.findViewById(R.id.tv_icon);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //hide时隐藏Text
        if (position != hidePosition) {
            holder.fl_back.setVisibility(View.VISIBLE);
            holder.tv_icon.setText(strList.get(position).getName());
            holder.tv_icon.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, strList.get(position).getImage()), null, null);
            holder.tv_icon.setOnClickListener(v-> {
                Intent it = null;
                try {
                    it = new Intent(context, Class.forName(strList.get(position).getAction()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                context.startActivity(it);
            });
        } else {
            holder.fl_back.setVisibility(View.GONE);
        }
        convertView.setId(position);

        return convertView;
    }

    private static class ViewHolder {
        FrameLayout fl_back;
        TextView tv_icon;
        ImageView iv_delete;

    }

    public void hideView(int pos) {
        hidePosition = pos;
        notifyDataSetChanged();
    }

    public void showHideView() {
        hidePosition = AdapterView.INVALID_POSITION;
        notifyDataSetChanged();
    }

    public void removeView(int pos) {
        strList.remove(pos);
        notifyDataSetChanged();
    }

    //更新拖动时的gridView
    public void swapView(int draggedPos, int destPos) {
        //从前向后拖动，其他item依次前移
        if (draggedPos < destPos) {
            strList.add(destPos + 1, getItem(draggedPos));
            strList.remove(draggedPos);
        }
        //从后向前拖动，其他item依次后移
        else if (draggedPos > destPos) {
            strList.add(destPos, getItem(draggedPos));
            strList.remove(draggedPos + 1);
        }
        hidePosition = destPos;
        notifyDataSetChanged();
    }
}