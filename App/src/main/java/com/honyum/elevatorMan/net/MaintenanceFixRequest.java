package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.
 */

public class MaintenanceFixRequest extends RequestBean {

    private MaintenanceTaskBody body;



    public MaintenanceTaskBody getBody() {
        return body;
    }

    public void setBody(MaintenanceTaskBody body) {
        this.body = body;
    }

    public class MaintenanceTaskBody extends RequestBody {

        private int page;

        private int rows;

        public void setPage(int page){
            this.page = page;
        }
        public int getPage(){
            return this.page;
        }
        public void setRows(int rows){
            this.rows = rows;
        }
        public int getRows(){
            return this.rows;
        }
    }

}
