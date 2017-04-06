package com.honyum.elevatorMan.net.base;

/**
 * Created by chang on 2015/9/21.
 */
public class ReportInitDateRequest extends RequestBean {

    private RequestHead head;

    private ReportInitDateReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public ReportInitDateReqBody getBody() {
        return body;
    }

    public void setBody(ReportInitDateReqBody body) {
        this.body = body;
    }

    public class ReportInitDateReqBody extends RequestBody {

        private String liftId;

        private String startMainTime;

        public String getLiftId() {
            return liftId;
        }

        public void setLiftId(String liftId) {
            this.liftId = liftId;
        }

        public String getStartMainTime() {
            return startMainTime;
        }

        public void setStartMainTime(String startMainTime) {
            this.startMainTime = startMainTime;
        }
    }
}
