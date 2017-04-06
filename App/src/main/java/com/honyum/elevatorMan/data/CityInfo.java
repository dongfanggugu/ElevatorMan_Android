package com.honyum.elevatorMan.data;

import java.util.List;

/**
 * Created by changhaozhang on 16/1/11.
 */
public class CityInfo extends Atom {

    private List<Province> provinceList;

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    /**
     * 从json返回CityInfo对象
     * @param json
     * @return
     */
    public static CityInfo getCityInfoFromJson(String json) {
        return (CityInfo) parseFromJson(CityInfo.class, json);
    }
}
