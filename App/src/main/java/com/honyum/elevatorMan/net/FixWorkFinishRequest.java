package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class FixWorkFinishRequest extends RequestBean {

    private FixWorkFinishBody body;



    public FixWorkFinishBody getBody() {
        return body;
    }

    public void setBody(FixWorkFinishBody body) {
        this.body = body;
    }

    public class FixWorkFinishBody extends RequestBody {

        public String getRepairOrderProcessId() {
            return repairOrderProcessId;
        }


        public FixWorkFinishBody setRepairOrderProcessId(String repairOrderProcessId) {
            this.repairOrderProcessId = repairOrderProcessId;
            return this;
        }

        private String repairOrderProcessId;
        private String state;
        private String finishResult;
        private String pictures;

        public String getState() {
            return state;
        }

        public FixWorkFinishBody setState(String state) {
            this.state = state;
            return this;
        }

        public String getFinishResult() {
            return finishResult;
        }

        public FixWorkFinishBody setFinishResult(String finishResult) {
            this.finishResult = finishResult;
            return this;
        }

        public String getPictures() {
            return pictures;
        }

        public FixWorkFinishBody setPictures(String pictures) {
            this.pictures = pictures;
            return this;
        }
    }

}
