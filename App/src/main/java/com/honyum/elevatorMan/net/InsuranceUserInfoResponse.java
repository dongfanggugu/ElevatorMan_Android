package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.InsuranceInfo;
import com.honyum.elevatorMan.net.base.Response;

/**
 * Created by Star on 2017/8/15.
 */

public class InsuranceUserInfoResponse extends Response {

    private InsuranceInfo body;

    public InsuranceInfo getBody() {
        return body;
    }

    public void setBody(InsuranceInfo body) {
        this.body = body;
    }

    public static InsuranceUserInfoResponse getInsuranceUserInfoResponse(String json)
    {


        return  (InsuranceUserInfoResponse) parseFromJson(InsuranceUserInfoResponse.class,json);
    }
}
