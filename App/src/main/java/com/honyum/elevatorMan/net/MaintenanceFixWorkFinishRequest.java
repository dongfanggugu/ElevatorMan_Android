package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class MaintenanceFixWorkFinishRequest extends RequestBean {

    private MaintenanceFixWorkFinishBody body;



    public MaintenanceFixWorkFinishBody getBody() {
        return body;
    }

    public void setBody(MaintenanceFixWorkFinishBody body) {
        this.body = body;
    }

    public class MaintenanceFixWorkFinishBody extends RequestBody {

        public String getRepairOrderProcessId() {
            return repairOrderProcessId;
        }


        public void setRepairOrderProcessId(String repairOrderProcessId) {
            this.repairOrderProcessId = repairOrderProcessId;
        }

        private String repairOrderProcessId;
        private String state;
        private String finishResult;
        private String pictures;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getFinishResult() {
            return finishResult;
        }

        public void setFinishResult(String finishResult) {
            this.finishResult = finishResult;
        }

        public String getPictures() {
            return pictures;
        }

        public void setPictures(String pictures) {
            this.pictures = pictures;
        }
    }

}
