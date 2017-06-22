package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/17.
 */

public class UploadImageRequest extends RequestBean {



    private UploadImageBody body;

    public UploadImageBody getBody() {
        return body;
    }

    public UploadImageRequest setBody(UploadImageBody body) {
        this.body = body;
        return this;
    }

    public class UploadImageBody extends RequestBody
    {
        public String getImg() {
            return img;
        }

        public UploadImageBody setImg(String img) {
            this.img = img;
            return this;
        }

        private String img;

    }
}
