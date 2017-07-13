package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;

public class AdvDetailResponse extends Response {

    private AdvDetailBody body;

    public AdvDetailBody getBody() {
        return body;
    }

    public void setBody(AdvDetailBody body) {
        this.body = body;
    }

    public static class AdvDetailBody extends ResponseBody {

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        private String content;


    }

    public static AdvDetailResponse getAdvDetail(String json) {
        return (AdvDetailResponse) parseFromJson(AdvDetailResponse.class, json);
    }
}
