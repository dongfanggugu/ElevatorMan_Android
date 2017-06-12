package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class MaintenanceFixWorkStartRequest extends RequestBean {

    private MaintenanceFixWorkStartBody body;



    public MaintenanceFixWorkStartBody getBody() {
        return body;
    }

    public void setBody(MaintenanceFixWorkStartBody body) {
        this.body = body;
    }

    public class MaintenanceFixWorkStartBody extends RequestBody {

        public String getRepairOrderProcessId() {
            return repairOrderProcessId;
        }

        public void setRepairOrderProcessId(String repairOrderProcessId) {
            this.repairOrderProcessId = repairOrderProcessId;
        }

        private String repairOrderProcessId;
    }

}
