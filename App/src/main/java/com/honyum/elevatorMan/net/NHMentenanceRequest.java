package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/15.
 */

public class NHMentenanceRequest extends RequestBean {
    public NHMentenanceBody getBody() {
        return body;
    }

    public void setBody(NHMentenanceBody body) {
        this.body = body;
    }

    private NHMentenanceBody body;

    public class NHMentenanceBody extends RequestBody {


        public String getBranchId() {
            return branchId;
        }

        public NHMentenanceBody setBranchId(String branchId) {
            this.branchId = branchId;
            return this;
        }

        private String branchId;
    }
}
