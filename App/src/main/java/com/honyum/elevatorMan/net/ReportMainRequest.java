package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.util.List;

/**
 * Created by chang on 2015/9/21.
 */
public class ReportMainRequest extends RequestBean {

    private RequestHead head;

    private  ReportMainBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public ReportMainBody getBody() {
        return body;
    }

    public void setBody(ReportMainBody body) {
        this.body = body;
    }

    public class ReportMainBody extends RequestBody{
        private String id;

        private String mainTime;

        private List<String> photoBase64;

        private List<String> photoFileName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMainTime() {
            return mainTime;
        }

        public void setMainTime(String mainTime) {
            this.mainTime = mainTime;
        }

        public List<String> getPhotoBase64() {
            return photoBase64;
        }

        public void setPhotoBase64(List<String> photoBase64) {
            this.photoBase64 = photoBase64;
        }

        public List<String> getPhotoFileName() {
            return photoFileName;
        }

        public void setPhotoFileName(List<String> photoFileName) {
            this.photoFileName = photoFileName;
        }
    }
}
