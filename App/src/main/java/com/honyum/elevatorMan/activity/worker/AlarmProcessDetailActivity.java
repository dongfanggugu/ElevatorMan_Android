package com.honyum.elevatorMan.activity.worker;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.AlarmProcessInfo;
import com.honyum.elevatorMan.net.AlarmProcessRequest;
import com.honyum.elevatorMan.net.AlarmProcessResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;

/**
 * Created by Star on 2017/7/10.
 */

public class AlarmProcessDetailActivity extends BaseActivityWraper {
    private String id;
    private RelativeLayout rl;
    private TextView tv_name;
    private TextView tv_state;
    private TextView tv_num;
    private TextView tv_time;
    private LinearLayout ll;
    private LayoutInflater lif;

    @Override
    public String getTitleString() {
        return "报警过程";
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("Id");
        lif = LayoutInflater.from(this);
        //rl = (RelativeLayout) lif.inflate(R.layout.alarm_process_item, null);
        ll = (LinearLayout) findViewById(R.id.main_layout);

        requestAlarmProcess();
        //ll.addView(rl);
    }

    public String getStateText(String state) {
        //String s = "";
        switch (state) {
            case "0":
                return "发生";
            case "1":
                return "已分配";

            case "2":
                return "已到达";

            case "3":
                return "已完成";

            case "5":
                return "物业已确认";
            default:
                return "";


        }
    }

    private AlarmProcessRequest getAlarmProcessBean() {
        AlarmProcessRequest ar = new AlarmProcessRequest();
        ar.setHead(new NewRequestHead()
                .setaccessToken(getConfig().getToken())
                .setuserId(getConfig().getUserId()));
        ar.setBody(ar.new AlarmProcessBody().setAlarmId(id));
        return ar;
    }

    private void requestAlarmProcess() {
        String server = getConfig().getServer() + NetConstant.URL_PROCESS_ALARM;

        NetTask myTask = new NetTask(server, getAlarmProcessBean()) {
            @Override
            protected void onResponse(NetTask task, String result) {

                AlarmProcessResponse apr = AlarmProcessResponse.getAlarmProcessResponse(result);
                for (AlarmProcessInfo s : apr.getBody()) {
                    rl = (RelativeLayout) lif.inflate(R.layout.alarm_process_item, null);
                    tv_name = (TextView) rl.findViewById(R.id.tv_name);
                    tv_state = (TextView) rl.findViewById(R.id.tv_state);
                    tv_num = (TextView) rl.findViewById(R.id.tv_num);
                    tv_time = (TextView) rl.findViewById(R.id.tv_time);

                    tv_name.setText(s.getConfirmName());
                    tv_state.setText(getStateText(s.getState()));
                    tv_num.setText(s.getTel());
                    tv_time.setText(s.getRecordTime());
                    ll.addView(rl);
                }
            }
        };
        addTask(myTask);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_alarm_process_detail;
    }
}
