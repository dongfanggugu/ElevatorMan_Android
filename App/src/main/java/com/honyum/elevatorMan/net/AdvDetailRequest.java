package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class AdvDetailRequest extends RequestBean {

    private AdvDetailBody body;



    public AdvDetailBody getBody() {
        return body;
    }

    public AdvDetailRequest setBody(AdvDetailBody body) {
        this.body = body;
        return this;
    }

    public class AdvDetailBody extends RequestBody {

        private String id;


        public String getId() {
            return id;
        }

        public AdvDetailBody setId(String id) {
            this.id = id;
            return this;
        }
    }

}
