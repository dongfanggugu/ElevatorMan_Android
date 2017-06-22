package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.MaintenanceTaskInfo;

import java.util.List;

/**
 * Created by Star on 2017/6/10.
 */

public class MSTaskListAdapter extends BaseListViewAdapter<MaintenanceTaskInfo> {

    String[] stateList = new String[]{"待确认","已确认","已出发","已到达","已完成","已评价","无法完成"};
    public MSTaskListAdapter(List datas, Context context) {
        super(datas, context, R.layout.mstask_detail_item);
        mContext = context;
    }

    @Override
    public void bindData(BaseViewHolder holder, final MaintenanceTaskInfo mti,int index) {
        holder.setText(R.id.tv_state, stateList[Integer.valueOf(mti.getState())]).
                setText(R.id.tv_person, mti.getMaintUserInfo().getName()).setText(R.id.tv_taskcode, mti.getTaskCode()).setText(R.id.tv_index,index+1+"")
                .setOnClickListener(R.id.ll_taskdetailinfo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListItemCallback)mContext).performItemCallback(mti);

            }
        });
    }
}
