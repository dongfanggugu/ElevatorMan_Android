package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

/**
 * Created by Star on 2017/6/7.
 */

public class FixResponse extends Response {
    private ResponseHead head;
    private List<FixInfo> body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public List<FixInfo> getBody() {
        return body;
    }

    public void setBody(List<FixInfo> body) {
        this.body = body;
    }

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static FixResponse getFixResponse(String json) {
        return (FixResponse) parseFromJson(FixResponse.class, json);
    }

}
