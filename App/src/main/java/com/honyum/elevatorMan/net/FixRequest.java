package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class FixRequest extends RequestBean {

    private FixRequestBody body;



    public FixRequestBody getBody() {
        return body;
    }

    public void setBody(FixRequestBody body) {
        this.body = body;
    }

    public class FixRequestBody extends RequestBody {

        private int page;

        private int rows;

        public FixRequestBody setPage(int page){
            this.page = page;
            return this;
        }
        public int getPage(){
            return this.page;
        }
        public FixRequestBody setRows(int rows){
            this.rows = rows;
            return this;
        }
        public int getRows(){
            return this.rows;
        }
    }

}
