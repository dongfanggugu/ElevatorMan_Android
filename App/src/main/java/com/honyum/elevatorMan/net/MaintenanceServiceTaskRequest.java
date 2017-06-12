package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/8.根据维保ID获取维保任务单列表
 */

public class MaintenanceServiceTaskRequest extends RequestBean{

    private MaintenanceTaskBody body;


    public MaintenanceTaskBody getBody() {
        return body;
    }

    public void setBody(MaintenanceTaskBody body) {
        this.body = body;
    }

    public class MaintenanceTaskBody extends RequestBody {

        private String maintOrderId;
        private int page;
        private int rows;

        public String getMaintOrderId() {
            return maintOrderId;
        }

        public MaintenanceTaskBody setMaintOrderId(String maintOrderId) {
            this.maintOrderId = maintOrderId;
            return this;
        }

        public int getPage() {
            return page;
        }

        public MaintenanceTaskBody setPage(int page) {
            this.page = page;
            return this;
        }

        public int getRows() {
            return rows;
        }

        public MaintenanceTaskBody setRows(int rows) {
            this.rows = rows;
            return this;
        }
    }
}
