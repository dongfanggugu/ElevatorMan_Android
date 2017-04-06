package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;


public class PropertyAddressListRequest extends RequestBean {

    private RequestHead head;

    private PalReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public PalReqBody getBody() {
        return body;
    }

    public void setBody(PalReqBody body) {
        this.body = body;
    }

    public class PalReqBody extends RequestBody {

        private String branchId;

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }
    }
}
