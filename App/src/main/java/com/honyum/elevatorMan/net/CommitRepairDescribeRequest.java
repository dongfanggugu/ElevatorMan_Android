package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;

import java.io.Serializable;

/**
 * Created by 李有鬼 on 2017/1/11 0011
 */

public class CommitRepairDescribeRequest extends RequestBean {

    private CommitRepairDescribeReqBody body;

    public CommitRepairDescribeReqBody getBody() {
        return body;
    }

    public void setBody(CommitRepairDescribeReqBody body) {
        this.body = body;
    }

    public class CommitRepairDescribeReqBody implements Serializable {

        private String repairId;

        private String finishResult;

        public String getRepairId() {
            return repairId;
        }

        public void setRepairId(String repairId) {
            this.repairId = repairId;
        }

        public String getFinishResult() {
            return finishResult;
        }

        public void setFinishResult(String finishResult) {
            this.finishResult = finishResult;
        }
    }
}
