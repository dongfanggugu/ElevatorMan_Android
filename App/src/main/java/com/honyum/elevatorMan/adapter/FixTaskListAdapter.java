package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.FixInfo;

import java.util.List;

/**
 * Created by Star on 2017/6/12.
 */

public class FixTaskListAdapter extends BaseListViewAdapter<FixInfo> {

    public FixTaskListAdapter(List datas, Context context) {
        super(datas, context, R.layout.fix_list_item);
        mContext = context;
    }
    @Override
    public void bindData(BaseViewHolder holder, final FixInfo o, int index) {

        holder.setText(R.id.tv_state, o.getState() + "")
                .setText(R.id.tv_contact, o.getVillaInfo().getContacts()+":"+o.getVillaInfo().getContactsTel())
                .setText(R.id.tv_time, mContext.getString(R.string.app_time)+o.getRepairTime())
                .setText(R.id.tv_index,index+1+"")
                .setOnClickListener(R.id.ll_taskdetailinfo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListItemCallback)mContext).performItemCallback(o);

                    }
                });
    }
}
