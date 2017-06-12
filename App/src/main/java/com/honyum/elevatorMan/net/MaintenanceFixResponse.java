package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.data.MaintenanceFixInfo;
import com.honyum.elevatorMan.data.MaintenanceServiceInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

/**
 * Created by Star on 2017/6/7.
 */

public class MaintenanceFixResponse extends Response {
    private ResponseHead head;
    private List<MaintenanceFixInfo> body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public List<MaintenanceFixInfo> getBody() {
        return body;
    }

    public void setBody(List<MaintenanceFixInfo> body) {
        this.body = body;
    }

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static MaintenanceFixResponse getMaintenanceFixInfoResponse(String json) {
        return (MaintenanceFixResponse) parseFromJson(MaintenanceFixResponse.class, json);
    }

}
