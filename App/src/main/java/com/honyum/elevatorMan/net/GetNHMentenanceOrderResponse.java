package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.MaintRecInfo;
import com.honyum.elevatorMan.data.newdata.ComapnyMentenanceInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by Star on 2017/6/15.
 */

public class GetNHMentenanceOrderResponse extends Response {


    public List<ComapnyMentenanceInfo> getBody() {
        return body;
    }

    public void setBody(List<ComapnyMentenanceInfo> body) {
        this.body = body;
    }

    private List<ComapnyMentenanceInfo> body;

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static GetNHMentenanceOrderResponse getNHMentenanceOrderResponse(String json) {
        return (GetNHMentenanceOrderResponse) parseFromJson(GetNHMentenanceOrderResponse.class, json);
    }
}
