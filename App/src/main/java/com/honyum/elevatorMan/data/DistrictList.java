package com.honyum.elevatorMan.data;

import java.util.List;

/**
 * Created by changhaozhang on 16/6/22.
 */
public class DistrictList extends Atom {

    private List<DistrictInfo> districts;

    public List<DistrictInfo> getDistricts() {
        return districts;
    }

    public void setDistricts(List<DistrictInfo> districts) {
        this.districts = districts;
    }

    public static DistrictList getDistricts(String json) {
        return (DistrictList) parseFromJson(DistrictList.class, json);
    }
}
