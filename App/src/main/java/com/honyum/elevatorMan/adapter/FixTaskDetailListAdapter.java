package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.data.FixTaskInfo;

import java.util.List;

/**
 * Created by Star on 2017/6/12.
 */

public class FixTaskDetailListAdapter extends BaseListViewAdapter<FixTaskInfo> {

    public FixTaskDetailListAdapter(List datas, Context context) {
        super(datas, context, R.layout.fix_rec_item
        );
        mContext = context;
    }
    @Override
    public void bindData(BaseViewHolder holder, final FixTaskInfo o, int index) {

        holder.setText(R.id.tv_state, o.getState() + "")
                .setText(R.id.tv_fix_num, mContext.getString(R.string.rec_id)+o.getId())
                .setText(R.id.tv_fix_worker, mContext.getString(R.string.fix_worker)+o.getWorkerName())
                .setText(R.id.tv_fix_contact,mContext.getString(R.string.contact_tel)+o.getWorkerTel())
                .setText(R.id.tv_fix_time,mContext.getString(R.string.fix_time)+o.getCreateTime())
                .setOnClickListener(R.id.ll_fix, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListItemCallback)mContext).performItemCallback(o);

                    }
                });
    }
}
