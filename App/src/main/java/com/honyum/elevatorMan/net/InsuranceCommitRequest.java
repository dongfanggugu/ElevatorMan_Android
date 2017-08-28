package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.InsuranceInfo;
import com.honyum.elevatorMan.net.base.RequestBean;

/**
 * Created by Star on 2017/8/15.
 */

public class InsuranceCommitRequest extends RequestBean {


    private InsuranceInfo body;

    public InsuranceInfo getBody() {
        return body;
    }

    public void setBody(InsuranceInfo body) {
        this.body = body;
    }
}
