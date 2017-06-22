package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.mydata.NHFixAndTask;
import com.honyum.elevatorMan.data.newdata.CompanyRepairInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by Star on 2017/6/15.
 */

public class NHFixCompanyResponse extends Response {


    public Map<String, List<NHFixAndTask>> getBody() {
        return body;
    }

    public void setBody(Map<String, List<NHFixAndTask>> body) {
        this.body = body;
    }

    private Map<String,List<NHFixAndTask>> body;

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static NHFixCompanyResponse getCompanyRepairInfoResponse(String json) {
        return (NHFixCompanyResponse) parseFromJson(NHFixCompanyResponse.class, json);
    }
}
