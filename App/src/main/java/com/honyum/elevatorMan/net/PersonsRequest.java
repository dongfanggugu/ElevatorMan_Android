package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/8/11.
 */

public class PersonsRequest extends RequestBean {


    private  PersonsBody body;

    public PersonsBody getBody() {
        return body;
    }

    public PersonsRequest setBody(PersonsBody body) {
        this.body = body;
        return  this;
    }

    public class PersonsBody extends RequestBody{
        public String getBranchId() {
            return branchId;
        }

        public PersonsBody setBranchId(String branchId) {
            this.branchId = branchId;
            return this;
        }

        private String branchId;
    }

}
