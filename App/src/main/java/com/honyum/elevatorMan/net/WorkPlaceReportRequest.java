package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by changhaozhang on 16/6/22.
 */
public class WorkPlaceReportRequest extends RequestBean {

    private RequestHead head;

    private WorkPlaceReportReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public WorkPlaceReportReqBody getBody() {
        return body;
    }

    public void setBody(WorkPlaceReportReqBody body) {
        this.body = body;
    }

    public class WorkPlaceReportReqBody extends RequestBody {

        private String resident_address;

        private String resident_province = "北京市";

        private String resident_city = "北京市";

        private String resident_county = "";

        private double resident_lng;

        private double resident_lat;

        public String getResident_address() {
            return resident_address;
        }

        public void setResident_address(String resident_address) {
            this.resident_address = resident_address;
        }

        public String getResident_province() {
            return resident_province;
        }

        public void setResident_province(String resident_province) {
            this.resident_province = resident_province;
        }

        public String getResident_city() {
            return resident_city;
        }

        public void setResident_city(String resident_city) {
            this.resident_city = resident_city;
        }

        public String getResident_county() {
            return resident_county;
        }

        public void setResident_county(String resident_county) {
            this.resident_county = resident_county;
        }

        public double getResident_lng() {
            return resident_lng;
        }

        public void setResident_lng(double resident_lng) {
            this.resident_lng = resident_lng;
        }

        public double getResident_lat() {
            return resident_lat;
        }

        public void setResident_lat(double resident_lat) {
            this.resident_lat = resident_lat;
        }
    }
}
