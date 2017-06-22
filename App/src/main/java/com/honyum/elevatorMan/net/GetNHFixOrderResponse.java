package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.newdata.ComapnyMentenanceInfo;
import com.honyum.elevatorMan.data.newdata.CompanyRepairInfo;
import com.honyum.elevatorMan.data.newdata1.RepairInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by Star on 2017/6/15.
 */

public class GetNHFixOrderResponse extends Response {


    public List<RepairInfo> getBody() {
        return body;
    }

    public void setBody(List<RepairInfo> body) {
        this.body = body;
    }

    private List<RepairInfo> body;

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static GetNHFixOrderResponse getNHFixOrderResponse(String json) {
        return (GetNHFixOrderResponse) parseFromJson(GetNHFixOrderResponse.class, json);
    }
}
