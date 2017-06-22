package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.MaintRecInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by Star on 2017/6/15.
 */

public class GetMiantListResponse extends Response {


    public Map<String, List<MaintRecInfo>> getBody() {
        return body;
    }

    public void setBody(Map<String, List<MaintRecInfo>> body) {
        this.body = body;
    }

    private Map<String,List<MaintRecInfo>> body;

    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static GetMiantListResponse getMiantListResponse(String json) {
        return (GetMiantListResponse) parseFromJson(GetMiantListResponse.class, json);
    }
}
