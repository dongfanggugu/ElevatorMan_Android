package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.ElevatorInfo;

import java.util.List;

/**
 * Created by Star on 2017/8/24.
 */

public class RecordUpdateAdapter extends BaseListViewAdapter<ElevatorInfo> {
    public RecordUpdateAdapter(List<ElevatorInfo> datas, Context context) {
        super(datas, context, R.layout.layout_ele_record_item);
    }

    @Override
    public void bindData(BaseViewHolder holder, final ElevatorInfo elevatorInfo, int index) {

        holder.setText(R.id.tv_elenum,elevatorInfo.getEleId()).setText(R.id.tv_cname,elevatorInfo.getProjectName()) .setOnClickListener(R.id.ll_record_content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListItemCallback)mContext).performItemCallback(elevatorInfo);

            }
        });

    }
}
