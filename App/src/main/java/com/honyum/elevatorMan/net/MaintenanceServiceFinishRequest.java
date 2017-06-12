package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class MaintenanceServiceFinishRequest extends RequestBean{

    private MaintenanceServiceFinishBody body;



    public MaintenanceServiceFinishBody getBody() {
        return body;
    }

    public void setBody(MaintenanceServiceFinishBody body) {
        this.body = body;
    }

    public class MaintenanceServiceFinishBody extends RequestBody {

        private String maintOrderProcessId;
        private String maintUserFeedback;

        public String getMaintUserFeedback() {
            return maintUserFeedback;
        }

        public MaintenanceServiceFinishBody setMaintUserFeedback(String maintUserFeedback) {
            this.maintUserFeedback = maintUserFeedback;
            return this;
        }

        public String getBeforeImg() {
            return beforeImg;
        }

        public MaintenanceServiceFinishBody setBeforeImg(String beforeImg) {
            this.beforeImg = beforeImg;
            return this;
        }

        public String getAfterImg() {
            return afterImg;
        }

        public MaintenanceServiceFinishBody setAfterImg(String afterImg) {
            this.afterImg = afterImg;
            return this;
        }

        private String beforeImg;
        private String afterImg;


        public String getMaintOrderProcessId() {
            return maintOrderProcessId;
        }

        public MaintenanceServiceFinishBody setMaintOrderProcessId(String maintOrderProcessId) {
            this.maintOrderProcessId = maintOrderProcessId;
            return this;
        }
    }
}
