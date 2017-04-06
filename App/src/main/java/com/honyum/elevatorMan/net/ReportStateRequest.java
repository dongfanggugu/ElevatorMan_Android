package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

public class ReportStateRequest extends RequestBean {

    private RequestHead head;

    private ReportStateReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public ReportStateReqBody getBody() {
        return body;
    }

    public void setBody(ReportStateReqBody body) {
        this.body = body;
    }


    public class ReportStateReqBody extends RequestBody {

        private String alarmId;

        private String state;  //救援状态

        private int injureCount = 0; //伤亡数量

        private int savedCount = 0;   //被救人数

        private String result = "";   //说明

        private String pic;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(String alarmId) {
            this.alarmId = alarmId;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getInjureCount() {
            return injureCount;
        }

        public void setInjureCount(int injureCount) {
            this.injureCount = injureCount;
        }

        public int getSavedCount() {
            return savedCount;
        }

        public void setSavedCount(int savedCount) {
            this.savedCount = savedCount;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }


    }

}
