package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/8/22.
 */

public class EleErrorUpdateRequest extends RequestBean {


    private EleErrorUpdateRequestBody body;

    public EleErrorUpdateRequestBody getBody() {
        return body;
    }

    public void setBody(EleErrorUpdateRequestBody body) {
        this.body = body;
    }


    public class EleErrorUpdateRequestBody extends RequestBody {


        private String liftNum;
        private String errorReason;


        public String getLiftNum() {
            return liftNum;
        }

        public EleErrorUpdateRequestBody setLiftNum(String liftNum) {
            this.liftNum = liftNum;
            return this;
        }

        public String getErrorReason() {
            return errorReason;
        }

        public EleErrorUpdateRequestBody setErrorReason(String errorReason) {
            this.errorReason = errorReason;
            return this;
        }
    }
}
