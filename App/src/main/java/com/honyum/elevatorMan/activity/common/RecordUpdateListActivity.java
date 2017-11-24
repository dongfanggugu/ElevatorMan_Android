package com.honyum.elevatorMan.activity.common;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.RecordUpdateAdapter;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.ElevatorInfo;
import com.honyum.elevatorMan.net.EleRecordUpdateRequest;
import com.honyum.elevatorMan.net.NewRequestHead;
import com.honyum.elevatorMan.net.UploadImageRequest;
import com.honyum.elevatorMan.net.UploadImageResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Star on 2017/8/24.
 */

public class RecordUpdateListActivity extends BaseActivityWraper implements ListItemCallback<ElevatorInfo> {


    InnerNode str1;
    InnerNode str2;
    InnerNode str3;

//    private String img1 = "";
//    private String img2 = "";
//    private String img3 = "";
//
//    private String url1 = "";
//    private String url2 = "";
//    private String url3 = "";


    class InnerNode {
        String url;
        String img;
        InnerNode next;

    }

    private RequestBean getImageRequestBean(String userId, String token, String path) {
        UploadImageRequest request = new UploadImageRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new UploadImageBody().setImg(path));
        return request;
    }

    public boolean checkUrl() {
        if (str1 == null || str2 == null || str3 == null)
            return false;
        else
            return true;
    }

    private void requestUploadImage(final InnerNode node, final ElevatorInfo data) {

        NetTask task = new NetTask(getConfig().getServer() + NetConstant.UP_LOAD_IMG,
                getImageRequestBean(getConfig().getUserId(), getConfig().getToken(), node.img)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                UploadImageResponse response = UploadImageResponse.getUploadImageResponse(result);
                if (response.getHead() != null && response.getHead().getRspCode().equals("0")) {
                    String url = response.getBody().getUrl();
                    node.url = url;
                    if (node.next != null) {
                        requestUploadImage(node.next, data);
                    } else if (checkUrl()) {
                        requestUpdateEleInfo(data);
                    }
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };
        addTask(task);

    }

    public void requestUpdateEleInfo(final ElevatorInfo data) {
        String server = getConfig().getServer() + NetConstant.ELE_RECORD_UPDATE;

        EleRecordUpdateRequest er = new EleRecordUpdateRequest();
        er.setHead(new NewRequestHead().setuserId(getConfig().getUserId()).setaccessToken(getConfig().getToken()));
        List<EleRecordUpdateRequest.EleRecordUpdateBody> data1 = new ArrayList<EleRecordUpdateRequest.EleRecordUpdateBody>();
        data1.add(er.new EleRecordUpdateBody()
                .setLiftNum(data.getEleId()).setLat(Double.valueOf(data.getY()))
                .setLng(Double.valueOf(data.getX())).setSignPicture(str1.url).setLogoPicture(str2.url).setDoorwayPicture(str3.url));
        er.setBody(data1);

        NetTask netTask = new NetTask(server, er) {
            @Override
            protected void onResponse(NetTask task, String result) {
                DataSupport.deleteAll(ElevatorInfo.class, "eleId = ? ", data.getEleId());
                datas.remove(0);
                mRecordUpdateAdapter.notifyDataSetChanged();
                submitEleInfo();
                //finish();
            }
        };
        addTask(netTask);
    }

    public void submitEleInfo() {


        str1 = new InnerNode();
        str2 = new InnerNode();
        str3 = new InnerNode();

        if(datas.get(0)!=null) {
            str1.img = datas.get(0).getSignImage();
            str2.img = datas.get(0).getUseImage();
            str3.img = datas.get(0).getPortImage();

            str2.next = str3;
            str1.next = str2;
            str3.next = null;


            requestUploadImage(str1, datas.get(0));
        }
        else{
            showToast("所有任务已提交完成！");
        }


    }


    private ListView lv_record_list;

    private TextView tv_submit_all;

    private List<ElevatorInfo> datas;

    private RecordUpdateAdapter mRecordUpdateAdapter;

    @Override
    public String getTitleString() {
        return "电梯信息上传";
    }

    @Override
    protected void initView() {

        lv_record_list = findView(R.id.lv_record_list);
        tv_submit_all = findView(R.id.tv_submit_all);
        tv_submit_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(datas!=null&&datas.size()>0)
                submitEleInfo();
            }
        });

        datas = DataSupport.findAll(ElevatorInfo.class);

        mRecordUpdateAdapter = new RecordUpdateAdapter(datas, this);


        lv_record_list.setAdapter(mRecordUpdateAdapter);
    }


    @Override
    protected int getLayoutID() {
        return R.layout.layout_updaterecord;
    }

    public void lookDetail(ElevatorInfo data) {
        Intent it = new Intent(this, ShowEleInfoActivity.class);
        it.putExtra("infos", data.getEleId());
        startActivity(it);
    }

    @Override
    public void performItemCallback(ElevatorInfo data) {
        lookDetail(data);

    }
}
