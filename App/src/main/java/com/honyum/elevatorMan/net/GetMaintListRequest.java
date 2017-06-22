package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/15.
 */

public class GetMaintListRequest extends RequestBean {
    public GetMaintListBody getBody() {
        return body;
    }

    public void setBody(GetMaintListBody body) {
        this.body = body;
    }

    private GetMaintListBody body;

    public class GetMaintListBody extends RequestBody {


        public String getBranchId() {
            return branchId;
        }

        public GetMaintListBody setBranchId(String branchId) {
            this.branchId = branchId;
            return this;
        }

        private String branchId;
    }
}
