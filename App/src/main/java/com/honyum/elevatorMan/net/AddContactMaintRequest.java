package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;


public class AddContactMaintRequest extends RequestBean {

    private AddCmReqBody body;

    public AddCmReqBody getBody() {
        return body;
    }

    public void setBody(AddCmReqBody body) {
        this.body = body;
    }

    public class AddCmReqBody extends RequestBody {

        private String communityId;

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }
    }
}
