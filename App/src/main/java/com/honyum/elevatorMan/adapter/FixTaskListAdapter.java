package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.FixInfo;

import java.util.List;

/**
 * Created by Star on 2017/6/12.
 */

public class FixTaskListAdapter extends BaseListViewAdapter<FixInfo> {

    String[] stateList = new String[]{"","待确认","已确认","","已委派","","维修中","待完成","维修完成","确认完成"};
    public FixTaskListAdapter(List datas, Context context) {
        super(datas, context, R.layout.fix_list_item);
        mContext = context;
    }
    @Override
    public void bindData(BaseViewHolder holder, final FixInfo o, int index) {

        holder.setText(R.id.tv_state, stateList[Integer.valueOf(o.getState())] )
                .setText(R.id.tv_contact, o.getName()
                        +" "+o.getTel())
                .setText(R.id.tv_time, "预约："+o.getRepairTime())
                .setText(R.id.tv_index,index+1+"").setText(R.id.tv_datenumber,o.getVillaInfo().getCellName())
                .setOnClickListener(R.id.ll_taskdetailinfo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListItemCallback)mContext).performItemCallback(o);

                    }
                });
    }
}
