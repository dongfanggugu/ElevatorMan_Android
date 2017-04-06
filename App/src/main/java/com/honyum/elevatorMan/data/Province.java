package com.honyum.elevatorMan.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by changhaozhang on 16/1/11.
 */
public class Province implements Serializable, Comparable {

    private String proname;

    private String pronamepinyin;

    private List<City> citys;

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getPronamepinyin() {
        return pronamepinyin;
    }

    public void setPronamepinyin(String pronamepinyin) {
        this.pronamepinyin = pronamepinyin;
    }

    public List<City> getCitys() {
        return citys;
    }

    public void setCitys(List<City> citys) {
        this.citys = citys;
    }

    @Override
    public int compareTo(Object another) {
        Province pro = (Province) another;
        return this.getPronamepinyin().compareTo(pro.getPronamepinyin());
    }

}
