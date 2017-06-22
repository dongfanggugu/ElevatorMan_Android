package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.mydata.NHFixAndTask;
import com.honyum.elevatorMan.data.newdata.CompanyRepairInfo;
import com.honyum.elevatorMan.data.newdata1.RepairInfo;

import java.util.List;

/**
 * Created by Star on 2017/6/15.
 */

public class CompanyNHFixListAdapter extends BaseListViewAdapter<NHFixAndTask> {
    public CompanyNHFixListAdapter(List datas, Context context) {
        super(datas, context, R.layout.company_order_item);
    }
    //String[] type = new String[]{"未支付", "已支付"};
    String[] stateList = new String[]{"","待确认","已确认","","已委派","","维修中","待完成","维修完成","确认完成"};
    @Override
    public void bindData(BaseViewHolder holder, final NHFixAndTask o, int index) {
        holder.setText(R.id.tv_taskcode, o.getVillaInfo().getCellName())
                .setText(R.id.tv_person, "故障类型："+o.getPhenomenon())
                .setText(R.id.tv_state, stateList[Integer.valueOf(o.getState())])
                .setText(R.id.tv_num, "编号："+o.getCode()).setText(R.id.tv_time, o.getCreateTime()).setOnClickListener(R.id.nhorder_detail, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListItemCallback)mContext).performItemCallback(o);

            }
        });

    }
}
