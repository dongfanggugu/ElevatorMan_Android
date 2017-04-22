package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;
import com.honyum.elevatorMan.net.base.ResponseHead;

/**
 * Created by changhaozhang on 2017/4/22.
 */
public class SignUploadResponse extends Response {

    private ResponseHead head;

    private SignUploadRspBody body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public SignUploadRspBody getBody() {
        return body;
    }

    public void setBody(SignUploadRspBody body) {
        this.body = body;
    }

    public static SignUploadResponse getResponse(String json) {
        return (SignUploadResponse) parseFromJson(SignUploadResponse.class, json);
    }

    public static class SignUploadRspBody extends ResponseBody {
        private String autograph;

        public String getAutograph() {
            return autograph;
        }

        public void setAutograph(String autograph) {
            this.autograph = autograph;
        }
    }
}
