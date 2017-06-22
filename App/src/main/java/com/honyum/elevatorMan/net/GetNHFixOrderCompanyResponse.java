package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.mydata.NHFixAndTask;
import com.honyum.elevatorMan.data.newdata1.RepairInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by Star on 2017/6/15.
 */

public class GetNHFixOrderCompanyResponse extends Response {


    public List<NHFixAndTask> getBody() {
        return body;
    }

    public void setBody(List<NHFixAndTask> body) {
        this.body = body;
    }

    private List<NHFixAndTask> body;

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static GetNHFixOrderCompanyResponse getNHFixOrderResponse(String json) {
        return (GetNHFixOrderCompanyResponse) parseFromJson(GetNHFixOrderCompanyResponse.class, json);
    }
}
