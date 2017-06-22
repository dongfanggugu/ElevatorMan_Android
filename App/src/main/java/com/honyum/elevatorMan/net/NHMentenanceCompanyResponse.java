package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.mydata.NHorderAndTask;
import com.honyum.elevatorMan.data.newdata.ComapnyMentenanceInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by Star on 2017/6/15.
 */

public class NHMentenanceCompanyResponse extends Response {


    public Map<String, List<NHorderAndTask>> getBody() {
        return body;
    }

    public void setBody(Map<String, List<NHorderAndTask>> body) {
        this.body = body;
    }

    private Map<String,List<NHorderAndTask>> body;

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static NHMentenanceCompanyResponse getNHMentenanceCompanyResponse(String json) {
        return (NHMentenanceCompanyResponse) parseFromJson(NHMentenanceCompanyResponse.class, json);
    }
}
