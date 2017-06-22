package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/15.
 */

public class GetAlarmListRequest extends RequestBean {


    public GetAlarmListBody getBody() {
        return body;
    }

    public void setBody(GetAlarmListBody body) {
        this.body = body;
    }

    private GetAlarmListBody body;

    public class GetAlarmListBody extends RequestBody {


        public String getBranchId() {
            return branchId;
        }

        public GetAlarmListBody setBranchId(String branchId) {
            this.branchId = branchId;
            return this;
        }

        private String branchId;

        public String getHistory() {
            return history;
        }

        public GetAlarmListBody setHistory(String history) {
            this.history = history;
            return this;
        }

        private String history;
    }
}
