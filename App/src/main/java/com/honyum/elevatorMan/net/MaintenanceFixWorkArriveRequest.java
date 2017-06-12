package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class MaintenanceFixWorkArriveRequest extends RequestBean {

    private MaintenanceFixWorkArriveBody body;



    public MaintenanceFixWorkArriveBody getBody() {
        return body;
    }

    public void setBody(MaintenanceFixWorkArriveBody body) {
        this.body = body;
    }

    public class MaintenanceFixWorkArriveBody extends RequestBody {

        public String getRepairOrderProcessId() {
            return repairOrderProcessId;
        }

        public void setRepairOrderProcessId(String repairOrderProcessId) {
            this.repairOrderProcessId = repairOrderProcessId;
        }

        private String repairOrderProcessId;
    }

}
