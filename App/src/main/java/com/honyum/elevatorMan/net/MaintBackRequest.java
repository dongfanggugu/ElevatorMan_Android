package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by changhaozhang on 2017/4/22.
 */
public class MaintBackRequest extends RequestBean {

    private RequestHead head;

    private MaintBackReqBody body;

    @Override
    public RequestHead getHead() {
        return head;
    }

    @Override
    public void setHead(RequestHead head) {
        this.head = head;
    }

    public MaintBackReqBody getBody() {
        return body;
    }

    public void setBody(MaintBackReqBody body) {
        this.body = body;
    }

    public static class MaintBackReqBody extends RequestBody {
        private String mainId;

        private String backReason;

        public String getMainId() {
            return mainId;
        }

        public void setMainId(String mainId) {
            this.mainId = mainId;
        }

        public String getBackReason() {
            return backReason;
        }

        public void setBackReason(String backReason) {
            this.backReason = backReason;
        }
    }
}
