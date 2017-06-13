package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.data.FixTaskInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

/**
 * Created by Star on 2017/6/7.
 */

public class FixTaskResponse extends Response {
    private ResponseHead head;
    private List<FixTaskInfo> body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public List<FixTaskInfo> getBody() {
        return body;
    }

    public void setBody(List<FixTaskInfo> body) {
        this.body = body;
    }

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static FixTaskResponse getFixTaskResponse(String json) {
        return (FixTaskResponse) parseFromJson(FixTaskResponse.class, json);
    }

}
