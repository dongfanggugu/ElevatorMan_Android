package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * 物业请求报警信息
 *
 * @author changhaozhang
 */
public class AlarmStateRequest extends RequestBean {

    private RequestHead head;

    private AlarmStateReqBody body;


    public RequestHead getHead() {
        return head;
    }


    public void setHead(RequestHead head) {
        this.head = head;
    }


    public AlarmStateReqBody getBody() {
        return body;
    }


    public void setBody(AlarmStateReqBody body) {
        this.body = body;
    }


    public class AlarmStateReqBody extends RequestBody {

        private String alarmId;

        public String getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(String alarmId) {
            this.alarmId = alarmId;
        }


    }
}
