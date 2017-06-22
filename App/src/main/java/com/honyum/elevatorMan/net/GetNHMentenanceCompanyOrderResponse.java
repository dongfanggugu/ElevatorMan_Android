package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.mydata.NHorderAndTask;
import com.honyum.elevatorMan.data.newdata.ComapnyMentenanceInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by Star on 2017/6/15.
 */

public class GetNHMentenanceCompanyOrderResponse extends Response {


    public List<NHorderAndTask> getBody() {
        return body;
    }

    public void setBody(List<NHorderAndTask> body) {
        this.body = body;
    }

    private List<NHorderAndTask> body;

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static GetNHMentenanceCompanyOrderResponse getNHMentenanceOrderResponse(String json) {
        return (GetNHMentenanceCompanyOrderResponse) parseFromJson(GetNHMentenanceCompanyOrderResponse.class, json);
    }
}
