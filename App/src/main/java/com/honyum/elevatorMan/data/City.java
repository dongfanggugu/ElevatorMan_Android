package com.honyum.elevatorMan.data;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by changhaozhang on 16/1/11.
 */

public class City implements Serializable, Comparable {

    private String cityname;

    private String citypinyin;

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCitypinyin() {
        return citypinyin;
    }

    public void setCitypinyin(String citypinyin) {
        this.citypinyin = citypinyin;
    }


    @Override
    public int compareTo(Object another) {
        City city = (City) another;
        return this.getCitypinyin().compareTo(city.getCitypinyin());
    }
}
