package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/8/22.
 */

public class EleRecordRequest extends RequestBean {


    private EleRecordRequestBody body;

    public EleRecordRequestBody getBody() {
        return body;
    }

    public void setBody(EleRecordRequestBody body) {
        this.body = body;
    }


    public class EleRecordRequestBody extends RequestBody {


        private String liftNum;


        public String getLiftNum() {
            return liftNum;
        }

        public EleRecordRequestBody setLiftNum(String liftNum) {
            this.liftNum = liftNum;
            return this;
        }
    }
}
