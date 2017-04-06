package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.RequestBean;

import java.io.Serializable;

/**
 * Created by 李有鬼 on 2017/1/8 0008
 */

public class AllMaintenanceProjectRequest extends RequestBean {

    private AllMaintenanceProjectReqBody body;

    public AllMaintenanceProjectReqBody getBody() {
        return body;
    }

    public void setBody(AllMaintenanceProjectReqBody body) {
        this.body = body;
    }

    public class AllMaintenanceProjectReqBody implements Serializable {

        private String communityId;

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }
    }
}
