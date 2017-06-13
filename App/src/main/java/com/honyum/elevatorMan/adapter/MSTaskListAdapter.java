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

    private Context mContext;
    public MSTaskListAdapter(List datas, Context context) {
        super(datas, context, R.layout.mstask_detail_item);
        mContext = context;
    }

    @Override
    public void bindData(BaseViewHolder holder, final MaintenanceTaskInfo mti,int index) {
        holder.setText(R.id.tv_state, mti.getMaintUserInfo().getState() + "").
                setText(R.id.tv_person, mti.getMaintUserInfo().getName()).setText(R.id.tv_taskcode, mti.getTaskCode()).setText(R.id.tv_index,index+1+"")
                .setOnClickListener(R.id.ll_taskdetailinfo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListItemCallback)mContext).performItemCallback(mti);

            }
        });
    }
}
