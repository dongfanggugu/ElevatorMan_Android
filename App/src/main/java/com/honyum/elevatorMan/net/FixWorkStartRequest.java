package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class FixWorkStartRequest extends RequestBean {

    private FixWorkStartBody body;



    public FixWorkStartBody getBody() {
        return body;
    }

    public void setBody(FixWorkStartBody body) {
        this.body = body;
    }

    public class FixWorkStartBody extends RequestBody {

        public String getRepairOrderProcessId() {
            return repairOrderProcessId;
        }

        public FixWorkStartBody setRepairOrderProcessId(String repairOrderProcessId) {
            this.repairOrderProcessId = repairOrderProcessId;
            return this;
        }

        private String repairOrderProcessId;
    }

}
