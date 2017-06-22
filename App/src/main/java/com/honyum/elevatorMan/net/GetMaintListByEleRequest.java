package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/15.
 */

public class GetMaintListByEleRequest extends RequestBean {
    public GetMaintListByEleBody getBody() {
        return body;
    }

    public void setBody(GetMaintListByEleBody body) {
        this.body = body;
    }

    private GetMaintListByEleBody body;

    public class GetMaintListByEleBody extends RequestBody {


        public String getElevatorId() {
            return elevatorId;
        }

        public GetMaintListByEleBody setElevatorId(String elevatorId) {
            this.elevatorId = elevatorId;
            return this;
        }

        private String elevatorId;
    }
}
