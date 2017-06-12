package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.data.MaintenanceServiceInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

/**
 * Created by Star on 2017/6/7.
 */

public class MaintenanceServiceResponse extends Response {

    private List<MaintenanceServiceInfo> body;

    public List<MaintenanceServiceInfo> getBody() {
        return body;
    }

    public void setBody(List<MaintenanceServiceInfo> body) {
        this.body = body;
    }

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static MaintenanceServiceResponse getMaintServiceInfoResponse(String json) {
        return (MaintenanceServiceResponse) parseFromJson(MaintenanceServiceResponse.class, json);
    }

}
