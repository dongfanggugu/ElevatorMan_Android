package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by changhaozhang on 16/3/18.
 */
public class NotifyFeedbackRequest extends RequestBean {

    private RequestHead head;

    private NotifyFeedbackReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public NotifyFeedbackReqBody getBody() {
        return body;
    }

    public void setBody(NotifyFeedbackReqBody body) {
        this.body = body;
    }

    public class NotifyFeedbackReqBody extends RequestBody {

        private String alarmId = "";

        private String type = "";

        private String role = "";   //物业:2; 维修工:3

        public String getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(String alarmId) {
            this.alarmId = alarmId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

}
