package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.FixComponent;

import java.util.List;

/**
 * Created by Star on 2017/6/13.
 */

public class FixPaymentAdapter extends BaseListViewAdapter<FixComponent> {


    public FixPaymentAdapter(Context context, List<FixComponent> datas) {
        super(datas, context, R.layout.pay_list_item);
        mContext = context;
    }


    @Override
    public void bindData(BaseViewHolder holder, final FixComponent fixComponent, final int index) {
        holder.setText(R.id.tv_componentname, fixComponent.getName())
                .setText(R.id.tv_moneycount, fixComponent.getPrice() + "")
                .setOnClickListener(R.id.iv_remove_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListItemCallback) mContext).performItemCallback(fixComponent);
                    }
                });
    }
}
