package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

public class AcceptAlarmRequest extends RequestBean {

    private RequestHead head;

    private AcceptAlarmReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public AcceptAlarmReqBody getBody() {
        return body;
    }

    public void setBody(AcceptAlarmReqBody body) {
        this.body = body;
    }


}
