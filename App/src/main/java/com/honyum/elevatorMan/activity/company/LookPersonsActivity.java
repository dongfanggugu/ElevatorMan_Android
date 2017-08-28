package com.honyum.elevatorMan.activity.company;

import android.view.View;
import android.widget.ListView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.PersonsListAdapter;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.PersonListInfo;
import com.honyum.elevatorMan.net.PersonListResponse;
import com.honyum.elevatorMan.net.PersonResponse;
import com.honyum.elevatorMan.net.PersonsRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;

import java.util.List;

/**
 * Created by Star on 2017/8/11.
 */

public class LookPersonsActivity extends BaseActivityWraper {


    private ListView personList ;

    private List<PersonListInfo> mData;

    private PersonsListAdapter mPersonsListAdapter;
    @Override
    public String getTitleString() {
        return "全员查看";
    }


    @Override
    protected void initView() {
        personList = findView(R.id.lv_personlist);
        getPersonsList();

    }

    public void fillListData(List<PersonListInfo> data)
    {
        mPersonsListAdapter = new PersonsListAdapter(data,this);
        personList.setAdapter(mPersonsListAdapter);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_personlist;
    }

    private RequestBean getPesonsListBean() {

        PersonsRequest rt = new PersonsRequest();
        rt.setHead(new NewRequestHead().setuserId(getConfig().getUserId()).setaccessToken(getConfig().getToken()));
        rt.setBody(rt.new PersonsBody().setBranchId(getConfig().getBranchId()));
        return rt;

    }
    private void getPersonsList() {
        String address = getConfig().getServer() + NetConstant.GET_PERSONS_LIST;
        NetTask netTask = new NetTask(address, getPesonsListBean()) {
            @Override
            protected void onResponse(NetTask task, String result) {

                PersonListResponse pr = PersonListResponse.getPersonListResponse(result);
                if (pr.getBody()!=null&&pr.getBody().size()>0) {

                    mData = pr.getBody();
                    fillListData(mData);
                }
                else
                {
                    findView(R.id.tip).setVisibility(View.VISIBLE);
                }



            }
        };
        addTask(netTask);
    }
}
