package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * 请求报警信息
 *
 * @author chang
 */
public class AlarmInfoRequest extends RequestBean {

    private RequestHead head;

    private AlarmInfoReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public AlarmInfoReqBody getBody() {
        return body;
    }

    public void setBody(AlarmInfoReqBody body) {
        this.body = body;
    }

    public class AlarmInfoReqBody extends RequestBody {

        private String id; // 报警事件id

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
