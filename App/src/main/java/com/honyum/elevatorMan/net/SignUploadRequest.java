package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by changhaozhang on 2017/4/22.
 */
public class SignUploadRequest extends RequestBean {

    private RequestHead head;

    private SignUploadReqBody body;

    @Override
    public RequestHead getHead() {
        return head;
    }

    @Override
    public void setHead(RequestHead head) {
        this.head = head;
    }

    public SignUploadReqBody getBody() {
        return body;
    }

    public void setBody(SignUploadReqBody body) {
        this.body = body;
    }

    public static class SignUploadReqBody extends RequestBody {
        private String autograph;

        public String getAutograph() {
            return autograph;
        }

        public void setAutograph(String autograph) {
            this.autograph = autograph;
        }
    }


}
