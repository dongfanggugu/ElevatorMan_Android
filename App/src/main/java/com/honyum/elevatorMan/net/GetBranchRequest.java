package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/15.
 */

public class GetBranchRequest extends RequestBean {


    public GetBranchBody getBody() {
        return body;
    }

    public void setBody(GetBranchBody body) {
        this.body = body;
    }

    private GetBranchBody body;

    public class GetBranchBody extends RequestBody {


        public String getBranchId() {
            return branchId;
        }

        public GetBranchBody setBranchId(String branchId) {
            this.branchId = branchId;
            return this;
        }

        private String branchId;
    }
}
