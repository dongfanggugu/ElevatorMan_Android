package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by LiYouGui on 2017/6/6.
 */

public class KnowledgeRequest extends RequestBean {

    private KnowledgeReqBody body;

    public KnowledgeReqBody getBody() {
        return body;
    }

    public void setBody(KnowledgeReqBody body) {
        this.body = body;
    }

    public static class KnowledgeReqBody extends RequestBody {

        private String kntype;

        public String getKntype() {
            return kntype;
        }

        public void setKntype(String kntype) {
            this.kntype = kntype;
        }
    }
}
