package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/15.
 */

public class GetDistanceRequest extends RequestBean {


    public GetDistanceBody getBody() {
        return body;
    }

    public void setBody(GetDistanceBody body) {
        this.body = body;
    }

    private GetDistanceBody body;

    public class GetDistanceBody extends RequestBody {


        public String getBranchId() {
            return branchId;
        }

        public GetDistanceBody setBranchId(String branchId) {
            this.branchId = branchId;
            return this;
        }

        private String branchId;

    }
}
