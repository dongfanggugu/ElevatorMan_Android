package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/7. 维保服务请求
 */

public class MaintenanceServiceStartRequest extends RequestBean {
    private MaintenanceServiceStartBody body;



    public MaintenanceServiceStartBody getBody() {
        return body;
    }

    public void setBody(MaintenanceServiceStartBody body) {
        this.body = body;
    }

    public class MaintenanceServiceStartBody extends RequestBody {


        public String getMaintOrderProcessId() {
            return maintOrderProcessId;
        }

        public MaintenanceServiceStartBody setMaintOrderProcessId(String maintOrderProcessId) {
            this.maintOrderProcessId = maintOrderProcessId;
            return this;
        }

        private String maintOrderProcessId;
    }


}
