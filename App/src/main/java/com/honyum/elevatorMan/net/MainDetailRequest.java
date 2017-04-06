package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by chang on 2015/11/2.
 */
public class MainDetailRequest extends RequestBean {

    private RequestHead head;

    private MainDetailReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public MainDetailReqBody getBody() {
        return body;
    }

    public void setBody(MainDetailReqBody body) {
        this.body = body;
    }

    public class MainDetailReqBody extends RequestBody {

        private String mainId = "";

        public String getMainId() {
            return mainId;
        }

        public void setMainId(String mainId) {
            this.mainId = mainId;
        }
    }
}
