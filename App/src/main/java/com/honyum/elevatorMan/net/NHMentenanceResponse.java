package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.newdata.ComapnyMentenanceInfo;
import com.honyum.elevatorMan.data.MaintRecInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by Star on 2017/6/15.
 */

public class NHMentenanceResponse extends Response {


    public Map<String, List<ComapnyMentenanceInfo>> getBody() {
        return body;
    }

    public void setBody(Map<String, List<ComapnyMentenanceInfo>> body) {
        this.body = body;
    }

    private Map<String,List<ComapnyMentenanceInfo>> body;

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static NHMentenanceResponse getNHMentenanceResponse(String json) {
        return (NHMentenanceResponse) parseFromJson(NHMentenanceResponse.class, json);
    }
}
