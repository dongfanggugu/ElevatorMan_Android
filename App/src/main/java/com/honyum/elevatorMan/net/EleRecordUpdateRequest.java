package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

import java.util.List;

/**
 * Created by Star on 2017/8/24.
 */

public class EleRecordUpdateRequest extends RequestBean {

    private List<EleRecordUpdateBody> body;

    public List<EleRecordUpdateBody> getBody() {
        return body;
    }

    public void setBody(List<EleRecordUpdateBody> body) {
        this.body = body;
    }


    public class EleRecordUpdateBody extends RequestBody {


        private String liftNum;
        private double lng;
        private double lat;
        private String logoPicture;
        private String signPicture;
        private String doorwayPicture;

        public double getLng() {
            return lng;
        }

        public EleRecordUpdateBody setLng(double lng) {
            this.lng = lng;
            return this;
        }

        public double getLat() {
            return lat;
        }

        public EleRecordUpdateBody setLat(double lat) {
            this.lat = lat;
            return this;
        }

        public String getLogoPicture() {
            return logoPicture;
        }

        public EleRecordUpdateBody setLogoPicture(String logoPicture) {
            this.logoPicture = logoPicture;
            return this;
        }

        public String getSignPicture() {
            return signPicture;
        }

        public EleRecordUpdateBody setSignPicture(String signPicture) {
            this.signPicture = signPicture;
            return this;
        }

        public String getDoorwayPicture() {
            return doorwayPicture;
        }

        public EleRecordUpdateBody setDoorwayPicture(String doorwayPicture) {
            this.doorwayPicture = doorwayPicture;
            return this;
        }

        public String getLiftNum() {
            return liftNum;
        }

        public EleRecordUpdateBody setLiftNum(String liftNum) {
            this.liftNum = liftNum;
            return this;
        }
    }
}
