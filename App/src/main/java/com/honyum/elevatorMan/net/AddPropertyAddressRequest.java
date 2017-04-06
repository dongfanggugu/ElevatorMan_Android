package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

public class AddPropertyAddressRequest extends RequestBean {

    private RequestHead head;

    private AddPaReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public AddPaReqBody getBody() {
        return body;
    }

    public void setBody(AddPaReqBody body) {
        this.body = body;
    }

    public class AddPaReqBody extends RequestBody {

        private String address;

        private String branchId;

        private double lng;

        private double lat;

        private String shortName;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }
    }
}
