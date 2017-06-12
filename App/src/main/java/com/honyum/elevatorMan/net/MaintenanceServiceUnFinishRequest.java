package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/7. 维保服务请求
 */

public class MaintenanceServiceUnFinishRequest extends RequestBean {
    private MaintenanceServiceUnFinishBody body;



    public MaintenanceServiceUnFinishBody getBody() {
        return body;
    }

    public void setBody(MaintenanceServiceUnFinishBody body) {
        this.body = body;
    }

    public class MaintenanceServiceUnFinishBody extends RequestBody {


        public String getMaintOrderProcessId() {
            return maintOrderProcessId;
        }

        public MaintenanceServiceUnFinishBody setMaintOrderProcessId(String maintOrderProcessId) {
            this.maintOrderProcessId = maintOrderProcessId;
            return this;
        }

        private String maintOrderProcessId;
        private String maintUserFeedback;

        public String getMaintUserFeedback() {
            return maintUserFeedback;
        }

        public MaintenanceServiceUnFinishBody setMaintUserFeedback(String maintUserFeedback) {
            this.maintUserFeedback = maintUserFeedback;
            return this;
        }

        public String getPlanTime() {
            return planTime;
        }

        public MaintenanceServiceUnFinishBody setPlanTime(String planTime) {
            this.planTime = planTime;
            return this;
        }

        private String planTime;

    }


}
