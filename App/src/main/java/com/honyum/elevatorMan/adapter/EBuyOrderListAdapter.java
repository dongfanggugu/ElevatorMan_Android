package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.EasyLadderOrderInfo;
import com.honyum.elevatorMan.data.FixInfo;

import java.util.List;

/**
 * Created by Star on 2017/6/12.
 */

public class EBuyOrderListAdapter extends BaseListViewAdapter<EasyLadderOrderInfo> {

    //String[] stateList = new String[]{"","待确认","已确认","","已委派","","维修中","待完成","维修完成","确认完成"};
    public EBuyOrderListAdapter(List datas, Context context) {
        super(datas, context, R.layout.fix_list_item);
        mContext = context;
    }
    @Override
    public void bindData(BaseViewHolder holder, final EasyLadderOrderInfo o, int index) {

        holder.setText(R.id.tv_state, o.getStateDesc() )
                .setText(R.id.tv_contact, o.getAddress())
                .setText(R.id.tv_time, o.getCreateTime())
                .setText(R.id.tv_index,index+1+"").setText(R.id.tv_datenumber,o.getName())
                .setOnClickListener(R.id.ll_taskdetailinfo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListItemCallback)mContext).performItemCallback(o);

                    }
                });
    }
}
