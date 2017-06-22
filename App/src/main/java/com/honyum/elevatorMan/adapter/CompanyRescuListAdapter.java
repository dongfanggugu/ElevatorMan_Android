package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.AlarmInfo1;

import java.util.List;

/**
 * Created by Star on 2017/6/15.
 */

public class CompanyRescuListAdapter extends BaseListViewAdapter<AlarmInfo1> {
    public CompanyRescuListAdapter(List datas, Context context) {
        super(datas, context, R.layout.mstask_detail_item);
    }

    @Override
    public void bindData(BaseViewHolder holder, final AlarmInfo1 o, int index) {
        //委托activity处理点击请求，adapter需要泛型支持
       // String[] stateList = new String[]{"","待出发","已出发","已到达","","检修完成","维修完成"};
        holder.setVisible(R.id.img_detail,false).setText(R.id.tv_taskcode,o.getCommunityAddress()).setText(R.id.tv_person,o.getAlarmTime()).setText(R.id.tv_index,index+1+"")
                .setOnClickListener(R.id.ll_taskdetailinfo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListItemCallback)mContext).performItemCallback(o);
                    }
                });

                if(o.getIsMisinformation().equals("1"))
                {
                    holder.setText(R.id.tv_state,"已撤销");
                }
                else if(o.getState().equals("1") || o.getState().equals("2"))
                {
                    holder.setText(R.id.tv_state,"已完成");
                }


    }
}
