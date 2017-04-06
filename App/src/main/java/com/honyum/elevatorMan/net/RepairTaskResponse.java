package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.data.RepairTaskInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by 李有鬼 on 2017/1/9 0009
 */

public class RepairTaskResponse extends Response {

    private List<RepairTaskInfo> body;

    public List<RepairTaskInfo> getBody() {
        return body;
    }

    public void setBody(List<RepairTaskInfo> body) {
        this.body = body;
    }

    public static RepairTaskResponse getResult(String json) {
        return (RepairTaskResponse) parseFromJson(RepairTaskResponse.class, json);
    }
}
