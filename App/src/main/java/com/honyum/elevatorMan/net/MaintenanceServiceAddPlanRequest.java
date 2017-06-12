package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/7. 添加维保服务
 */

public class MaintenanceServiceAddPlanRequest extends RequestBean {
    private MaintenanceServiceAddPlanBody body;



    public MaintenanceServiceAddPlanBody getBody() {
        return body;
    }

    public void setBody(MaintenanceServiceAddPlanBody body) {
        this.body = body;
    }

    public class MaintenanceServiceAddPlanBody extends RequestBody {



        private String maintOrderId;

        private String planTime;

        public String getMaintOrderId() {
            return maintOrderId;
        }

        public MaintenanceServiceAddPlanBody setMaintOrderId(String maintOrderId) {
            this.maintOrderId = maintOrderId;
            return this;
        }

        public String getPlanTime() {
            return planTime;
        }

        public MaintenanceServiceAddPlanBody setPlanTime(String planTime) {
            this.planTime = planTime;
            return  this;
        }
    }


}
