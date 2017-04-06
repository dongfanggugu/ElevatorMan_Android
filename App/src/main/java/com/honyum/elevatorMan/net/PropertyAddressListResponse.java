package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.PropertyAddressInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;


public class PropertyAddressListResponse extends Response {

    private List<PropertyAddressInfo> body;

    public List<PropertyAddressInfo> getBody() {
        return body;
    }

    public void setBody(List<PropertyAddressInfo> body) {
        this.body = body;
    }

    public static PropertyAddressListResponse getPal(String json) {
        return (PropertyAddressListResponse) parseFromJson(PropertyAddressListResponse.class, json);
    }
}
