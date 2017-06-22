package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.MaintRecInfo;

import java.util.List;

/**
 * Created by Star on 2017/6/15.
 */

public class CompanyMentenanceList1Adapter extends BaseListViewAdapter<MaintRecInfo> {
    public CompanyMentenanceList1Adapter(List datas, Context context) {
        super(datas, context, R.layout.mstask_detail_item);
    }

    @Override
    public void bindData(BaseViewHolder holder, final MaintRecInfo o, int index) {
        //委托activity处理点击请求，adapter需要泛型支持
        holder.setVisible(R.id.img_detail,false).setText(R.id.tv_taskcode,o.getAddress()).setText(R.id.tv_person,o.getUserName()).setVisible(R.id.tv_state,false).setText(R.id.tv_index,index+1+"")
                .setOnClickListener(R.id.ll_taskdetailinfo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListItemCallback)mContext).performItemCallback(o);

                    }
                });

    }
}
