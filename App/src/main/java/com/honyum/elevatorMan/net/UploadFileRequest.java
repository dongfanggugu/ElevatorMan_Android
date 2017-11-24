package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/10/26.
 */

public class UploadFileRequest extends RequestBean {

    private UploadFileRequestBody body;

    public UploadFileRequestBody getBody() {
        return body;
    }

    public void setBody(UploadFileRequestBody body) {
        this.body = body;
    }

    public class UploadFileRequestBody extends RequestBody
    {
        public String getName() {
            return name;
        }

        public UploadFileRequestBody setName(String name) {
            this.name = name;
            return this;
        }

        public String getImg() {
            return img;
        }

        public UploadFileRequestBody setImg(String img) {
            this.img = img;
            return this;
        }

        private String name;
        private String img;

    }
}
