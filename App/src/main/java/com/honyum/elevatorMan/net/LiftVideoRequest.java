package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by chang on 2016/3/3.
 */
public class LiftVideoRequest extends RequestBean {

    private RequestHead head;

    private LiftVideoReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public LiftVideoReqBody getBody() {
        return body;
    }

    public void setBody(LiftVideoReqBody body) {
        this.body = body;
    }

    public class LiftVideoReqBody extends RequestBody {

        private String elevatorId = "";

        public String getElevatorId() {
            return elevatorId;
        }

        public void setElevatorId(String elevatorId) {
            this.elevatorId = elevatorId;
        }
    }
}
