package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.RequestBean;

import java.io.Serializable;

/**
 * Created by 李有鬼 on 2017/1/9 0009
 */

public class RepairTaskRequest extends RequestBean {

    private RepairTaskReqBody body;

    public RepairTaskReqBody getBody() {
        return body;
    }

    public void setBody(RepairTaskReqBody body) {
        this.body = body;
    }

    public class RepairTaskReqBody implements Serializable {

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
