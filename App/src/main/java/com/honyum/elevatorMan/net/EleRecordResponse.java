package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.EleInfo;
import com.honyum.elevatorMan.net.base.Response;

/**
 * Created by Star on 2017/8/22.
 */

public class EleRecordResponse extends Response {


    private EleInfo body;


    public EleInfo getBody() {
        return body;
    }

    public void setBody(EleInfo body) {
        this.body = body;
    }

    public static EleRecordResponse getEleRecordResponse(String json) {
        return (EleRecordResponse) parseFromJson(EleRecordResponse.class, json);
    }
}
