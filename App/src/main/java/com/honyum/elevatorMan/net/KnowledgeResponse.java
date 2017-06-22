package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.data.mydata.KnowledgeInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by LiYouGui on 2017/6/6.
 */

public class KnowledgeResponse extends Response {

    private List<KnowledgeInfo> body;

    public List<KnowledgeInfo> getBody() {
        return body;
    }

    public static KnowledgeResponse getKnowledgeResponse(String json) {
        return (KnowledgeResponse)parseFromJson(KnowledgeResponse.class, json);
    }

    public void setBody(List<KnowledgeInfo> body) {
        this.body = body;
    }
}
