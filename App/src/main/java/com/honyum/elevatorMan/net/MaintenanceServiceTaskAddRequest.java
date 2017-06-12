package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/7. 添加维保任务
 */

public class MaintenanceServiceTaskAddRequest extends RequestBean {
    private MaintenanceServiceTaskAddBody body;



    public MaintenanceServiceTaskAddBody getBody() {
        return body;
    }

    public void setBody(MaintenanceServiceTaskAddBody body) {
        this.body = body;
    }

    public class MaintenanceServiceTaskAddBody extends RequestBody {



        private String repairOrderId;

        public String getRepairOrderId() {
            return repairOrderId;
        }

        public void setRepairOrderId(String repairOrderId) {
            this.repairOrderId = repairOrderId;
        }

        public String getPlanTime() {
            return planTime;
        }

        public void setPlanTime(String planTime) {
            this.planTime = planTime;
        }

        private String planTime;


    }


}
