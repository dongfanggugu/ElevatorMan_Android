package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.data.RepairTaskInfo;

import java.util.List;


/**
 * Created by 李有鬼 on 2017/1/9 0009
 */

public class RepairTaskAdapter extends MyBaseAdapter<RepairTaskInfo> {

    public RepairTaskAdapter(Context context, List<RepairTaskInfo> dataSource) {
        super(context, dataSource);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_repair_task_item, parent, false);
            vh = new ViewHolder();
            vh.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
            vh.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            vh.tvTel = (TextView) convertView.findViewById(R.id.tv_tel);
            vh.tvState = (TextView) convertView.findViewById(R.id.tv_state);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        RepairTaskInfo info = getItem(position);

        vh.tvNumber.setText("" + (position + 1) + "");

        vh.tvName.setText(info.getTel());

        vh.tvTel.setText(info.getPhenomenon());

        vh.tvState.setText(info.getCreateTime());

        return convertView;
    }

    private class ViewHolder {
        private TextView tvNumber, tvName, tvTel, tvState;
    }
}
