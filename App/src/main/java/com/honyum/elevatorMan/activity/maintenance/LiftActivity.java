package com.honyum.elevatorMan.activity.maintenance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.LiftInfo;
import com.honyum.elevatorMan.net.AlarmListRequest;
import com.honyum.elevatorMan.net.LiftInfoResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.RemindUtils;

import org.w3c.dom.Text;

import java.util.List;

public class LiftActivity extends BaseFragmentActivity {

    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lift);
        initTitleBar();
        initView();
    }


    private void initTitleBar() {
        initTitleBar("我的电梯", R.id.title_lift,
                R.drawable.back_normal, backClickListener);
    }


    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_lift);
        requestLiftInfo();
    }



    /**
     * 获取请求的bean
     *
     * @param userId
     * @param token
     * @return
     */
    private RequestBean getRequestBean(String userId, String token) {

        //只需要发送一个head即可，这里使用请求报警列表的request bean
        AlarmListRequest request = new AlarmListRequest();
        RequestHead head = new RequestHead();

        head.setUserId(userId);
        head.setAccessToken(token);

        request.setHead(head);

        return request;
    }

    /**
     * 请求电梯信息
     */
    private void requestLiftInfo() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_GET_LIFT_INFO,
                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                LiftInfoResponse response = LiftInfoResponse.getLiftInfoResponse(result);
                List<LiftInfo> liftInfoList = response.getBody();

                if (0 == liftInfoList.size()) {
                    findViewById(R.id.tv_tip).setVisibility(View.VISIBLE);
                    return;
                }

                //设置提醒
                for (LiftInfo liftInfo : liftInfoList) {
                    RemindUtils.setRemind(LiftActivity.this, liftInfo, getConfig().getUserId());
                }

                mListView.setAdapter(new MyAdapter(LiftActivity.this, liftInfoList));
                setListener(mListView);

            }
        };
        addTask(task);
    }

    private class MyAdapter extends BaseAdapter {

        private Context mContext;

        private List<LiftInfo> mList;

        public MyAdapter(Context context, List<LiftInfo> list) {
            mContext = context;
            mList = list;
        }
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = View.inflate(mContext, R.layout.layout_lift_item, null);
            }
            LiftInfo info = mList.get(position);
            convertView.setTag(info);
            TextView tvCode = (TextView) convertView.findViewById(R.id.tv_code);
            TextView tvAdd = (TextView) convertView.findViewById(R.id.tv_address);

            tvCode.setText(info.getNum());
            tvAdd.setText(info.getAddress());

            return convertView;
        }
    }

    private void setListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LiftInfo info = (LiftInfo) view.getTag();
                Intent intent = new Intent(LiftActivity.this, LiftDetailActivity.class);
                intent.putExtra("info", info);
                startActivity(intent);
            }
        });
    }

}
