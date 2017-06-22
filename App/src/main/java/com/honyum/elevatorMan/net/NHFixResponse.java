package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.newdata.ComapnyMentenanceInfo;
import com.honyum.elevatorMan.data.newdata.CompanyRepairInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by Star on 2017/6/15.
 */

public class NHFixResponse extends Response {


    public Map<String, List<CompanyRepairInfo>> getBody() {
        return body;
    }

    public void setBody(Map<String, List<CompanyRepairInfo>> body) {
        this.body = body;
    }

    private Map<String,List<CompanyRepairInfo>> body;

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static NHFixResponse getCompanyRepairInfoResponse(String json) {
        return (NHFixResponse) parseFromJson(NHFixResponse.class, json);
    }
}
