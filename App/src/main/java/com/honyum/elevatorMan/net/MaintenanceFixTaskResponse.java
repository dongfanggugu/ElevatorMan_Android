package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.data.MaintenanceFixInfo;
import com.honyum.elevatorMan.data.MaintenanceFixTaskInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

/**
 * Created by Star on 2017/6/7.
 */

public class MaintenanceFixTaskResponse extends Response {
    private ResponseHead head;
    private List<MaintenanceFixTaskInfo> body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public List<MaintenanceFixTaskInfo> getBody() {
        return body;
    }

    public void setBody(List<MaintenanceFixTaskInfo> body) {
        this.body = body;
    }

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static MaintenanceFixTaskResponse getMaintenanceFixTaskInfoResponse(String json) {
        return (MaintenanceFixTaskResponse) parseFromJson(MaintenanceFixTaskResponse.class, json);
    }

}
