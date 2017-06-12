package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.data.MaintenanceTaskInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

/**
 * Created by Star on 2017/6/8.
 */

public class MiantenanceServiceTaskResponse extends Response {
    private ResponseHead head;
    private List<MaintenanceTaskInfo> body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public List<MaintenanceTaskInfo> getBody() {
        return body;
    }

    public void setBody(List<MaintenanceTaskInfo> body) {
        this.body = body;
    }

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static MiantenanceServiceTaskResponse getMaintTaskInfoResponse(String json) {
        return (MiantenanceServiceTaskResponse) parseFromJson(MiantenanceServiceTaskResponse.class, json);
    }

}