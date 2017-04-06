package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by changhaozhang on 15/8/18.
 */
public class WorkersRequest extends RequestBean {
    private RequestHead head;

    private WorkersReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public WorkersReqBody getBody() {
        return body;
    }

    public void setBody(WorkersReqBody body) {
        this.body = body;
    }

    public class WorkersReqBody extends RequestBody {

        private String alarmId;     //纬度

        public String getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(String alarmId) {
            this.alarmId = alarmId;
        }
    }
}
