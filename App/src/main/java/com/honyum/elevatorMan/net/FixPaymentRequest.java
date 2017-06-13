package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.FixComponent;
import com.honyum.elevatorMan.net.base.RequestBean;

import java.util.List;

/**
 * Created by Star on 2017/6/13.
 */

public class FixPaymentRequest extends RequestBean {
    public List<FixComponent> getBody() {
        return body;
    }

    public void setBody(List<FixComponent> body) {
        this.body = body;
    }

    private List<FixComponent> body;
}
