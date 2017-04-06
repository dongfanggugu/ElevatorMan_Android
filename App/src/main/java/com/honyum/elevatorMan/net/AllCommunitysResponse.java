package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.MaintenanceProjectInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;


public class AllCommunitysResponse extends Response {

    private List<MaintenanceProjectInfo> body;

    public List<MaintenanceProjectInfo> getBody() {
        return body;
    }

    public void setBody(List<MaintenanceProjectInfo> body) {
        this.body = body;
    }

    public static AllCommunitysResponse getAllCommunity(String json) {
        return (AllCommunitysResponse) parseFromJson(AllCommunitysResponse.class, json);
    }
}
