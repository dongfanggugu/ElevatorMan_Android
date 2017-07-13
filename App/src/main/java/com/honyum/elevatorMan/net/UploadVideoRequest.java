package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.io.Serializable;

public class UploadVideoRequest extends RequestBean {

    private RequestHead head;

    private RequestBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public RequestBody getBody() {
        return body;
    }

    public void setBody(RequestBody body) {
        this.body = body;
    }

    public class RequestBody implements Serializable {

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        /**
         * 本地音频文件Base64编码
         */
        private String video;


    }
}
