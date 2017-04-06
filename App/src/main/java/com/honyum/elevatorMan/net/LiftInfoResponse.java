package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.LiftInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

/**
 * Created by chang on 2015/9/17.
 */
public class LiftInfoResponse extends Response {

    private ResponseHead head;

    private List<LiftInfo> body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public List<LiftInfo> getBody() {
        return body;
    }

    public void setBody(List<LiftInfo> body) {
        this.body = body;
    }

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static LiftInfoResponse getLiftInfoResponse(String json) {
        return (LiftInfoResponse) parseFromJson(LiftInfoResponse.class, json);
    }
}
