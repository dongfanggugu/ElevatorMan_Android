package com.honyum.elevatorMan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.worker.RepairInfoActivity;
import com.honyum.elevatorMan.adapter.RepairTaskAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.data.RepairTaskInfo;
import com.honyum.elevatorMan.net.RepairTaskRequest;
import com.honyum.elevatorMan.net.RepairTaskResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepairUnderwayFragment extends Fragment {


    private View mView;

    private BaseFragmentActivity mActivity;

    private Config mConfig;

    private PullToRefreshListView ptrListView;

    public RepairUnderwayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_repair_underway, container, false);

        mActivity = (BaseFragmentActivity) getActivity();

        mConfig = mActivity.getConfig();

        initPtrListView();

        return mView;
    }

    private void initPtrListView() {
        ptrListView = (PullToRefreshListView) mView.findViewById(R.id.ptrListView);
        ptrListView.setMode(PullToRefreshBase.Mode.DISABLED);

        ptrListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                requestUnderwayRepair();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        requestUnderwayRepair();
    }

    private void requestUnderwayRepair() {
        String server = mConfig.getServer() + NetConstant.GET_UNDERWAY_REPAIR;

        RepairTaskRequest request = new RepairTaskRequest();
        RequestHead head = new RequestHead();
        RepairTaskRequest.RepairTaskReqBody body = request.new RepairTaskReqBody();

        head.setAccessToken(mConfig.getToken());
        head.setUserId(mConfig.getUserId());

        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                ptrListView.onRefreshComplete();
                RepairTaskResponse response = RepairTaskResponse.getResult(result);
                initView(response.getBody());
            }
        };

        mActivity.addTask(netTask);
    }

    private void initView(List<RepairTaskInfo> body) {
        ListView listView = ptrListView.getRefreshableView();
        RepairTaskAdapter adapter = new RepairTaskAdapter(mActivity, body);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepairTaskInfo info = (RepairTaskInfo) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), RepairInfoActivity.class);
                intent.putExtra("underway", true);
                intent.putExtra("info", info);
                startActivity(intent);
            }
        });
    }
}
