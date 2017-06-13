package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class FixWorkArriveRequest extends RequestBean {

    private FixWorkArriveBody body;



    public FixWorkArriveBody getBody() {
        return body;
    }

    public void setBody(FixWorkArriveBody body) {
        this.body = body;
    }

    public class FixWorkArriveBody extends RequestBody {

        public String getRepairOrderProcessId() {
            return repairOrderProcessId;
        }

        public FixWorkArriveBody setRepairOrderProcessId(String repairOrderProcessId) {
            this.repairOrderProcessId = repairOrderProcessId;
            return this;
        }

        private String repairOrderProcessId;
    }

}
