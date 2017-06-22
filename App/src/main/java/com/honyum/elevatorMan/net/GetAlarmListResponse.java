package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.data.AlarmInfo1;
import com.honyum.elevatorMan.data.BranchInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by Star on 2017/6/15.
 */

public class GetAlarmListResponse extends Response {
    public List<AlarmInfo1> getBody() {
        return body;
    }

    public void setBody(List<AlarmInfo1> body) {
        this.body = body;
    }

    private List<AlarmInfo1> body;
    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static GetAlarmListResponse getAlarmListResponse(String json) {
        return (GetAlarmListResponse) parseFromJson(GetAlarmListResponse.class, json);
    }
}
