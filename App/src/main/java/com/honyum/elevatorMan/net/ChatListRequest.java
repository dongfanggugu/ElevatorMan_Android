package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.io.Serializable;

public class ChatListRequest extends RequestBean {

    private RequestHead head;

    private RequestBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public RequestBody getBody() {
        return body;
    }

    public void setBody(RequestBody body) {
        this.body = body;
    }

    public class RequestBody implements Serializable {

        private String alarmId;

        private int rows;

        private Long maxCode;

        public String getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(String alarmId) {
            this.alarmId = alarmId;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public Long getMaxCode() {
            return maxCode;
        }

        public void setMaxCode(Long maxCode) {
            this.maxCode = maxCode;
        }
    }
}
