package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;

/**
 * Created by Star on 2017/7/11.
 */

public class AlarmProcessRequest extends RequestBean {


    private AlarmProcessBody body;

    public AlarmProcessBody getBody() {
        return body;
    }

    public AlarmProcessRequest setBody(AlarmProcessBody body) {
        this.body = body;
        return this;
    }

    public class AlarmProcessBody {
        private String alarmId;

        public String getAlarmId() {
            return alarmId;
        }

        public AlarmProcessBody setAlarmId(String AlarmId) {
            alarmId = AlarmId;
            return this;
        }
    }
}
