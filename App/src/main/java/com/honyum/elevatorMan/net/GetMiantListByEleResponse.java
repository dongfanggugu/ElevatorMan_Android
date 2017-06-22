package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.MaintRecInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by Star on 2017/6/15.
 */

public class GetMiantListByEleResponse extends Response {


    public List<MaintRecInfo> getBody() {
        return body;
    }

    public void setBody(List<MaintRecInfo> body) {
        this.body = body;
    }

    private List<MaintRecInfo> body;


    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static GetMiantListByEleResponse getMiantListByEleResponse(String json) {
        return (GetMiantListByEleResponse) parseFromJson(GetMiantListByEleResponse.class, json);
    }
}
