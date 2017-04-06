package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * 确认报警完成
 *
 * @author changhaozhang
 */

public class ConfirmAlarmRequest extends RequestBean {

    private RequestHead head;

    private ConfirmAlarmReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public ConfirmAlarmReqBody getBody() {
        return body;
    }

    public void setBody(ConfirmAlarmReqBody body) {
        this.body = body;
    }

    public class ConfirmAlarmReqBody extends RequestBody {

        private String alarmId; // 报警事件id

        public String getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(String alarmId) {
            this.alarmId = alarmId;
        }


    }
}
