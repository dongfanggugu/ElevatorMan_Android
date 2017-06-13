package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class FixTaskRequest extends RequestBean {

    private FixTaskBody body;



    public FixTaskBody getBody() {
        return body;
    }

    public void setBody(FixTaskBody body) {
        this.body = body;
    }

    public class FixTaskBody extends RequestBody {

        private String repairOrderId;

        public String getRepairOrderId() {
            return repairOrderId;
        }

        public FixTaskBody setRepairOrderId(String repairOrderId) {
            this.repairOrderId = repairOrderId;
            return this;
        }
    }

}
