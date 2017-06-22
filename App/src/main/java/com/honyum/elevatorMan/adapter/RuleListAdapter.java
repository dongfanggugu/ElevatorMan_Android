package com.honyum.elevatorMan.adapter;

import android.content.Context;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.mydata.KnowledgeInfo;

import java.util.List;

/**
 * Created by Star on 2017/6/21.
 */

public class RuleListAdapter extends BaseListViewAdapter<KnowledgeInfo> {

    public RuleListAdapter(List<KnowledgeInfo> datas, Context context) {
        super(datas, context, R.layout.layout_nous_item);
    }

    @Override
    public void bindData(BaseViewHolder vh, final KnowledgeInfo knowledgeInfo, int index) {
        vh.setText(R.id.tv_noues_title, knowledgeInfo.getTitle())
                .setText(R.id.tv_noues_keywords,  knowledgeInfo.getKeywords())
                .setText(R.id.tv_noues_num,index+1+"").setOnClickListener(R.id.ll_container,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListItemCallback)mContext).performItemCallback(knowledgeInfo);

            }
        });
    }
}
