package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by Star on 2017/6/7.
 */

public class EmptyRequest extends RequestBean {
    private RequestHead head = new RequestHead();

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }
}
