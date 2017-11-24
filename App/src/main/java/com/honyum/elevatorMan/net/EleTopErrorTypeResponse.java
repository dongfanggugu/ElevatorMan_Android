package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.EleInfo;
import com.honyum.elevatorMan.data.TopErrorTypeInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by Star on 2017/8/22.
 */

public class EleTopErrorTypeResponse extends Response {


    private List<TopErrorTypeInfo> body;


    public List<TopErrorTypeInfo> getBody() {
        return body;
    }

    public void setBody(List<TopErrorTypeInfo> body) {
        this.body = body;
    }

    public static EleTopErrorTypeResponse getEleTopErrorTypeResponse(String json) {
        return (EleTopErrorTypeResponse) parseFromJson(EleTopErrorTypeResponse.class, json);
    }
}
