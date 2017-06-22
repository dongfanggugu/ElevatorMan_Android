package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/15.
 */

public class GetNHMentenanceOrderRequest extends RequestBean {


    public GetNHMentenanceOrderBody getBody() {
        return body;
    }

    public void setBody(GetNHMentenanceOrderBody body) {
        this.body = body;
    }

    private GetNHMentenanceOrderBody body;

    public class GetNHMentenanceOrderBody extends RequestBody {


        public String getBranchId() {
            return branchId;
        }

        public GetNHMentenanceOrderBody setBranchId(String branchId) {
            this.branchId = branchId;
            return this;
        }

        private String branchId;
    }
}
