package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by chang on 2015/10/21.
 */
public class ReportPlanRequest extends RequestBean {

    private RequestHead head;

    private ReportPlanReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public ReportPlanReqBody getBody() {
        return body;
    }

    public void setBody(ReportPlanReqBody body) {
        this.body = body;
    }

    public class ReportPlanReqBody extends RequestBody {

        private String id = "";

        private String planTime = "";

        private String mainType = "";

        private String remarks = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlanTime() {
            return planTime;
        }

        public void setPlanTime(String planTime) {
            this.planTime = planTime;
        }

        public String getMainType() {
            return mainType;
        }

        public void setMainType(String mainType) {
            this.mainType = mainType;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
