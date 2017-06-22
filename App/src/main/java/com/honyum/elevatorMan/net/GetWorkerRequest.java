package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/15.
 */

public class GetWorkerRequest extends RequestBean {
    public GetWorkerBody getBody() {
        return body;
    }

    public void setBody(GetWorkerBody body) {
        this.body = body;
    }

    private GetWorkerBody body;

    public class GetWorkerBody extends RequestBody {


        public String getBranchId() {
            return branchId;
        }

        public GetWorkerBody setBranchId(String branchId) {
            this.branchId = branchId;
            return this;
        }

        private String branchId;
    }
}
