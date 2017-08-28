package com.honyum.elevatorMan.adapter;

import android.content.Context;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.data.PersonListInfo;

import java.util.List;

/**
 * Created by Star on 2017/8/11.
 */

public class PersonsListAdapter extends BaseListViewAdapter<PersonListInfo> {
    public PersonsListAdapter(List<PersonListInfo> datas, Context context) {
        super(datas, context, R.layout.persons_item);
    }

    @Override
    public void bindData(BaseViewHolder holder, PersonListInfo o, int index) {

        String state = "不在线";
        if(o.getLat()>0)
        {
            state = "在线";

        }
        holder.setText(R.id.tv_name, o.getName())
                .setText(R.id.tv_state,state);

    }
}
