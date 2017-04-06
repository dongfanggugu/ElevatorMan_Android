package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * 请求关联的报警列表
 *
 * @author changhaozhang
 */
public class AlarmListRequest extends RequestBean {

    private RequestHead head;

    private AlarmListReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public AlarmListReqBody getBody() {
        return body;
    }

    public void setBody(AlarmListReqBody body) {
        this.body = body;
    }

    public class AlarmListReqBody extends RequestBody {

        private String scope = "";

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }

}
