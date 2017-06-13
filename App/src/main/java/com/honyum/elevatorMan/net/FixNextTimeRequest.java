package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/13.
 */

public class FixNextTimeRequest extends RequestBean {

    private FixNextTimeBody body;

    public FixNextTimeBody getBody() {
        return body;
    }

    public void setBody(FixNextTimeBody body) {
        this.body = body;
    }

    public class FixNextTimeBody extends RequestBody
    {
        public String getRepairOrderId() {
            return repairOrderId;
        }

        public FixNextTimeBody setRepairOrderId(String repairOrderId) {
            this.repairOrderId = repairOrderId;
            return this;
        }

        public String getPlanTime() {
            return planTime;
        }

        public FixNextTimeBody setPlanTime(String planTime) {
            this.planTime = planTime;
            return this;
        }

        private String repairOrderId;
        private String planTime;
    }

}
