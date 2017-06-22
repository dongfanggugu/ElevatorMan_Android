package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.mydata.NHorderAndTask;
import com.honyum.elevatorMan.data.newdata.ComapnyMentenanceInfo;

import java.util.List;

/**
 * Created by Star on 2017/6/15.
 */

public class CompanyNHMentenanceListAdapter extends BaseListViewAdapter<NHorderAndTask> {

    public CompanyNHMentenanceListAdapter(List datas, Context context) {
        super(datas, context, R.layout.company_order_item);
    }

    String[] type = new String[]{"未支付", "已支付"};

    @Override
    public void bindData(BaseViewHolder holder, final NHorderAndTask o, int index) {
        holder.setText(R.id.tv_taskcode, o.getVillaInfo().getAddress()).setText(R.id.tv_person, o.getMainttypeInfo().getName())
                .setText(R.id.tv_state, type[Integer.valueOf(o.getIsPay())])
                .setText(R.id.tv_num, "编号："+o.getCode()).setText(R.id.tv_time, o.getCreateTime()).setOnClickListener(R.id.nhorder_detail, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListItemCallback)mContext).performItemCallback(o);

            }
        });
    }
}
