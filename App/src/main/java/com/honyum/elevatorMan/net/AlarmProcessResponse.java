package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.data.AlarmProcessInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by Star on 2017/7/11.
 */

public class AlarmProcessResponse extends Response {

   private List<AlarmProcessInfo> body;


    public List<AlarmProcessInfo> getBody() {
        return body;
    }

    public void setBody(List<AlarmProcessInfo> body) {
        this.body = body;
    }

    /**
     * 根据json生成对象
     *
     * @param json
     * @return
     */
    public static AlarmProcessResponse getAlarmProcessResponse(String json) {
        return (AlarmProcessResponse) parseFromJson(AlarmProcessResponse.class,
                json);
    }
}
