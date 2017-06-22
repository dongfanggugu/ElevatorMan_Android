package com.honyum.elevatorMan.activity.common;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.RuleListAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.mydata.KnowledgeInfo;
import com.honyum.elevatorMan.net.KnowledgeRequest;
import com.honyum.elevatorMan.net.KnowledgeResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestHead;

public class NousActivity extends BaseFragmentActivity implements ListItemCallback<KnowledgeInfo> {

    private ListView nous_ListView;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_list);

        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        title = bundle.getString("kntype");

        initTitleBar(title, R.id.nous_title,
                R.drawable.back_normal, backClickListener);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestRepairOrder();
    }

    private void requestRepairOrder() {
        String server = getConfig().getServer() + NetConstant.GET_KNOWLEDGE_BYKNTYPE;
        //final String request = "{\"head\":{\"osType\":\"2\",\"rows\":\"100\",\"page\":\"1\",\"kntype\":\""+ title+ "\"},\"body\":{}}";
        KnowledgeRequest request = new KnowledgeRequest();
        KnowledgeRequest.KnowledgeReqBody body = new KnowledgeRequest.KnowledgeReqBody();
        RequestHead head = new RequestHead();

        body.setKntype(title);
        request.setHead(head);
        request.setBody(body);
        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                final KnowledgeResponse response = KnowledgeResponse.getKnowledgeResponse(result);
                final RuleListAdapter adapter = new RuleListAdapter(response.getBody(), NousActivity.this);
                nous_ListView.setAdapter(adapter);
            }
        };
        addBackGroundTask(netTask);
    }

    private void initView() {
        nous_ListView = (ListView) findViewById(R.id.nous_ListView);
    }

    @Override
    public void performItemCallback(KnowledgeInfo data) {
        Intent intent = new Intent(NousActivity.this, NousDetailActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("kntype", title);
        bundle.putString("content",data.getContent());
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
