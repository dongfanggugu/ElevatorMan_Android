package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by chang on 2015/10/29.
 */
public class ReportPlanStateRequest extends RequestBean {

    private RequestHead head;

    private ReportPlanStateReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public ReportPlanStateReqBody getBody() {
        return body;
    }

    public void setBody(ReportPlanStateReqBody body) {
        this.body = body;
    }

    public class ReportPlanStateReqBody extends RequestBody {

        private String mainId;

        private int verify = 1;  //0:拒绝 1：确认 2:维保完成确认

        public String getMainId() {
            return mainId;
        }

        public void setMainId(String mainId) {
            this.mainId = mainId;
        }

        public int getVerify() {
            return verify;
        }

        public void setVerify(int verify) {
            this.verify = verify;
        }
    }
}
