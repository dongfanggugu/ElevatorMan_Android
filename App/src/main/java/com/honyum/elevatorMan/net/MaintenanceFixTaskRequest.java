package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class MaintenanceFixTaskRequest extends RequestBean {

    private MaintenanceFixTaskBody body;



    public MaintenanceFixTaskBody getBody() {
        return body;
    }

    public void setBody(MaintenanceFixTaskBody body) {
        this.body = body;
    }

    public class MaintenanceFixTaskBody extends RequestBody {

        private String repairOrderId;

        public String getRepairOrderId() {
            return repairOrderId;
        }

        public void setRepairOrderId(String repairOrderId) {
            this.repairOrderId = repairOrderId;
        }
    }

}
