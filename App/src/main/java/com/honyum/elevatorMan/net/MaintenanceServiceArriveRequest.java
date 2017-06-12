package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class MaintenanceServiceArriveRequest extends RequestBean{

    private MaintenanceServiceArriveBody body;



    public MaintenanceServiceArriveBody getBody() {
        return body;
    }

    public void setBody(MaintenanceServiceArriveBody body) {
        this.body = body;
    }

    public class MaintenanceServiceArriveBody extends RequestBody {

        private String maintOrderProcessId;

        public String getMaintOrderProcessId() {
            return maintOrderProcessId;
        }

        public MaintenanceServiceArriveBody setMaintOrderProcessId(String maintOrderProcessId) {
            this.maintOrderProcessId = maintOrderProcessId;
            return this;
        }
    }
}
